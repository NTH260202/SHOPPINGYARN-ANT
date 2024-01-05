package com.artiexh.data.jpa.projection;

import java.math.BigDecimal;
import java.time.Instant;

public interface Bill {
	Long getOrderId();

	BigDecimal getOrderAmount();

	String getPriceUnit();

	Long getOwnerId();

	Byte getStatus();

	Byte getOrderStatus();

	Instant getCreatedDate();
}
