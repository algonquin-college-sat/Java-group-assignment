/*****************************************************************c******************o*******v******id********
 * File: StorePojo.java
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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.algonquincollege.cst8277.rest.ProductSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
*
* Description: model for the Store object
*/
@Entity(name="Store")
@Table(name="STORE")
@AttributeOverride(name="id", column=@Column(name="store_id"))
public class StorePojo extends PojoBase implements Serializable {
    private static final long serialVersionUID = 1L;
 
    /**
     * Store name
     */
    protected String storeName;
    /**
     * List of products in store
     */
    protected Set<ProductPojo> products = new HashSet<>();

    /**
     * Constructor
     */
    // JPA requires each @Entity class have a default constructor
    public StorePojo() {
    }

    /**
     * Get store name
     * 
     * @return String
     */
    public String getStoreName() {
        return storeName;
    }
    
    /**
     * Set store name
     * 
     * @param storeName
     */
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    
    /**
     * Get products in store
     * 
     * @return Set<ProductPojo>
     */
    @JsonSerialize(using = ProductSerializer.class)
      //Discovered what I think is a bug: you should be able to list them in any order,
      //but it turns out, EclipseLink's JPA implementation needs the @JoinColumn StorePojo's PK
      //first, the 'inverse' to ProductPojo's PK second
    @ManyToMany
    @JoinTable(name = "stores_products",
    joinColumns = @JoinColumn(name = "store_id"),
    inverseJoinColumns = @JoinColumn(name = "product_id"))
    public Set<ProductPojo> getProducts() {
        return products;
    }
    
    /**
     * Set products
     * 
     * @param products
     */
    public void setProducts(Set<ProductPojo> products) {
        this.products = products;
    }
}
