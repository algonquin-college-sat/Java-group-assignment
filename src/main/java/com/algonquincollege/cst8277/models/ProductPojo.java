/*****************************************************************c******************o*******v******id********
 * File: OrderPojo.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * update by : Hanna Bernyk 040904190
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
*
* Description: model for the Product object
*/

@Entity(name="Product")
@Table(name="PRODUCT")
@AttributeOverride(name="id", column=@Column(name="product_id"))
public class ProductPojo extends PojoBase implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String description;
    protected String serialNo;
    protected Set<StorePojo> stores = new HashSet<>();

    // JPA requires each @Entity class have a default constructor
    public ProductPojo() {
    }
    
    /**
     * @return the value for firstName
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description new value for description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public String getSerialNo() {
        return serialNo;
    }
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
    
    @JsonInclude(Include.NON_NULL)
    @ManyToMany
    @JoinTable(name = "stores_products",
    joinColumns = @JoinColumn(name = "product_id"),
    inverseJoinColumns = @JoinColumn(name = "store_id"))
    public Set<StorePojo> getStores() {
        return stores;
    }
    public void setStores(Set<StorePojo> stores) {
        this.stores = stores;
    }

}