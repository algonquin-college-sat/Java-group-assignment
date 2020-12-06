/*****************************************************************c******************o*******v******id********
 * File: OrderPojo.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * update by : Hanna Bernyk 040904190
 * update by : Oladotun Akinlabi 040892548
 * update by : Jeffrey Sharpe 040936079
 * 
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.algonquincollege.cst8277.rest.ProductSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
*
* Description: model for the Order object
*/
@Entity(name="Order")
@Table(name="ORDER_TBL")
@AttributeOverride(name="id", column=@Column(name="ORDER_ID"))
public class OrderPojo extends PojoBase implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name="DESCRIPTION")
    protected String description;
    
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

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name="ORDER_LINE", joinColumns = @JoinColumn(name = "OWNINGORDER_ORDER_ID"))
    public List<OrderLinePojo> getOrderlines() {
	    if(this.orderlines == null) {
	        this.orderlines = new ArrayList<OrderLinePojo>();
	    }
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