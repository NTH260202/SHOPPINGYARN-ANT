package com.artiexh.api.service;

import com.artiexh.data.jpa.entity.ArtistEntity;
import com.artiexh.model.domain.Shop;
import com.artiexh.model.rest.address.AddressResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ShopService {

	Page<Shop> getShopInPage(Specification<ArtistEntity> specification, Pageable pageable);

	Shop getShopById(Long id);

	Shop getShopByUsername(String usename);

	AddressResponse getShopAddress(Long id);

	//Page<Product> getShopProduct(String username, Query query, Pageable pageable);


}
