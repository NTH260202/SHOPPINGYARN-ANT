package com.artiexh.model.rest.order.request;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PaymentQueryProperties {
	private String vnp_IpAddr;

	private String vnp_TransactionStatus;

	private String vnp_TransactionNo;

	private String vnp_ResponseCode;

	private String vnp_SecureHash;

	private String vnp_TxnRef;

	private String vnp_SecureHashType;

	private String vnp_OrderInfo;

	private String vnp_CardType;

	private String vnp_Amount;

	private String vnp_PayDate;

	private String vnp_BankCode;
}
