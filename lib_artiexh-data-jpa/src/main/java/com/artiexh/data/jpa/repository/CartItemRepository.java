package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.CartItemEntity;
import com.artiexh.data.jpa.entity.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, CartItemId> {

	@Query("select count(item.id) from CartItemEntity item where item.id.cartId = :cartId")
	int countAllByCartId(@Param("cartId") Long cartId);

}
