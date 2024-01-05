package com.artiexh.data.jpa.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_transaction")
@Builder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
public class OrderTransactionEntity {
	@Id
	@Tsid
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "bank_code")
	private String bankCode;

	@Column(name = "card_type")
	private String cardType;

	@Column(name = "order_info")
	private String orderInfo;

	@Column(name = "pay_date")
	private Instant payDate;

	@Column(name = "price_amount", precision = 38, scale = 2)
	private BigDecimal priceAmount;

	@Column(name = "response_code")
	private String responseCode;

	@Column(name = "transaction_no")
	private String transactionNo;

	@Column(name = "transaction_status")
	private String transactionStatus;

	@Column(name = "order_id")
	private Long orderId;

}