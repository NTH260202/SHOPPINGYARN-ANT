package com.artiexh.api.service.marketplace;

import com.artiexh.data.jpa.entity.ProductEntity;
import com.artiexh.model.domain.Product;
import com.artiexh.model.domain.ProductSuggestion;
import com.artiexh.model.rest.marketplace.salecampaign.filter.ProductPageFilter;
import com.artiexh.model.rest.marketplace.salecampaign.response.ProductMarketplaceResponse;
import com.artiexh.model.rest.marketplace.salecampaign.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.Query;

public interface ProductService {
	Product create(ProductEntity entity);

	Product update(ProductEntity entity);

	Page<ProductMarketplaceResponse> getAllMarketplaceResponse(Pageable pageable, Query query);

	Page<ProductResponse> getAllProductResponse(Pageable pageable, Query query);

	ProductMarketplaceResponse getByCampaignIdAndProductCode(long id, String productCode);

	Page<ProductMarketplaceResponse> getAllByArtist(String artistUsername, Pageable pageable, ProductPageFilter filter);

	Page<ProductSuggestion> getSuggestionInPage(Query query, Pageable pageable);

	void delete(ProductEntity entity);

	void refreshOpenSearchIndex();

//	Page<Product> getInPage(Query query, Pageable pageable);
//
//	Product getDetail(long id);
//
//	Product create(long artistId, Product product, ProductInCampaignEntity productInCampaign);
//
//	Product update(long artistId, Product product);
//
//	void delete(long userId, long productId);
}
