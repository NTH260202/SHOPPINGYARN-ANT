package com.artiexh.api.service.impl;

import com.artiexh.api.service.OrderService;
import com.artiexh.api.service.SchedulerService;
import com.artiexh.api.service.marketplace.SaleCampaignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class SchedulerServiceImpl implements SchedulerService {
	private final SaleCampaignService saleCampaignService;
	private final OrderService orderService;

	@Override
	@Scheduled(cron = "${cron.close-expired-sale-campaigns}")
	public void closeExpiredSaleCampaigns() {
		log.info("Start job close expired sale campaigns");
		saleCampaignService.closeExpiredSaleCampaigns();
		log.info("Finish job close expired sale campaigns");
	}

	@Override
	@Scheduled(cron = "${cron.close-expired-sale-campaigns}")
	public void closeExpiredOrders() {
		log.info("Start job close timeout orders");
		orderService.closedTimeoutOrder();
		log.info("Finish job close timeout orders");
	}

}
