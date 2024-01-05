package com.artiexh.api.service.provider;

import com.artiexh.api.base.common.Endpoint;
import com.artiexh.api.base.exception.ErrorCode;
import com.artiexh.api.base.exception.InvalidException;
import com.artiexh.data.jpa.entity.ProviderEntity;
import com.artiexh.model.domain.Provider;
import com.artiexh.model.rest.campaign.response.CampaignProviderResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

public interface ProviderService {
	Provider create(Provider provider);

	Provider update(Provider provider);

	Provider getById(String businessCode);

	Page<Provider> getInPage(Pageable pageable);

	Page<Provider> getInPage(Specification<ProviderEntity> specification, Pageable pageable);

	Set<CampaignProviderResponse> getAllSupportedCustomProducts(Long artistId, Set<Long> customProductIds);

	void delete(String businessCode);
}
