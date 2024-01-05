package com.artiexh.api.service.impl;

import com.artiexh.api.base.exception.ErrorCode;
import com.artiexh.api.base.exception.InvalidException;
import com.artiexh.api.service.CartService;
import com.artiexh.data.jpa.entity.*;
import com.artiexh.data.jpa.repository.CartItemRepository;
import com.artiexh.data.jpa.repository.CartRepository;
import com.artiexh.data.jpa.repository.ProductInventoryRepository;
import com.artiexh.data.jpa.repository.ProductRepository;
import com.artiexh.model.domain.Cart;
import com.artiexh.model.mapper.CartMapper;
import com.artiexh.model.rest.marketplace.cart.request.CartItemRequest;
import com.artiexh.model.rest.marketplace.cart.request.CartItemWithQuantityRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;
	private final ProductInventoryRepository productInventoryRepository;
	private final CartMapper cartMapper;

	@Override
	@Transactional
	public Cart getCart(long userId) {
		CartEntity cartEntity = getOrCreateCartEntity(userId);
		return cartMapper.entityToDomain(cartEntity);
	}

	@Override
	@Transactional
	public Cart updateCartItem(long userId, Set<CartItemWithQuantityRequest> items) {
		CartEntity cartEntity = getOrCreateCartEntity(userId);

		if (items.isEmpty()) {
			cartEntity.getCartItems().clear();
			return cartMapper.entityToDomain(cartRepository.save(cartEntity));
		}

		int numOfItemsBelongToUser = productInventoryRepository.countAllByProductCodeInAndOwnerId(
			items.stream().map(CartItemWithQuantityRequest::getProductCode).toList(),
			userId
		);
		if (numOfItemsBelongToUser > 0) {
			throw new InvalidException(ErrorCode.UPDATE_CART_ITEM_FAILED);
		}

		Set<CartItemEntity> newCartItemEntity = new HashSet<>();
		for (var item : items) {
			boolean found = false;
			for (var entity : cartEntity.getCartItems()) {
				if (entity.getId().getProductCode().equals(item.getProductCode())) {
					entity.setQuantity(item.getQuantity());
					found = true;
				}
			}
			if (!found) {
				CartItemId cartItemId = new CartItemId(
					cartEntity.getId(),
					item.getSaleCampaignId(),
					item.getProductCode()
				);
				ProductEntity productEntity = ProductEntity.builder()
					.id(new ProductEntityId(item.getProductCode(), item.getSaleCampaignId()))
					.build();

				newCartItemEntity.add(CartItemEntity.builder()
					.id(cartItemId)
					.product(productEntity)
					.quantity(item.getQuantity())
					.build()
				);
			}
		}

		cartEntity.getCartItems().addAll(newCartItemEntity);
		return cartMapper.entityToDomain(cartRepository.save(cartEntity));
	}

	@Override
	@Transactional
	public Cart addOneItem(long userId, CartItemRequest item) {
		if (productInventoryRepository.existsByProductCodeAndOwnerId(item.getProductCode(), userId)) {
			throw new InvalidException(ErrorCode.UPDATE_CART_ITEM_FAILED);
		}

		var cartEntity = getOrCreateCartEntity(userId);
		CartItemId cartItemId = new CartItemId(userId, item.getSaleCampaignId(), item.getProductCode());

		cartEntity.getCartItems().stream()
			.filter(itemEntity -> itemEntity.getId().equals(cartItemId))
			.findAny()
			.ifPresentOrElse(
				cartItemEntity -> {
					cartItemEntity.setQuantity(cartItemEntity.getQuantity() + 1);
					cartRepository.save(cartEntity);
				},
				() -> {
					ProductEntity productEntity = ProductEntity.builder()
						.id(new ProductEntityId(item.getProductCode(), item.getSaleCampaignId()))
						.build();
					var cartItemEntity = CartItemEntity.builder()
						.id(cartItemId)
						.product(productEntity)
						.quantity(1)
						.build();
					cartEntity.getCartItems().add(cartItemEntity);
					cartRepository.save(cartEntity);
				}
			);

		return cartMapper.entityToDomain(cartEntity);
	}

	@Override
	@Transactional
	public void deleteItemToCart(Set<CartItemId> itemIds) {
		if (itemIds.isEmpty()) {
			return;
		}
		cartItemRepository.deleteAllById(itemIds);
	}

	private CartEntity getOrCreateCartEntity(long userId) {
		return cartRepository.findById(userId)
			.orElseGet(() -> {
				CartEntity newCartEntity = CartEntity.builder().id(userId).build();
				return cartRepository.save(newCartEntity);
			});
	}

}
