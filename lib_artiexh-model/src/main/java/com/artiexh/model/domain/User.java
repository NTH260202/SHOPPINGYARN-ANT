package com.artiexh.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends Account {
	@JsonIgnore
	private String googleId;
	@JsonIgnore
	private String facebookId;
	private Set<Subscription> subscriptionsTo;
	private Cart shoppingCart;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer cartItemCount;
}
