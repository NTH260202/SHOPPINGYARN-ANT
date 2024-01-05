package com.artiexh.api.service.impl;

import com.artiexh.api.service.ShopService;
import com.artiexh.api.service.marketplace.ProductService;
import com.artiexh.data.jpa.entity.ArtistEntity;
import com.artiexh.data.jpa.repository.ArtistRepository;
import com.artiexh.model.domain.Shop;
import com.artiexh.model.mapper.ShopMapper;
import com.artiexh.model.rest.address.AddressResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {
	private final ArtistRepository artistRepository;
	private final ProductService productService;
	private final ShopMapper shopMapper;

	@Override
	public Page<Shop> getShopInPage(Specification<ArtistEntity> specification, Pageable pageable) {
		return artistRepository.findAll(specification, pageable)
			.map(shopMapper::entityToDomain);
	}

	@Override
	public Shop getShopById(Long id) {
		return artistRepository.findById(id)
			.map(shopMapper::entityToDomain)
			.orElseThrow(() -> new EntityNotFoundException("id " + id + " not existed"));
	}

	@Override
	public Shop getShopByUsername(String usename) {
		return artistRepository.findByUsername(usename)
			.map(shopMapper::entityToDomain)
			.orElseThrow(() -> new EntityNotFoundException("Artist " + usename + " not existed"));
	}

	@Override
	public AddressResponse getShopAddress(Long id) {
		return artistRepository.findById(id)
			.map(shopMapper::entityToShopAddressResponse)
			.orElseThrow(() -> new EntityNotFoundException("id " + id + " not existed"));
	}

//	@Override
//	public Page<Product> getShopProduct(String username, Query query, Pageable pageable) {
//		if (!artistRepository.existsByUsername(username)) {
//			throw new EntityNotFoundException("Artist " + username + " not existed");
//		}
//		return productService.getInPage(query, pageable);
//	}

}
