/*****************************************************************c******************o*******v******id********
 * File: CustomerResource.java
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

import static com.algonquincollege.cst8277.utils.MyConstants.ADMIN_ROLE;
import static com.algonquincollege.cst8277.utils.MyConstants.CUSTOMER_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_PATH;
import static com.algonquincollege.cst8277.utils.MyConstants.USER_ROLE;
import static com.algonquincollege.cst8277.utils.MyConstants.CUSTOMER_ADDRESS_RESOURCE_PATH;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.soteria.WrappingCallerPrincipal;

import com.algonquincollege.cst8277.ejb.CustomerService;
import com.algonquincollege.cst8277.models.AddressPojo;
import com.algonquincollege.cst8277.models.CustomerPojo;
import com.algonquincollege.cst8277.models.SecurityUser;

@Path(CUSTOMER_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource {

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
     * Inject Security Context
     */
    @Inject
    protected SecurityContext sc;


    /**
     * @return Response. Get all customers
     * 
     */
    @RolesAllowed({"ADMIN_ROLE"})
    @GET
    public Response getCustomers() {
        servletContext.log("retrieving all customers ...");
        List<CustomerPojo> custs = customerServiceBean.getAllCustomers();
        Response response = Response.ok(custs).build();
        return response;
    }

    /**
     * 
     * @param id. Customer Id
     * @return Response. Return Selected Customer by Id
     */
    @GET
    @Path("{id}")
    public Response getCustomerById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to retrieve specific customer " + id);
        Response response = null;
        CustomerPojo cust = null;

        if (sc.isCallerInRole(ADMIN_ROLE)) {
            cust = customerServiceBean.getCustomerById(id);
            response = Response.status( cust == null ? NOT_FOUND : OK).entity(cust).build();
        }
        else if (sc.isCallerInRole(USER_ROLE)) {
            WrappingCallerPrincipal wCallerPrincipal = (WrappingCallerPrincipal)sc.getCallerPrincipal();
            SecurityUser sUser = (SecurityUser)wCallerPrincipal.getWrapped();
            cust = sUser.getCustomer();
            if (cust != null && cust.getId() == id) {
                response = Response.status(OK).entity(cust).build();
            }
            else {
                throw new ForbiddenException();
            }
        }
        else {
            response = Response.status(BAD_REQUEST).build();
        }
        return response;
    }

    /**
     * 
     * @param id. Customer Id
     * @return Response. Result of customer deleting
     */
    @RolesAllowed({"ADMIN_ROLE"})
    @DELETE
    @Path("{id}")
    public Response deleteCustomerById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to delete specific customer " + id);
        Response response = null;
        Boolean result = true;

        result = customerServiceBean.deleteCustomerById(id);
        response = Response.status( result == false ? NOT_FOUND : OK).entity(result).build();
        return response;
    }


    /**
     * 
     * @param newCustomer
     * @return Response. Return saved new customer
     */
    @POST
    public Response addCustomer(CustomerPojo newCustomer) {
      Response response = null;
      CustomerPojo saveCustomerPojo = customerServiceBean.persistCustomer(newCustomer);
      response = Response.ok(newCustomer).build();
      return response;
    }

    /**
     * 
     * @param newCustomer
     * @return Response. Return saved new customer
     */
    @RolesAllowed({"ADMIN_ROLE"})
    @PUT
    public Response updateCustomer(CustomerPojo newCustomer) {
      Response response = null;
      CustomerPojo saveCustomerPojo = customerServiceBean.updateCustomer(newCustomer);
      response = Response.ok(newCustomer).build();
      return response;
    }

    /**
     * 
     * @param id. Customer id.
     * @param newAddress. New Address
     * @return Response. Return updated Customer
     */
    @RolesAllowed({"ADMIN_ROLE", "USER_ROLE"})
    @PUT
    @Path("/addAddressForCustomer/{id}")
    public Response addAddressForCustomer(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, AddressPojo newAddress) {
      Response response = null;
      CustomerPojo updatedCustomer = customerServiceBean.setAddressFor(id, newAddress);
      response = Response.ok(updatedCustomer).build();
      return response;
    }
}