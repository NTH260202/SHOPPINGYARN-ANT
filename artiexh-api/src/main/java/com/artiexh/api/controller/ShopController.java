package com.artiexh.api.controller;

import com.artiexh.api.base.common.Endpoint;
import com.artiexh.api.service.ShopService;
import com.artiexh.api.service.campaign.CampaignService;
import com.artiexh.api.service.marketplace.ProductService;
import com.artiexh.model.domain.Shop;
import com.artiexh.model.mapper.CampaignMapper;
import com.artiexh.model.mapper.ProductMapper;
import com.artiexh.model.rest.PageResponse;
import com.artiexh.model.rest.PaginationAndSortingRequest;
import com.artiexh.model.rest.address.AddressResponse;
import com.artiexh.model.rest.shop.ShopPageFilter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(Endpoint.Shop.ROOT)
@RequiredArgsConstructor
public class ShopController {
	private final ShopService shopService;
	private final ProductService productService;
	private final ProductMapper productMapper;
	private final CampaignService campaignService;
	private final CampaignMapper campaignMapper;
	@Value("${artiexh.security.admin.id}")
	private Long rootAdminId;

	@GetMapping
	public PageResponse<Shop> getAllShops(
		@ParameterObject @Valid ShopPageFilter filter,
		@ParameterObject @Valid PaginationAndSortingRequest paginationAndSortingRequest
	) {
		return new PageResponse<>(
			shopService.getShopInPage(filter.getSpecification(), paginationAndSortingRequest.getPageable())
		);
	}

	@GetMapping("/{username}")
	public Shop getShop(@PathVariable String username) {
		try {
			return shopService.getShopByUsername(username);
		} catch (EntityNotFoundException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
		}
	}

	@GetMapping("/{id}" + Endpoint.Shop.ADDRESS)
	public AddressResponse getShopAddress(@PathVariable Long id) {
		try {
			return shopService.getShopAddress(id);
		} catch (EntityNotFoundException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
		}
	}

//	@GetMapping("/{username}/product")
//	public PageResponse<ProductResponse> getShopProduct(@PathVariable String username,
//														@ParameterObject @Valid GetAllProductFilter filter,
//														@ParameterObject @Valid PaginationAndSortingRequest paginationAndSortingRequest) {
//		try {
//			filter.setUsername(username);
//			Page<Product> productPage = shopService.getShopProduct(
//				username,
//				filter.getQuery(),
//				paginationAndSortingRequest.getPageable()
//			);
//			return new PageResponse<>(productMapper.productPageToProductResponsePage(productPage));
//		} catch (EntityNotFoundException ex) {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
//		}
//	}

	@GetMapping("/arty-shop")
	public Shop getArtyShop() {
		try {
			return shopService.getShopById(rootAdminId);
		} catch (EntityNotFoundException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
		}
	}

//	@GetMapping("/arty-shop/product")
//	public PageResponse<ProductResponse> getArtyShopProduct(
//		@ParameterObject @Valid ShopProductFilter filter,
//		@ParameterObject @Valid PaginationAndSortingRequest paginationAndSortingRequest
//	) {
//		filter.setShopId(rootAdminId);
//		Page<Product> productPage = productService.getInPage(
//			filter.getQuery(),
//			paginationAndSortingRequest.getPageable()
//		);
//		return new PageResponse<>(productMapper.productPageToProductResponsePage(productPage));
//	}
}
