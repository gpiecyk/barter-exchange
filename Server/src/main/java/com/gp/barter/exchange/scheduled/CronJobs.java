package com.gp.barter.exchange.scheduled;

import com.gp.barter.exchange.persistence.model.OfferData;
import com.gp.barter.exchange.persistence.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CronJobs {

    private final OfferService offerService;

    @Autowired
    public CronJobs(OfferService offerService) {
        this.offerService = offerService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteOldOffers() {
        List<OfferData> offers = offerService.findOldOffers();
        offers.forEach(offerService::delete);
        System.out.println(offers.size() + " offers were successfully removed");
    }
}
