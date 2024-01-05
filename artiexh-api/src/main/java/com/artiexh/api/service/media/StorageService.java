package com.artiexh.api.service.media;

import com.artiexh.model.rest.media.FileResponse;
import com.artiexh.model.rest.media.FileResponseList;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StorageService {
	FileResponse upload(MultipartFile multipartFile, Long userId) throws IOException;

	FileResponseList upload(List<MultipartFile> multipartFile, Long userId) throws IOException;

	void updateSharedUsers(Long userId, Long[] sharedIds, Long mediaId);

	FileStreamResponse download(Long id, Long userId, boolean isAdmin) throws IOException;
}
