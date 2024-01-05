package com.artiexh.api.service.marketplace.impl;

import com.artiexh.api.service.marketplace.ProductOpenSearchService;
import com.artiexh.data.opensearch.model.ProductDocument;
import com.artiexh.model.domain.CampaignSaleStatus;
import com.artiexh.model.domain.Product;
import com.artiexh.model.domain.ProductSuggestion;
import com.artiexh.model.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.UpdateResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductOpenSearchServiceImpl implements ProductOpenSearchService {
	private final ElasticsearchOperations openSearchTemplate;
	private final ProductMapper productMapper;

	@Override
	public ProductDocument create(Product product) {
		return openSearchTemplate.save(productMapper.domainToDocument(product));
	}

	@Override
	public UpdateResponse update(Product product) {
		return openSearchTemplate.update(productMapper.domainToDocument(product));
	}

	@Override
	public UpdateResponse updateCampaignStatus(Long campaignId, String productCode, CampaignSaleStatus status) {
		String documentId = campaignId + "-" + productCode;
		var document = openSearchTemplate.get(documentId, ProductDocument.class);
		if (document != null) {
			document.getCampaign().setStatus(status.getByteValue());
			return openSearchTemplate.update(document);
		} else {
			return UpdateResponse.of(UpdateResponse.Result.NOOP);
		}
	}

	@Override
	public void delete(Long campaignId, String productCode) {
		String documentId = campaignId + "-" + productCode;
		openSearchTemplate.delete(documentId, ProductDocument.class);
	}

	@Override
	public Page<ProductDocument> getAll(Pageable pageable, Query query) {
		query.setPageable(pageable);
		SearchHits<ProductDocument> hits = openSearchTemplate.search(query, ProductDocument.class);
		SearchPage<ProductDocument> hitPage = SearchHitSupport.searchPageFor(hits, pageable);
		return hitPage.map(SearchHit::getContent);
	}

	@Override
	public Page<ProductSuggestion> getSuggestionInPage(Query query, Pageable pageable) {
		query.setPageable(pageable);
		SearchHits<ProductDocument> hits = openSearchTemplate.search(query, ProductDocument.class);
		SearchPage<ProductDocument> hitPage = SearchHitSupport.searchPageFor(hits, pageable);
		return hitPage.map(searchHit -> ProductSuggestion.builder().name(searchHit.getContent().getName()).build());
	}

	@Override
	public void updateCampaignInfo(Map<String, ProductDocument.Campaign> campaign) {
		for (Map.Entry<String, ProductDocument.Campaign> entry : campaign.entrySet()) {
			var document = openSearchTemplate.get(entry.getKey(), ProductDocument.class);
			if (document != null) {
				document.setCampaign(entry.getValue());
				openSearchTemplate.update(document);
			}
		}
	}

	@Override
	public void refreshIndex() {
		openSearchTemplate.indexOps(ProductDocument.class).refresh();
	}

}
