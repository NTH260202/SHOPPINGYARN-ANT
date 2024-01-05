package com.artiexh.model.domain;

import com.artiexh.model.validation.CurrencyType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Money {

	@NotNull
	private BigDecimal amount;

	@CurrencyType
	@NotNull
	private String unit;

}
