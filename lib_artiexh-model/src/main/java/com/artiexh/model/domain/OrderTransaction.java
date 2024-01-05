package com.artiexh.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class OrderTransaction {
	private Long id;

	private String transactionNo;

	private Long orderId;

	private BigDecimal priceAmount;

	private String bankCode;

	private String cardType;

	private String orderInfo;

	private Instant payDate;

	private String responseCode;

	private String transactionStatus;
}
