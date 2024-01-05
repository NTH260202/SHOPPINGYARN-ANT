package com.artiexh.model.rest.media;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponseList {
	List<FileResponse> fileResponses;
}
