package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.ProductEntity;
import com.artiexh.data.jpa.projection.SoldProduct;
import com.artiexh.data.opensearch.model.ProductDocument;
import com.artiexh.model.domain.*;
import com.artiexh.model.rest.marketplace.salecampaign.response.ProductInSaleCampaignResponse;
import com.artiexh.model.rest.marketplace.salecampaign.response.ProductMarketplaceResponse;
import com.artiexh.model.rest.marketplace.salecampaign.response.ProductResponse;
import com.artiexh.model.rest.marketplace.salecampaign.response.ProductStatisticResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Mapper(
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = {
		ProductCategoryMapper.class,
		ProductTagMapper.class,
		ArtistMapper.class,
		ProductAttachMapper.class,
		ProductInventoryMapper.class,
		CampaignTypeMapper.class,
		CampaignSaleMapper.class,
	}
)
public interface ProductMapper {

	@Named("entityToDomainWithoutCampaign")
	@Mapping(target = "price.amount", source = "priceAmount")
	@Mapping(target = "price.unit", source = "priceUnit")
	@Mapping(target = "campaignSale.products", ignore = true)
	Product entityToDomainWithoutCampaign(ProductEntity entity);

	@Mapping(target = "price.amount", source = "priceAmount")
	@Mapping(target = "price.unit", source = "priceUnit")
	@Mapping(target = "campaignSale.products", ignore = true)
	Product entityToDomain(ProductEntity entity);

	@Named("entityToProductInSaleResponse")
	@Mapping(target = "productCode", source = "productInventory.productCode")
	@Mapping(target = "name", source = "productInventory.name")
	@Mapping(target = "thumbnailUrl", source = "productInventory.attaches", qualifiedByName = "getProductThumbnailUrl")
	@Mapping(target = "status", source = "productInventory.status")
	@Mapping(target = "price.unit", source = "priceUnit")
	@Mapping(target = "price.amount", source = "priceAmount")
	@Mapping(target = "averageRate", source = "productInventory.averageRate")
	@Mapping(target = "type", source = "productInventory.type")
	@Mapping(target = "owner", source = "productInventory.owner")
	@Mapping(target = "description", source = "productInventory.description")
	@Mapping(target = "maxItemsPerOrder", source = "productInventory.maxItemsPerOrder")
	@Mapping(target = "deliveryType", source = "productInventory.deliveryType")
	@Mapping(target = "tags", source = "productInventory.tags")
	@Mapping(target = "attaches", source = "productInventory.attaches")
	@Mapping(target = "category", source = "productInventory.category")
	@Mapping(target = "paymentMethods", source = "productInventory.paymentMethods")
	@Mapping(target = "weight", source = "productInventory.weight")
	ProductInSaleCampaignResponse entityToProductInSaleResponse(ProductEntity entity);

	@Mapping(target = "productCode", source = "productInventory.productCode")
	@Mapping(target = "name", source = "productInventory.name")
	@Mapping(target = "thumbnailUrl", source = "productInventory.attaches", qualifiedByName = "getProductThumbnailUrl")
	@Mapping(target = "status", source = "productInventory.status")
	@Mapping(target = "averageRate", source = "productInventory.averageRate")
	@Mapping(target = "type", source = "productInventory.type")
	@Mapping(target = "owner", source = "productInventory.owner")
	@Mapping(target = "description", source = "productInventory.description")
	@Mapping(target = "maxItemsPerOrder", source = "productInventory.maxItemsPerOrder")
	@Mapping(target = "deliveryType", source = "productInventory.deliveryType")
	@Mapping(target = "tags", source = "productInventory.tags")
	@Mapping(target = "attaches", source = "productInventory.attaches")
	@Mapping(target = "category", source = "productInventory.category")
	@Mapping(target = "paymentMethods", source = "productInventory.paymentMethods")
	@Mapping(target = "weight", source = "productInventory.weight")
	ProductInSaleCampaignResponse domainToProductInSaleResponse(Product domain);

	@Mapping(target = "productCode", source = "productInventory.productCode")
	@Mapping(target = "name", source = "productInventory.name")
	@Mapping(target = "thumbnailUrl", source = "productInventory.attaches", qualifiedByName = "getProductThumbnailUrl")
	@Mapping(target = "status", source = "productInventory.status")
	@Mapping(target = "averageRate", source = "productInventory.averageRate")
	@Mapping(target = "type", source = "productInventory.type")
	@Mapping(target = "owner", source = "productInventory.owner")
	@Mapping(target = "description", source = "productInventory.description")
	@Mapping(target = "maxItemsPerOrder", source = "productInventory.maxItemsPerOrder")
	@Mapping(target = "deliveryType", source = "productInventory.deliveryType")
	@Mapping(target = "tags", source = "productInventory.tags")
	@Mapping(target = "attaches", source = "productInventory.attaches")
	@Mapping(target = "category", source = "productInventory.category")
	@Mapping(target = "paymentMethods", source = "productInventory.paymentMethods")
	@Mapping(target = "weight", source = "productInventory.weight")
	@Mapping(target = "inventoryPrice", source = "productInventory.price")
	@Mapping(target = "inventoryQuantity", source = "productInventory.quantity")
	ProductResponse domainToProductResponse(Product domain);

	@Named("entityToProductResponse")
	@Mapping(target = "productCode", source = "productInventory.productCode")
	@Mapping(target = "name", source = "productInventory.name")
	@Mapping(target = "thumbnailUrl", source = "productInventory.attaches", qualifiedByName = "getProductThumbnailUrl")
	@Mapping(target = "status", source = "productInventory.status")
	@Mapping(target = "price.unit", source = "priceUnit")
	@Mapping(target = "price.amount", source = "priceAmount")
	@Mapping(target = "averageRate", source = "productInventory.averageRate")
	@Mapping(target = "type", source = "productInventory.type")
	@Mapping(target = "quantity", expression = "java(entity.getQuantity() - entity.getSoldQuantity())")
	@Mapping(target = "owner", source = "productInventory.owner")
	@Mapping(target = "description", source = "productInventory.description")
	@Mapping(target = "maxItemsPerOrder", source = "productInventory.maxItemsPerOrder")
	@Mapping(target = "deliveryType", source = "productInventory.deliveryType")
	@Mapping(target = "tags", source = "productInventory.tags")
	@Mapping(target = "attaches", source = "productInventory.attaches")
	@Mapping(target = "category", source = "productInventory.category")
	@Mapping(target = "paymentMethods", source = "productInventory.paymentMethods")
	@Mapping(target = "weight", source = "productInventory.weight")
	@Mapping(target = "saleCampaign", source = "campaignSale")
	ProductMarketplaceResponse entityToProductResponse(ProductEntity entity);

	@Mapping(target = "owner.province", source = "owner.ward.district.province")
	@Mapping(target = "saleCampaign", source = "campaign")
	ProductMarketplaceResponse documentToMarketplaceResponse(ProductDocument document);

	@Mapping(target = "owner.province", source = "owner.ward.district.province")
	ProductResponse documentToProductResponse(ProductDocument document);

	@Named("getProductThumbnailUrl")
	default String getThumbnailUrl(Set<ProductAttach> productAttaches) {
		return productAttaches.stream()
			.filter(attach -> attach.getType() == ProductAttachType.THUMBNAIL)
			.findFirst()
			.map(ProductAttach::getUrl)
			.orElse(null);
	}

	@Mapping(target = "campaign", source = "campaignSale")
	@Mapping(target = "type", source = "productInventory.type")
	@Mapping(target = "tags", source = "productInventory.tags")
	@Mapping(target = "status", source = "productInventory.status")
	@Mapping(target = "productCode", source = "productInventory.productCode")
	@Mapping(target = "owner", source = "productInventory.owner")
	@Mapping(target = "name", source = "productInventory.name")
	@Mapping(target = "averageRate", source = "productInventory.averageRate")
	@Mapping(target = "category", source = "productInventory.category")
	ProductDocument domainToDocument(Product product);

	@Mapping(target = "type", source = "productInventory.type")
	@Mapping(target = "tags", source = "productInventory.tags")
	@Mapping(target = "status", source = "productInventory.status")
	@Mapping(target = "productCode", source = "productInventory.productCode")
	@Mapping(target = "owner", source = "productInventory.owner")
	@Mapping(target = "name", source = "productInventory.name")
	@Mapping(target = "category", source = "productInventory.category")
	@Mapping(target = "averageRate", source = "productInventory.averageRate")
	@Mapping(target = "price.unit", source = "priceUnit")
	@Mapping(target = "price.amount", source = "priceAmount")
	@Mapping(target = "campaign", source = "campaignSale")
	ProductDocument entityToDocument(ProductEntity entity);

	ProductDocument.Campaign campaignToCampaignDocument(CampaignSale campaign);

	default ProductStatisticResponse entityToStatisticResponse(SoldProduct product) {
		BigDecimal revenue = BigDecimal.ZERO;
		Long quantity = 0L;
		if (product.getQuantity() != null) {
			quantity = product.getQuantity();
		}
		if (product.getRevenue() != null) {
			revenue = product.getRevenue();
		}
		return ProductStatisticResponse.builder()
			.name(product.getName())
			.productCode(product.getProductCode())
			.soldQuantity(product.getSoldQuantity())
			.revenue(Money.builder()
				.amount(revenue)
				.unit("VND")
				.build())
			.quantity(quantity)
			.build();
	}

//	default Page<ProductResponse> productPageToProductResponsePage(Page<Product> productPage) {
//		return productPage.map(this::domainToProductResponse);
//	}
//
//	ProductResponse domainToProductResponse(Product product);


	//	@Mapping(target = "price.unit", source = "priceUnit")
//	@Mapping(target = "price.amount", source = "priceAmount")
//	@Mapping(target = "thumbnailUrl", source = "attaches", qualifiedByName = "getProductThumbnailUrl")
//	@Mapping(target = "owner", qualifiedByName = "basicArtistInfo")
//	@Mapping(target = "shop", qualifiedByName = "basicShopInfo")
//	@Mapping(target = "bundles", source = "bundles", qualifiedByName = "bundleEntitiesToDomains")
//	@Mapping(target = "bundleItems", source = "bundleItems", qualifiedByName = "bundleItemEntitiesToDomains")
//	@Mapping(target = "productInCampaign.id", source = "productInCampaignId")
//	Product entityToDomain(ProductEntity productEntity);

//	@Named("bundleEntitiesToDomains")
//	@IterableMapping(qualifiedByName = "bundleEntityToDomain")
//	Set<Product> bundleEntitiesToDomains(Set<ProductEntity> productEntities);

//	@Named("bundleEntityToDomain")
//	@Mapping(target = "price.unit", source = "priceUnit")
//	@Mapping(target = "price.amount", source = "priceAmount")
//	@Mapping(target = "thumbnailUrl", source = "attaches", qualifiedByName = "getProductThumbnailUrl")
//	@Mapping(target = "owner", qualifiedByName = "basicArtistInfo")
//	@Mapping(target = "shop", qualifiedByName = "basicShopInfo")
//	@Mapping(target = "bundleItems", ignore = true)
//	@Mapping(target = "productInCampaign.id", source = "productInCampaignId")
//	Product bundleEntityToDomain(ProductEntity productEntity);

//	@Named("bundleItemEntitiesToDomains")
//	@IterableMapping(qualifiedByName = "bundleItemEntityToDomain")
//	Set<Product> bundleItemEntitiesToDomains(Set<ProductEntity> productEntities);

//	@Named("bundleItemEntityToDomain")
//	@Mapping(target = "price.unit", source = "priceUnit")
//	@Mapping(target = "price.amount", source = "priceAmount")
//	@Mapping(target = "thumbnailUrl", source = "attaches", qualifiedByName = "getProductThumbnailUrl")
//	@Mapping(target = "owner", qualifiedByName = "basicArtistInfo")
//	@Mapping(target = "shop", qualifiedByName = "basicShopInfo")
//	@Mapping(target = "bundles", ignore = true)
//	@Mapping(target = "productInCampaign.id", source = "productInCampaignId")
//	Product bundleItemEntityToDomain(ProductEntity productEntity);

	//	@Mapping(target = "priceUnit", source = "price.unit")
//	@Mapping(target = "priceAmount", source = "price.amount")
//	@Mapping(target = "averageRate", constant = "0f")
//	@Mapping(target = "productInCampaignId", source = "productInCampaign.id")
//	ProductEntity domainToEntity(Product product);
//
//	Product documentToDomain(ProductDocument productDocument);
//
//	ProductDocument domainToDocument(Product product);

//	@Mapping(target = "price.unit", source = "priceUnit")
//	@Mapping(target = "price.amount", source = "priceAmount")
//	ProductDocument entityToDocument(ProductEntity productEntity);

	//	@Mapping(target = "category.id", source = "categoryId")
//	@Mapping(target = "bundleItems", source = "bundleItems", qualifiedByName = "bundleItemsToProductSet")
//	Product createProductRequestToProduct(CreateProductRequest createProductRequest);
//
//	@Mapping(target = "productInCampaign.id", source = "productInCampaignId")
//		//@Mapping(target = "bundleItems", source = "bundleItems", qualifiedByName = "bundleItemsToProductSet")
//	Product publishProductRequestToProduct(FinalizeProductRequest productRequest);
//
//	@Mapping(target = "productInCampaign.id", source = "id")
//	@Mapping(target = "category", source = "customProduct.category")
//	@Mapping(target = "tags", source = "customProduct.tags")
//	Product productInCampaignToProduct(ProductInCampaign productInCampaign);
//
//	@Named("bundleItemsToProductSet")
//	default Set<Product> bundleItemsToProductSet(Set<Long> bundleItems) {
//		if (bundleItems == null) {
//			return null;
//		}
//
//		return bundleItems.stream()
//			.map(id -> Product.builder().id(id).build())
//			.collect(Collectors.toSet());
//	}
//
//	@Mapping(target = "category.id", source = "categoryId")
//	@Mapping(target = "bundleItems", source = "bundleItems", qualifiedByName = "bundleItemsToProductSet")
//	Product updateProductRequestToProduct(UpdateProductRequest updateProductRequest);

	default Integer toValue(ProductStatus status) {
		return status.getValue();
	}

	default ProductStatus toProductStatus(Integer value) {
		return ProductStatus.fromValue(value);
	}

	default ProductStatus toProductStatus(Byte value) {
		return ProductStatus.fromValue(value);
	}

	default Integer toValue(ProductType type) {
		return type.getValue();
	}

	default ProductType toProductType(Integer value) {
		return ProductType.fromValue(value);
	}

	default ProductType toProductType(Byte value) {
		return ProductType.fromValue(value);
	}

	default Integer toValue(PaymentMethod paymentMethod) {
		return paymentMethod.getValue();
	}

	default PaymentMethod toPaymentMethod(Integer value) {
		return PaymentMethod.fromValue(value);
	}

	default PaymentMethod toPaymentMethod(Byte value) {
		return PaymentMethod.fromValue(value);
	}

	default Integer toValue(DeliveryType type) {
		return type.getValue();
	}

	default DeliveryType toDeliveryType(Integer value) {
		return DeliveryType.fromValue(value);
	}

	default DeliveryType toDeliveryType(Byte value) {
		return DeliveryType.fromValue(value);
	}

}
