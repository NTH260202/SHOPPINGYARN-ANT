package com.artiexh.api.service.impl;

import com.artiexh.api.service.TagService;
import com.artiexh.data.jpa.repository.ProductTagRepository;
import com.artiexh.model.mapper.ProductTagMapper;
import com.artiexh.model.rest.tag.ProductTagResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
	private final ProductTagRepository tagRepository;
	private final ProductTagMapper tagMapper;

	@Override
	public Page<ProductTagResponse> getInPage(Pageable pageable) {
		return tagRepository.findAll(pageable).map(tagMapper::entityToResponse);
	}
}
