/*****************************************************************c******************o*******v******id********
 * File: OrderLinePk.java
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
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderLinePk implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * Owning Order Id
     */
    protected int owningOrderId;
    
    /**
     * Order Line Number
     */
    protected int orderLineNo;

    /**
     * @return the value for owningOrderId
     */
    public int getOwningOrderId() {
        return owningOrderId;
    }
    
    /**
     * @param owningOrderId new value for owningOrderId
     */
    public void setOwningOrderId(int owningOrderId) {
        this.owningOrderId = owningOrderId;
    }

    /**
     * @return the value for orderLineNo
     */
    public int getOrderLineNo() {
        return orderLineNo;
    }

    /**
     * @param orderLineNo new value for orderLineNo
     */
    public void setOrderLineNo(int orderLineNo) {
        this.orderLineNo = orderLineNo;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(orderLineNo, owningOrderId);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OrderLinePk)) {
            return false;
        }
        OrderLinePk other = (OrderLinePk) obj;
        return orderLineNo == other.orderLineNo && owningOrderId == other.owningOrderId;
    }

}