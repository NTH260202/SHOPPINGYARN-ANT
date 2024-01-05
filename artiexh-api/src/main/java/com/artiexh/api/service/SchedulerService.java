package com.artiexh.api.service;

import org.springframework.stereotype.Service;

@Service
public interface SchedulerService {
	void closeExpiredSaleCampaigns();

	void closeExpiredOrders();
}
