package com.artiexh.api.service.productinventory.impl;

import com.artiexh.api.service.productinventory.ProductInventoryOpenSearchService;
import com.artiexh.model.mapper.ProductInventoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductInventoryOpenSearchServiceImpl implements ProductInventoryOpenSearchService {
	private final ElasticsearchOperations openSearchTemplate;
	private final ProductInventoryMapper productInventoryMapper;

//	@Override
//	public ProductDocument create(ProductInventory productInventory) {
//		return openSearchTemplate.save(productInventoryMapper.domainToDocument(productInventory));
//	}
//
//	@Override
//	public ProductDocument update(ProductInventory productInventory) {
//		return openSearchTemplate.save(productInventoryMapper.domainToDocument(productInventory));
//	}
//
//	@Override
//	public ProductDocument updateSaveCampaign(String productCode, Money price, CampaignSale campaignSale) {
//		var document = openSearchTemplate.get(productCode, ProductDocument.class);
//		if (document != null) {
//			document.setCampaign(productInventoryMapper.campaignToCampaignDocument(campaignSale));
//			document.getPrice().setAmount(price.getAmount());
//			document.getPrice().setUnit(price.getUnit());
//			openSearchTemplate.save(document);
//		}
//		return document;
//	}
//
//	@Override
//	public ProductDocument updatePrice(String productCode, Money price) {
//		var document = openSearchTemplate.get(productCode, ProductDocument.class);
//		if (document != null) {
//			document.getPrice().setAmount(price.getAmount());
//			document.getPrice().setUnit(price.getUnit());
//			openSearchTemplate.save(document);
//		}
//		return document;
//	}
//
//	@Override
//	@Async
//	public void removeSaveCampaign(String productCode) {
//		var document = openSearchTemplate.get(productCode, ProductDocument.class);
//		if (document == null) {
//			return;
//		}
//
//		document.setCampaign(null);
//		document.setPrice(null);
//
//		openSearchTemplate.save(document);
//	}
//
//	@Override
//	public Page<ProductDocument> getAll(Pageable pageable, Query query) {
//		query.setPageable(pageable);
//		SearchHits<ProductDocument> hits = openSearchTemplate.search(query, ProductDocument.class);
//		SearchPage<ProductDocument> hitPage = SearchHitSupport.searchPageFor(hits, pageable);
//		return hitPage.map(SearchHit::getContent);
//	}
//
//	@Override
//	public Page<ProductSuggestion> getSuggestionInPage(Query query, Pageable pageable) {
//		query.setPageable(pageable);
//		SearchHits<ProductDocument> hits = openSearchTemplate.search(query, ProductDocument.class);
//		SearchPage<ProductDocument> hitPage = SearchHitSupport.searchPageFor(hits, pageable);
//		return hitPage.map(searchHit -> ProductSuggestion.builder().name(searchHit.getContent().getName()).build());
//	}

}
