package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.OrderHistoryEntity;
import com.artiexh.model.domain.OrderHistory;
import com.artiexh.model.domain.OrderHistoryStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderHistoryMapper {

	@Mapping(target = "status", source = "id.status")
	OrderHistory entityToOrderHistory(OrderHistoryEntity entity);

	default OrderHistoryStatus fromValue(Integer value) {
		return OrderHistoryStatus.from(value);
	}

	default List<OrderHistory> entitiesToOrderHistories(Set<OrderHistoryEntity> entities) {
		if (entities == null) {
			return Collections.emptyList();
		}

		return entities.stream()
			.map(this::entityToOrderHistory)
			.sorted(Comparator.comparingInt(o -> o.getStatus().getValue()))
			.toList();
	}
}
