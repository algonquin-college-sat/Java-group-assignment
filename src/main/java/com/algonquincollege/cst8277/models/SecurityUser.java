/*****************************************************************c******************o*******v******id********
 * File: SecurityUser.java
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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
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

    /**
     * Constants
     */
    public static final String USER_FOR_OWNING_CUST_QUERY =
        "userForOwningCust";
    public static final String SECURITY_USER_BY_NAME_QUERY =
        "userByName";

    /**
     * Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    
    /**
     * username
     */
    protected String username;
    
    /**
     * pwHash
     */
    protected String pwHash;
    
    /**
     * roles
     */
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name= "SECURITY_USER_SECURITY_ROLE",
               joinColumns = {@JoinColumn(name="user_id")},
               inverseJoinColumns = {@JoinColumn(name="role_id")})
    protected Set<SecurityRole> roles = new HashSet<>();
    
    /**
     * Customer
     */
    @OneToOne(cascade=CascadeType.ALL)
    protected CustomerPojo cust;

    /**
     * Constructor
     */
    public SecurityUser() {
        super();
    }

    /**
     * @return the value for id
     */
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
     * @return the value for username
     */
    public String getUsername() {
        return username;
    }
    /**
     * @param username new value for username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    
    /**
     * @return the value for pwHash
     */
    @JsonIgnore
    public String getPwHash() {
        return pwHash;
    }
    
    /**
     * @param pwHash new value for pwHash
     */
    public void setPwHash(String pwHash) {
        this.pwHash = pwHash;
    }
    
    /**
     * @return the value for roles
     */
    @JsonInclude(Include.NON_NULL)
    @JsonSerialize(using = SecurityRoleSerializer.class)
    public Set<SecurityRole> getRoles() {
        return roles;
    }

    /**
     * @param roles new value for roles
     */
    public void setRoles(Set<SecurityRole> roles) {
        this.roles = roles;
    }

    /**
     * @return the value for cust
     */
    public CustomerPojo getCustomer() {
        return cust;
    }
    
    /**
     * @param cust new value for cust
     */
    public void setCustomer(CustomerPojo cust) {
        this.cust = cust;
    }

    /**
     * @return the value for name
     */
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