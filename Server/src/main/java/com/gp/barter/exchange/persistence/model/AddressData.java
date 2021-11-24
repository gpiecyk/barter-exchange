package com.gp.barter.exchange.persistence.model;

import com.gp.barter.exchange.persistence.model.common.AbstractAuditFields;
import com.gp.barter.exchange.util.constants.TableColumnNames;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "ADDRESSES")
@AttributeOverrides({
        @AttributeOverride(name = "auditCd", column = @Column(name = TableColumnNames.ADD_AUDIT_CD)),
        @AttributeOverride(name = "auditMd", column = @Column(name = TableColumnNames.ADD_AUDIT_MD)),
        @AttributeOverride(name = "auditRd", column = @Column(name = TableColumnNames.ADD_AUDIT_RD))
})
public class AddressData extends AbstractAuditFields {

    @Id
    @GeneratedValue
    @Column(name = TableColumnNames.ADD_ID, nullable = false)
    private Long id;

    @Column(name = TableColumnNames.ADD_STREET, nullable = false, length = 200)
    private String street;

    @Column(name = TableColumnNames.ADD_POST_CODE, nullable = false, length = 45)
    private String postCode;

    @Column(name = TableColumnNames.ADD_CITY, nullable = false, length = 45)
    private String city;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
