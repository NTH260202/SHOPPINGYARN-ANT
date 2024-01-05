package com.artiexh.api.service.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileStreamResponse {
	private InputStream content;
	private String contentType;
}
