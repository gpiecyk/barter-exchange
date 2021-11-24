package com.gp.barter.exchange.rest.dto;


import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TransactionDto {

    private Long id;
    private String offeringUserEmail;
    private String publisherUserEmail;
    private String message;
    private List<String> itemsForExchange = new LinkedList<>();
    private Long offerId;
    private String offerTitle;
    private Set<PictureDto> urls;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOfferingUserEmail() {
        return offeringUserEmail;
    }

    public void setOfferingUserEmail(String offeringUserEmail) {
        this.offeringUserEmail = offeringUserEmail;
    }

    public String getPublisherUserEmail() {
        return publisherUserEmail;
    }

    public void setPublisherUserEmail(String publisherUserEmail) {
        this.publisherUserEmail = publisherUserEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getItemsForExchange() {
        return itemsForExchange;
    }

    public void setItemsForExchange(List<String> itemsForExchange) {
        this.itemsForExchange = itemsForExchange;
    }

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public String getOfferTitle() {
        return offerTitle;
    }

    public void setOfferTitle(String offerTitle) {
        this.offerTitle = offerTitle;
    }

    public Set<PictureDto> getUrls() {
        return urls;
    }

    public void setUrls(Set<PictureDto> urls) {
        this.urls = urls;
    }
}
