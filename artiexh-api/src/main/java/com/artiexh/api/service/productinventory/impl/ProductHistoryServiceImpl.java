package com.artiexh.api.service.productinventory.impl;

import com.artiexh.api.service.productinventory.ProductHistoryService;
import com.artiexh.data.jpa.entity.CampaignEntity;
import com.artiexh.data.jpa.entity.ProductHistoryDetailEntity;
import com.artiexh.data.jpa.entity.ProductHistoryEntity;
import com.artiexh.data.jpa.entity.embededmodel.ProductHistoryEntityDetailId;
import com.artiexh.data.jpa.repository.CampaignRepository;
import com.artiexh.data.jpa.repository.ProductHistoryDetailRepository;
import com.artiexh.data.jpa.repository.ProductHistoryRepository;
import com.artiexh.data.jpa.repository.ProductInventoryRepository;
import com.artiexh.model.domain.ProductHistory;
import com.artiexh.model.domain.ProductHistoryAction;
import com.artiexh.model.domain.ProductInventoryQuantity;
import com.artiexh.model.domain.SourceCategory;
import com.artiexh.model.mapper.CampaignMapper;
import com.artiexh.model.mapper.ProductHistoryMapper;
import com.artiexh.model.rest.producthistory.ProductHistoryDetailPageResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductHistoryServiceImpl implements ProductHistoryService {
	private final ProductHistoryRepository productHistoryRepository;
	private final ProductHistoryDetailRepository productHistoryDetailRepository;
	private final ProductHistoryMapper productHistoryMapper;
	private final CampaignRepository campaignRepository;
	private final CampaignMapper campaignMapper;
	private final ProductInventoryRepository productInventoryRepository;

	@Override
	@Transactional
	public void create(ProductHistoryAction action, Long sourceId, String sourceName, SourceCategory sourceCategory, Set<ProductInventoryQuantity> productQuantities) {
		Set<ProductHistoryDetailEntity> productHistoryDetails = new HashSet<>();
		ProductHistoryEntity productHistory = ProductHistoryEntity.builder()
			.action(action.getByteValue())
			.sourceId(sourceId)
			.sourceName(sourceName)
			.sourceCategory(sourceCategory.getByteValue())
			.productHistoryDetails(productHistoryDetails)
			.build();
		productHistoryRepository.save(productHistory);
		productHistory.getProductHistoryDetails().addAll(productQuantities.stream()
			.map(product -> ProductHistoryDetailEntity.builder()
				.quantity(product.getQuantity())
				.id(ProductHistoryEntityDetailId.builder()
					.productHistoryId(productHistory.getId())
					.productCode(product.getProductCode())
					.build())
				.quantity(product.getQuantity())
				.currentQuantity(product.getCurrentQuantity())
				.build())
			.collect(Collectors.toSet()));
	}

	@Override
	public Page<ProductHistory> getInPage(Pageable pageable, Specification<ProductHistoryEntity> specification) {
		Page<ProductHistoryEntity> productHistoryPage = productHistoryRepository.findAll(specification, pageable);
		return productHistoryPage.map(productHistoryMapper::entityToDomainWithoutProductHistoryDetail);
	}

	@Override
	public ProductHistory getById(Long id) {
		ProductHistoryEntity productHistory = productHistoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
		ProductHistory result = productHistoryMapper.entityToDomain(productHistory);
		if (productHistory.getSourceId() != null) {
			switch (SourceCategory.fromValue(productHistory.getSourceCategory())) {
				case CAMPAIGN_SALE -> {
					//TODO: Query campaign sale
					CampaignEntity campaign;
				}
				case CAMPAIGN_REQUEST -> {
					CampaignEntity campaign = campaignRepository.findById(productHistory.getSourceId()).orElse(null);
					result.setProductSource(campaignMapper.entityToSourceDomain(campaign));
				}
			}
		}
		return result;
	}

	@Override
	public Page<ProductHistoryDetailPageResponse> getDetailInPage(Pageable pageable, Specification<ProductHistoryDetailEntity> specification) {
		Page<ProductHistoryDetailEntity> result = productHistoryDetailRepository.findAll(specification, pageable);
		return result.map(productHistoryMapper::entityToDetailPageResponse);
	}
}
