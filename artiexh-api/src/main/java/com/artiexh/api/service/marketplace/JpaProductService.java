package com.artiexh.api.service.marketplace;

import com.artiexh.data.jpa.entity.ProductEntity;
import com.artiexh.data.jpa.entity.ProductEntityId;
import com.artiexh.data.opensearch.model.ProductDocument;
import com.artiexh.model.domain.Product;
import com.artiexh.model.rest.marketplace.salecampaign.response.ProductMarketplaceResponse;
import com.artiexh.model.rest.marketplace.salecampaign.response.ProductResponse;
import org.springframework.data.domain.Page;

public interface JpaProductService {

	Product create(ProductEntity productEntity);

	Product update(ProductEntity productEntity);

	Page<ProductMarketplaceResponse> fillDocumentToMarketplaceResponse(Page<ProductDocument> documentPage);

	Page<ProductResponse> fillDocumentToProductResponse(Page<ProductDocument> documentPage);

	ProductMarketplaceResponse getById(ProductEntityId id);

	void delete(ProductEntity entity);


//	Page<Product> fillProductPage(Page<Product> productPage);
//
//	Product getDetail(long id);
//
//	Product create(long artistId, Product product, ProductInCampaignEntity productInCampaign);
//
//	Product update(long artistId, Product product);
//
//	void delete(long userId, long productId);
}
