package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.CampaignOrderEntity;
import com.artiexh.data.jpa.entity.OrderDetailEntity;
import com.artiexh.model.domain.CampaignOrder;
import com.artiexh.model.domain.CampaignOrderStatus;
import com.artiexh.model.domain.OrderDetail;
import com.artiexh.model.rest.order.admin.response.AdminCampaignOrderResponse;
import com.artiexh.model.rest.order.response.OrderDetailResponse;
import com.artiexh.model.rest.order.user.response.AdminCampaignOrderResponsePage;
import com.artiexh.model.rest.order.user.response.CampaignOrderResponsePage;
import com.artiexh.model.rest.order.user.response.UserCampaignOrderDetailResponse;
import com.artiexh.model.rest.order.user.response.UserCampaignOrderResponse;
import org.mapstruct.*;

import java.util.Set;

@Mapper(
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = {
		ProductInventoryMapper.class,
		CampaignSaleMapper.class,
		OrderMapper.class,
		AccountMapper.class,
		ProductMapper.class,
		ProductAttachMapper.class,
		CampaignTypeMapper.class,
		OrderHistoryMapper.class
	}
)
public interface CampaignOrderMapper {

	@Mapping(target = "order.campaignOrders", ignore = true)
	@Mapping(target = "campaignSale.products", ignore = true)
	CampaignOrder entityToDomain(CampaignOrderEntity entity);

	@Named("entityToDomainWithoutOrder")
	@Mapping(target = "order", ignore = true)
	@Mapping(target = "campaignSale.products", ignore = true)
	CampaignOrder entityToDomainWithoutOrder(CampaignOrderEntity entity);

	@IterableMapping(qualifiedByName = "entityToDomainWithoutOrder")
	@Named("entitiesToDomainsWithoutOrder")
	Set<CampaignOrder> entitiesToDomainsWithoutOrder(Set<CampaignOrderEntity> entities);

//	@Mapping(target = "createdDate", ignore = true)
//	@Mapping(target = "modifiedDate", ignore = true)
//	CampaignOrderEntity orderToOrderEntity(CampaignOrder campaignOrder);

	@Mapping(target = "user", source = "order.user")
	@Mapping(target = "campaignSale", qualifiedByName = "entityToResponse")
	@Mapping(target = "order", source = "order", qualifiedByName = "entityToAdminResponse")
	AdminCampaignOrderResponse entityToAdminResponse(CampaignOrderEntity entity);

	@Named("entityToUserResponse")
	@Mapping(target = "campaignSale", qualifiedByName = "entityToResponse")
	UserCampaignOrderResponse entityToUserResponse(CampaignOrderEntity entity);

	@Named("domainToUserDetailResponse")
	UserCampaignOrderDetailResponse domainToUserDetailResponse(CampaignOrder campaignOrder);

	UserCampaignOrderDetailResponse entityToUserDetailResponse(CampaignOrderEntity entity);

	@IterableMapping(qualifiedByName = "entityToUserResponse")
	@Named("entitiesToUserResponses")
	Set<UserCampaignOrderResponse> entitiesToUserResponses(Set<CampaignOrderEntity> entities);

	@Mapping(target = "campaignSale", qualifiedByName = "entityToResponse")
	CampaignOrderResponsePage entityToUserResponsePage(CampaignOrderEntity campaignOrder);

	@Mapping(target = "campaignSale", qualifiedByName = "entityToResponse")
	@Mapping(target = "user", source = "order.user")
	@Mapping(target = "shipment", source = "order")
	AdminCampaignOrderResponsePage entityToAdminResponsePage(CampaignOrderEntity campaignOrder);

	@Named("domainToUserResponsePage")
	CampaignOrderResponsePage domainToUserResponsePage(CampaignOrder campaignOrder);

	@IterableMapping(qualifiedByName = "domainToUserResponsePage")
	@Named("domainsToUserResponsePages")
	Set<CampaignOrderResponsePage> domainsToUserResponsePages(Set<CampaignOrder> campaignOrders);

	@Mapping(target = "productCode", source = "product.productInventory.productCode")
	@Mapping(target = "status", source = "product.productInventory.status")
	@Mapping(target = "price", source = "product.price")
	@Mapping(target = "name", source = "product.productInventory.name")
	@Mapping(target = "thumbnailUrl", source = "product.productInventory.thumbnailUrl")
	@Mapping(target = "description", source = "product.productInventory.description")
	@Mapping(target = "type", source = "product.productInventory.type")
	@Mapping(target = "remainingQuantity", expression = "java(order.getProduct().getQuantity() - order.getProduct().getSoldQuantity())")
	@Mapping(target = "maxItemsPerOrder", source = "product.productInventory.maxItemsPerOrder")
	@Mapping(target = "deliveryType", source = "product.productInventory.deliveryType")
	OrderDetailResponse domainToOrderDetailResponse(OrderDetail order);

	@Mapping(target = "productCode", source = "product.id.productCode")
	@Mapping(target = "status", source = "product.productInventory.status")
	@Mapping(target = "price.unit", source = "product.priceUnit")
	@Mapping(target = "price.amount", source = "priceAmount")
	@Mapping(target = "name", source = "product.productInventory.name")
	@Mapping(target = "thumbnailUrl", source = "product.productInventory.attaches", qualifiedByName = "getProductThumbnailUrl")
	@Mapping(target = "description", source = "product.productInventory.description")
	@Mapping(target = "type", source = "product.productInventory.type")
	@Mapping(target = "remainingQuantity", expression = "java(entity.getProduct().getQuantity() - entity.getProduct().getSoldQuantity())")
	@Mapping(target = "maxItemsPerOrder", source = "product.productInventory.maxItemsPerOrder")
	@Mapping(target = "deliveryType", source = "product.productInventory.deliveryType")
	OrderDetailResponse entityToOrderDetailResponse(OrderDetailEntity entity);

	@Mapping(target = "product.campaignSale.products", ignore = true)
	@Mapping(target = "product.productInventory.productInCampaign", ignore = true)
	OrderDetail entityToDomain(OrderDetailEntity entity);

	default Integer toValue(CampaignOrderStatus status) {
		return status.getValue();
	}

	default CampaignOrderStatus toOrderStatus(Integer value) {
		return CampaignOrderStatus.fromValue(value);
	}
}
