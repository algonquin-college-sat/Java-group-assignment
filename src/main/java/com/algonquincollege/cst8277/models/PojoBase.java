/*****************************************************************c******************o*******v******id********
 * File: PojoBase.java
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
import java.time.LocalDateTime;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Abstract class that is base of (class) hierarchy for all c.a.cst8277.models @Entity classes
 */
@MappedSuperclass
public abstract class PojoBase implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Id
     */
    protected int id;
    
    /**
     * Created
     */
    protected LocalDateTime created;
    
    /**
     * Updated
     */
    protected LocalDateTime updated;
    
    /**
     * Version
     */
    protected int version;

    /**
     * @return the value for id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    /**
     * @param id new value for id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the value for created
     */
    public LocalDateTime getCreatedDate() {
        return created;
    }

    /**
     * @param created new value for created
     */
    public void setCreatedDate(LocalDateTime created) {
        this.created = created;
    }

    /**
     * @return the value for updated
     */
    public LocalDateTime getUpdatedDate() {
        return updated;
    }

    /**
     * @param product new value for product
     */
    public void setUpdatedDate(LocalDateTime updated) {
        this.updated = updated;
    }

    /**
     * @return the value for version
     */
    public int getVersion() {
        return version;
    }
    
    /**
     * @param version new value for version
     */
    public void setVersion(int version) {
        this.version = version;
    }

    // It is a good idea for hashCode() to use the @Id property
    // since it maps to table's PK and that is how Db determine's identity
    // (same for equals()
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PojoBase)) {
            return false;
        }
        PojoBase other = (PojoBase)obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }
}