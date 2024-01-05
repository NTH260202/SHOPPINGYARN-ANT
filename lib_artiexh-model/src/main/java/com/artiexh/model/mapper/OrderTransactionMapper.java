package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.OrderTransactionEntity;
import com.artiexh.model.domain.OrderTransaction;
import com.artiexh.model.domain.ResponseCode;
import com.artiexh.model.rest.transaction.OrderTransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Comparator;
import java.util.Set;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderTransactionMapper {
	@Mapping(target = "message", source = "responseCode", qualifiedByName = "toMessage")
	OrderTransactionResponse domainToResponse(OrderTransaction domain);

	@Mapping(target = "message", source = "responseCode", qualifiedByName = "toMessage")
	OrderTransactionResponse entityToResponse(OrderTransactionEntity entity);

	OrderTransaction entityToDomain(OrderTransactionEntity entity);

	@Named("toMessage")
	default String toMessage(String responseCode) {
		return ResponseCode.fromCode(responseCode).getMessage();
	}

	@Named("getCurrentTransaction")
	default OrderTransactionResponse getCurrentTransaction(Set<OrderTransactionEntity> orderTransactions) {
		OrderTransactionEntity orderTransaction = orderTransactions.stream().max(Comparator.comparing(OrderTransactionEntity::getPayDate)).orElse(null);
		return entityToResponse(orderTransaction);
	}

	@Named("getCurrentTransactionDomain")
	default OrderTransaction getCurrentTransactionDomain(Set<OrderTransactionEntity> orderTransactions) {
		OrderTransactionEntity orderTransaction = orderTransactions.stream().max(Comparator.comparing(OrderTransactionEntity::getPayDate)).orElse(null);
		return entityToDomain(orderTransaction);
	}
}
