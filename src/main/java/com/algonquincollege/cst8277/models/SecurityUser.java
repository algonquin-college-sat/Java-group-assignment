/*****************************************************************c******************o*******v******id********
 * File: SecurityUser.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * update by : Oladotun Akinlabi 04.....
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.algonquincollege.cst8277.rest.SecurityRoleSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * User class used for (JSR-375) Java EE Security authorization/authentication
 */
@MappedSuperclass
@Entity(name = "SecurityUser")
@Table(name = "SECURITY_USER")
@AttributeOverride(name="id", column=@Column(name="USER_ID"))
public class SecurityUser implements Serializable, Principal {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    public static final String USER_FOR_OWNING_CUST_QUERY =
        "userForOwningCust";
    public static final String SECURITY_USER_BY_NAME_QUERY =
        "userByName";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    protected String username;
    protected String pwHash;
    protected Set<SecurityRole> roles = new HashSet<>();
    
    @OneToOne(cascade=CascadeType.ALL)
    protected CustomerPojo cust;

    public SecurityUser() {
        super();
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPwHash() {
        return pwHash;
    }
    public void setPwHash(String pwHash) {
        this.pwHash = pwHash;
    }
    
    @JsonInclude(Include.NON_NULL)
    @JsonSerialize(using = SecurityRoleSerializer.class)
    public Set<SecurityRole> getRoles() {
        return roles;
    }
    public void setRoles(Set<SecurityRole> roles) {
        this.roles = roles;
    }

    public CustomerPojo getCustomer() {
        return cust;
    }
    public void setCustomer(CustomerPojo cust) {
        this.cust = cust;
    }

    //Principal
    @JsonIgnore
    @Override
    public String getName() {
        return getUsername();
    }

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
        if (getClass() != obj.getClass()) {
            return false;
        }
        SecurityUser other = (SecurityUser)obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

}