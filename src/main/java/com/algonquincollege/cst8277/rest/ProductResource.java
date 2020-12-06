/*****************************************************************c******************o*******v******id********
 * File: ProductResource.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * update by : Hanna Bernyk 040904190
 * update by : Oladotun Akinlabi 040892548
 * update by : Jeffrey Sharpe 040936079
 *
 */
package com.algonquincollege.cst8277.rest;

import static com.algonquincollege.cst8277.utils.MyConstants.CUSTOMER_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.PRODUCT_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_PATH;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_PRODUCT_ID_ELEMENT;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_STORE_ID_ELEMENT;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.algonquincollege.cst8277.ejb.CustomerService;
import com.algonquincollege.cst8277.models.CustomerPojo;
import com.algonquincollege.cst8277.models.ProductPojo;

@Path(PRODUCT_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

    /**
     * Inject Customer Service
     */
    @EJB
    protected CustomerService customerServiceBean;

    /**
     * Inject Servlet Context
     */
    @Inject
    protected ServletContext servletContext;

    /**
     * Get All Products
     * 
     * @return Response. Returns all products
     */
    @RolesAllowed({"ADMIN_ROLE", "USER_ROLE"})
    @GET
    public Response getProducts() {
        servletContext.log("retrieving all products ...");
        List<ProductPojo> custs = customerServiceBean.getAllProducts();
        Response response = Response.ok(custs).build();
        return response;
    }

    /**
     * Get Product by Id
     * 
     * @param id. Product Id
     * @return Response. Returns found product.
     */
    @RolesAllowed({"ADMIN_ROLE", "USER_ROLE"})
    @GET
    @Path("{id}")
    public Response getProductById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to retrieve specific product " + id);
        ProductPojo theProduct = customerServiceBean.getProductById(id);
        Response response = Response.ok(theProduct).build();
        return response;
    }

    /**
     * Delete Product by Id.
     * 
     * @param id. Product Id
     * @return Response. Returns the result of deleting(true - SUCCESS, false - FAILD)
     */
    @RolesAllowed({"ADMIN_ROLE"})
    @DELETE
    @Path("{id}")
    public Response deleteProductById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to delete specific product " + id);
        Response response = null;
        Boolean result = true;

        result = customerServiceBean.deleteProductById(id);
        response = Response.status( result == false ? NOT_FOUND : OK).entity(result).build();
        return response;
    }

    /**
     * Add new Product
     * 
     * @param newProduct. New product for saving
     * @return Response. Returns saved product
     */
    @RolesAllowed({"ADMIN_ROLE"})
    @POST
    public Response addProduct(ProductPojo newProduct) {
      Response response = null;
      ProductPojo saveCustomerPojo = customerServiceBean.persistProduct(newProduct);
      response = Response.ok(newProduct).build();
      return response;
    }

    /**
     * Link Product and Store
     * 
     * @param productId
     * @param storeId
     * @return Response. Returns updated product
     */
    @RolesAllowed({"ADMIN_ROLE"})
    @PUT
    @Path("/addProductToStore/{productId}/{storeId}")
    public Response addProductToStore(@PathParam(RESOURCE_PATH_PRODUCT_ID_ELEMENT) int productId,
        @PathParam(RESOURCE_PATH_STORE_ID_ELEMENT) int storeId) {
        Response response = null;
        ProductPojo updatedProduct = customerServiceBean.linkProductAndStore(productId, storeId);
        response = Response.ok(updatedProduct).build();
        return response;
    }

}