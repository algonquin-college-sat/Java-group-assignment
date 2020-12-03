/*****************************************************************c******************o*******v******id********
 * File: CustomerPojo.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * update by : Hanna Bernyk 040904190
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
    private static final long serialVersionUID = 1L;

    public static final String ALL_CUSTOMERS_QUERY_NAME = "allCustomers";

    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phoneNumber;
    protected AddressPojo shippingAddress;
    protected AddressPojo billingAddress;
	
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

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    //dont use CascadeType.All (skipping CascadeType.REMOVE): what if two customers
    //live at the same address and 1 leaves the house but the other does not?
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "SHIPPINGADDRESS_ADDR_ID")
    public AddressPojo getShippingAddress() {
        return shippingAddress;
    }
    public void setShippingAddress(AddressPojo shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "BILLINGADDRESS_ADDR_ID")
    public AddressPojo getBillingAddress() {
        return billingAddress;
    }
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