package com.artiexh.api.service.marketplace.impl;

import com.artiexh.api.service.marketplace.JpaProductService;
import com.artiexh.api.service.marketplace.ProductOpenSearchService;
import com.artiexh.api.service.marketplace.ProductService;
import com.artiexh.data.jpa.entity.ProductEntity;
import com.artiexh.data.jpa.entity.ProductEntityId;
import com.artiexh.data.jpa.repository.ArtistRepository;
import com.artiexh.data.opensearch.model.ProductDocument;
import com.artiexh.model.domain.Product;
import com.artiexh.model.domain.ProductSuggestion;
import com.artiexh.model.mapper.ProductMapper;
import com.artiexh.model.rest.marketplace.salecampaign.filter.ProductPageFilter;
import com.artiexh.model.rest.marketplace.salecampaign.response.ProductMarketplaceResponse;
import com.artiexh.model.rest.marketplace.salecampaign.response.ProductResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	private final ArtistRepository artistRepository;
	private final JpaProductService jpaProductService;
	private final ProductOpenSearchService productOpenSearchService;
	private final ProductMapper productMapper;

	@Override
	public Product create(ProductEntity entity) {
		Product result;
		try {
			result = jpaProductService.create(entity);
			productOpenSearchService.create(result);
		} catch (Exception e) {
			log.warn("Insert product to db fail", e);
			throw e;
		}
		return result;
	}

	@Override
	public Product update(ProductEntity entity) {
		Product result;
		try {
			result = jpaProductService.update(entity);
			productOpenSearchService.update(result);
			refreshOpenSearchIndex();
		} catch (Exception e) {
			log.warn("Update product to db fail", e);
			throw e;
		}
		return result;
	}

	@Override
	public Page<ProductMarketplaceResponse> getAllMarketplaceResponse(Pageable pageable, Query query) {
		Page<ProductDocument> documentPage = productOpenSearchService.getAll(pageable, query);
		return jpaProductService.fillDocumentToMarketplaceResponse(documentPage);
	}

	@Override
	public Page<ProductResponse> getAllProductResponse(Pageable pageable, Query query) {
		Page<ProductDocument> documentPage = productOpenSearchService.getAll(pageable, query);
		return jpaProductService.fillDocumentToProductResponse(documentPage);
	}

	@Override
	public ProductMarketplaceResponse getByCampaignIdAndProductCode(long id, String productCode) {
		return jpaProductService.getById(new ProductEntityId(productCode, id));
	}

	@Override
	public Page<ProductMarketplaceResponse> getAllByArtist(String artistUsername, Pageable pageable, ProductPageFilter filter) {
		if (artistRepository.existsByUsername(artistUsername)) {
			filter.setArtistUsername(artistUsername);
			return getAllMarketplaceResponse(pageable, filter.getMarketplaceQuery(true));
		} else {
			throw new EntityNotFoundException("Artist not found");
		}
	}

	@Override
	public Page<ProductSuggestion> getSuggestionInPage(Query query, Pageable pageable) {
		return productOpenSearchService.getSuggestionInPage(query, pageable);
	}

	@Override
	public void delete(ProductEntity entity) {
		try {
			Product product = productMapper.entityToDomainWithoutCampaign(entity);
			jpaProductService.delete(entity);
			productOpenSearchService.delete(product.getCampaignSale().getId(), product.getProductInventory().getProductCode());
			refreshOpenSearchIndex();
		} catch (Exception e) {
			log.warn("Delete product to db fail", e);
			throw e;
		}
	}

	@Override
	public void refreshOpenSearchIndex() {
		productOpenSearchService.refreshIndex();
	}
}
