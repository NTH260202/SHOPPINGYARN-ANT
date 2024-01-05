package com.artiexh.api.controller;

import com.artiexh.api.base.common.Endpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = Endpoint.Product.ROOT)
public class ProductInSaleController {
//	private final ProductService productService;
//	private final ProductMapper productMapper;
//
//	@PutMapping(path = Endpoint.Product.PRODUCT_DETAIL)
//	@PreAuthorize("hasAnyAuthority('ARTIST','ADMIN')")
//	public ProductResponse update(Authentication authentication, @PathVariable("id") long id, @RequestBody @Valid UpdateProductRequest request) {
//		long userId = (long) authentication.getPrincipal();
//
//		Product product = productMapper.updateProductRequestToProduct(request);
//		product.setId(id);
//		try {
//			Product updatedProduct = productService.update(userId, product);
//			return productMapper.domainToProductResponse(updatedProduct);
//		} catch (EntityNotFoundException ex) {
//			throw new ResponseStatusException(ErrorCode.PRODUCT_NOT_FOUND.getCode(), ErrorCode.PRODUCT_NOT_FOUND.getMessage(), ex);
//		} catch (IllegalArgumentException ex) {
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
//		}
//	}
//
//	@DeleteMapping(path = Endpoint.Product.PRODUCT_DETAIL)
//	public void delete(Authentication authentication, @PathVariable("id") long id) {
//		Long userId = (Long) authentication.getPrincipal();
//		try {
//			productService.delete(userId, id);
//		} catch (IllegalArgumentException ex) {
//			throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, ex.getMessage(), ex);
//		}
//	}
//
//	@GetMapping()
//	@PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN', 'ARTIST')")
//	public PageResponse<ProductResponse> getInPage(
//		@ParameterObject @Valid PaginationAndSortingRequest paginationAndSortingRequest
//	) {
//		GetAllProductFilter filter = new GetAllProductFilter();
//		Page<Product> productPage = productService.getInPage(
//			filter.matchAllQuery(),
//			paginationAndSortingRequest.getPageable()
//		);
//		return new PageResponse<>(productMapper.productPageToProductResponsePage(productPage));
//	}
//
//	@GetMapping("/{id}")
//	@PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN', 'ARTIST')")
//	public ProductResponse getDetail(@PathVariable("id") long id) {
//		Product product;
//		try {
//			product = productService.getDetail(id);
//		} catch (EntityNotFoundException ex) {
//			throw new ResponseStatusException(ErrorCode.PRODUCT_NOT_FOUND.getCode(), ErrorCode.PRODUCT_NOT_FOUND.getMessage(), ex);
//		}
//		return productMapper.domainToProductResponse(product);
//	}
}
