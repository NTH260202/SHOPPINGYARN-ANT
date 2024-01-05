package com.artiexh.api.service.impl;

import com.artiexh.api.service.CollectionService;
import com.artiexh.data.jpa.entity.CollectionEntity;
import com.artiexh.data.jpa.repository.CollectionRepository;
import com.artiexh.model.domain.Collection;
import com.artiexh.model.mapper.CollectionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {
	private final CollectionRepository collectionRepository;
	private final CollectionMapper collectionMapper;

	@Override
	@Transactional
	public Collection create(Collection collection) {
		CollectionEntity collectionEntity = collectionMapper.domainToEntity(collection);
		collectionRepository.save(collectionEntity);

		collection.setId(collection.getId());
		return collection;
	}

	public Page<Collection> getAll(Specification<CollectionEntity> specification, Pageable pageable) {
		return collectionRepository.findAll(specification, pageable).map(collectionMapper::entityToDomain);
	}
}
