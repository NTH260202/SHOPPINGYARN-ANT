package com.artiexh.api.service.marketplace;

import com.artiexh.data.opensearch.model.ProductDocument;
import com.artiexh.model.domain.CampaignSaleStatus;
import com.artiexh.model.domain.Product;
import com.artiexh.model.domain.ProductSuggestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.UpdateResponse;

import java.util.Map;

public interface ProductOpenSearchService {

	ProductDocument create(Product product);

	UpdateResponse update(Product product);

	UpdateResponse updateCampaignStatus(Long campaignId, String productCode, CampaignSaleStatus status);

	void delete(Long campaignId, String productCode);

	Page<ProductDocument> getAll(Pageable pageable, Query query);

	Page<ProductSuggestion> getSuggestionInPage(Query query, Pageable pageable);

	void updateCampaignInfo(Map<String, ProductDocument.Campaign> campaign);

	void refreshIndex();
}