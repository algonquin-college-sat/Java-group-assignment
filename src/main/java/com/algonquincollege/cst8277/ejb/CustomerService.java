/*****************************************************************c******************o*******v******id********
 * File: CustomerService.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * update by : Hanna Bernyk 040904190
 * update by : Oladotun Akinlabi 040892548
 * update by : Jeffrey Sharpe 040936079
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
import static com.algonquincollege.cst8277.utils.MyConstants.PARAM_CUST_ROLE_NAME;

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
import com.algonquincollege.cst8277.models.OrderLinePk;
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
    /**
     * Define constants
     */
    private static final long serialVersionUID = 1L;
    public static final String CUSTOMER_PU = "acmeCustomers-PU";

    /**
     * Inject Entity Manager
     */
    @PersistenceContext(name = CUSTOMER_PU)
    protected EntityManager em;
    
    /**
     * Inject Servlet Context
     */
    @Inject
    protected ServletContext servletContext;
    
    /**
     * Inject Pbkdf2PasswordHash
     */
    @Inject
    protected Pbkdf2PasswordHash pbAndjPasswordHash;

    /**
     * Get All customers
     * 
     * @return List<CustomerPojo>. Returns all customers.
     */
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

    /**
     * Get Customer by Id
     * 
     * @param custPK. Customer Id
     * @return CustomerPojo. Returns found customer
     */
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

    /**
     * Delete Customer by Id
     * 
     * @param custPK. Customer Id
     * @return Boolean. Returns the result of opperation (true - SUCCESS, false - FAILD)
     */
    @Transactional
    public Boolean deleteCustomerById(int custPK) {
        CustomerPojo customerToDelete = em.find(CustomerPojo.class, custPK);
        if(customerToDelete != null) {
            em.remove(customerToDelete);
            return true;
        }
        return false;
    }

    
    /**
     * Persist Customer.
     * 
     * @param newCustomer. Customer for saving.
     * @return CustomerPojo. Returns saved customer
     */
    @Transactional
    public CustomerPojo persistCustomer(CustomerPojo newCustomer) {
        buildUserForNewCustomer(newCustomer);
        return newCustomer;
    }

    /**
     * Update Customer.
     * 
     * @param customer. Customer for updating.
     * @return CustomerPojo. Returns saved customer
     */
    @Transactional
    public CustomerPojo updateCustomer(CustomerPojo customer) {
        em.merge(customer);
        return customer;
    }

    /**
     * Create a new SecurityUser and a new Customer
     * 
     * @param newCustomerWithIdTimestamps
     */
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
        SecurityRole userRole = em.createNamedQuery(ROLE_BY_NAME_QUERY, SecurityRole.class)
            .setParameter(PARAM_CUST_ROLE_NAME, USER_ROLE).getSingleResult();
        userForNewCustomer.getRoles().add(userRole);
        userRole.getUsers().add(userForNewCustomer);
        em.persist(userForNewCustomer);
    }

    /**
     * Set Address for a customer
     * 
     * @param custId. Customer Id
     * @param newAddress. new Address
     * @return CustomerPojo. Return updated Customer
     */
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

    /**
     * Get All products
     * 
     * @return List<ProductPojo>. Returns the list of all products
     */
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

    /**
     * Get Product by Id
     * 
     * @param prodPK. Product Id
     * @return ProductPojo. Returns found product
     */
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

    /**
     * Update Product
     * 
     * @param productToUpdate. Product for saving
     * @return ProductPojo. Returns updated product
     */
    @Transactional
    public ProductPojo updateProduct(ProductPojo productToUpdate) {
        em.merge(productToUpdate);
        return productToUpdate;
    }

    /**
     * Persist Product
     * 
     * @param newProduct. Product for saving
     * @return ProductPojo. Returns saved product
     */
    @Transactional
    public ProductPojo persistProduct(ProductPojo newProduct) {
        em.persist(newProduct);
        return newProduct;
    }

    /**
     * Delete product by Id/
     * 
     * @param prodId. Product Id
     * @return Boolean. Returns the result of deleting(true - SUCCESS, false - FAILD)
     */
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
    
    /**
     * Get All Stores
     * 
     * @return List<StorePojo>. Returns all stores
     */
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

    /**
     * Get Store by Id
     * 
     * @param storePK. Store Id
     * @return StorePojo. Returns found Store
     */
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

    /**
     * Save store
     * 
     * @param newStore
     * @return StorePojo. Returns saved store
     */
    @Transactional
    public StorePojo persistStore(StorePojo newStore) {
        em.persist(newStore);
        return newStore;
    }

    /**
     * Update store
     * 
     * @param storeToSave
     * @return StorePojo. Returns updated store
     */
    @Transactional
    public StorePojo updateStore(StorePojo storeToSave) {
        em.merge(storeToSave);
        return storeToSave;
    }

    /**
     * Delete Store by Id
     * 
     * @param storeId. Store Id
     * @return Boolean. Returns the result of deleting(true - SUCCESS, false - FAILD)
     */
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
    
    /**
     * Get Order by Id
     * 
     * @param orderPK. Order Id
     * @return OrderPojo. Returns found order
     */
    public OrderPojo getOrderById(int orderPK) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<OrderPojo> q = cb.createQuery(OrderPojo.class);
            Root<OrderPojo> c = q.from(OrderPojo.class);
            q.where(cb.equal(c.get("id"), orderPK));
            TypedQuery<OrderPojo> q2 = em.createQuery(q);
            if(q2.getResultList().size() > 0) {
                return q2.getSingleResult();
            } else {
                return null;
            }
        }
        catch (Exception e) {
            servletContext.log("Error during calling getOrderById function.", e);
            return null;
        }
    }

    /**
     * Get All Orders
     * 
     * @return List<OrderPojo>. Returns all orders
     */
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
    
    /**
     * Save Order
     * 
     * @param newOrder
     * @return OrderPojo. Returns saved/updated Order
     */
    @Transactional
    public OrderPojo persistOrder(OrderPojo newOrder) {
        em.merge(newOrder);
        return newOrder;
    }
    
    /**
     * Delete Order By Id
     * 
     * @param orderId. Order Id
     * @return Boolean. Returns the result of deleting(true - SUCCESS, false - FAILD)
     */
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

    /**
     * 
     * Save order line
     * 
     * @param productPK. Product Id
     * @param orderPK Order Id
     * @param amount. Amount
     * @return OrderLInePojo. Returns saved order Line
     */
    @Transactional
    public OrderLinePojo persistOrderLine(int productPK, int orderPK, double amount) {
        int numberOfLines = 0;
        ProductPojo productToAdd = em.find(ProductPojo.class, productPK);
        OrderPojo orderToUpdate = em.find(OrderPojo.class, orderPK);
        List<OrderLinePojo> orderLines = getAllOrderLines(orderPK);
        if(orderLines != null) {
            numberOfLines = orderLines.size();
        }
        
        if(productToAdd != null && orderToUpdate != null) {
            OrderLinePojo orderLinePojo = new OrderLinePojo();
            orderLinePojo.setOwningOrder(orderToUpdate);
            orderLinePojo.setProduct(productToAdd);
            orderLinePojo.setAmount(amount);
            OrderLinePk newOrderLinePk = new OrderLinePk();
            newOrderLinePk.setOrderLineNo(numberOfLines+1);
            newOrderLinePk.setOwningOrderId(orderToUpdate.getId());
            orderLinePojo.setPk(newOrderLinePk);
            em.persist(orderLinePojo);
            return orderLinePojo;
        }
        return null;
    }

    /**
     * Link product with store
     * 
     * @param productId. Product Id
     * @param storeId. Store Id
     * @return ProductPojo. Returns linked product
     */
    @Transactional
    public ProductPojo linkProductAndStore(int productId, int storeId) {
        ProductPojo productToUpdate = em.find(ProductPojo.class, productId);
        StorePojo storeToUpdate = em.find(StorePojo.class, storeId);
        if(productToUpdate != null && storeToUpdate != null) {
            productToUpdate.getStores().add(storeToUpdate);
        }
        return productToUpdate;
    }


    /**
     * Get All Order Lines by Order Id
     * 
     * @param orderPK. Order Id
     * @return List<OrderLinePojo>. Returns all found order lines
     */
    public List<OrderLinePojo> getAllOrderLines(int orderPK) {
        OrderPojo foundOrder = em.find(OrderPojo.class, orderPK);
        
        if(foundOrder != null && foundOrder.getOrderlines() != null) {
            return foundOrder.getOrderlines();
        }
        
        return null;
       
    }

    /**
     * Get All Order Lines, by Order
     * 
     * @param order
     * @return List<OrderLinePojo>. Returns all found order lines
     */
    public List<OrderLinePojo> getAllOrderLines(OrderPojo order) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OrderLinePojo> q = cb.createQuery(OrderLinePojo.class);
        Root<OrderLinePojo> c = q.from(OrderLinePojo.class);
        q.where(cb.equal(c.get("owningOrder"), order));
        TypedQuery<OrderLinePojo> q2 = em.createQuery(q);
        return q2.getResultList();
    }


    /**
     * Delete Order Line by Order Line Number and Order Id
     * 
     * @param orderLineNumber. Order Line Number
     * @param orderId. Order Id
     * @return Boolean. Result of deleting Order Line(true - SUCCESS, false - FAILD)
     */
    public Boolean deleteOrderLine(int orderLineNumber, int orderId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OrderLinePojo> q = cb.createQuery(OrderLinePojo.class);
        Root<OrderLinePojo> c = q.from(OrderLinePojo.class);
        OrderLinePk orderLinePk =  new OrderLinePk();
        orderLinePk.setOwningOrderId(orderId);
        orderLinePk.setOrderLineNo(orderLineNumber);
        q.where(cb.equal(c.get("primaryKey"), orderLinePk));
        TypedQuery<OrderLinePojo> q2 = em.createQuery(q);
        
        if(q2.getResultList().size() > 0) {
            OrderLinePojo orderLineToDelete = q2.getSingleResult();
            if(orderLineToDelete != null) {
                em.remove(orderLineToDelete);
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * Update Order Line
     * 
     * @param orderLineNumber. Order Line Number
     * @param orderId. Order Id
     * @param newAmount. New Amount
     * @return OrderLinePojo. Returns updated Order Line.
     */
    public OrderLinePojo updateOrderLine(int orderLineNumber, int orderId, double newAmount) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OrderLinePojo> q = cb.createQuery(OrderLinePojo.class);
        Root<OrderLinePojo> c = q.from(OrderLinePojo.class);
        OrderLinePk orderLinePk =  new OrderLinePk();
        orderLinePk.setOwningOrderId(orderId);
        orderLinePk.setOrderLineNo(orderLineNumber);
        q.where(cb.equal(c.get("primaryKey"), orderLinePk));
        TypedQuery<OrderLinePojo> q2 = em.createQuery(q);
        
        if(q2.getResultList().size() > 0) {
            OrderLinePojo orderLineToUpdate = q2.getSingleResult();
            if(orderLineToUpdate != null) {
                orderLineToUpdate.setAmount(newAmount);
                em.persist(orderLineToUpdate);
                return orderLineToUpdate;
            }
        }
        return null;
        
    }

    /**
     * Get Order Line by Order Id and Order Line Number
     * 
     * 
     * @param orderId. Order Id
     * @param orderLineNumber. Order Line Number
     * @return OrderLinePojo. Returns updated OrderLinePojo
     */
    public OrderLinePojo getOrderLineByOrderAndNumber(int orderId, int orderLineNumber) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OrderLinePojo> q = cb.createQuery(OrderLinePojo.class);
        Root<OrderLinePojo> c = q.from(OrderLinePojo.class);
        OrderLinePk orderLinePk =  new OrderLinePk();
        orderLinePk.setOwningOrderId(orderId);
        orderLinePk.setOrderLineNo(orderLineNumber);
        q.where(cb.equal(c.get("primaryKey"), orderLinePk));
        TypedQuery<OrderLinePojo> q2 = em.createQuery(q);
        
        if(q2.getResultList().size() > 0) {
            OrderLinePojo orderLineToView = q2.getSingleResult();
            return orderLineToView;
        } else {
            return null;
        }
    }
}