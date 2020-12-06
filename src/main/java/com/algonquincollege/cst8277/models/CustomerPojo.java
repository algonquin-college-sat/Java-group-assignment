/*****************************************************************c******************o*******v******id********
 * File: CustomerPojo.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * update by : Hanna Bernyk 040904190
 * update by : Oladotun Akinlabi 040892548
 * update by : Jeffrey Sharpe 040936079
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
*
* Description: model for the Customer object
*/
@Entity(name="Customer")
@Table(name="CUSTOMER")
@AttributeOverride(name="id", column=@Column(name="CUSTOMER_ID"))
public class CustomerPojo extends PojoBase implements Serializable {
    /*
     * Define the constants
     */
    private static final long serialVersionUID = 1L;

    public static final String ALL_CUSTOMERS_QUERY_NAME = "allCustomers";

    /**
     * First name
     */
    protected String firstName;
    
    /**
     * Last name
     */
    protected String lastName;
    
    /**
     * Email
     */
    protected String email;
    
    /**
     * Phone number
     */
    protected String phoneNumber;
    
    /**
     * Shipping address
     */
    protected AddressPojo shippingAddress;
    /**
     * Billing address
     */
    protected AddressPojo billingAddress;
	
    /**
     * Constructor
     */
    // JPA requires each @Entity class have a default constructor
	public CustomerPojo() {
	}
	
    /**
     * @return the value for firstName
     */
    @Column(name = "FNAME")
    public String getFirstName() {
        return firstName;
    }
    /**
     * @param firstName new value for firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the value for lastName
     */
    @Column(name = "LNAME")
    public String getLastName() {
        return lastName;
    }
    /**
     * @param lastName new value for lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the value for email
     */
    public String getEmail() {
        return email;
    }
    /**
     * @param email new value for email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the value for phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber new value for phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the value for Shipping Address
     */
    //dont use CascadeType.All (skipping CascadeType.REMOVE): what if two customers
    //live at the same address and 1 leaves the house but the other does not?
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "SHIPPINGADDRESS_ADDR_ID")
    public AddressPojo getShippingAddress() {
        return shippingAddress;
    }
    
    /**
     * @param shipping Address new value for shipping Address
     */
    public void setShippingAddress(AddressPojo shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    /**
     * @return the value for Billing Address
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "BILLINGADDRESS_ADDR_ID")
    public AddressPojo getBillingAddress() {
        return billingAddress;
    }

    /**
     * @param billing Address new value for billing Address
     */
    public void setBillingAddress(AddressPojo billingAddress) {
        this.billingAddress = billingAddress;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder
            .append("Customer [id=")
            .append(id)
            .append(", ");
        if (firstName != null) {
            builder
                .append("firstName=")
                .append(firstName)
                .append(", ");
        }
        if (lastName != null) {
            builder
                .append("lastName=")
                .append(lastName)
                .append(", ");
        }
        if (phoneNumber != null) {
            builder
                .append("phoneNumber=")
                .append(phoneNumber)
                .append(", ");
        }
        if (email != null) {
            builder
                .append("email=")
                .append(email)
                .append(", ");
        }
        builder.append("]");
        return builder.toString();
    }

}