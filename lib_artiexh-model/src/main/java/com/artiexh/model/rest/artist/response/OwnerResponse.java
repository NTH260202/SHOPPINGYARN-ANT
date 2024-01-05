package com.artiexh.model.rest.artist.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerResponse {
	@JsonSerialize(using = ToStringSerializer.class)
	private String id;
	private String username;
	private String displayName;
	private String avatarUrl;
	private Province province;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Province {
		@JsonSerialize(using = ToStringSerializer.class)
		private Short id;
		private String name;
		private Country country;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Country {
		@JsonSerialize(using = ToStringSerializer.class)
		private Short id;
		private String name;
	}
}
