/*****************************************************************c******************o*******v******id********
 * File: OrderSystemTestSuite.java
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 *
 * @date 2020 10
 *
 * update by : Oladotun Akinlabi 040892548
 * update by : Jeffrey Sharpe 040936079
 * update by : Hanna Bernyk 040904190
 */
package com.algonquincollege.cst8277;

import static com.algonquincollege.cst8277.utils.MyConstants.ADMIN_ROLE;
import static com.algonquincollege.cst8277.utils.MyConstants.APPLICATION_API_VERSION;
import static com.algonquincollege.cst8277.utils.MyConstants.CUSTOMER_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_ADMIN_USER;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_ADMIN_USER_PASSWORD;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_USER_PASSWORD;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_USER_PREFIX;
import static com.algonquincollege.cst8277.utils.MyConstants.ORDER_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.PRODUCT_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static com.algonquincollege.cst8277.utils.MyConstants.STORE_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.USER_ROLE;
import static com.algonquincollege.cst8277.utils.MyConstants.SLASH;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.soteria.WrappingCallerPrincipal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.algonquincollege.cst8277.models.AddressPojo;
import com.algonquincollege.cst8277.models.CustomerPojo;
import com.algonquincollege.cst8277.models.OrderPojo;
import com.algonquincollege.cst8277.models.ProductPojo;
import com.algonquincollege.cst8277.models.SecurityUser;
import com.algonquincollege.cst8277.models.StorePojo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class OrderSystemTestSuite {
    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LoggerFactory.getLogger(_thisClaz);

    static final String APPLICATION_CONTEXT_ROOT = "rest-orderSystem";
    static final String HTTP_SCHEMA = "http";
    static final String HOST = "localhost";
    
    //TODO - if you changed your Payara's default port (to say for example 9090)
    //       your may need to alter this constant
    static final int PORT = 8080;
    private int CUST_ID = 1;
    private int PRODUCT_ID = 1;
    private int STORE_ID = 1;
    private int ORDER_ID = 2;
    private int PRODUCT_ID_1 = 1;

    // test fixture(s)
    static URI uri;
    static HttpAuthenticationFeature adminAuth;
    static HttpAuthenticationFeature userAuth;

    @BeforeAll
    public static void oneTimeSetUp() throws Exception {
        logger.debug("oneTimeSetUp");
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        uri = UriBuilder
            .fromUri(APPLICATION_CONTEXT_ROOT + APPLICATION_API_VERSION)
            .scheme(HTTP_SCHEMA)
            .host(HOST)
            .port(PORT)
            .build();
        adminAuth = HttpAuthenticationFeature.basic(DEFAULT_ADMIN_USER, DEFAULT_ADMIN_USER_PASSWORD);
        userAuth = HttpAuthenticationFeature.basic(DEFAULT_USER_PREFIX, DEFAULT_USER_PASSWORD);
    }

    protected WebTarget webTarget;
    @BeforeEach
    public void setUp() {
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        webTarget = client.target(uri);
    }

    @Test
    public void test01_all_customers_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        List<CustomerPojo> custs = response.readEntity(new GenericType<List<CustomerPojo>>(){});
        assertThat(custs, is(not(empty())));
        assertThat(custs.size(), greaterThan(0));
    }
    
    // TODO - create39 more test-cases that send GET/PUT/POST/DELETE messages
    // to REST'ful endpoints for the OrderSystem entities using the JAX-RS
    // ClientBuilder APIs

    @Test
    public void test02_all_stores_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(adminAuth)
            .path(STORE_RESOURCE_NAME)
            .request()
            .get();
        
        assertThat(response.getStatus(), is(200));
        List<StorePojo> stores = response.readEntity(new GenericType<List<StorePojo>>(){});
        assertThat(stores, is(not(empty())));
        assertThat(stores.size(), greaterThan(0));
    }
    
    @Test
    public void test03_all_orders_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(adminAuth)
            .path(ORDER_RESOURCE_NAME)
            .request()
            .get();
        
        assertThat(response.getStatus(), is(200));
        List<OrderPojo> orders = response.readEntity(new GenericType<List<OrderPojo>>(){});
        assertThat(orders, is(not(empty())));
        assertThat(orders.size(), greaterThan(0));
    }

    @Test
    public void test04_all_products_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(adminAuth)
            .path(PRODUCT_RESOURCE_NAME)
            .request()
            .get();
        
        assertThat(response.getStatus(), is(200));
        List<ProductPojo> custs = response.readEntity(new GenericType<List<ProductPojo>>(){});
        assertThat(custs, is(not(empty())));
        assertThat(custs.size(), greaterThan(0));
    }

    @Test
    public void test05_get_all_Customers_By_Id_With_AdminRole() {
        Response response = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + SLASH + CUST_ID)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        CustomerPojo savedCust = response.readEntity(CustomerPojo.class);
        assertThat(savedCust.getId(), is(CUST_ID));
    }
    
    @Test
    public void test06_get_all_Products_By_Id_() {
        Response response = webTarget
            .register(adminAuth)
            .path(PRODUCT_RESOURCE_NAME + SLASH + PRODUCT_ID)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        ProductPojo savedProductPojo = response.readEntity(ProductPojo.class);
        assertThat(savedProductPojo.getId(), is(notNullValue()));
    }
    
    @Test
    public void test07_get_all_stores_By_Id_() {
        Response response = webTarget
            .register(adminAuth)
            .path(STORE_RESOURCE_NAME + SLASH + STORE_ID)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        StorePojo savedStorePojo = response.readEntity(StorePojo.class);
        assertThat(savedStorePojo.getId(), is(notNullValue()));
    }
    
    @Test
    public void test08_get_all_orders_By_Id_() {
        Response response = webTarget
            .register(adminAuth)
            .path(ORDER_RESOURCE_NAME + SLASH + ORDER_ID)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        StorePojo savedStorePojo = response.readEntity(StorePojo.class);
        assertThat(savedStorePojo.getId(), is(notNullValue()));
    }
    
    @Test
    public void test09_update_Customer_with_Admin_Role() {
        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.setId(CUST_ID);
        customerPojo.setLastName("LastName updated");
        customerPojo.setFirstName("FirstName updated");
        Response response = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + SLASH)
            .request(MediaType.APPLICATION_JSON)
            .put(Entity.entity(customerPojo, MediaType.APPLICATION_JSON));
        assertThat(response.getStatus(), is(200));
        CustomerPojo updatedProject = response.readEntity(CustomerPojo.class);
        assertThat(updatedProject.getLastName(), is(equalTo("LastName updated")));
        assertThat(updatedProject.getFirstName(), is(equalTo("FirstName updated")));
    }
    
    @Test
    public void test10_update_Order_with_Admin_Role() {
        String descriptionToUpdate = "New Description";
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setId(ORDER_ID);
        orderPojo.setDescription(descriptionToUpdate);
        Response response = webTarget
            .register(adminAuth)
            .path(ORDER_RESOURCE_NAME + SLASH)
            .request(MediaType.APPLICATION_JSON)
            .put(Entity.entity(orderPojo, MediaType.APPLICATION_JSON));
        assertThat(response.getStatus(), is(200));
        OrderPojo updatedProject = response.readEntity(OrderPojo.class);
        assertThat(updatedProject.getDescription(), is(equalTo(descriptionToUpdate)));
        assertEquals(updatedProject.getId(), ORDER_ID);
    }
    
    @Test
    public void test11_updateProduct_with_Admin_Role() {
        ProductPojo productPojo = new ProductPojo();
        String newDescription = "Description updated";
        String newSerialNumber = "SerialNo updated";
        productPojo.setId(PRODUCT_ID);
        productPojo.setDescription(newDescription);
        productPojo.setSerialNo(newSerialNumber);
        Response response = webTarget
            .register(adminAuth)
            .path(PRODUCT_RESOURCE_NAME + SLASH)
            .request(MediaType.APPLICATION_JSON).
            put(Entity.entity(productPojo, MediaType.APPLICATION_JSON));
        assertThat(response.getStatus(), is(200));
        ProductPojo updatedProject = response.readEntity(ProductPojo.class);
        assertThat(updatedProject.getDescription(), is(equalTo(newDescription)));
        assertThat(updatedProject.getSerialNo(), is(equalTo(newSerialNumber)));
    }
    
    @Test
    public void test12_update_store_with_Admin_Role() {
        String newStoreName = "StoreName updated";
        StorePojo storePojo = new StorePojo();
        storePojo.setId(STORE_ID);
        storePojo.setStoreName(newStoreName);
        Response response = webTarget
            .register(adminAuth)
            .path(STORE_RESOURCE_NAME + SLASH)
            .request(MediaType.APPLICATION_JSON)
            .put(Entity.entity(storePojo, MediaType.APPLICATION_JSON));
        assertThat(response.getStatus(), is(200));
        StorePojo updatedProject = response.readEntity(StorePojo.class);
        assertThat(updatedProject.getStoreName(), is(equalTo(newStoreName)));
    }
    
    @Test
    public void test13_update_Customer_with_User_Role_forbiddenError() {
        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.setId(CUST_ID);
        customerPojo.setLastName("LastName updated");
        customerPojo.setFirstName("FirstName updated");
        Response response = webTarget
            .register(userAuth)
            .path(CUSTOMER_RESOURCE_NAME + SLASH)
            .request(MediaType.APPLICATION_JSON)
            .put(Entity.entity(customerPojo, MediaType.APPLICATION_JSON));
        assertThat(response.getStatus(), is(401));
    }
    
    @Test
    public void test14_update_Order_with_User_Role_forbiddenError() {
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setId(ORDER_ID);
        orderPojo.setDescription("Description Updated");
        Response response = webTarget
            .register(userAuth)
            .path(ORDER_RESOURCE_NAME + SLASH)
            .request(MediaType.APPLICATION_JSON)
            .put(Entity.entity(orderPojo, MediaType.APPLICATION_JSON));
        assertThat(response.getStatus(), is(401));
    }
    
    @Test
    public void test15_update_Product_with_User_Role_forbiddenError() {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setId(PRODUCT_ID);
        productPojo.setDescription("Description updated2");
        productPojo.setSerialNo("newSerial2");
        Response response = webTarget
            .register(userAuth)
            .path(PRODUCT_RESOURCE_NAME + SLASH)
            .request(MediaType.APPLICATION_JSON)
            .put(Entity.entity(productPojo, MediaType.APPLICATION_JSON));
        assertThat(response.getStatus(), is(401));
    }
    
    @Test
    public void test16_update_store_with_User_Role_forbiddenError() {
        StorePojo storePojo = new StorePojo();
        storePojo.setId(STORE_ID);
        storePojo.setStoreName("StoreName updated");
        Response response = webTarget
            .register(userAuth)
            .path(STORE_RESOURCE_NAME + SLASH)
            .request(MediaType.APPLICATION_JSON)
            .put(Entity.entity(storePojo, MediaType.APPLICATION_JSON));
        assertThat(response.getStatus(), is(401));
    }

    @Test
    public void test17_add_New_customer_with_Admin_Role() {
        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.setLastName("Akinlabi");
        customerPojo.setFirstName("Dotun");
        Response response = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(customerPojo, MediaType.APPLICATION_JSON));
        assertThat(response.getStatus(), is(200));
        CustomerPojo newCustomer = response.readEntity(CustomerPojo.class);
        assertThat(newCustomer.getId(), is(notNullValue()));
        CUST_ID = newCustomer.getId();
    }
    
    @Test
    public void test18_add_New_order_with_Admin_Role() {
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setDescription("New Description Added");
        Response response = webTarget
            .register(adminAuth)
            .path(ORDER_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(orderPojo, MediaType.APPLICATION_JSON));
        assertThat(response.getStatus(), is(200));
        OrderPojo newOrder = response.readEntity(OrderPojo.class);
        assertThat(newOrder.getId(), is(notNullValue()));
        ORDER_ID = newOrder.getId();
    }
    
    @Test
    public void test19_add_New_product_with_Admin_Role() {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setDescription(" Irish Spring Soap");
        Response response = webTarget
            .register(adminAuth)
            .path(PRODUCT_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(productPojo, MediaType.APPLICATION_JSON));
        assertThat(response.getStatus(), is(200));
        ProductPojo newProduct = response.readEntity(ProductPojo.class);
        assertThat(newProduct.getId(), is(notNullValue()));
        PRODUCT_ID = newProduct.getId();
    }
    
    @Test
    public void test20_add_New_Store_with_Admin_Role() {
        StorePojo storePojo = new StorePojo();
        storePojo.setStoreName("African Store");
        Response response = webTarget
            .register(adminAuth)
            .path(STORE_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(storePojo, MediaType.APPLICATION_JSON));
        assertThat(response.getStatus(), is(200));
        StorePojo newStore = response.readEntity(StorePojo.class);
        assertThat(newStore.getId(), is(notNullValue()));
        STORE_ID = newStore.getId();
    }
    
    @Test
    public void test21_add_New_customer_with_User_Role_forbiddenError() {
        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.setLastName("Akinlabi");
        customerPojo.setFirstName("Dotun");
        Response response = webTarget
            .register(userAuth)
            .path(STORE_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(customerPojo, MediaType.APPLICATION_JSON));
        assertThat(response.getStatus(), is(401));
    }
    
    @Test
    public void test22_add_New_order_with_User_Role_forbiddenError() {
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setDescription("New Description Added");
        Response response = webTarget
            .register(userAuth)
            .path(STORE_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(orderPojo, MediaType.APPLICATION_JSON));
        assertThat(response.getStatus(), is(401));
    }
    
    @Test
    public void test23_add_New_product_with_User_Role_forbiddenError() {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setDescription(" Irish Spring Soap");
        Response response = webTarget
            .register(userAuth)
            .path(STORE_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(productPojo, MediaType.APPLICATION_JSON));
        assertThat(response.getStatus(), is(401));
    }
    
    @Test
    public void test24_add_New_Store_with_User_Role_forbiddenError() {
        StorePojo storePojo = new StorePojo();
        storePojo.setStoreName("American Store");
        Response response = webTarget
            .register(userAuth)
            .path(STORE_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(storePojo, MediaType.APPLICATION_JSON));
        assertThat(response.getStatus(), is(401));
    }
    
    @Test
    public void test25_get_all_customers_permit_All() {
        Response response = webTarget
            .register(userAuth)
            .path(CUSTOMER_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .get();
        assertThat(response.getStatus(), is(401));
    }
    
    @Test
    public void test26_get_all_orders_forbiddenError() {
        Response response = webTarget
            .register(userAuth)
            .path(ORDER_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .get();
        assertThat(response.getStatus(), is(401));
    }
    
    @Test
    public void test27_get_all_products_permit_All() {
        Response response = webTarget
            .path(PRODUCT_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .get();
        assertThat(response.getStatus(), is(200));
        List<ProductPojo> products = response.readEntity(new GenericType<List<ProductPojo>>() {});
        assertThat(products, is(not(empty())));
    }
    
    @Test
    public void test28_get_all_stores_permit_All() {
        Response response = webTarget
            .path(STORE_RESOURCE_NAME)
            .request(MediaType.APPLICATION_JSON)
            .get();
        assertThat(response.getStatus(), is(200));
        List<StorePojo> stores = response.readEntity(new GenericType<List<StorePojo>>() {});
        assertThat(stores, is(not(empty())));
    }
    
    @Test
    public void test29_get_customer_By_Id_ForbiddenForUserWithoutUserRole() {
        Response response = webTarget
            .register(userAuth)
            .path(CUSTOMER_RESOURCE_NAME + SLASH + CUST_ID)
            .request(MediaType.APPLICATION_JSON)
            .get();
        assertThat(response.getStatus(), is(401));
    }
    
    @Test
    public void test30_get_order_By_Id_permit_All() {
        Response response = webTarget
            .path(ORDER_RESOURCE_NAME + SLASH + ORDER_ID)
            .request(MediaType.APPLICATION_JSON)
            .get();
        assertThat(response.getStatus(), is(200));
        OrderPojo newOrder = response.readEntity(OrderPojo.class);
        assertThat(newOrder.getId(), is(notNullValue()));
    }
    
    @Test
    public void test31_get_product_By_Id_permit_All() {
        Response response = webTarget
            .path(PRODUCT_RESOURCE_NAME + SLASH + PRODUCT_ID)
            .request(MediaType.APPLICATION_JSON)
            .get();
        assertThat(response.getStatus(), is(200));
        ProductPojo newProduct = response.readEntity(ProductPojo.class);
        assertThat(newProduct.getId(), is(notNullValue()));
    }
    
    @Test
    public void test32_get_store_By_Id_permit_All() {
        Response response = webTarget
            .path(STORE_RESOURCE_NAME + SLASH + STORE_ID)
            .request(MediaType.APPLICATION_JSON)
            .get();
        assertThat(response.getStatus(), is(200));
        StorePojo newStore = response.readEntity(StorePojo.class);
        assertThat(newStore.getId(), is(notNullValue()));
    }

}