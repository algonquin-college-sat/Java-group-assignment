/*****************************************************************c******************o*******v******id********
 * File: CustomerResource.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * update by : I. Am. A. Student 040nnnnnnn
 *
 */
package com.algonquincollege.cst8277.rest;

import static com.algonquincollege.cst8277.utils.MyConstants.ADMIN_ROLE;
import static com.algonquincollege.cst8277.utils.MyConstants.ORDER_RESOURCE_NAME;
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
import com.algonquincollege.cst8277.models.OrderPojo;
import com.algonquincollege.cst8277.models.SecurityUser;

@Path(ORDER_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {

    @EJB
    protected CustomerService customerServiceBean;

    @Inject
    protected ServletContext servletContext;

    @Inject
    protected SecurityContext sc;

    @GET
    public Response getOrders() {
        servletContext.log("retrieving all orders ...");
        List<OrderPojo> orders = customerServiceBean.getAllOrders();
        Response response = Response.ok(orders).build();
        return response;
    }

    @GET
    @Path("{id}")
    public Response getOrderById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to retrieve specific order " + id);
        Response response = null;
        OrderPojo order = null;

        if (sc.isCallerInRole(ADMIN_ROLE)) {
            order = customerServiceBean.getOrderById(id);
            response = Response.status( order == null ? NOT_FOUND : OK).entity(order).build();
        }else {
            response = Response.status(BAD_REQUEST).build();
        }
        return response;
    }

    @DELETE
    @Path("{id}")
    public Response deleteOrderById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to delete specific order " + id);
        Response response = null;
        Boolean result = true;

        result = customerServiceBean.deleteCustomerById(id);
        response = Response.status( result == false ? NOT_FOUND : OK).entity(result).build();
        /*
         * if (sc.isCallerInRole(ADMIN_ROLE)) {
         * result = customerServiceBean.deleteCustomerById(id);
         * response = Response.status( result == false ? NOT_FOUND : OK).entity(result).build();
         * }else {
         * response = Response.status(BAD_REQUEST).build();
         * }
         */        return response;
    }

    
    @POST
    public Response addOrder(CustomerPojo newCustomer) {
      Response response = null;
      CustomerPojo saveCustomerPojo = customerServiceBean.persistCustomer(newCustomer);
      //Create security user
      customerServiceBean.buildUserForNewCustomer(saveCustomerPojo);
      response = Response.ok(saveCustomerPojo).build();
      return response;
    }

    @Consumes("application/xml")
    public Response addAddressForCustomer(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, AddressPojo newAddress) {
      Response response = null;
      CustomerPojo updatedCustomer = customerServiceBean.setAddressFor(id, newAddress);
      response = Response.ok(updatedCustomer).build();
      return response;
    }
    
    //TODO - endpoints for setting up Orders/OrderLines

}