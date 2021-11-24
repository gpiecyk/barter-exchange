package com.gp.barter.exchange.persistence.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gp.barter.exchange.persistence.model.common.AbstractAuditFields;
import com.gp.barter.exchange.util.constants.TableColumnNames;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "OFFERS")
@AttributeOverrides({
        @AttributeOverride(name = "auditCd", column = @Column(name = TableColumnNames.OFR_AUDIT_CD)),
        @AttributeOverride(name = "auditMd", column = @Column(name = TableColumnNames.OFR_AUDIT_MD)),
        @AttributeOverride(name = "auditRd", column = @Column(name = TableColumnNames.OFR_AUDIT_RD))
})
public class OfferData extends AbstractAuditFields {

    @Id
    @GeneratedValue
    @Column(name = TableColumnNames.OFR_ID, nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = TableColumnNames.OFR_USER_ID_FK)
    private UserData user;
    @Column(name = TableColumnNames.OFR_USER_ID_FK, nullable = false, insertable = false, updatable = false)
    private Long userFkId;

    @Column(name = TableColumnNames.OFR_TITLE, nullable = false)
    private String title;

    @Column(name = TableColumnNames.OFR_DESCRIPTION, nullable = false)
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = TableColumnNames.OFR_END_DATE)
    private Date endDate;

    @Column(name = TableColumnNames.OFR_CATEGORY, nullable = false)
    private String category;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "offer", fetch = FetchType.EAGER)
    private Set<PictureData> pictures;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    public Long getUserFkId() {
        return userFkId;
    }

    public void setUserFkId(Long userFkId) {
        this.userFkId = userFkId;
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

    public Set<PictureData> getPictures() {
        return pictures;
    }

    public void setPictures(Set<PictureData> pictures) {
        this.pictures = pictures;
    }
}
