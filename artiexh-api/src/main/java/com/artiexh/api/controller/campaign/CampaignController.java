package com.artiexh.api.controller.campaign;

import com.artiexh.api.base.common.Endpoint;
import com.artiexh.api.base.exception.ArtiexhConfigException;
import com.artiexh.api.base.exception.ErrorCode;
import com.artiexh.api.base.exception.InvalidException;
import com.artiexh.api.base.service.SystemConfigService;
import com.artiexh.api.service.campaign.CampaignService;
import com.artiexh.api.service.marketplace.SaleCampaignService;
import com.artiexh.model.rest.campaign.request.FinalizeProductRequest;
import com.artiexh.model.rest.campaign.response.ArtiexhProfitConfig;
import com.artiexh.model.rest.campaign.response.ProductInCampaignDetailResponse;
import com.artiexh.model.rest.marketplace.salecampaign.response.SaleCampaignDetailResponse;
import com.artiexh.model.rest.product.response.ProductResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Set;

import static com.artiexh.api.base.common.Const.SystemConfigKey.DEFAULT_PROFIT_PERCENTAGE;

@RestController
@RequestMapping(Endpoint.Campaign.ROOT)
@RequiredArgsConstructor
public class CampaignController {
	private final CampaignService campaignService;
	private final SaleCampaignService saleCampaignService;
	private final SystemConfigService systemConfigService;

	@PostMapping("/{id}/finalize-product")
	@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
	public Set<ProductResponse> finalizeProduct(@PathVariable("id") Long campaignId,
												@RequestBody @Valid Set<FinalizeProductRequest> request) {
		try {
			return campaignService.finalizeProduct(campaignId, request);
		} catch (EntityNotFoundException ex) {
			throw new InvalidException(ErrorCode.ENTITY_NOT_FOUND, ex.getMessage());
		}
	}

	@PostMapping("/{id}/publish-to-product-inventory")
	@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
	public void publishProduct(
		Authentication authentication,
		@PathVariable("id") Long campaignId,
		@RequestBody @Valid Map<String, Long> productQuantities
	) {
		try {
			long staffId = (long) authentication.getPrincipal();
			campaignService.staffFinishManufactureCampaign(productQuantities, campaignId, staffId, null);
		} catch (IllegalArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		} catch (EntityNotFoundException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		}
	}

	@GetMapping("/{id}/product/{product-id}")
	@PreAuthorize("hasAnyAuthority('ARTIST','ADMIN','STAFF')")
	public ProductInCampaignDetailResponse getProductInCampaign(@PathVariable("product-id") Long productId,
																@PathVariable Long id) {
		try {
			return campaignService.getProductInCampaign(id, productId);
		} catch (EntityNotFoundException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		}
	}

	@PostMapping("/{id}/sale-campaign")
	@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
	public SaleCampaignDetailResponse createSaleCampaignFromCampaignRequest(Authentication authentication,
																			@PathVariable long id) {
		long staffId = (long) authentication.getPrincipal();
		try {
			return saleCampaignService.createSaleCampaign(staffId, id);
		} catch (EntityNotFoundException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		} catch (IllegalArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		} catch (ArtiexhConfigException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
		}
	}

	@GetMapping("/artiexh-percentage")
	@PreAuthorize("hasAnyAuthority('ARTIST','ADMIN','STAFF')")
	public ArtiexhProfitConfig getArtiexhChargePercentage() {
		var config = systemConfigService.getOrThrow(DEFAULT_PROFIT_PERCENTAGE,
			() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Missing artiexh default profit percentage")
		);

		int result;
		try {
			result = Integer.parseInt(config);
			return new ArtiexhProfitConfig(result);
		} catch (NumberFormatException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid artiexh default profit percentage", ex);
		}
	}

}
