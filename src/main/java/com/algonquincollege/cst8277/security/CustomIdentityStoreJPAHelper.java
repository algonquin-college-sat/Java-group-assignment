/*****************************************************************c******************o*******v******id********
 * File: CustomIdentityStoreJPAHelper.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * update by : Hanna Bernyk 040904190
 * update by : Oladotun Akinlabi 040892548
 * update by : Jeffrey Sharpe 040936079
 * 
 */
package com.algonquincollege.cst8277.security;

import static com.algonquincollege.cst8277.models.SecurityUser.SECURITY_USER_BY_NAME_QUERY;
import static com.algonquincollege.cst8277.utils.MyConstants.PARAM1;
import static com.algonquincollege.cst8277.utils.MyConstants.PU_NAME;
import static java.util.Collections.emptySet;

import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import com.algonquincollege.cst8277.models.ProductPojo;
import com.algonquincollege.cst8277.models.SecurityRole;
import com.algonquincollege.cst8277.models.SecurityUser;

/*
 * Stateless Session bean should also be a Singleton
 */
public class CustomIdentityStoreJPAHelper {
    public static final String CUSTOMER_PU = "acmeCustomers-PU";

    /**
     * Inject Entity Manager
     */
    @PersistenceContext(name = CUSTOMER_PU)
    protected EntityManager em;

    /**
     * Find User by Name
     * 
     * @param username.
     * @return SecurityUser. Returns found Security User
     */
    public SecurityUser findUserByName(String username) {
        SecurityUser user = null;
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<SecurityUser> q = cb.createQuery(SecurityUser.class);
            Root<SecurityUser> c = q.from(SecurityUser.class);
            q.where(cb.equal(c.get("username"), username));
            TypedQuery<SecurityUser> q2 = em.createQuery(q);
            user = q2.getSingleResult();
        }
        catch (Exception e) {
            //e.printStackTrace();
        }
        return user;
    }

    /**
     * Find set of Role Names for user
     * 
     * @param username
     * @return Set<String>. Returns set of Role Names by username
     */
    
    public Set<String> findRoleNamesForUser(String username) {
        Set<String> roleNames = emptySet();
        SecurityUser securityUser = findUserByName(username);
        if (securityUser != null) {
            roleNames = securityUser.getRoles().stream().map(s -> s.getRoleName()).collect(Collectors.toSet());
        }
        return roleNames;
    }

    /**
     * Save SecurityUser
     * 
     * @param user
     */
    
    @Transactional
    public void saveSecurityUser(SecurityUser user) {
        if(user != null) {
            em.persist(user);
        }
    }

    /**
     * Save SecurityRole
     * 
     * @param role
     */
    
    @Transactional
    public void saveSecurityRole(SecurityRole role) {
        if(role != null) {
            em.persist(role);
        }
    }
}