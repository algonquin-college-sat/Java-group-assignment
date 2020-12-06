/*****************************************************************c******************o*******v******id********
 * File: BillingAddressPojo.java
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

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("B")
public class BillingAddressPojo extends AddressPojo implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    protected boolean isAlsoShipping;

    // JPA requires each @Entity class have a default constructor
    public BillingAddressPojo() {
    }

    public boolean isAlsoShipping() {
        return isAlsoShipping;
    }
    public void setAlsoShipping(boolean isAlsoShipping) {
        this.isAlsoShipping = isAlsoShipping;
    }

}