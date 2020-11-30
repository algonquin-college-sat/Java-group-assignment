/*****************************************************************c******************o*******v******id********
 * File: OrderLinePojo.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * update by : I. Am. A. Student 040nnnnnnn
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;

/**
*
* Description: model for the OrderLine object
*/
public class OrderLinePojo implements Serializable {
    private static final long serialVersionUID = 1L;

    protected OrderLinePk primaryKey;
    protected OrderPojo owningOrder;
    protected Double amount;
    protected ProductPojo product;

    // JPA requires each @Entity class have a default constructor
    public OrderLinePojo() {
    }

    public OrderLinePk getPk() {
        return primaryKey;
    }
    public void setPk(OrderLinePk primaryKey) {
        this.primaryKey = primaryKey;
    }
    
    public OrderPojo getOwningOrder() {
        return owningOrder;
    }
    public void setOwningOrder(OrderPojo owningOrder) {
        this.owningOrder = owningOrder;
    }

    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public ProductPojo getProduct() {
        return product;
    }
    public void setProduct(ProductPojo product) {
        this.product = product;
    }

}