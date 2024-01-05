package com.artiexh.api.service.marketplace.impl;

import com.artiexh.api.base.exception.ErrorCode;
import com.artiexh.api.base.exception.InvalidException;
import com.artiexh.api.service.marketplace.ProductOpenSearchService;
import com.artiexh.api.service.marketplace.ProductService;
import com.artiexh.api.service.marketplace.SaleCampaignService;
import com.artiexh.api.service.notification.NotificationService;
import com.artiexh.api.service.productinventory.ProductInventoryJpaService;
import com.artiexh.data.jpa.entity.*;
import com.artiexh.data.jpa.entity.embededmodel.ReferenceData;
import com.artiexh.data.jpa.entity.embededmodel.ReferenceEntity;
import com.artiexh.data.jpa.projection.ProductInSaleId;
import com.artiexh.data.jpa.projection.SoldProduct;
import com.artiexh.data.jpa.repository.*;
import com.artiexh.data.opensearch.model.ProductDocument;
import com.artiexh.model.domain.*;
import com.artiexh.model.mapper.CampaignSaleMapper;
import com.artiexh.model.mapper.ProductMapper;
import com.artiexh.model.rest.PaginationAndSortingRequest;
import com.artiexh.model.rest.marketplace.salecampaign.filter.MarketplaceSaleCampaignFilter;
import com.artiexh.model.rest.marketplace.salecampaign.request.ProductInSaleRequest;
import com.artiexh.model.rest.marketplace.salecampaign.request.SaleCampaignRequest;
import com.artiexh.model.rest.marketplace.salecampaign.request.UpdateProductInSaleRequest;
import com.artiexh.model.rest.marketplace.salecampaign.response.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SaleCampaignServiceImpl implements SaleCampaignService {
	private final CampaignSaleRepository campaignSaleRepository;
	private final ProductInventoryRepository productInventoryRepository;
	private final ArtistRepository artistRepository;
	private final CampaignRepository campaignRepository;
	private final ProductRepository productRepository;
	private final CampaignSaleMapper campaignSaleMapper;
	private final ProductMapper productMapper;
	private final ProductService productService;
	private final ProductOpenSearchService productOpenSearchService;
	private final ProductInventoryJpaService productInventoryJpaService;
	private final NotificationService notificationService;

	@Override
	@Transactional
	public SaleCampaignDetailResponse createSaleCampaign(long creatorId, SaleCampaignRequest request) {
		if (request.getPublicDate().isAfter(request.getFrom())) {
			throw new InvalidException(ErrorCode.PUBLIC_DATE_INVALID);
		}

		if (request.getFrom().isAfter(request.getTo())) {
			throw new InvalidException(ErrorCode.FROM_DATE_INVALID);
		}

		ArtistEntity artistEntity = artistRepository.findById(request.getArtistId())
			.orElseThrow(() -> new InvalidException(ErrorCode.ARTIST_INFO_NOT_FOUND));


		var entity = campaignSaleRepository.save(CampaignSaleEntity.builder()
			.name(request.getName())
			.description(request.getDescription())
			.publicDate(request.getPublicDate())
			.from(request.getFrom())
			.to(request.getTo())
			.createdBy(creatorId)
			.owner(artistEntity)
			.content(request.getContent())
			.thumbnailUrl(request.getThumbnailUrl())
			.type(request.getType().getByteValue())
			.status(CampaignSaleStatus.DRAFT.getByteValue())
			.build());

		notificationService.sendTo(artistEntity.getId(), NotificationMessage.builder()
			.type(NotificationType.PRIVATE)
			.ownerId(artistEntity.getId())
			.title("Chiến dịch mới mới")
			.content("Bạn vưa có một chiến dịch bán mới " + entity.getName())
			.referenceData(ReferenceData.builder()
				.referenceEntity(ReferenceEntity.CAMPAIGN_SALE)
				.id(entity.getId().toString())
				.build())
			.build());

		return campaignSaleMapper.entityToDetailResponse(entity);
	}

	@Override
	@Transactional
	public SaleCampaignDetailResponse createSaleCampaign(long creatorId, Long campaignRequestId) {
		CampaignEntity campaignEntity = campaignRepository.findById(campaignRequestId)
			.orElseThrow(() -> new EntityNotFoundException("Yêu cầu chiến dịch " + campaignRequestId + " không tìm thấy"));

		var profitPercentage = BigDecimal.valueOf(campaignEntity.getAdminProfitPercentage());

		if (!Boolean.TRUE.equals(campaignEntity.getIsFinalized())) {
			throw new InvalidException(ErrorCode.CAMPAIGN_REQUEST_NOT_FINALIZED);
		}

		if (campaignEntity.getCampaignSale() != null) {
			throw new InvalidException(ErrorCode.CAMPAIGN_REQUEST_USED);
		}

		var entity = campaignSaleRepository.save(CampaignSaleEntity.builder()
			.name(campaignEntity.getName())
			.description(campaignEntity.getDescription())
			.from(campaignEntity.getFrom())
			.to(campaignEntity.getTo())
			.createdBy(creatorId)
			.owner(campaignEntity.getOwner())
			.content(campaignEntity.getContent())
			.thumbnailUrl(campaignEntity.getThumbnailUrl())
			.type(campaignEntity.getType())
			.status(CampaignSaleStatus.DRAFT.getByteValue())
			.campaignRequest(campaignEntity)
			.build());
		var result = campaignSaleMapper.entityToDetailResponse(entity);

		Set<Long> productInCampaignIds = campaignEntity.getProductInCampaigns().stream()
			.map(ProductInCampaignEntity::getId)
			.collect(Collectors.toSet());

		var productInventoryEntities = productInventoryRepository.findAllByProductInCampaignIdIn(productInCampaignIds);
		if (productInventoryEntities.size() != productInCampaignIds.size()) {
			throw new InvalidException(ErrorCode.PRODUCT_FINALIZED_NOT_ENOUGH);
		}

		Set<ProductEntity> productEntities = productInventoryEntities.stream()
			.map(productInventory -> {
				var profit = productInventory.getPriceAmount()
					.subtract(productInventory.getManufacturingPrice());
				var artistProfit = profit.subtract(
					profit.multiply(profitPercentage).divide(BigDecimal.valueOf(100), RoundingMode.HALF_EVEN)
				);
				return ProductEntity.builder()
					.id(new ProductEntityId(productInventory.getProductCode(), entity.getId()))
					.productInventory(productInventory)
					.campaignSale(entity)
					.priceAmount(productInventory.getPriceAmount())
					.priceUnit(productInventory.getPriceUnit())
					.artistProfit(artistProfit)
					.build();
			})
			.collect(Collectors.toSet());

		productEntities.forEach(productService::create);

		Long ownerId = campaignEntity.getOwner().getId();
		notificationService.sendTo(ownerId, NotificationMessage.builder()
			.type(NotificationType.PRIVATE)
			.ownerId(ownerId)
			.title("Chiến dịch mới mới")
			.content("Bạn vưa có một chiến dịch bán mới " + entity.getName())
			.referenceData(ReferenceData.builder()
				.referenceEntity(ReferenceEntity.CAMPAIGN_SALE)
				.id(entity.getId().toString())
				.build())
			.build());
		return result;
	}

	@Override
	@Transactional
	public SaleCampaignDetailResponse updateSaleCampaign(Long id, SaleCampaignRequest request) {
		CampaignSaleEntity entity = campaignSaleRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Chiến dịch bán " + id + " không tìm thấy"));

		if (entity.getStatus() == CampaignSaleStatus.CLOSED.getByteValue()) {
			throw new InvalidException(ErrorCode.UPDATE_SALE_CAMPAIGN_FAILED);
		}

		if (entity.getStatus() == CampaignSaleStatus.ACTIVE.getByteValue()) {
			Instant now = Instant.now();
			if (now.isAfter(entity.getTo())) {
				throw new InvalidException(ErrorCode.UPDATE_SALE_CAMPAIGN_FAILED);
			}
			if (now.isAfter(entity.getFrom()) && !entity.getFrom().equals(request.getFrom())) {
				throw new InvalidException(ErrorCode.UPDATE_FROM_FAILED);
			}
			if (entity.getPublicDate() != null && now.isAfter(entity.getPublicDate())
				&& !entity.getPublicDate().equals(request.getPublicDate())) {
				throw new InvalidException(ErrorCode.UPDATE_PUBLIC_DATE_FAILED);
			}
		}

		if (request.getPublicDate() != null && request.getPublicDate().isAfter(request.getFrom())) {
			throw new InvalidException(ErrorCode.PUBLIC_DATE_INVALID);
		} else {
			entity.setPublicDate(request.getPublicDate());
		}

		if (request.getFrom().isAfter(request.getTo())) {
			throw new InvalidException(ErrorCode.FROM_DATE_INVALID);
		} else {
			entity.setFrom(request.getFrom());
			entity.setTo(request.getTo());
		}

		ArtistEntity artistEntity = artistRepository.findById(request.getArtistId())
			.orElseThrow(() -> new InvalidException(ErrorCode.ARTIST_INFO_NOT_FOUND));

		if (entity.getOwner().getId() == null) {
			entity.setOwner(artistEntity);
		} else if (entity.getProducts().isEmpty()) {
			entity.setOwner(artistEntity);
		} else if (!entity.getOwner().getId().equals(artistEntity.getId())) {
			throw new InvalidException(ErrorCode.NOT_ALLOWED_OWNER_UPDATED);
		}

		entity.setName(request.getName());
		entity.setDescription(request.getDescription());
		entity.setContent(request.getContent());
		entity.setThumbnailUrl(request.getThumbnailUrl());
		entity.setType(request.getType().getByteValue());

		//Update Campaign info to Opensearch
		ProductDocument.Campaign campaignInfo = campaignSaleMapper.entityToDocument(entity);
		Set<ProductInSaleId> productInSaleIds = productRepository.findAllByCampaignSaleId(entity.getId());
		Map<String, ProductDocument.Campaign> campaignMap = new HashMap<>();
		for (ProductInSaleId productInSaleId : productInSaleIds) {
			campaignMap.put(productInSaleId.getCampaignSaleId().toString() + "-" + productInSaleId.getProductCode(), campaignInfo);
		}
		productOpenSearchService.updateCampaignInfo(campaignMap);
		productService.refreshOpenSearchIndex();

		return campaignSaleMapper.entityToDetailResponse(entity);
	}

	@Override
	@Transactional
	public void updateStatus(Long id, CampaignSaleStatus status) {
		CampaignSaleEntity entity = campaignSaleRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Chiến dịch bán " + id + " không tìm thấy"));

		if (entity.getStatus() == status.getByteValue()) {
			return;
		}

		switch (CampaignSaleStatus.from(entity.getStatus())) {
			case DRAFT -> updateCampaignFromDraft(entity, status);
			case ACTIVE -> updateCampaignFromActive(entity, status);
			case CLOSED -> throw new InvalidException(ErrorCode.NOT_ALLOWED_CLOSED_CAMPAIGN);
		}

		Long ownerId = entity.getOwner().getId();
		notificationService.sendTo(ownerId, NotificationMessage.builder()
			.type(NotificationType.PRIVATE)
			.ownerId(ownerId)
			.title("Chiến dịch cập nhật")
			.content("Trạng thái chiến dịch " + entity.getName() + " vừa được cập nhật sang " + status)
			.referenceData(ReferenceData.builder()
				.referenceEntity(ReferenceEntity.CAMPAIGN_SALE)
				.id(entity.getId().toString())
				.build())
			.build());
	}

	private void updateCampaignFromDraft(CampaignSaleEntity entity, CampaignSaleStatus status) {
		if (status == CampaignSaleStatus.CLOSED) {
			entity.setStatus(status.getByteValue());
			campaignSaleRepository.save(entity);
			refundProductInventory(entity, status, entity.getProducts());
		} else { // ACTIVE
			entity.setStatus(status.getByteValue());
			campaignSaleRepository.save(entity);
			for (var productEntity : entity.getProducts()) {
				productOpenSearchService.updateCampaignStatus(
					entity.getId(),
					productEntity.getProductInventory().getProductCode(),
					status
				);
			}
		}
	}

	private void updateCampaignFromActive(CampaignSaleEntity entity, CampaignSaleStatus status) {
		if (status == CampaignSaleStatus.DRAFT) {
			Instant now = Instant.now();
			if (now.isAfter(entity.getFrom())) {
				throw new InvalidException(ErrorCode.NOT_ALLOWED_DRAFT_CAMPAIGN);
			}
			for (var productEntity : entity.getProducts()) {
				productOpenSearchService.updateCampaignStatus(
					entity.getId(),
					productEntity.getProductInventory().getProductCode(),
					status
				);
			}
		} else {
			refundProductInventory(entity, status, entity.getProducts());
		}

		entity.setStatus(status.getByteValue());
		campaignSaleRepository.save(entity);
	}

	@Override
	public Page<SaleCampaignResponse> getAll(Pageable pageable, Specification<CampaignSaleEntity> specification) {
		return campaignSaleRepository.findAll(specification, pageable)
			.map(campaignSaleMapper::entityToResponse);
	}

	@Override
	public SaleCampaignDetailResponse getDetail(Long id) {
		return campaignSaleRepository.findById(id)
			.map(campaignSaleMapper::entityToDetailResponse)
			.orElseThrow(EntityNotFoundException::new);
	}

	@Override
	public SaleCampaignDetailResponse getDetail(Long id, Long ownerId) {
		return campaignSaleRepository.findCampaignSaleEntityByIdAndOwnerId(id, ownerId)
			.map(campaignSaleMapper::entityToDetailResponse)
			.orElseThrow(EntityNotFoundException::new);
	}

	@Override
	public Page<SaleCampaignResponse> getAllByArtist(String artistUsername,
													 Pageable pageable,
													 MarketplaceSaleCampaignFilter filter) {
		if (!artistRepository.existsByUsername(artistUsername)) {
			throw new EntityNotFoundException();
		}
		filter.setUsername(artistUsername);
		return getAll(pageable, filter.getMarketplaceSpecification(true));
	}

	@Override
	@Transactional(readOnly = true)
	public CampaignStatistics getStatistics(Long campaignId) {
		CampaignSaleEntity campaignEntity = campaignSaleRepository.findById(campaignId).orElseThrow(EntityNotFoundException::new);

		PaginationAndSortingRequest pagination = new PaginationAndSortingRequest();
		pagination.setSortBy("soldQuantity");
		List<SoldProduct> products = getSoldProduct(campaignId, pagination.getPageable()).getContent();

		BigDecimal revenue = BigDecimal.ZERO;
		BigDecimal profit = BigDecimal.ZERO;
		List<ProductStatisticResponse> productStatisticResponses = new LinkedList<>();

		for (SoldProduct product : products) {
			revenue = revenue.add(product.getRevenue() == null ? BigDecimal.ZERO : product.getRevenue());
			profit = profit.add(product.getArtistProfit() == null ? BigDecimal.ZERO : product.getArtistProfit());
			productStatisticResponses.add(
				productMapper.entityToStatisticResponse(product)
			);
		}

		ProductStatisticResponse bestSold = productStatisticResponses.stream().findFirst().orElse(null);
		ProductStatisticResponse worstSold = productStatisticResponses.stream().reduce((prev, next) -> next).orElse(null);

		return CampaignStatistics.builder()
			.revenue(Money.builder()
				.amount(revenue)
				.unit("VND")
				.build())
			.profit(Money.builder()
				.amount(profit)
				.unit("VND")
				.build())
			.campaignId(campaignId)
			.from(campaignEntity.getFrom())
			.to(campaignEntity.getTo())
			.name(campaignEntity.getName())
			.products(productStatisticResponses)
			.bestSoldProduct(bestSold)
			.worstSoldProduct(worstSold)
			.build();
	}

	@Override
	@Transactional
	public Set<ProductResponse> createProductInSaleCampaign(Long campaignId, Set<ProductInSaleRequest> requests) {
		CampaignSaleEntity entity = campaignSaleRepository.findById(campaignId)
			.orElseThrow(() -> new EntityNotFoundException("Sale campaign " + campaignId + " not found"));

		if (entity.getStatus() != CampaignSaleStatus.DRAFT.getByteValue()) {
			throw new InvalidException(ErrorCode.ADD_PRODUCT_CAMPAIGN_SALE_FAILED);
		}

		if (entity.getCampaignRequest() != null) {
			throw new InvalidException(ErrorCode.ADD_PRODUCT_CAMPAIGN_SALE_FROM_REQUEST_FAILED);
		}

		if (entity.getOwner() == null) {
			throw new InvalidException(ErrorCode.OWNER_NOT_FOUND);
		}

		Set<ProductEntity> products = requests.stream()
			.map(request -> {
				ProductInventoryEntity productInventory = productInventoryRepository.findById(request.getProductCode())
					.orElseThrow(() -> new InvalidException(ErrorCode.PRODUCT_INVENTORY_INFO_NOT_FOUND));
				if (!productInventory.getOwner().getId().equals(entity.getOwner().getId())) {
					throw new InvalidException(ErrorCode.PRODUCT_INVENTORY_OWNER_INVALID, ErrorCode.PRODUCT_INVENTORY_OWNER_INVALID.getMessage() + request.getProductCode());
				}
				if (request.getQuantity() != null && request.getQuantity() > productInventory.getQuantity()) {
					throw new InvalidException(ErrorCode.QUANTITY_NOT_ENOUGH);
				}
				return Pair.of(request, productInventory);
			})
			.map(pair -> {
				var productEntityBuilder = ProductEntity.builder()
					.id(new ProductEntityId(pair.getFirst().getProductCode(), entity.getId()))
					.productInventory(pair.getSecond())
					.campaignSale(entity)
					.artistProfit(pair.getFirst().getArtistProfit());

				if (pair.getFirst().getPrice() != null) {
					productEntityBuilder
						.priceAmount(pair.getFirst().getPrice().getAmount())
						.priceUnit(pair.getFirst().getPrice().getUnit());
				}

				if (pair.getFirst().getQuantity() != null) {
					productEntityBuilder.quantity(pair.getFirst().getQuantity());
				}

				return productEntityBuilder.build();
			})
			.collect(Collectors.toSet());

		reduceProductInventory(entity, CampaignSaleStatus.DRAFT, products);
		Stream<Product> result = products.stream().map(productService::create);
		productService.refreshOpenSearchIndex();

		return result.map(productMapper::domainToProductResponse)
			.collect(Collectors.toSet());
	}

	@Override
	@Transactional
	public ProductResponse updateProductInSaleCampaign(long campaignId,
													   String productCode,
													   UpdateProductInSaleRequest request) {
		ProductEntity productEntity = productRepository.findById(new ProductEntityId(productCode, campaignId))
			.orElseThrow(() -> new EntityNotFoundException("Product " + productCode + " in campaign " + campaignId + " not found"));

		if (productEntity.getCampaignSale().getStatus() == CampaignSaleStatus.CLOSED.getByteValue()) {
			throw new InvalidException(ErrorCode.UPDATE_PRODUCT_CAMPAIGN_SALE_FAILED);
		}

		Instant now = Instant.now();
		if (now.isAfter(productEntity.getCampaignSale().getTo())) {
			throw new InvalidException(ErrorCode.UPDATE_PRODUCT_CAMPAIGN_SALE_FAILED);
		}

		if (request.getPrice() != null) {
			if (productEntity.getCampaignSale().getCampaignRequest() != null) {
				throw new InvalidException(ErrorCode.UPDATE_PRODUCT_CAMPAIGN_SALE_FROM_REQUEST_FAILED);
			} else {
				productEntity.setPriceAmount(request.getPrice().getAmount());
				productEntity.setPriceUnit(request.getPrice().getUnit());
			}
		}

		if (request.getArtistProfit() != null) {
			if (productEntity.getCampaignSale().getCampaignRequest() != null) {
				throw new InvalidException(ErrorCode.UPDATE_PRODUCT_CAMPAIGN_SALE_FROM_REQUEST_FAILED);
			} else {
				productEntity.setArtistProfit(request.getArtistProfit());
			}
		}

		if (request.getQuantity() != null) {
			int compareResult = Integer.compare(request.getQuantity(), productEntity.getQuantity());

			if (compareResult > 0) {
				// add quantity
				if (productEntity.getProductInventory().getQuantity() < (request.getQuantity() - productEntity.getQuantity())) {
					throw new InvalidException(ErrorCode.QUANTITY_NOT_ENOUGH);
				} else {
					Set<ProductInventoryQuantity> productQuantities = Set.of(
						ProductInventoryQuantity.builder()
							.productCode(productEntity.getProductInventory().getProductCode())
							.quantity((long) request.getQuantity() - productEntity.getQuantity())
							.build()
					);
					productInventoryJpaService.reduceQuantity(
						campaignId,
						productEntity.getCampaignSale().getName(),
						SourceCategory.CAMPAIGN_SALE,
						productQuantities
					);
				}
			}

			if (compareResult < 0) {
				// reduce quantity
				if (request.getQuantity() < productEntity.getSoldQuantity()) {
					throw new InvalidException(ErrorCode.QUANTITY_INVALID);
				} else {
					Set<ProductInventoryQuantity> productQuantities = Set.of(
						ProductInventoryQuantity.builder()
							.productCode(productEntity.getProductInventory().getProductCode())
							.quantity((long) productEntity.getQuantity() - request.getQuantity())
							.build()
					);
					productInventoryJpaService.refundQuantity(
						campaignId,
						productEntity.getCampaignSale().getName(),
						SourceCategory.CAMPAIGN_SALE,
						productQuantities
					);
				}
			}

			productEntity.setQuantity(request.getQuantity());
		}

		return productMapper.domainToProductResponse(productService.update(productEntity));
	}

	@Override
	@Transactional
	public void deleteProductInSaleCampaign(Long campaignId, Set<String> productCodes) {
		CampaignSaleEntity entity = campaignSaleRepository.findById(campaignId)
			.orElseThrow(() -> new EntityNotFoundException("Sale campaign " + campaignId + " not found"));

		if (entity.getStatus() != CampaignSaleStatus.DRAFT.getByteValue()) {
			throw new InvalidException(ErrorCode.DELETE_PRODUCT_CAMPAIGN_SALE_FAILED);
		}

		if (entity.getCampaignRequest() != null) {
			throw new InvalidException((ErrorCode.DELETE_PRODUCT_CAMPAIGN_SALE_FROM_REQUEST_FAILED));
		}

		Set<ProductEntity> productEntities = productCodes.stream()
			.map(productCode -> productRepository.findById(new ProductEntityId(productCode, campaignId))
				.orElseThrow(EntityNotFoundException::new)
			)
			.collect(Collectors.toSet());

		refundProductInventory(entity, CampaignSaleStatus.DRAFT, productEntities);
		productEntities.forEach(productService::delete);
	}

	@Override
	@Transactional
	public void closeExpiredSaleCampaigns() {
		Instant closedTime = Instant.now().minus(Duration.ofDays(3));
		campaignSaleRepository.streamAllByStatusAndToBefore(CampaignSaleStatus.ACTIVE.getByteValue(), closedTime)
			.forEach(campaignSaleEntity -> updateCampaignFromActive(campaignSaleEntity, CampaignSaleStatus.CLOSED));
	}

	@Override
	public Page<ProductStatisticResponse> getProductStatistic(Long campaignSaleId, Pageable pageable) {
		return getSoldProduct(campaignSaleId, pageable).map(productMapper::entityToStatisticResponse);
	}

	private Page<SoldProduct> getSoldProduct(Long campaignSaleId, Pageable pageable) {
		return productRepository.getSoldProducts(campaignSaleId, pageable);
	}

	private void reduceProductInventory(CampaignSaleEntity entity, CampaignSaleStatus status, Set<ProductEntity> products) {
		Set<ProductInventoryQuantity> productQuantities = products.stream()
			.map(productEntity -> {
				// check quantity
				if (productEntity.getQuantity() > productEntity.getProductInventory().getQuantity()) {
					throw new InvalidException(ErrorCode.QUANTITY_NOT_ENOUGH);
				}
				// update opensearch
				productOpenSearchService.updateCampaignStatus(
					entity.getId(),
					productEntity.getProductInventory().getProductCode(),
					status
				);
				return ProductInventoryQuantity.builder()
					.productCode(productEntity.getProductInventory().getProductCode())
					.quantity((long) productEntity.getQuantity() - productEntity.getSoldQuantity())
					.build();
			})
			.collect(Collectors.toSet());

		// reduce inventory quantity
		productInventoryJpaService.reduceQuantity(entity.getId(), entity.getName(), SourceCategory.CAMPAIGN_SALE, productQuantities);
	}

	private void refundProductInventory(CampaignSaleEntity entity, CampaignSaleStatus status, Set<ProductEntity> products) {
		Set<ProductInventoryQuantity> productQuantities = products.stream()
			.map(productEntity -> {
				//check quantity and price
				if (productEntity.getQuantity() == null || productEntity.getQuantity() == 0 || productEntity.getPriceAmount() == null) {
					throw new InvalidException(ErrorCode.PRODUCT_IN_SALE_INVALID);
				}

				// update opensearch
				productOpenSearchService.updateCampaignStatus(
					entity.getId(),
					productEntity.getProductInventory().getProductCode(),
					status
				);
				return ProductInventoryQuantity.builder()
					.productCode(productEntity.getProductInventory().getProductCode())
					.quantity((long) productEntity.getQuantity() - productEntity.getSoldQuantity())
					.build();
			})
			.collect(Collectors.toSet());

		// reduce inventory quantity
		productInventoryJpaService.refundQuantity(entity.getId(), entity.getName(), SourceCategory.CAMPAIGN_SALE, productQuantities);
	}
}
