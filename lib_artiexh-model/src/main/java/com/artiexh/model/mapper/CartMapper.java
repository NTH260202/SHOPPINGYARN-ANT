package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.CartEntity;
import com.artiexh.data.jpa.entity.CartItemEntity;
import com.artiexh.data.jpa.entity.ProductEntity;
import com.artiexh.data.jpa.entity.ProductInventoryEntity;
import com.artiexh.model.domain.*;
import com.artiexh.model.rest.marketplace.cart.response.CartCampaignResponse;
import com.artiexh.model.rest.marketplace.cart.response.CartItemResponse;
import com.artiexh.model.rest.marketplace.cart.response.CartResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = {
		ArtistMapper.class,
		ProductInventoryMapper.class,
		CampaignSaleMapper.class,
		CampaignTypeMapper.class
	}
)
public interface CartMapper {

	@Mapping(target = "cartItems", source = "cartItems")
	Cart entityToDomain(CartEntity cartEntity);

	@Mapping(target = "product", qualifiedByName = "productEntityToCartItemProduct")
	CartItem entityToDomain(CartItemEntity cartItemEntity);

	@Named("productEntityToCartItemProduct")
	@Mapping(target = "productInventory", qualifiedByName = "productInventoryEntityToCartItemProductInventory")
	@Mapping(target = "price.amount", source = "priceAmount")
	@Mapping(target = "price.unit", source = "priceUnit")
	@Mapping(target = "campaignSale.products", ignore = true)
	Product productEntityToCartItemProduct(ProductEntity entity);

	@Named("productInventoryEntityToCartItemProductInventory")
	@Mapping(target = "price", ignore = true)
	@Mapping(target = "thumbnailUrl", source = "attaches", qualifiedByName = "getProductThumbnailUrl")
	@Mapping(target = "owner", source = "owner", qualifiedByName = "basicArtistInfo")
	@Mapping(target = "tags", ignore = true)
	@Mapping(target = "attaches", ignore = true)
	@Mapping(target = "productInCampaign", ignore = true)
	ProductInventory productInventoryEntityToCartItemProductInventory(ProductInventoryEntity entity);

	@Mapping(target = "id", ignore = true)
	CartEntity domainToEntity(Cart cart);

	@Mapping(target = "campaigns", source = "cartItems", qualifiedByName = "campaignsResponseMapping")
	CartResponse domainToCartResponse(Cart cart);

	@Named("campaignsResponseMapping")
	default Set<CartCampaignResponse> campaignsResponseMapping(Set<CartItem> cartItems) {
		if (cartItems == null) {
			return Collections.emptySet();
		}
		return cartItems.stream()
			.collect(Collectors.groupingBy(
				cartItem -> cartItem.getProduct().getCampaignSale(),
				Collectors.mapping(this::domainToCartItemResponse, Collectors.toSet())
			))
			.entrySet().stream()
			.map(this::entryProductByCampaignToCartCampaignResponse)
			.collect(Collectors.toSet());
	}

	@Mapping(target = "saleCampaign", source = "entry.key")
	@Mapping(target = "items", source = "entry.value")
	CartCampaignResponse entryProductByCampaignToCartCampaignResponse(Map.Entry<CampaignSale, Set<CartItemResponse>> entry);

	@Mapping(target = "productCode", source = "product.productInventory.productCode")
	@Mapping(target = "status", source = "product.productInventory.status")
	@Mapping(target = "name", source = "product.productInventory.name")
	@Mapping(target = "thumbnailUrl", source = "product.productInventory.thumbnailUrl")
	@Mapping(target = "price", source = "product.price")
	@Mapping(target = "description", source = "product.productInventory.description")
	@Mapping(target = "type", source = "product.productInventory.type")
	@Mapping(target = "remainingQuantity", expression = "java(cartItem.getProduct().getQuantity() - cartItem.getProduct().getSoldQuantity())")
	@Mapping(target = "maxItemsPerOrder", source = "product.productInventory.maxItemsPerOrder")
	@Mapping(target = "deliveryType", source = "product.productInventory.deliveryType")
	@Mapping(target = "weight", source = "product.productInventory.weight")
	CartItemResponse domainToCartItemResponse(CartItem cartItem);

//	CartItemEntity domainToEntity(CartItem cartItem);

}
