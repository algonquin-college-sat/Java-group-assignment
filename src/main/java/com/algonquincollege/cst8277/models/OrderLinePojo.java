/*****************************************************************c******************o*******v******id********
 * File: OrderLinePojo.java
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

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
*
* Description: model for the OrderLine object
*/

@Entity(name="OrderLine")
@Table(name="ORDER_LINE")
@AttributeOverride(name="id", column=@Column(name="ORDER_LINE_ID"))
public class OrderLinePojo implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected OrderLinePk primaryKey;

    /**
     * Owning Order
     */
    @MapsId("owningOrderId")
    @ManyToOne
    protected OrderPojo owningOrder;
    
    /**
     * Amoung
     */
    protected Double amount;
    
    /**
     * Product
     */
    protected ProductPojo product;

    /**
     * Constructor
     */
    // JPA requires each @Entity class have a default constructor
    public OrderLinePojo() {
    }

    /**
     * @return the value for primaryKey
     */
    public OrderLinePk getPk() {
        return primaryKey;
    }

    /**
     * @param primaryKey new value for primaryKey
     */
    public void setPk(OrderLinePk primaryKey) {
        this.primaryKey = primaryKey;
    }
    
    /**
     * @return the value for owningOrder
     */
    public OrderPojo getOwningOrder() {
        return owningOrder;
    }

    /**
     * @param owningOrder new value for owningOrder
     */
    public void setOwningOrder(OrderPojo owningOrder) {
        this.owningOrder = owningOrder;
    }

    /**
     * @return the value for amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * @param amount new value for amount
     */

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * @return the value for product
     */
    public ProductPojo getProduct() {
        return product;
    }
    
    /**
     * @param product new value for product
     */
    public void setProduct(ProductPojo product) {
        this.product = product;
    }

}