package com.artiexh.api.service.campaign.impl;

import com.artiexh.api.base.exception.ArtiexhConfigException;
import com.artiexh.api.base.exception.ErrorCode;
import com.artiexh.api.base.exception.InvalidException;
import com.artiexh.api.base.service.SystemConfigService;
import com.artiexh.api.service.campaign.CampaignService;
import com.artiexh.api.service.campaign.ProductInCampaignService;
import com.artiexh.api.service.notification.NotificationService;
import com.artiexh.api.service.productinventory.ProductInventoryJpaService;
import com.artiexh.data.jpa.entity.*;
import com.artiexh.data.jpa.entity.embededmodel.ProductVariantProviderId;
import com.artiexh.data.jpa.entity.embededmodel.ReferenceData;
import com.artiexh.data.jpa.entity.embededmodel.ReferenceEntity;
import com.artiexh.data.jpa.projection.ProductInventoryCode;
import com.artiexh.data.jpa.repository.*;
import com.artiexh.model.domain.*;
import com.artiexh.model.mapper.*;
import com.artiexh.model.rest.PaginationAndSortingRequest;
import com.artiexh.model.rest.campaign.request.*;
import com.artiexh.model.rest.campaign.response.*;
import com.artiexh.model.rest.product.response.ProductResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.artiexh.api.base.common.Const.SystemConfigKey.DEFAULT_PROFIT_PERCENTAGE;
import static com.artiexh.model.domain.CampaignStatus.ALLOWED_ADMIN_VIEW_STATUS;

@Log4j2
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CampaignServiceImpl implements CampaignService {
	private final CampaignRepository campaignRepository;
	private final ProductInCampaignRepository productInCampaignRepository;
	private final CustomProductRepository customProductRepository;
	private final ProviderRepository providerRepository;
	private final CampaignHistoryRepository campaignHistoryRepository;
	private final AccountRepository accountRepository;
	private final ProductInCampaignMapper productInCampaignMapper;
	private final CampaignMapper campaignMapper;
	private final ProductInventoryJpaService productService;
	private final ProductInventoryMapper productMapper;
	private final ProviderMapper providerMapper;
	private final ArtistRepository artistRepository;
	private final ProductInCampaignService productInCampaignService;
	private final ProductTagMapper productTagMapper;
	private final ProductCategoryMapper productCategoryMapper;
	private final CustomProductMapper customProductMapper;
	private final ProductInventoryJpaService productInventoryJpaService;
	private final ProductInventoryRepository productInventoryRepository;
	private final ProductVariantProviderRepository productVariantProviderRepository;
	private final NotificationService notificationService;
	private final SystemConfigService systemConfigService;
	private final ObjectMapper jsonMapper;

	@Override
	@Transactional
	public CampaignDetailResponse createCampaign(Long ownerId, ArtistCampaignRequest request) {
		ArtistEntity ownerEntity = artistRepository.getReferenceById(ownerId);
		validateCampaignTypeWithRole(ownerEntity, request);
		validateCreateCustomProductRequest(ownerEntity, request.getProviderId(), request.getProducts());

		var campaignEntity = campaignRepository.save(
			CampaignEntity.builder()
				.status(CampaignStatus.DRAFT.getByteValue())
				.owner(ownerEntity)
				.providerId(request.getProviderId())
				.name(request.getName())
				.description(request.getDescription())
				.thumbnailUrl(request.getThumbnailUrl())
				.content(request.getContent())
				.type(request.getType().getByteValue())
				.from(request.getFrom())
				.to(request.getTo())
				.build()
		);

		var savedProductInCampaigns = request.getProducts().stream().map(productInCampaignRequest -> {
			var customProductEntity = customProductRepository.getReferenceById(productInCampaignRequest.getCustomProductId());
			if (Boolean.TRUE.equals(customProductEntity.getIsDeleted())) {
				throw new InvalidException(ErrorCode.DELETED_CUSTOM_PRODUCT);
			}

			var productInCampaignEntity = productInCampaignMapper.createRequestToEntity(productInCampaignRequest);
			productInCampaignEntity.setCustomProduct(customProductEntity);
			productInCampaignEntity.setCampaign(campaignEntity);

			return productInCampaignRepository.save(productInCampaignEntity);
		}).collect(Collectors.toSet());

		var createCampaignHistoryEntity = campaignHistoryRepository.save(CampaignHistoryEntity.builder()
			.id(CampaignHistoryId.builder().campaignId(campaignEntity.getId()).build())
			.action(CampaignHistoryAction.CREATE.getByteValue())
			.updatedBy(ownerEntity.getUsername())
			.build()
		);

		campaignEntity.setProductInCampaigns(savedProductInCampaigns);
		campaignEntity.setCampaignHistories(Set.of(createCampaignHistoryEntity));
		return buildCampaignDetailResponse(campaignEntity);
	}

	@Override
	@Transactional
	public CampaignDetailResponse createPublicCampaign(Long createdBy, CreatePublicCampaignRequest request) {
		ArtistEntity ownerEntity = artistRepository.getReferenceById(request.getArtistId());
		AccountEntity account = accountRepository.getReferenceById(createdBy);

		CampaignEntity campaignEntity = CampaignEntity.builder()
			.owner(ownerEntity)
			.status(CampaignStatus.DRAFT.getByteValue())
			.name(request.getName())
			.type(CampaignType.PUBLIC.getByteValue())
			.createdBy(account)
			.build();
		campaignRepository.save(campaignEntity);

		CampaignHistoryEntity campaignHistory = CampaignHistoryEntity.builder()
			.id(CampaignHistoryId.builder().campaignId(campaignEntity.getId()).build())
			.action(CampaignHistoryAction.CREATE.getByteValue())
			.updatedBy(account.getUsername())
			.build();
//		campaignHistoryRepository.save(campaignHistory);

		campaignEntity.setCampaignHistories(Set.of(campaignHistory));
		return buildCampaignDetailResponse(campaignEntity);
	}

	@Override
	@Transactional
	public CampaignDetailResponse updateCampaign(Long ownerId, ArtistCampaignRequest request) {
		ArtistEntity ownerEntity = artistRepository.getReferenceById(ownerId);
		validateCampaignTypeWithRole(ownerEntity, request);

		var oldCampaignEntity = campaignRepository.findById(request.getId())
			.orElseThrow(() -> new EntityNotFoundException("Chiến dịch " + request.getId() + " không tìm thấy"));

		if (!oldCampaignEntity.getOwner().getId().equals(ownerId)) {
			throw new InvalidException(ErrorCode.CAMPAIGN_OWNER_INVALID);
		}

		if (!oldCampaignEntity.getStatus().equals(CampaignStatus.DRAFT.getByteValue())
			&& !oldCampaignEntity.getStatus().equals(CampaignStatus.REQUEST_CHANGE.getByteValue())) {
			throw new InvalidException(ErrorCode.UPDATE_CAMPAIGN_REQUEST);
		}

		validateCreateCustomProductRequest(ownerEntity, request.getProviderId(), request.getProducts());

		var customProductIdToProductInCampaignMap = oldCampaignEntity.getProductInCampaigns().stream()
			.collect(Collectors.toMap(
				productInCampaignEntity -> productInCampaignEntity.getCustomProduct().getId(),
				productInCampaign -> productInCampaign)
			);

		var productInCampaigns = request.getProducts().stream()
			.map(productInCampaignRequest -> {
				ProductInCampaignEntity productInCampaignEntity =
					customProductIdToProductInCampaignMap.get(productInCampaignRequest.getCustomProductId());

				if (productInCampaignEntity != null) {
					productInCampaignMapper.createRequestToEntity(productInCampaignRequest, productInCampaignEntity);
				} else {
					var customProductEntity = customProductRepository.getReferenceById(productInCampaignRequest.getCustomProductId());
					productInCampaignEntity = productInCampaignMapper.createRequestToEntity(productInCampaignRequest);
					productInCampaignEntity.setCustomProduct(customProductEntity);
					productInCampaignEntity.setCampaign(oldCampaignEntity);
				}

				if (Boolean.TRUE.equals(productInCampaignEntity.getCustomProduct().getIsDeleted())) {
					throw new InvalidException(ErrorCode.DELETED_CUSTOM_PRODUCT);
				}
				return productInCampaignRepository.save(productInCampaignEntity);
			})
			.collect(Collectors.toSet());

		oldCampaignEntity.setName(request.getName());
		oldCampaignEntity.setProviderId(request.getProviderId());
		oldCampaignEntity.setDescription(request.getDescription());
		oldCampaignEntity.setThumbnailUrl(request.getThumbnailUrl());
		oldCampaignEntity.setContent(request.getContent());
		oldCampaignEntity.setType(request.getType().getByteValue());
		oldCampaignEntity.setFrom(request.getFrom());
		oldCampaignEntity.setTo(request.getTo());
		oldCampaignEntity.getProductInCampaigns().clear();
		oldCampaignEntity.getProductInCampaigns().addAll(productInCampaigns);

		var savedCampaignEntity = campaignRepository.save(oldCampaignEntity);
		return buildCampaignDetailResponse(savedCampaignEntity);
	}

	private CampaignDetailResponse buildCampaignDetailResponse(CampaignEntity campaignEntity) {
		var result = campaignMapper.entityToDetailResponse(campaignEntity);

		PaginationAndSortingRequest pagination = new PaginationAndSortingRequest();
		pagination.setSortBy("id.eventTime");
		Set<CampaignHistory> campaignHistories = new HashSet<>(getCampaignHistory(campaignEntity.getId(), pagination.getPageable()).getContent());

		result.setCampaignHistories(campaignHistories);

		if (campaignEntity.getProviderId() != null) {
			var provider = providerRepository.getReferenceById(campaignEntity.getProviderId());
			result.setProvider(providerMapper.entityToInfo(provider));
			fillProviderConfigToResponse(campaignEntity.getProviderId(), result.getProducts());
		}

		return result;
	}

	private void fillProviderConfigToResponse(String providerId, Set<ProductInCampaignResponse> productInCampaignResponses) {
		for (var productInCampaignResponse : productInCampaignResponses) {
			customProductRepository.getReferenceById(productInCampaignResponse.getCustomProduct().getId())
				.getVariant().getProviderConfigs().stream()
				.filter(config -> config.getId().getBusinessCode().equals(providerId))
				.findAny()
				.map(providerConfigEntity -> new ProviderConfigResponse(
					providerConfigEntity.getManufacturingTime(),
					providerConfigEntity.getMinQuantity(),
					providerConfigEntity.getBasePriceAmount()
				))
				.ifPresent(productInCampaignResponse::setProviderConfig);
		}
	}

	private void validateCampaignTypeWithRole(ArtistEntity ownerEntity, ArtistCampaignRequest request) {
		Role role = Role.fromValue(ownerEntity.getRole());
		CampaignType type = request.getType();

		if (role == Role.ARTIST && type == CampaignType.PUBLIC) {
			throw new InvalidException(ErrorCode.INVALID_PUBLIC_CAMPAIGN_OWNER, "Artist không thể tạo chiến dịch loại PUBLIC");
		}

		if ((role == Role.ADMIN || role == Role.STAFF) && type != CampaignType.PUBLIC) {
			throw new InvalidException(ErrorCode.INVALID_PUBLIC_CAMPAIGN_OWNER, "Quản trị viên hoặc nhân viên chỉ có tạo chiến dịch loại PUBLIC");
		}
	}

	private void validateCreateCustomProductRequest(ArtistEntity ownerEntity,
													String providerId,
													Set<ProductInCampaignRequest> requests) {
		if (providerId != null && !providerRepository.existsById(providerId)) {
			throw new InvalidException(ErrorCode.PROVIDER_INFO_NOT_FOUND);
		}

		for (var productInCampaignRequest : requests) {
			var customProductEntity = customProductRepository.findById(productInCampaignRequest.getCustomProductId())
				.orElseThrow(() -> new InvalidException(ErrorCode.CUSTOM_PRODUCT_INFO_NOT_FOUND, ErrorCode.CUSTOM_PRODUCT_INFO_NOT_FOUND.getMessage() + productInCampaignRequest.getCustomProductId()));

			if (ownerEntity.getRole() == Role.ARTIST.getByteValue()
				&& !ownerEntity.getId().equals(customProductEntity.getArtist().getId())) {
				throw new InvalidException(ErrorCode.CUSTOM_PRODUCT_OWNER_INVALID, ErrorCode.CUSTOM_PRODUCT_OWNER_INVALID.getMessage() + customProductEntity.getId());
			}

			validateProviderSupportCustomProduct(providerId, customProductEntity, productInCampaignRequest);
		}
	}

	private void validateProviderSupportCustomProduct(String providerId,
													  CustomProductEntity customProductEntity,
													  ProductInCampaignRequest productRequest) {
		if (providerId != null) {
			var providerConfig = customProductEntity.getVariant().getProviderConfigs().stream()
				.filter(config -> providerId.equals(config.getId().getBusinessCode()))
				.findAny()
				.orElseThrow(() -> new InvalidException(ErrorCode.UNSUPPORTED_PROVIDER, "Bản thiết kế " + customProductEntity.getId() + " không được hỗ trợ bởi " + providerId));

			if (productRequest.getQuantity() != null && providerConfig.getMinQuantity() > productRequest.getQuantity()) {
				throw new InvalidException(ErrorCode.QUANTITY_RANGE_INVALID, ErrorCode.QUANTITY_RANGE_INVALID.getMessage() + providerConfig.getMinQuantity());
			}

			if (productRequest.getPrice() != null
				&& productRequest.getPrice().getAmount() != null
				&& providerConfig.getBasePriceAmount().compareTo(productRequest.getPrice().getAmount()) > 0) {
				throw new InvalidException(ErrorCode.PRICE_RANGE_INVALID, ErrorCode.PRICE_RANGE_INVALID.getMessage() + providerConfig.getBasePriceAmount());
			}
		}
	}

	@Override
	public Page<CampaignResponse> getAllCampaigns(Specification<CampaignEntity> specification, Pageable pageable) {
		return campaignRepository.findAll(specification, pageable).map(campaignMapper::entityToResponse);
	}

	@Override
	public CampaignDetailResponse getCampaignDetail(Long userId, Long campaignId) {
		var userEntity = accountRepository.findById(userId)
			.orElseThrow(() -> new UsernameNotFoundException("user " + userId + " not found"));

		var campaignEntity = campaignRepository.findById(campaignId)
			.orElseThrow(EntityNotFoundException::new);

		if (userEntity.getRole() == Role.ADMIN.getByteValue()) {
			if (!ALLOWED_ADMIN_VIEW_STATUS.contains(CampaignStatus.fromValue(campaignEntity.getStatus()))
				&& !campaignEntity.getOwner().getId().equals(userId)) {
				throw new InvalidException(ErrorCode.CAMPAIGN_REQUEST_NOT_FOUND);
			}
		} else if (!campaignEntity.getOwner().getId().equals(userId)) {
			throw new InvalidException(ErrorCode.CAMPAIGN_OWNER_INVALID);
		}

		return buildCampaignDetailResponse(campaignEntity);
	}

	@Override
	public PublishedCampaignDetailResponse getCampaignDetail(Long campaignId) {
		CampaignEntity campaignEntity = campaignRepository.findById(campaignId)
			.orElseThrow(EntityNotFoundException::new);

		PublishedCampaignDetailResponse result = campaignMapper.entityToPublishedCampaignDetailResponse(campaignEntity);

		if (campaignEntity.getProviderId() != null) {
			ProviderEntity provider = providerRepository.findById(campaignEntity.getProviderId()).orElse(ProviderEntity.builder().build());
			result.setProvider(providerMapper.entityToInfo(provider));
		} else {
			throw new InvalidException(ErrorCode.PROVIDER_INFO_NOT_FOUND);
		}
		return result;
	}

	@Override
	@Transactional
	public CampaignResponse artistUpdateStatus(Long artistId, UpdateCampaignStatusRequest request) {
		var campaignEntity = campaignRepository.findById(request.getId())
			.orElseThrow(() -> new EntityNotFoundException("Yêu cầu chiến dịch " + request.getId() + " không tìm thấy"));

		if (!campaignEntity.getOwner().getId().equals(artistId)) {
			throw new InvalidException(ErrorCode.CAMPAIGN_OWNER_INVALID);
		}

		ArtistEntity artistEntity = artistRepository.getReferenceById(artistId);

		var response = switch (request.getStatus()) {
			case CANCELED -> artistCancelCampaign(campaignEntity, artistEntity, request.getMessage());
			case WAITING -> artistSubmitCampaign(campaignEntity, artistEntity, request.getMessage());
			default ->
				throw new InvalidException(ErrorCode.UPDATE_CAMPAIGN_STATUS_FAILED, "Trạng thái chiến dịch chỉ có thể cập nhật sang WAITING hoặc CANCELED");
		};
		notificationService.sendAll(Role.ADMIN, NotificationMessage.builder()
			.type(NotificationType.GROUP)
			.title("Cập nhật trạng thái chiến dịch")
			.content("Trạng thái chiến dịch " + campaignEntity.getName() + " đã được cập nhật sang " + request.getStatus().name())
			.referenceData(ReferenceData.builder()
				.referenceEntity(ReferenceEntity.CAMPAIGN_REQUEST)
				.id(campaignEntity.getId().toString())
				.build())
			.build());
		return response;
	}

	private CampaignResponse artistCancelCampaign(CampaignEntity campaignEntity, ArtistEntity artistEntity, String message) {
		if (campaignEntity.getStatus() != CampaignStatus.DRAFT.getByteValue()
			&& campaignEntity.getStatus() != CampaignStatus.REQUEST_CHANGE.getByteValue()
			&& campaignEntity.getStatus() != CampaignStatus.WAITING.getByteValue()) {
			throw new InvalidException(ErrorCode.UPDATE_CAMPAIGN_STATUS_FAILED, "Trạng thái của chiến dịch yêu cầu chỉ có thể cập nhật từ DRAFT, REQUEST_CHANGE hoặc WAITING sang CANCELED");
		}

		if (campaignEntity.getStatus() == CampaignStatus.WAITING.getByteValue()) {
			campaignEntity.getProductInCampaigns().forEach(productInCampaignEntity -> {
				var customProductEntity = productInCampaignEntity.getCustomProduct();
				customProductEntity.setCampaignLock(null);
				customProductRepository.save(customProductEntity);
			});
		}

		campaignEntity.getProductInCampaigns().forEach(productInCampaignEntity -> {
			var customProductEntity = productInCampaignEntity.getCustomProduct();
			var savedCustomProduct = customProductMapper.entityToDetailResponse(customProductEntity);
			try {
				productInCampaignEntity.setSavedCustomProduct(jsonMapper.writeValueAsString(savedCustomProduct));
				productInCampaignRepository.save(productInCampaignEntity);
			} catch (JsonProcessingException e) {
				log.error("Parse customProductResponse to json fail", e);
			}
		});

		campaignEntity.setStatus(CampaignStatus.CANCELED.getByteValue());
		campaignEntity.getCampaignHistories().add(
			CampaignHistoryEntity.builder()
				.id(CampaignHistoryId.builder().campaignId(campaignEntity.getId()).build())
				.action(CampaignHistoryAction.CANCEL.getByteValue())
				.message(message)
				.updatedBy(artistEntity.getUsername())
				.build()
		);

		return campaignMapper.entityToResponse(campaignRepository.save(campaignEntity));
	}

	private CampaignResponse artistSubmitCampaign(CampaignEntity campaignEntity, ArtistEntity artistEntity, String message) {
		if (campaignEntity.getStatus() != CampaignStatus.DRAFT.getByteValue()
			&& campaignEntity.getStatus() != CampaignStatus.REQUEST_CHANGE.getByteValue()) {
			throw new InvalidException(ErrorCode.UPDATE_CAMPAIGN_STATUS_FAILED, "Trạng thái của chiến dịch yêu cầu chỉ có thể cập nhật từ DRAFT, REQUEST_CHANGE sang WAITING");
		}

		if (campaignEntity.getFrom() == null || campaignEntity.getTo() == null) {
			throw new InvalidException(ErrorCode.FROM_TO_VALIDATION);
		}

		if (campaignEntity.getProductInCampaigns().isEmpty()) {
			throw new InvalidException(ErrorCode.PRODUCT_CAMPAIGN_VALIDATION);
		}

		for (var productInCampaignEntity : campaignEntity.getProductInCampaigns()) {
			if (productInCampaignEntity.getQuantity() == null) {
				throw new InvalidException(ErrorCode.PRODUCT_CAMPAIGN_QUANTITY_VALIDATION, "Sản phẩm " + productInCampaignEntity.getId() + " phải có thông tin số lượng");
			}
			if (productInCampaignEntity.getPriceUnit() == null || productInCampaignEntity.getPriceAmount() == null) {
				throw new InvalidException(ErrorCode.PRODUCT_CAMPAIGN_PRICE_VALIDATION, "Sản phẩm " + productInCampaignEntity.getId() + " phải có thông tin giá");
			}
		}

		campaignEntity.getProductInCampaigns().stream()
			.map(ProductInCampaignEntity::getCustomProduct)
			.forEach(customProductEntity -> {
				if (customProductEntity.getCampaignLock() != null) {
					throw new InvalidException(ErrorCode.LOCKED_CUSTOM_PRODUCT, "Sản phẩm thiết kế " + customProductEntity.getId() + " hiện đang thuộc về chiến dịch " + customProductEntity.getCampaignLock());
				}
				customProductEntity.setCampaignLock(campaignEntity.getId());
				customProductRepository.save(customProductEntity);
			});

		campaignEntity.setStatus(CampaignStatus.WAITING.getByteValue());
		campaignEntity.getCampaignHistories().add(
			CampaignHistoryEntity.builder()
				.id(CampaignHistoryId.builder().campaignId(campaignEntity.getId()).build())
				.action(CampaignHistoryAction.SUBMIT.getByteValue())
				.message(message)
				.updatedBy(artistEntity.getUsername())
				.build()
		);

		return campaignMapper.entityToResponse(campaignRepository.save(campaignEntity));
	}


	@Override
	@Transactional
	public CampaignResponse reviewCampaign(Long staffId, UpdateCampaignStatusRequest request) {
		var campaignEntity = campaignRepository.findById(request.getId())
			.orElseThrow(() -> new EntityNotFoundException("campaignId " + request.getId() + " not valid"));

		AccountEntity accountEntity = accountRepository.getReferenceById(staffId);
		var response = switch (request.getStatus()) {
			case REQUEST_CHANGE -> staffRequestChangeCampaign(campaignEntity, accountEntity, request.getMessage());
			case APPROVED -> staffApproveCampaign(campaignEntity, accountEntity, request.getMessage());
			case REJECTED -> staffRejectCampaign(campaignEntity, accountEntity, request.getMessage());
			case MANUFACTURING -> staffStartManufactureCampaign(campaignEntity, accountEntity, request.getMessage());
			default ->
				throw new InvalidException(ErrorCode.UPDATE_CAMPAIGN_STATUS_FAILED, "Trạng thái chiến dịch chỉ có thể cập nhật thành REQUEST_CHANGE, APPROVED, REJECTED hoặc MANUFACTURING");
		};

		Long ownerId = campaignEntity.getOwner().getId();
		notificationService.sendTo(ownerId, NotificationMessage.builder()
			.type(NotificationType.PRIVATE)
			.ownerId(ownerId)
			.title("Cập nhật trạng thái chiến dịch")
			.content("Trạng thái chiến dịch " + campaignEntity.getName() + " đã được cập nhật sang " + request.getStatus().name())
			.referenceData(ReferenceData.builder()
				.referenceEntity(ReferenceEntity.CAMPAIGN_REQUEST)
				.id(campaignEntity.getId().toString())
				.build())
			.build());
		return response;
	}

	private CampaignResponse staffRequestChangeCampaign(CampaignEntity campaignEntity,
														AccountEntity accountEntity,
														String message) {
		if (campaignEntity.getStatus() != CampaignStatus.WAITING.getByteValue()) {
			throw new InvalidException(ErrorCode.UPDATE_CAMPAIGN_STATUS_FAILED, "Trạng thái chiến dịch chỉ có thể cập nhật từ WAITING sang REQUEST_CHANGE");
		}

		campaignEntity.getProductInCampaigns().stream()
			.map(ProductInCampaignEntity::getCustomProduct)
			.forEach(inventoryItemEntity -> {
				inventoryItemEntity.setCampaignLock(null);
				customProductRepository.save(inventoryItemEntity);
			});

		campaignEntity.setStatus(CampaignStatus.REQUEST_CHANGE.getByteValue());
		campaignEntity.getCampaignHistories().add(
			CampaignHistoryEntity.builder()
				.id(CampaignHistoryId.builder().campaignId(campaignEntity.getId()).build())
				.action(CampaignHistoryAction.REQUEST_CHANGE.getByteValue())
				.message(message)
				.updatedBy(accountEntity.getUsername())
				.build()
		);

		return campaignMapper.entityToResponse(campaignRepository.save(campaignEntity));
	}

	private CampaignResponse staffApproveCampaign(CampaignEntity campaignEntity,
												  AccountEntity accountEntity,
												  String message) {
		if (campaignEntity.getStatus() != CampaignStatus.WAITING.getByteValue()) {
			throw new InvalidException(ErrorCode.UPDATE_CAMPAIGN_STATUS_FAILED, "Trạng thái chiến dịch chỉ có thể cập nhật từ WAITING sang APPROVED");
		}

		campaignEntity.getProductInCampaigns()
			.forEach(productInCampaign -> {
				var customProductEntity = productInCampaign.getCustomProduct();
				customProductEntity.setCampaignLock(null);
				customProductRepository.save(customProductEntity);

				String providerId = productInCampaign.getCampaign().getProviderId();
				Long variantId = productInCampaign.getCustomProduct().getVariant().getId();
				ProductVariantProviderEntity providerConfig = productVariantProviderRepository.findById(new ProductVariantProviderId(variantId, providerId))
					.orElseThrow(() -> new InvalidException(ErrorCode.UNSUPPORTED_PROVIDER, "Không tìm thấy thông tin cung cấp cho mẫu " + variantId + " và nhà cung cấp " + providerId));
				productInCampaign.setBasePriceAmount(providerConfig.getBasePriceAmount());
				productInCampaign.setManufacturingTime(providerConfig.getManufacturingTime());
				productInCampaign.setMinQuantity(providerConfig.getMinQuantity());
				var savedCustomProduct = customProductMapper.entityToDetailResponse(customProductEntity);
				try {
					productInCampaign.setSavedCustomProduct(jsonMapper.writeValueAsString(savedCustomProduct));
					productInCampaignRepository.save(productInCampaign);
				} catch (JsonProcessingException e) {
					log.error("Parse customProductResponse to json fail", e);
				}
				productInCampaignRepository.save(productInCampaign);
			});

		campaignEntity.setStatus(CampaignStatus.APPROVED.getByteValue());
		campaignEntity.getCampaignHistories().add(
			CampaignHistoryEntity.builder()
				.id(CampaignHistoryId.builder().campaignId(campaignEntity.getId()).build())
				.action(CampaignHistoryAction.APPROVE.getByteValue())
				.message(message)
				.updatedBy(accountEntity.getUsername())
				.build()
		);

		int profitPercentage = Integer.parseInt(systemConfigService.getOrThrow(DEFAULT_PROFIT_PERCENTAGE, () -> new ArtiexhConfigException("Missing artiexh default profit percentage")));
		campaignEntity.setAdminProfitPercentage(profitPercentage);

		return campaignMapper.entityToResponse(campaignRepository.save(campaignEntity));
	}

	private CampaignResponse staffRejectCampaign(CampaignEntity campaignEntity,
												 AccountEntity accountEntity,
												 String message) {
		if (campaignEntity.getStatus() != CampaignStatus.WAITING.getByteValue()) {
			throw new InvalidException(ErrorCode.UPDATE_CAMPAIGN_STATUS_FAILED, "Trạng thái chiến dịch chỉ có thể cập nhật từ WAITING sang REJECTED");
		}

		campaignEntity.getProductInCampaigns().forEach(productInCampaignEntity -> {
			var inventoryItemEntity = productInCampaignEntity.getCustomProduct();
			inventoryItemEntity.setCampaignLock(null);
			customProductRepository.save(inventoryItemEntity);

			var savedCustomProduct = customProductMapper.entityToDetailResponse(productInCampaignEntity.getCustomProduct());
			try {
				productInCampaignEntity.setSavedCustomProduct(jsonMapper.writeValueAsString(savedCustomProduct));
				productInCampaignRepository.save(productInCampaignEntity);
			} catch (JsonProcessingException e) {
				log.error("Parse customProductResponse to json fail", e);
			}
		});

		campaignEntity.setStatus(CampaignStatus.REJECTED.getByteValue());
		campaignEntity.getCampaignHistories().add(
			CampaignHistoryEntity.builder()
				.id(CampaignHistoryId.builder().campaignId(campaignEntity.getId()).build())
				.action(CampaignHistoryAction.REJECT.getByteValue())
				.message(message)
				.updatedBy(accountEntity.getUsername())
				.build()
		);

		return campaignMapper.entityToResponse(campaignRepository.save(campaignEntity));
	}

	private CampaignResponse staffStartManufactureCampaign(CampaignEntity campaignEntity,
														   AccountEntity accountEntity,
														   String message) {
		if (campaignEntity.getStatus() != CampaignStatus.APPROVED.getByteValue()) {
			throw new InvalidException(ErrorCode.UPDATE_CAMPAIGN_STATUS_FAILED, "Trạng thái chiến dịch chỉ có thể cập nhật từ APPROVED sang MANUFACTURING");
		}

		campaignEntity.setStatus(CampaignStatus.MANUFACTURING.getByteValue());
		campaignEntity.getCampaignHistories().add(
			CampaignHistoryEntity.builder()
				.id(CampaignHistoryId.builder().campaignId(campaignEntity.getId()).build())
				.action(CampaignHistoryAction.MANUFACTURING.getByteValue())
				.message(message)
				.updatedBy(accountEntity.getUsername())
				.build()
		);

		return campaignMapper.entityToResponse(campaignRepository.save(campaignEntity));
	}

	@Override
	@Transactional
	public void staffFinishManufactureCampaign(Map<String, Long> productQuantities,
											   Long campaignId,
											   Long staffId,
											   String message) {
		var campaignEntity = campaignRepository.findById(campaignId)
			.orElseThrow(() -> new EntityNotFoundException("Chiến dịch " + campaignId + " không tìm thấy"));

		Set<ProductInventoryQuantity> productInventoryQuantities = new HashSet<>();
		Set<ProductInventoryCode> productInventoryCodes = productInventoryRepository.getAllByCampaignId(campaignEntity.getId());
		boolean isProductExisted = true;
		for (ProductInventoryCode productInventoryCode : productInventoryCodes) {
			Long quantity = productQuantities.get(productInventoryCode.getProductInCampaignId().toString());
			if (quantity == null) {
				isProductExisted = false;
			} else {
				productInventoryQuantities.add(ProductInventoryQuantity.builder()
					.productCode(productInventoryCode.getProductCode())
					.quantity(quantity)
					.build());
			}
		}
		if (productInventoryCodes.size() != productQuantities.size() || !isProductExisted) {
			throw new InvalidException(ErrorCode.PRODUCT_INVENTORY_INFO_NOT_FOUND);
		}

		AccountEntity accountEntity = accountRepository.getReferenceById(staffId);
		if (campaignEntity.getStatus() != CampaignStatus.APPROVED.getByteValue()
			&& campaignEntity.getStatus() != CampaignStatus.MANUFACTURING.getByteValue()) {
			throw new InvalidException(ErrorCode.UPDATE_CAMPAIGN_STATUS_FAILED, "Trạng thái chiến dịch chỉ có thể cập nhật từ APPROVED hoặc MANUFACTURING sang MANUFACTURED");
		}

		campaignEntity.setStatus(CampaignStatus.MANUFACTURED.getByteValue());
		campaignEntity.getCampaignHistories().add(
			CampaignHistoryEntity.builder()
				.id(CampaignHistoryId.builder().campaignId(campaignEntity.getId()).build())
				.action(CampaignHistoryAction.MANUFACTURED.getByteValue())
				.message(message)
				.updatedBy(accountEntity.getUsername())
				.build()
		);

		campaignEntity = campaignRepository.save(campaignEntity);

		productInventoryJpaService.updateQuantityFromCampaignRequest(
			campaignEntity.getId(),
			campaignEntity.getName(),
			productInventoryQuantities
		);

		campaignMapper.entityToResponse(campaignEntity);

		Long ownerId = campaignEntity.getOwner().getId();
		notificationService.sendTo(ownerId, NotificationMessage.builder()
			.type(NotificationType.PRIVATE)
			.ownerId(ownerId)
			.title("Cập nhật trạng thái chiến dịch")
			.content("Trạng thái chiến dịch " + campaignEntity.getName() + " đã được cập nhật sang " + CampaignStatus.MANUFACTURED.name())
			.referenceData(ReferenceData.builder()
				.referenceEntity(ReferenceEntity.CAMPAIGN_REQUEST)
				.id(campaignEntity.getId().toString())
				.build())
			.build());
	}

	@Override
	public Page<CampaignHistory> getCampaignHistory(Long campaignId, Pageable pageable) {
		Page<CampaignHistoryEntity> historyPage = campaignHistoryRepository.findCampaignHistoryEntitiesByIdCampaignId(campaignId, pageable);
		return historyPage.map(campaignMapper::campaignHistoryEntityToCampaignHistory);
	}

	@Override
	@Transactional
	public Set<ProductResponse> finalizeProduct(Long campaignId, Set<FinalizeProductRequest> request) {
		CampaignEntity campaign = campaignRepository.findById(campaignId).orElseThrow(EntityNotFoundException::new);

		if (!CampaignStatus.ALLOWED_PUBLISHED_STATUS.contains(CampaignStatus.fromValue(campaign.getStatus()))) {
			throw new InvalidException(ErrorCode.FINALIZED_CAMPAIGN_NOT_ALLOWED);
		}

		if (Boolean.TRUE.equals(campaign.getIsFinalized())) {
			throw new InvalidException(ErrorCode.CAMPAIGN_REQUEST_FINALIZED);
		}

		Set<Long> productCampaignIds = campaign.getProductInCampaigns().stream()
			.map(ProductInCampaignEntity::getId)
			.collect(Collectors.toSet());
		Set<Long> productIds = request.stream()
			.map(FinalizeProductRequest::getProductInCampaignId)
			.collect(Collectors.toSet());

		if (!productCampaignIds.equals(productIds)) {
			throw new InvalidException(ErrorCode.FINALIZED_CAMPAIGN_PRODUCT_VALIDATION);
		}

		Set<String> productCodes = request.stream()
			.map(FinalizeProductRequest::getProductCode)
			.collect(Collectors.toSet());
		boolean isExistedCode = productInventoryRepository.existsAllByProductCodeIn(productCodes);
		if (isExistedCode || productCodes.size() != request.size()) {
			throw new InvalidException(ErrorCode.PRODUCT_CODE_DUPLICATED);
		}
		Set<ProductResponse> productResponses = new HashSet<>();
		for (FinalizeProductRequest finalizeProductRequest : request) {
			ProductInCampaignEntity productInCampaign =
				productInCampaignRepository.findById(finalizeProductRequest.getProductInCampaignId())
					.orElseThrow(() -> new InvalidException(ErrorCode.PRODUCT_CAMPAIGN_INFO_NOT_FOUND, ErrorCode.PRODUCT_CAMPAIGN_INFO_NOT_FOUND.getMessage() + finalizeProductRequest.getProductInCampaignId()));

			//Update ProductInCampaign

			if (campaign.getStatus().byteValue() == CampaignStatus.APPROVED.getByteValue()) {
				ProductInCampaign uncommittedProduct = productInCampaignMapper.requestToDomain(finalizeProductRequest);
				productInCampaignService.update(productInCampaign, uncommittedProduct);
			}

			//Product product = productMapper.productInCampaignToProduct(uncommittedProduct);
			ProductInventory product = productMapper.finalizeProductRequestToProduct(finalizeProductRequest);
			product.setTags(productInCampaign.getCustomProduct().getTags().stream().map(productTagMapper::entityToDomain).collect(Collectors.toSet()));
			product.setCategory(productCategoryMapper.entityToDomain(productInCampaign.getCustomProduct().getCategory()));
			product.setPaymentMethods(Set.of(PaymentMethod.VN_PAY.getByteValue()));
			product.setManufacturingPrice(productInCampaign.getBasePriceAmount());

			product = productService.create(campaign.getOwner().getId(), product, productInCampaign);
			productResponses.add(productMapper.domainToProductResponse(product));
		}

		campaign.setIsFinalized(true);
		campaignRepository.save(campaign);

		Long ownerId = campaign.getOwner().getId();
		notificationService.sendTo(ownerId, NotificationMessage.builder()
			.type(NotificationType.PRIVATE)
			.ownerId(ownerId)
			.title("Cập nhật trạng thái chiến dịch")
			.content("Chiến dịch " + campaign.getName() + " đã được xác nhận lần cuối")
			.referenceData(ReferenceData.builder()
				.referenceEntity(ReferenceEntity.CAMPAIGN_REQUEST)
				.id(campaign.getId().toString())
				.build())
			.build());

		return productResponses;
	}

	@Override
	public Page<ProductInCampaignResponse> getAllProductCampaign(Long campaignId, Pageable pageable) {
		return productInCampaignRepository.findAllByCampaignId(campaignId, pageable).map(productInCampaignMapper::entityToResponse);
	}

	@Override
	public ProductInCampaignDetailResponse getProductInCampaign(Long campaignId, Long productId) {
		ProductInCampaignEntity campaignProduct = productInCampaignRepository.findAllByCampaignIdAndId(campaignId, productId)
			.orElseThrow(EntityNotFoundException::new);
		return productInCampaignMapper.entityToDetailResponse(campaignProduct);
	}

}
