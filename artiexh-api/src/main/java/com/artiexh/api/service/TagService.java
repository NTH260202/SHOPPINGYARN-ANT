package com.artiexh.api.service;

import com.artiexh.model.rest.tag.ProductTagResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TagService {
	Page<ProductTagResponse> getInPage(Pageable pageable);
}
