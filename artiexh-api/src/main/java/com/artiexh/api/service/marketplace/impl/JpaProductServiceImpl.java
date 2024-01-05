package com.artiexh.api.service.marketplace.impl;

import com.artiexh.api.service.marketplace.JpaProductService;
import com.artiexh.data.jpa.entity.ProductEntity;
import com.artiexh.data.jpa.entity.ProductEntityId;
import com.artiexh.data.jpa.repository.*;
import com.artiexh.data.opensearch.model.ProductDocument;
import com.artiexh.model.domain.DeliveryType;
import com.artiexh.model.domain.Money;
import com.artiexh.model.domain.Product;
import com.artiexh.model.mapper.AddressMapper;
import com.artiexh.model.mapper.ProductAttachMapper;
import com.artiexh.model.mapper.ProductInventoryMapper;
import com.artiexh.model.mapper.ProductMapper;
import com.artiexh.model.rest.marketplace.salecampaign.response.ProductMarketplaceResponse;
import com.artiexh.model.rest.marketplace.salecampaign.response.ProductResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JpaProductServiceImpl implements JpaProductService {
	private final ArtistRepository artistRepository;
	private final ProductCategoryRepository productCategoryRepository;
	private final ProductAttachRepository productAttachRepository;
	private final ProductTagRepository productTagRepository;
	private final ProductInCampaignRepository productInCampaignRepository;
	private final ProductRepository productRepository;
	private final ProductMapper productMapper;
	private final ProductInventoryMapper productInventoryMapper;
	private final AddressMapper addressMapper;
	private final ProductAttachMapper productAttachMapper;
	@Value("${artiexh.security.admin.id}")
	private Long rootAdminId;

	@Override
	@Transactional
	public Product create(ProductEntity productEntity) {
		return productMapper.entityToDomainWithoutCampaign(productRepository.save(productEntity));
	}

	@Override
	@Transactional
	public Product update(ProductEntity productEntity) {
		return productMapper.entityToDomainWithoutCampaign(productRepository.save(productEntity));
	}

	@Override
	public Page<ProductMarketplaceResponse> fillDocumentToMarketplaceResponse(Page<ProductDocument> documentPage) {
		var ids = documentPage.map(document -> new ProductEntityId(document.getProductCode(), document.getCampaign().getId()));

		Map<ProductEntityId, ProductEntity> entityMap = productRepository.findByIdIn(ids.toSet()).stream()
			.collect(Collectors.toMap(ProductEntity::getId, entity -> entity));

		return documentPage.map(productDocument -> {
			ProductMarketplaceResponse response = productMapper.documentToMarketplaceResponse(productDocument);

			ProductEntity entity = entityMap.get(ProductEntityId.builder()
				.productCode(productDocument.getProductCode())
				.campaignSaleId(productDocument.getCampaign().getId())
				.build());
			response.setThumbnailUrl(productInventoryMapper.getThumbnailUrl(entity.getProductInventory().getAttaches()));
			response.setQuantity((long) entity.getQuantity() - entity.getSoldQuantity());
			response.getOwner().setAvatarUrl(entity.getProductInventory().getOwner().getAvatarUrl());
			response.setDescription(entity.getProductInventory().getDescription());
			Long maxItemsPerOrder = entity.getProductInventory().getMaxItemsPerOrder();
			response.setMaxItemsPerOrder(maxItemsPerOrder != null ? maxItemsPerOrder.intValue() : null);
			response.setDeliveryType(DeliveryType.fromValue(entity.getProductInventory().getDeliveryType()));
			response.setAttaches(productAttachMapper.entitiesToDomains(entity.getProductInventory().getAttaches()));
			response.setPaymentMethods(productInventoryMapper.byteArrayToPaymentMethodSet(entity.getProductInventory().getPaymentMethods()));
			response.setWeight(entity.getProductInventory().getWeight());

			return response;
		});
	}

	@Override
	public Page<ProductResponse> fillDocumentToProductResponse(Page<ProductDocument> documentPage) {
		var ids = documentPage.map(document -> new ProductEntityId(document.getProductCode(), document.getCampaign().getId()));

		Map<String, ProductEntity> entityMap = productRepository.findByIdIn(ids.toSet()).stream()
			.collect(Collectors.toMap(entity -> entity.getId().getProductCode(), entity -> entity));

		return documentPage.map(productDocument -> {
			ProductResponse response = productMapper.documentToProductResponse(productDocument);

			ProductEntity entity = entityMap.get(productDocument.getProductCode());
			response.setThumbnailUrl(productInventoryMapper.getThumbnailUrl(entity.getProductInventory().getAttaches()));
			response.setQuantity(entity.getQuantity());
			response.setSoldQuantity(entity.getSoldQuantity());
			response.setInventoryPrice(new Money(entity.getProductInventory().getPriceAmount(), entity.getProductInventory().getPriceUnit()));
			response.setInventoryQuantity(entity.getProductInventory().getQuantity().intValue());
			response.getOwner().setAvatarUrl(entity.getProductInventory().getOwner().getAvatarUrl());
			response.setDescription(entity.getProductInventory().getDescription());
			Long maxItemsPerOrder = entity.getProductInventory().getMaxItemsPerOrder();
			response.setMaxItemsPerOrder(maxItemsPerOrder != null ? maxItemsPerOrder.intValue() : null);
			response.setDeliveryType(DeliveryType.fromValue(entity.getProductInventory().getDeliveryType()));
			response.setAttaches(productAttachMapper.entitiesToDomains(entity.getProductInventory().getAttaches()));
			response.setPaymentMethods(productInventoryMapper.byteArrayToPaymentMethodSet(entity.getProductInventory().getPaymentMethods()));
			response.setWeight(entity.getProductInventory().getWeight());
			response.setArtistProfit(entity.getArtistProfit());
			response.setManufacturingPrice(entity.getProductInventory().getManufacturingPrice());

			return response;
		});
	}

	@Override
	public ProductMarketplaceResponse getById(ProductEntityId id) {
		return productRepository.findById(id)
			.map(productMapper::entityToProductResponse)
			.orElseThrow(() -> new EntityNotFoundException("Product not found"));
	}

	@Override
	@Transactional
	public void delete(ProductEntity entity) {
		productRepository.delete(entity);
	}

//	public Page<Product> fillProductPage(Page<Product> productPage) {
//		// get missing fields from db: thumbnailUrl, remainingQuantity, owner.avatarUrl, description
//		Set<Long> hitIds = productPage.stream()
//			.map(Product::getId)
//			.collect(Collectors.toSet());
//		Map<Long, ProductEntity> entities = productRepository.findAllById(hitIds).stream()
//			.collect(Collectors.toMap(ProductEntity::getId, product -> product));
//
//		for (var product : productPage) {
//			String thumbnailUrl = productAttachRepository.findThumbnailByProductId(product.getId()).orElse(null);
//			product.setThumbnailUrl(thumbnailUrl);
//
//			var entity = entities.get(product.getId());
//			product.setQuantity(entity.getQuantity());
//			product.getOwner().setAvatarUrl(entity.getOwner().getAvatarUrl());
//			product.setDescription(entity.getDescription());
//			product.setSoldQuantity(entity.getSoldQuantity());
//		}
//
//		return productPage;
//	}
//
//	@Override
//	public Product getDetail(long id) {
//		ProductEntity product = productRepository.findById(id)
//			.orElseThrow(EntityNotFoundException::new);
//		return productMapper.entityToDomain(product);
//	}
//
//	@Override
//	@Transactional
//	public Product create(long artistId, Product product, ProductInCampaignEntity productInCampaign) {
//		ArtistEntity artistEntity = artistRepository.findById(artistId)
//			.orElseThrow(() -> new IllegalArgumentException("Artist not valid"));
//
//		ProductCategoryEntity categoryEntity = productCategoryRepository.findById(product.getCategory().getId())
//			.orElseThrow(() -> new IllegalArgumentException("Category not valid"));
//
//		if (isManyThumbnail(product.getAttaches())) {
//			throw new IllegalArgumentException("Only one thumbnail allowed");
//		}
//
//		Set<ProductTagEntity> tagEntities = getTagEntities(product.getTags());
//
//		Set<ProductEntity> bundleItems = getBundleItems(product);
//
//		ProductEntity productEntity = productMapper.domainToEntity(product);
//		productEntity.setOwner(artistEntity);
//		productEntity.setCategory(categoryEntity);
//		productEntity.setTags(tagEntities);
//		productEntity.setBundleItems(bundleItems);
//
//		ArtistEntity adminShop = artistRepository.findById(rootAdminId)
//			.orElseThrow(() -> new IllegalArgumentException("Admin shop is not configured"));
//
//		productEntity.setShop(adminShop);
//		productEntity.setProductInCampaignId(productInCampaign.getId());
//		productEntity.setCampaignId(productInCampaign.getCampaign().getId());
//
//		ProductEntity savedProductEntity = productRepository.save(productEntity);
//
//		product = productMapper.entityToDomain(savedProductEntity);
//		product.setCampaign(Campaign.builder().id(productEntity.getCampaignId()).build());
//		return product;
//	}
//
//	@Override
//	@Transactional
//	public Product update(long artistId, Product product) {
//		ProductEntity savedProduct = productRepository.findById(product.getId())
//			.orElseThrow(() -> new EntityNotFoundException("Product id not found"));
//
//		if (savedProduct.getOwner().getId() != artistId) {
//			throw new AccessDeniedException("Product owner not valid");
//		}
//
//		if (isManyThumbnail(product.getAttaches())) {
//			throw new IllegalArgumentException("Only one thumbnail allowed");
//		}
//
//		ProductEntity productEntity = productMapper.domainToEntity(product);
//		productEntity.setOwner(ArtistEntity.builder().id(artistId).build());
//		productEntity.setShop(ArtistEntity.builder().id(artistId).build());
//		productEntity.setBundleItems(getBundleItems(product));
//		productEntity.setTags(getTagEntities(product.getTags()));
//		productEntity.setCategory(productCategoryRepository.findById(product.getCategory().getId())
//			.orElseThrow(() -> new IllegalArgumentException("Category not valid"))
//		);
//
//		ProductEntity savedProductEntity = productRepository.save(productEntity);
//		return productMapper.entityToDomain(savedProductEntity);
//	}
//
//	private Set<ProductEntity> getBundleItems(Product product) {
//		if (ProductType.BUNDLE.equals(product.getType())) {
//			if (product.getBundleItems() == null || product.getBundleItems().isEmpty()) {
//				throw new IllegalArgumentException("Bundle must contain at least one item");
//			}
//
//			var itemEntities = productRepository.findAllByIdIn(
//				product.getBundleItems().stream()
//					.map(Product::getId)
//					.collect(Collectors.toSet())
//			);
//
//			if (itemEntities.size() != product.getBundleItems().size()) {
//				var existedIds = itemEntities.stream().map(ProductEntity::getId).collect(Collectors.toSet());
//				var notExistedIds = product.getBundleItems().stream()
//					.map(Product::getId)
//					.filter(id -> !existedIds.contains(id))
//					.map(String::valueOf)
//					.collect(Collectors.joining(","));
//				throw new IllegalArgumentException("Bundle item not existed: " + notExistedIds);
//			}
//
//			if (itemEntities.stream().anyMatch(item -> ProductType.BUNDLE.getByteValue() == item.getType())) {
//				throw new IllegalArgumentException("Bundle item must not be bundle product");
//			}
//
//			var totalWeight = itemEntities.stream().mapToDouble(ProductEntity::getWeight).sum();
//			if (product.getWeight() < totalWeight) {
//				throw new IllegalArgumentException("Bundle weight must be greater than or equal total weight of items");
//			}
//
//			var totalPrice = itemEntities.stream()
//				.map(ProductEntity::getPriceAmount)
//				.reduce(BigDecimal::add)
//				.orElse(BigDecimal.ZERO);
//			if (product.getPrice().getAmount().compareTo(totalPrice) > 0) {
//				throw new IllegalArgumentException("Bundle price must be less than or equal total price of items");
//			}
//
//			return itemEntities;
//		} else {
//			if (product.getBundleItems() != null && !product.getBundleItems().isEmpty()) {
//				throw new IllegalArgumentException("Only bundle product can contain bundleItems");
//			}
//			return null;
//		}
//	}
//
//	private Set<ProductTagEntity> getTagEntities(Set<ProductTag> tags) {
//		Set<String> tagNames = tags.stream().map(ProductTag::getName).collect(Collectors.toSet());
//		Set<ProductTagEntity> tagEntities = productTagRepository.findAllByNameIn(tagNames);
//		Set<String> existedTagNames = tagEntities.stream().map(ProductTagEntity::getName).collect(Collectors.toSet());
//		tagEntities.addAll(
//			tagNames.stream()
//				.filter(tagName -> !existedTagNames.contains(tagName))
//				.map(tagName -> ProductTagEntity.builder().name(tagName).build())
//				.collect(Collectors.toSet())
//		);
//		return tagEntities;
//	}
//
//	private boolean isManyThumbnail(Set<ProductAttach> attaches) {
//		return attaches.stream().filter(attach -> ProductAttachType.THUMBNAIL.equals(attach.getType())).count() > 1;
//	}
//
//	@Override
//	public void delete(long userId, long productId) {
//		productRepository.findById(productId)
//			.ifPresentOrElse(
//				product -> {
//					if (!product.getOwner().getId().equals(userId)) {
//						throw new IllegalArgumentException("This product is not own by user");
//					}
//					productRepository.softDelete(productId);
//				},
//				() -> {
//					throw new IllegalArgumentException("Product not found");
//				}
//			);
//	}
}
