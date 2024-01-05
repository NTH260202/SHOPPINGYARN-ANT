package com.artiexh.api.service.marketplace;

import com.artiexh.data.jpa.entity.CampaignSaleEntity;
import com.artiexh.data.jpa.entity.ProductEntity;
import com.artiexh.model.domain.CampaignSaleStatus;
import com.artiexh.model.rest.marketplace.salecampaign.filter.MarketplaceSaleCampaignFilter;
import com.artiexh.model.rest.marketplace.salecampaign.request.ProductInSaleRequest;
import com.artiexh.model.rest.marketplace.salecampaign.request.SaleCampaignRequest;
import com.artiexh.model.rest.marketplace.salecampaign.request.UpdateProductInSaleRequest;
import com.artiexh.model.rest.marketplace.salecampaign.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public interface SaleCampaignService {

	SaleCampaignDetailResponse createSaleCampaign(long creatorId, SaleCampaignRequest request);

	SaleCampaignDetailResponse createSaleCampaign(long creatorId, Long campaignRequestId);

	SaleCampaignDetailResponse updateSaleCampaign(Long id, SaleCampaignRequest request);

	void updateStatus(Long id, CampaignSaleStatus status);

	Page<SaleCampaignResponse> getAll(Pageable pageable, Specification<CampaignSaleEntity> specification);

	SaleCampaignDetailResponse getDetail(Long id);

	SaleCampaignDetailResponse getDetail(Long id, Long ownerId);

	Page<SaleCampaignResponse> getAllByArtist(String artistUsername,
											  Pageable pageable,
											  MarketplaceSaleCampaignFilter filter);

	CampaignStatistics getStatistics(Long campaignId);

	Page<ProductStatisticResponse> getProductStatistic(Long campaignSaleId, Pageable pageable);

	Set<ProductResponse> createProductInSaleCampaign(Long campaignId, Set<ProductInSaleRequest> requests);

	ProductResponse updateProductInSaleCampaign(long campaignId,
												String productCode,
												UpdateProductInSaleRequest request);

	void deleteProductInSaleCampaign(Long campaignId, Set<String> productCodes);

	void closeExpiredSaleCampaigns();
}
