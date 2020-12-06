/*****************************************************************c******************o*******v******id********
 * File: StoreResource.java
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
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_PATH;
import static com.algonquincollege.cst8277.utils.MyConstants.STORE_RESOURCE_NAME;
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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.algonquincollege.cst8277.ejb.CustomerService;
import com.algonquincollege.cst8277.models.CustomerPojo;
import com.algonquincollege.cst8277.models.StorePojo;

@Path(STORE_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StoreResource {

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
     * Get All Stores
     * 
     * @return Response. Returns all stores
     */
    @RolesAllowed({"ADMIN_ROLE", "USER_ROLE"})
    @GET
    public Response getStores() {
        servletContext.log("retrieving all stores ...");
        List<StorePojo> stores = customerServiceBean.getAllStores();
        Response response = Response.ok(stores).build();
        return response;
    }

    /**
     * Get Store by Id
     * 
     * @param id. Store Id
     * @return Response. Returns found store
     */
    @RolesAllowed({"ADMIN_ROLE", "USER_ROLE"})
    @GET
    @Path("{id}")
    public Response getStoreById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to retrieve specific store " + id);
        StorePojo theStore = customerServiceBean.getStoreById(id);
        Response response = Response.ok(theStore).build();
        return response;
    }

    
    /**
     * Add new Store
     * 
     * @param newStore
     * @return Response. Returns saved new store
     */
    @RolesAllowed({"ADMIN_ROLE"})
    @POST
    public Response addStore(StorePojo newStore) {
      Response response = null;
      StorePojo saveCustomerPojo = customerServiceBean.persistStore(newStore);
      response = Response.ok(newStore).build();
      return response;
    }

    /**
     * Delete Store by Id
     * 
     * @param id. Store Id.
     * @return Response. Returns the result of deleting(true - SUCCESS, false - FAILD)
     */
    @RolesAllowed({"ADMIN_ROLE"})
    @DELETE
    @Path("{id}")
    public Response deleteStoreById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to delete specific store " + id);
        Response response = null;
        Boolean result = true;

        result = customerServiceBean.deleteStoreById(id);
        response = Response.status( result == false ? NOT_FOUND : OK).entity(result).build();
        return response;
    }
}