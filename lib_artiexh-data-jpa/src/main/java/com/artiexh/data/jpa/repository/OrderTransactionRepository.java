package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.OrderTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTransactionRepository extends JpaRepository<OrderTransactionEntity, Long> {
}
