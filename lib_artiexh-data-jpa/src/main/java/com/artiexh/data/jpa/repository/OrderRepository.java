package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.OrderEntity;
import com.artiexh.data.jpa.projection.Bill;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.HibernateHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long>, JpaSpecificationExecutor<OrderEntity> {
	@Query(nativeQuery = true,
		value = """
			SELECT co.id as orderId, sum(od.price_amount * od.quantity) + co.shipping_fee as orderAmount, p.price_unit as priceUnit, o.user_id as ownerId, co.status as status, o.status as orderStatus, o.created_date as createdDate
			from `order` o
			inner join campaign_order co on o.id = co.order_id
			inner join order_detail od on co.id = od.campaign_order_id
			inner join product p on od.campaign_sale_id = p.campaign_sale_id and od.product_code = p.product_code
			where o.id = :id
			group by co.id""")
	List<Bill> getBillInfo(@Param("id") Long id);

	Optional<OrderEntity> findByIdAndUserId(Long id, Long userId);

	@QueryHints(value = {
		@QueryHint(name = HibernateHints.HINT_FETCH_SIZE, value = "1"),
		@QueryHint(name = HibernateHints.HINT_CACHEABLE, value = "false"),
	})
	Stream<OrderEntity> streamAllByStatusAndCreatedDateBefore(byte status, Instant closedTime);

}
