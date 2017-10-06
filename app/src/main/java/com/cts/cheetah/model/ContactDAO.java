package com.cts.cheetah.model;

/**
 * Created by manu.palassery on 20-07-2017.
 */

public class ContactDAO {

    String contactNo;
    String contactPerson;
    String alternateContactNo;
    String alternateContactPerson;

    public ContactDAO() {
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getAlternateContactNo() {
        return alternateContactNo;
    }

    public void setAlternateContactNo(String alternateContactNo) {
        this.alternateContactNo = alternateContactNo;
    }

    public String getAlternateContactPerson() {
        return alternateContactPerson;
    }

    public void setAlternateContactPerson(String alternateContactPerson) {
        this.alternateContactPerson = alternateContactPerson;
    }
}
