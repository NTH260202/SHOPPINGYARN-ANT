package com.artiexh.model.rest.post;

import com.artiexh.model.domain.ProductAttach;
import com.artiexh.model.rest.account.AccountProfile;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDetail {

	@JsonSerialize(using = ToStringSerializer.class)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;

	private String description;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Schema(allOf = {AccountProfile.class})
	private AccountProfile owner;

	private Set<ProductAttach> attaches;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private int likes;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private int numOfComments;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Instant createdDate;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Instant modifiedDate;
}
