package com.artiexh.model.rest.media;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.impl.StringArraySerializer;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSharedUsersRequest {
	@NotEmpty
	@JsonSerialize(using = StringArraySerializer.class)
	private Long[] sharedUserIds;
}
