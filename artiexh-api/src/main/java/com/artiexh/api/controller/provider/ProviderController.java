package com.artiexh.api.controller.provider;

import com.artiexh.api.base.common.Endpoint;
import com.artiexh.api.base.exception.ErrorCode;
import com.artiexh.api.base.exception.InvalidException;
import com.artiexh.api.service.provider.ProviderService;
import com.artiexh.model.domain.Provider;
import com.artiexh.model.mapper.ProviderMapper;
import com.artiexh.model.rest.PageResponse;
import com.artiexh.model.rest.PaginationAndSortingRequest;
import com.artiexh.model.rest.provider.ProviderDetail;
import com.artiexh.model.rest.provider.ProviderInfo;
import com.artiexh.model.rest.provider.filter.ProviderFilter;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = Endpoint.Provider.ROOT)
public class ProviderController {
	private final ProviderService providerService;
	private final ProviderMapper providerMapper;

	//Create Provider
	@PostMapping
	@PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
	public ProviderDetail create(@Valid @RequestBody ProviderDetail detail) {
		try {
			Provider provider = providerMapper.detailToDomain(detail);
			provider = providerService.create(provider);
			return providerMapper.domainToDetail(provider);
		} catch (EntityExistsException exception) {
			throw new InvalidException(ErrorCode.PROVIDER_EXISTED);
		}
	}

	//Get Provider Detail
	@GetMapping
	public PageResponse<ProviderInfo> getInPage(
		@ParameterObject PaginationAndSortingRequest paginationAndSortingRequest,
		@ParameterObject ProviderFilter providerFilter) {
		Page<Provider> providers = providerService.getInPage(providerFilter.getSpecification(), paginationAndSortingRequest.getPageable());
		return new PageResponse<>(providers.map(providerMapper::domainToInfo));
	}

	@GetMapping(Endpoint.Provider.DETAIL)
	public ProviderDetail getById(
		@PathVariable("id") String businessCode) {
		try {
			Provider provider = providerService.getById(businessCode);
			return providerMapper.domainToDetail(provider);
		} catch (EntityNotFoundException exception) {
			throw new InvalidException(ErrorCode.PROVIDER_NOT_FOUND, exception.getMessage());
		}
	}

	@PutMapping(Endpoint.Provider.DETAIL)
	@PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
	public ProviderDetail update(
		@Valid @RequestBody ProviderDetail detail,
		@PathVariable("id") String businessCode) {
		try {
			Provider provider = providerMapper.detailToDomain(detail);
			provider.setBusinessCode(businessCode);
			provider = providerService.update(provider);
			return providerMapper.domainToDetail(provider);
		} catch (EntityNotFoundException exception) {
			throw new InvalidException(ErrorCode.ENTITY_NOT_FOUND, exception.getMessage());
		}
	}

	@DeleteMapping(Endpoint.Provider.DETAIL)
	public void delete(@PathVariable("id") String businessCode) {
		try {
			providerService.delete(businessCode);
		} catch (EntityNotFoundException exception) {
			throw new InvalidException(ErrorCode.PROVIDER_NOT_FOUND);
		}

	}
}
