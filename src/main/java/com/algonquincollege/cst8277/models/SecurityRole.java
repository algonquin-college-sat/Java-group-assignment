/*****************************************************************c******************o*******v******id********
 * File: SecurityRole.java
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


/**
 * Role class used for (JSR-375) Java EE Security authorization/authentication
 */

@Entity(name="SecurityRole")
@Table(name="SECURITY_ROLE")
@NamedQuery(
    name="roleByName",
    query="SELECT c FROM SecurityRole c WHERE c.roleName=:custRoleName"
)
@AttributeOverride(name="id", column=@Column(name="ROLE_ID"))
public class SecurityRole implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * Constant
     */
    public static final String ROLE_BY_NAME_QUERY = "roleByName";

    /**
     * Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    
    /**
     * Role Name
     */
    protected String roleName;
    
    /**
     * Users
     */
    @ManyToMany(mappedBy = "roles", cascade = CascadeType.PERSIST)
    protected Set<SecurityUser> users;

    
    /**
     * Constructor
     */
    public SecurityRole() {
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
     * @return the value for roleName
     */
    public String getRoleName() {
        return roleName;
    }
    
    /**
     * @param roleName new value for roleName
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * @return the value for users
     */
    @JsonInclude(Include.NON_NULL)
    public Set<SecurityUser> getUsers() {
        return users;
    }
    
    /**
     * @param users new value for users
     */
    public void setUsers(Set<SecurityUser> users) {
        this.users = users;
    }
    
    /**
     * Add users to role
     * @param user
     */
    public void addUserToRole(SecurityUser user) {
        getUsers().add(user);
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
        SecurityRole other = (SecurityRole)obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }
}