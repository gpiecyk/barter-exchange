package com.gp.barter.exchange.persistence.model;

import com.gp.barter.exchange.persistence.model.common.AbstractAuditFields;
import com.gp.barter.exchange.util.constants.TableColumnNames;

import javax.persistence.*;

@Entity
@Table(name = "USERS")
@AttributeOverrides({
        @AttributeOverride(name = "auditCd", column = @Column(name = TableColumnNames.USR_AUDIT_CD)),
        @AttributeOverride(name = "auditMd", column = @Column(name = TableColumnNames.USR_AUDIT_MD)),
        @AttributeOverride(name = "auditRd", column = @Column(name = TableColumnNames.USR_AUDIT_RD))
})
public class UserData extends AbstractAuditFields {

    @Id
    @GeneratedValue
    @Column(name = TableColumnNames.USR_ID, nullable = false)
    private Long id;

    @Column(name = TableColumnNames.USR_FIRST_NAME, nullable = false, length = 45)
    private String firstName;

    @Column(name = TableColumnNames.USR_LAST_NAME, nullable = false, length = 45)
    private String lastName;

    @Column(name = TableColumnNames.USR_EMAIL, nullable = false, length = 200)
    private String email;

    @Column(name = TableColumnNames.USR_PASSWORD, nullable = false, length = 300)
    private String password;

    @Column(name = TableColumnNames.USR_PHONE_NUMBER, length = 45)
    private String phoneNumber;

    @Column(name = TableColumnNames.USR_STATUS, nullable = false, length = 1)
    private String status;

    @OneToOne
    @JoinColumn(name = TableColumnNames.USR_ADDRESS_FK_ID)
    private AddressData addressData;
    @Column(name = TableColumnNames.USR_ADDRESS_FK_ID, nullable = false, insertable = false, updatable = false)
    private Long addressFkId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AddressData getAddressData() {
        return addressData;
    }

    public void setAddressData(AddressData addressesData) {
        this.addressData = addressesData;
    }

    public Long getAddressFkId() {
        return addressFkId;
    }

    public void setAddressFkId(Long addressFkId) {
        this.addressFkId = addressFkId;
    }
}
