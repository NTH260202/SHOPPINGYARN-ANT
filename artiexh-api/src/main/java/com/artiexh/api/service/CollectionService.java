package com.artiexh.api.service;

import com.artiexh.data.jpa.entity.CollectionEntity;
import com.artiexh.model.domain.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface CollectionService {
	Collection create(Collection collection);

	Page<Collection> getAll(Specification<CollectionEntity> specification, Pageable pageable);
}
