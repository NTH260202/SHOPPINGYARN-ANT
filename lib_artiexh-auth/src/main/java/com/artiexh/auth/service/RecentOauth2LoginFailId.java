package com.artiexh.auth.service;

public interface RecentOauth2LoginFailId {

	void put(String providerId, String sub);

	Boolean remove(String providerId, String sub);

	boolean contain(String providerId, String sub);

}
