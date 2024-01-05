package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.ProductAttachEntity;
import com.artiexh.data.jpa.entity.ProductInventoryEntity;
import com.artiexh.model.domain.*;
import com.artiexh.model.rest.campaign.request.FinalizeProductRequest;
import com.artiexh.model.rest.product.request.UpdateProductRequest;
import com.artiexh.model.rest.product.response.ProductResponse;
import org.mapstruct.*;

import java.util.Set;

@Mapper(
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = {
		AccountMapper.class,
		ProductCategoryMapper.class,
		ProductTagMapper.class,
		ArtistMapper.class,
		ProductAttachMapper.class,
		ShopMapper.class,
		ProductInCampaignMapper.class,
		CustomProductMapper.class,
		CampaignMapper.class,
		CampaignTypeMapper.class
	}
)
public interface ProductInventoryMapper {
	@Mapping(target = "price.unit", source = "priceUnit")
	@Mapping(target = "price.amount", source = "priceAmount")
	@Mapping(target = "thumbnailUrl", source = "attaches", qualifiedByName = "getProductThumbnailUrl")
	@Mapping(target = "owner", qualifiedByName = "basicArtistInfo")
		//@Mapping(target = "bundles", source = "bundles", qualifiedByName = "bundleEntitiesToDomains")
		//@Mapping(target = "bundleItems", source = "bundleItems", qualifiedByName = "bundleItemEntitiesToDomains")
	ProductInventory entityToDomain(ProductInventoryEntity productEntity);

	@Mapping(target = "category.id", source = "categoryId")
		//@Mapping(target = "bundleItems", source = "bundleItems", qualifiedByName = "bundleItemsToProductSet")
	ProductInventory updateProductRequestToProduct(UpdateProductRequest updateProductRequest);

	ProductResponse domainToProductResponse(ProductInventory product);

	@Mapping(target = "priceUnit", source = "price.unit")
	@Mapping(target = "priceAmount", source = "price.amount")
	@Mapping(target = "averageRate", constant = "0f")
	ProductInventoryEntity domainToEntity(ProductInventory product);

	@Mapping(target = "priceUnit", source = "price.unit")
	@Mapping(target = "priceAmount", source = "price.amount")
	@Mapping(target = "averageRate", constant = "0f")
	@Mapping(target = "productInCampaign", ignore = true)
	@Mapping(target = "productCode", ignore = true)
	@Mapping(target = "owner", ignore = true)
	ProductInventoryEntity domainToEntity(ProductInventory product, @MappingTarget ProductInventoryEntity productInventoryEntity);

	@Mapping(target = "productInCampaign.id", source = "productInCampaignId")
	@Mapping(target = "status", constant = "AVAILABLE")
	@Mapping(target = "deliveryType", constant = "SHIP")
	@Mapping(target = "weight", constant = "500F")
	@Mapping(target = "quantity", constant = "0L")
	ProductInventory finalizeProductRequestToProduct(FinalizeProductRequest request);

	default Integer toValue(ProductStatus status) {
		return status.getValue();
	}

	default Byte toByteValue(ProductStatus status) {
		return status.getByteValue();
	}

	default ProductStatus toProductStatus(Integer value) {
		return ProductStatus.fromValue(value);
	}

	default Integer toValue(ProductType type) {
		return type.getValue();
	}

	default Byte toByteValue(ProductType type) {
		return type.getByteValue();
	}

	default ProductType toProductType(Integer value) {
		return ProductType.fromValue(value);
	}

	default Integer toValue(PaymentMethod paymentMethod) {
		return paymentMethod.getValue();
	}

	default PaymentMethod toPaymentMethod(Integer value) {
		return PaymentMethod.fromValue(value);
	}

	Set<PaymentMethod> byteArrayToPaymentMethodSet(Byte[] byteArray);

	default Integer toValue(DeliveryType type) {
		return type.getValue();
	}

	default DeliveryType toDeliveryType(Integer value) {
		return DeliveryType.fromValue(value);
	}

	@Named("getProductThumbnailUrl")
	default String getThumbnailUrl(Set<ProductAttachEntity> productAttachEntities) {
		return productAttachEntities.stream()
			.filter(attachEntity -> attachEntity.getType() == ProductAttachType.THUMBNAIL.getValue())
			.findFirst()
			.map(ProductAttachEntity::getUrl)
			.orElse(null);
	}
}
