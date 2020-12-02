/*****************************************************************c******************o*******v******id********
 * File: CustomerService.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * update by : Hanna Bernyk 040904190
 *
 */
package com.algonquincollege.cst8277.ejb;

import static com.algonquincollege.cst8277.models.SecurityRole.ROLE_BY_NAME_QUERY;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_KEY_SIZE;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_PROPERTY_ALGORITHM;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_PROPERTY_ITERATIONS;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_SALT_SIZE;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_USER_PASSWORD;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_USER_PREFIX;
import static com.algonquincollege.cst8277.utils.MyConstants.PARAM1;
import static com.algonquincollege.cst8277.utils.MyConstants.PROPERTY_ALGORITHM;
import static com.algonquincollege.cst8277.utils.MyConstants.PROPERTY_ITERATIONS;
import static com.algonquincollege.cst8277.utils.MyConstants.PROPERTY_KEYSIZE;
import static com.algonquincollege.cst8277.utils.MyConstants.PROPERTY_SALTSIZE;
import static com.algonquincollege.cst8277.utils.MyConstants.USER_ROLE;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.servlet.ServletContext;
import com.algonquincollege.cst8277.models.AddressPojo;
import com.algonquincollege.cst8277.models.CustomerPojo;
import com.algonquincollege.cst8277.models.OrderLinePojo;
import com.algonquincollege.cst8277.models.OrderPojo;
import com.algonquincollege.cst8277.models.ProductPojo;
import com.algonquincollege.cst8277.models.SecurityRole;
import com.algonquincollege.cst8277.models.SecurityUser;
import com.algonquincollege.cst8277.models.ShippingAddressPojo;
import com.algonquincollege.cst8277.models.StorePojo;

/**
 * Stateless Singleton Session Bean - CustomerService
 */
@Singleton
public class CustomerService implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String CUSTOMER_PU = "acmeCustomers-PU";
    @PersistenceContext(name = CUSTOMER_PU)
    protected EntityManager em;
    @Inject
    protected ServletContext servletContext;
    @Inject
    protected Pbkdf2PasswordHash pbAndjPasswordHash;
    // TODO

    public List<CustomerPojo> getAllCustomers() {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<CustomerPojo> q = cb.createQuery(CustomerPojo.class);
            Root<CustomerPojo> c = q.from(CustomerPojo.class);
            q.select(c);
            TypedQuery<CustomerPojo> q2 = em.createQuery(q);
            List<CustomerPojo> allCustomers = q2.getResultList();
            return allCustomers;
        }
        catch (Exception e) {
            servletContext.log("Error during calling getAllCustomers function.", e);
            return null;
        }
    }

    public CustomerPojo getCustomerById(int custPK) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<CustomerPojo> q = cb.createQuery(CustomerPojo.class);
            Root<CustomerPojo> c = q.from(CustomerPojo.class);
            q.where(cb.equal(c.get("id"), custPK));
            TypedQuery<CustomerPojo> q2 = em.createQuery(q);
            return q2.getSingleResult();
        }
        catch (Exception e) {
            servletContext.log("Error during calling getCustomerById function.", e);
            return null;
        }
    }

    @Transactional
    public Boolean deleteCustomerById(int custPK) {
        CustomerPojo customerToDelete = em.find(CustomerPojo.class, custPK);
        if(customerToDelete != null) {
            em.remove(customerToDelete);
            return true;
        }
        return false;
    }

    
    @Transactional
    public CustomerPojo persistCustomer(CustomerPojo newCustomer) {
        buildUserForNewCustomer(newCustomer);
        return newCustomer;
    }

    @Transactional
    public void buildUserForNewCustomer(CustomerPojo newCustomerWithIdTimestamps) {
        SecurityUser userForNewCustomer = new SecurityUser();
        userForNewCustomer.setUsername(DEFAULT_USER_PREFIX + "" + newCustomerWithIdTimestamps.getId());
        Map<String, String> pbAndjProperties = new HashMap<>();
        pbAndjProperties.put(PROPERTY_ALGORITHM, DEFAULT_PROPERTY_ALGORITHM);
        pbAndjProperties.put(PROPERTY_ITERATIONS, DEFAULT_PROPERTY_ITERATIONS);
        pbAndjProperties.put(PROPERTY_SALTSIZE, DEFAULT_SALT_SIZE);
        pbAndjProperties.put(PROPERTY_KEYSIZE, DEFAULT_KEY_SIZE);
        pbAndjPasswordHash.initialize(pbAndjProperties);
        String pwHash = pbAndjPasswordHash.generate(DEFAULT_USER_PASSWORD.toCharArray());
        userForNewCustomer.setPwHash(pwHash);
        userForNewCustomer.setCustomer(newCustomerWithIdTimestamps);
        /**SecurityRole userRole = em.createNamedQuery(ROLE_BY_NAME_QUERY,
        SecurityRole.class).setParameter(PARAM1, USER_ROLE).getSingleResult();
        userForNewCustomer.getRoles().add(userRole);
        userRole.getUsers().add(userForNewCustomer);
        **/
        em.persist(userForNewCustomer);
    }

    @Transactional
    public CustomerPojo setAddressFor(int custId, AddressPojo newAddress) {
        CustomerPojo updatedCustomer = em.find(CustomerPojo.class, custId);
        if (newAddress instanceof ShippingAddressPojo) {
            updatedCustomer.setShippingAddress(newAddress);
        }
        else {
            updatedCustomer.setBillingAddress(newAddress);
        }
        em.merge(updatedCustomer);
        return updatedCustomer;
    }

    public List<ProductPojo> getAllProducts() {
        // example of using JPA Criteria query instead of JPQL
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<ProductPojo> q = cb.createQuery(ProductPojo.class);
            Root<ProductPojo> c = q.from(ProductPojo.class);
            q.select(c);
            TypedQuery<ProductPojo> q2 = em.createQuery(q);
            List<ProductPojo> allProducts = q2.getResultList();
            return allProducts;
        }
        catch (Exception e) {
            return null;
        }
    }

    public ProductPojo getProductById(int prodPK) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<ProductPojo> q = cb.createQuery(ProductPojo.class);
            Root<ProductPojo> c = q.from(ProductPojo.class);
            q.where(cb.equal(c.get("id"), prodPK));
            TypedQuery<ProductPojo> q2 = em.createQuery(q);
            return q2.getSingleResult();
        }
        catch (Exception e) {
            servletContext.log("Error during calling getProductById function.", e);
            return null;
        }
    }

    @Transactional
    public ProductPojo persistProduct(ProductPojo newProduct) {
        em.persist(newProduct);
        return newProduct;
    }

    @Transactional
    public Boolean deleteProductById(int prodId) {
        Boolean result = false;
        ProductPojo productToDelete = em.find(ProductPojo.class, prodId);
        if(productToDelete != null) {
            em.remove(productToDelete);
            result = true;
        }
        return result;
    }
    
    public List<StorePojo> getAllStores() {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<StorePojo> q = cb.createQuery(StorePojo.class);
            Root<StorePojo> c = q.from(StorePojo.class);
            q.select(c);
            TypedQuery<StorePojo> q2 = em.createQuery(q);
            List<StorePojo> allStores = q2.getResultList();
            return allStores;
        }
        catch (Exception e) {
            return null;
        }
    }

    public StorePojo getStoreById(int storePK) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<StorePojo> q = cb.createQuery(StorePojo.class);
            Root<StorePojo> c = q.from(StorePojo.class);
            q.where(cb.equal(c.get("id"), storePK));
            TypedQuery<StorePojo> q2 = em.createQuery(q);
            return q2.getSingleResult();
        }
        catch (Exception e) {
            servletContext.log("Error during calling getProductById function.", e);
            return null;
        }
    }

    @Transactional
    public StorePojo persistStore(StorePojo newStore) {
        em.persist(newStore);
        return newStore;
    }
    
    @Transactional
    public Boolean deleteStoreById(int storeId) {
        Boolean result = false;
        StorePojo storeToDelete = em.find(StorePojo.class, storeId);
        if(storeToDelete != null) {
            em.remove(storeToDelete);
            result = true;
        }
        return result;
    }
    /*
     * 
     * public OrderPojo getAllOrders ... getOrderbyId ... build Orders with OrderLines ...
     * 
     */

    public OrderPojo getOrderById(int orderPK) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<OrderPojo> q = cb.createQuery(OrderPojo.class);
            Root<OrderPojo> c = q.from(OrderPojo.class);
            q.where(cb.equal(c.get("id"), orderPK));
            TypedQuery<OrderPojo> q2 = em.createQuery(q);
            return q2.getSingleResult();
        }
        catch (Exception e) {
            servletContext.log("Error during calling getOrderById function.", e);
            return null;
        }
    }

    public List<OrderPojo> getAllOrders() {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<OrderPojo> q = cb.createQuery(OrderPojo.class);
            Root<OrderPojo> c = q.from(OrderPojo.class);
            q.select(c);
            TypedQuery<OrderPojo> q2 = em.createQuery(q);
            List<OrderPojo> allOrders = q2.getResultList();
            return allOrders;
        }
        catch (Exception e) {
            return null;
        }
    }
    
    @Transactional
    public OrderPojo persistOrder(OrderPojo newOrder) {
        em.persist(newOrder);
        return newOrder;
    }
    
    @Transactional
    public Boolean deleteOrderById(int orderId) {
        Boolean result = false;
        OrderPojo orderToDelete = em.find(OrderPojo.class, orderId);
        if(orderToDelete != null) {
            em.remove(orderToDelete);
            result = true;
        }
        return result;
    }

    @Transactional
    public OrderLinePojo persistOrderLine(OrderLinePojo newOrderLine) {
        em.persist(newOrderLine);
        return newOrderLine;
    }

    @Transactional
    public Boolean deleteOrderLineById(int ordLinePK) {
        OrderLinePojo orderLineToDelete = em.find(OrderLinePojo.class, ordLinePK);
        if(orderLineToDelete != null) {
            em.remove(orderLineToDelete);
            return true;
        }
        return false;
    }

}