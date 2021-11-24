package com.gp.barter.exchange.rest.dto;


import java.util.Date;
import java.util.Set;

public class OfferDto {

    private Long id;
    private String title;
    private String description;
    private Date endDate;
    private String category;
    private Set<PictureDto> urls;
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Set<PictureDto> getUrls() {
        return urls;
    }

    public void setUrls(Set<PictureDto> urls) {
        this.urls = urls;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
