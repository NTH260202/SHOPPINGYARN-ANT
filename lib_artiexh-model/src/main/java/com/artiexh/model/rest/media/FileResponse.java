package com.artiexh.model.rest.media;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponse {
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	private String presignedUrl;
	private String fileName;
	private String name;
}
