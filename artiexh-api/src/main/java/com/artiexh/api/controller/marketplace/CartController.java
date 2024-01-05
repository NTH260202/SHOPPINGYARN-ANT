package com.artiexh.api.controller.marketplace;

import com.artiexh.api.base.common.Endpoint;
import com.artiexh.api.service.CartService;
import com.artiexh.data.jpa.entity.CartItemId;
import com.artiexh.model.domain.Cart;
import com.artiexh.model.mapper.CartMapper;
import com.artiexh.model.rest.marketplace.cart.request.CartItemRequest;
import com.artiexh.model.rest.marketplace.cart.request.DeleteCartItemsRequest;
import com.artiexh.model.rest.marketplace.cart.request.UpdateCartRequest;
import com.artiexh.model.rest.marketplace.cart.response.CartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(Endpoint.Cart.ROOT)
public class CartController {

	private final CartService cartService;
	private final CartMapper cartMapper;

	@GetMapping
	@PreAuthorize("hasAnyAuthority('USER','ARTIST')")
	public CartResponse getCart(Authentication authentication) {
		long userId = (Long) authentication.getPrincipal();
		Cart cart = cartService.getCart(userId);
		return cartMapper.domainToCartResponse(cart);
	}

	@PutMapping(Endpoint.Cart.ITEM)
	@PreAuthorize("hasAnyAuthority('USER','ARTIST')")
	public CartResponse updateCart(Authentication authentication,
								   @RequestBody @Validated UpdateCartRequest request) {
		try {
			long userId = (Long) authentication.getPrincipal();
			Cart cart = cartService.updateCartItem(userId, request.getItems());
			return cartMapper.domainToCartResponse(cart);
		} catch (IllegalArgumentException exception) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
		}
	}

	@PatchMapping(Endpoint.Cart.ITEM_ADD_QUANTITY)
	@PreAuthorize("hasAnyAuthority('USER','ARTIST')")
	public CartResponse addItemToCart(Authentication authentication,
									  @RequestBody @Validated CartItemRequest request) {
		long userId = (Long) authentication.getPrincipal();
		Cart cart = cartService.addOneItem(userId, request);
		return cartMapper.domainToCartResponse(cart);
	}

	@DeleteMapping(Endpoint.Cart.ITEM)
	@PreAuthorize("hasAnyAuthority('USER','ARTIST')")
	public CartResponse deleteCartItem(Authentication authentication,
									   @RequestBody @Validated DeleteCartItemsRequest deleteItemsRequest) {
		long userId = (Long) authentication.getPrincipal();
		cartService.deleteItemToCart(deleteItemsRequest.getItems().stream().map(request ->
			new CartItemId(userId, request.getSaleCampaignId(), request.getProductCode())).collect(Collectors.toSet())
		);
		Cart cart = cartService.getCart(userId);
		return cartMapper.domainToCartResponse(cart);
	}

}
