package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.OrderEntity;
import com.artiexh.model.domain.Order;
import com.artiexh.model.rest.order.admin.response.AdminOrderResponse;
import com.artiexh.model.rest.order.admin.response.DetailAdminOrderResponse;
import com.artiexh.model.rest.order.user.response.DetailUserOrderResponse;
import com.artiexh.model.rest.order.user.response.UserOrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = {
		UserMapper.class,
		CampaignOrderMapper.class,
		OrderTransactionMapper.class,
		ProductMapper.class,
		UserMapper.class
	})
public interface OrderMapper {

	@Mapping(target = "user", qualifiedByName = "entityToBasicUser")
	@Mapping(target = "campaignOrders", source = "campaignOrders", qualifiedByName = "entitiesToDomainsWithoutOrder")
	@Mapping(target = "currentTransaction", source = "orderTransactions", qualifiedByName = "getCurrentTransactionDomain")
	Order entityToDomain(OrderEntity entity);

	@Named("domainToResponse")
	UserOrderResponse domainToUserResponse(Order order);

	@Named("domainToAdminResponse")
	AdminOrderResponse domainToAdminResponse(Order order);

	@Named("entityToResponse")
	UserOrderResponse entityToUserResponse(OrderEntity order);

	@Mapping(target = "owner", source = "user")
	@Mapping(target = "currentTransaction", source = "orderTransactions", qualifiedByName = "getCurrentTransaction")
	@Named("entityToAdminResponse")
	AdminOrderResponse entityToAdminResponse(OrderEntity order);

	@Mapping(target = "currentTransaction", source = "orderTransactions", qualifiedByName = "getCurrentTransaction")
	@Mapping(target = "campaignOrders", source = "campaignOrders", qualifiedByName = "entitiesToUserResponses")
	DetailUserOrderResponse entityToUserDetailResponse(OrderEntity entity);

	@Mapping(target = "currentTransaction", source = "orderTransactions", qualifiedByName = "getCurrentTransaction")
	@Mapping(target = "campaignOrders", source = "campaignOrders", qualifiedByName = "entitiesToUserResponses")
	DetailAdminOrderResponse entityToAdminDetailResponse(OrderEntity entity);

	@Named("domainToDetailResponse")
	DetailUserOrderResponse domainToUserDetailResponse(Order order);
}
