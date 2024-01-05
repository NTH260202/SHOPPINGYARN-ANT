package com.artiexh.api.base.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Endpoint {

	public static final String PREFIX = "/api/v1";

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static final class OAuth2 {
		public static final String ROOT = PREFIX + "/oauth2";
		public static final String AUTHORIZATION = "/authorization";
		public static final String CALLBACK = "/callback/*";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static final class Auth {
		public static final String ROOT = PREFIX + "/auth";
		public static final String LOGIN = "/login";
		public static final String REFRESH = "/refresh";
		public static final String LOGOUT = "/logout";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Registration {
		public static final String ROOT = PREFIX + "/registration";
		public static final String USER = "/user";
		public static final String PRINTER_PROVIDER = "/printer-provider";
		public static final String ARTIST = "/artist";
		public static final String STAFF = "/staff";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Account {
		public static final String ROOT = PREFIX + "/account";
		public static final String ME = "/me";
		public static final String PROFILE = "/{id}/profile";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class User {
		public static final String ROOT = PREFIX + "/user";
		public static final String ADDRESS = "/address";
		public static final String ORDER = "/order";
		public static final String CAMPAIGN_ORDER = "/campaign-order";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Product {
		public static final String ROOT = PREFIX + "/product";
		public static final String PRODUCT_DETAIL = "/{id}";
		public static final String SUGGESTION = "/suggestion";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ProductInventory {
		public static final String ROOT = PREFIX + "/product-inventory";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ProductInSale {
		public static final String ROOT = PREFIX + "/product-in-sale";
		public static final String PRODUCT_DETAIL = "/{id}";
		public static final String SUGGESTION = "/suggestion";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Cart {
		public static final String ROOT = PREFIX + "/cart";
		public static final String ITEM = "/item";
		public static final String ITEM_ADD_QUANTITY = "/item/add";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Artist {
		public static final String ROOT = PREFIX + "/artist";
		public static final String ARTIST_PROFILE = "/{username}";
		public static final String ARTIST_PRODUCT = "/product-in-sale";
		public static final String ARTIST_ORDER = "/order-campaign";
		public static final String ARTIST_POST = "/{id}/post";
		public static final String ARTIST_CAMPAIGN = "/{id}/campaign";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Category {
		public static final String ROOT = PREFIX + "/category";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Tag {
		public static final String ROOT = PREFIX + "/tag";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Province {
		public static final String ROOT = PREFIX + "/province";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Order {
		public static final String ROOT = PREFIX + "/order";
		public static final String CHECKOUT = "/checkout";
		public static final String SHIPPING_FEE = "/shipping-fee";
		public static final String PAYMENT = "/{id}/payment";
		public static final String PAYMENT_RETURN = "/payment/vnpay-return";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Media {
		public static final String ROOT = PREFIX + "/media";
		public static final String DOWNLOAD = "/download";
		public static final String PUBLIC_UPLOAD = "/public-upload";
		public static final String UPLOAD = "/upload";
		public static final String DETAIL = "/{id}";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ProductTemplate {
		public static final String ROOT = PREFIX + "/product-template";

		public static final String DETAIL = "/{id}";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Provider {
		public static final String ROOT = PREFIX + "/provider";
		public static final String DETAIL = "/{id}";
		public static final String CONFIG = "/{id}/config";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ProductVariant {
		public static final String ROOT = PREFIX + "/product-variant";
		public static final String DETAIL = "/{id}";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Address {

		public static final String ROOT = PREFIX + "/address";
		public static final String COUNTRY = "/country";
		public static final String PROVINCE = "/province";
		public static final String DISTRICT = "/district";
		public static final String WARD = "/ward";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Collection {
		public static final String ROOT = PREFIX + "/collection";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Shop {
		public static final String ROOT = PREFIX + "/shop";
		public static final String ADDRESS = "/address";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Option {
		public static final String ROOT = PREFIX + "/option";
		public static final String TEMPLATE = "/template";
		public static final String ACTIVE_OPTION = "/active-option";
		public static final String DETAIL = "/{id}";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class CustomProduct {
		public static final String ROOT = PREFIX + "/custom-product";
		public static final String DETAIL = "/{id}";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Config {
		public static final String ROOT = PREFIX + "/config";
		public static final String SYNC_PRODUCT_OPEN_SEARCH = "/sync-product-to-open-search";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ProviderCategory {
		public static final String ROOT = PREFIX + "/provider-category";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ArtistCampaign {
		public static final String ROOT = PREFIX + "/artist-campaign";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class PublicCampaign {
		public static final String ROOT = PREFIX + "/public-campaign";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Post {
		public static final String ROOT = PREFIX + "/post";
		public static final String DETAIL = "/{id}";
		public static final String COMMENT = "/comment";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Marketplace {
		public static final String ROOT = PREFIX + "/marketplace";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Admin {
		public static final String ROOT = PREFIX + "/admin";
		public static final String CAMPAIGN_ORDER = "/campaign-order";
		public static final String ORDER = "/order";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Campaign {
		public static final String ROOT = PREFIX + "/campaign";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ProductHistory {
		public static final String ROOT = PREFIX + "/product-history";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ProductHistoryDetail {
		public static final String ROOT = PREFIX + "/product-history-detail";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class SaleCampaign {
		public static final String ROOT = PREFIX + "/sale-campaign";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Notification {
		public static final String ROOT = PREFIX + "/notification";
	}
}
