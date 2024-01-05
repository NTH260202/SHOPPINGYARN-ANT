package com.artiexh.data.jpa.projection;

import java.math.BigDecimal;

public interface SoldProduct {
	String getName();
	String getProductCode();
	Long getSoldQuantity();
	Long getOrderQuantity();
	Long getQuantity();
	BigDecimal getPriceAmount();
	BigDecimal getArtistProfit();
	BigDecimal getRevenue();
}
