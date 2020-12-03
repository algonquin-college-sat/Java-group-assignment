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
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
*
* Description: model for the Order object
*/
@Entity(name="Order")
@Table(name="ORDER_TBL")
@AttributeOverride(name="id", column=@Column(name="ORDER_ID"))
public class OrderPojo extends PojoBase implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String description;
    @OneToMany
    protected List<OrderLinePojo> orderlines;
    protected CustomerPojo owningCustomer;
    
    // JPA requires each @Entity class have a default constructor
	public OrderPojo() {
	}
	
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

	public List<OrderLinePojo> getOrderlines() {
		return this.orderlines;
	}
	public void setOrderlines(List<OrderLinePojo> orderlines) {
		this.orderlines = orderlines;
	}
	public OrderLinePojo addOrderline(OrderLinePojo orderline) {
		getOrderlines().add(orderline);
		orderline.setOwningOrder(this);
		return orderline;
	}
	public OrderLinePojo removeOrderline(OrderLinePojo orderline) {
		getOrderlines().remove(orderline);
        orderline.setOwningOrder(null);
		return orderline;
	}

	public CustomerPojo getOwningCustomer() {
		return this.owningCustomer;
	}
	public void setOwningCustomer(CustomerPojo owner) {
		this.owningCustomer = owner;
	}

}