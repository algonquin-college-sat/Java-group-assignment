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
import static com.algonquincollege.cst8277.utils.MyConstants.ORDER_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_PRODUCT_ID_ELEMENT;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ORDER_LINE_NUMBER_ELEMENT;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ORDER_LINE_AMOUNT_ELEMENT;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_STORE_ID_ELEMENT;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ORDER_ID_ELEMENT;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_AMOUNT_ELEMENT;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;

import java.security.Principal;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
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

import org.glassfish.soteria.WrappingCallerPrincipal;

import com.algonquincollege.cst8277.ejb.CustomerService;
import com.algonquincollege.cst8277.models.CustomerPojo;
import com.algonquincollege.cst8277.models.OrderLinePojo;
import com.algonquincollege.cst8277.models.OrderPojo;
import com.algonquincollege.cst8277.models.ProductPojo;
import com.algonquincollege.cst8277.models.SecurityUser;

@Path(ORDER_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {

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
     * 
     * Get All Orders
     * 
     * @return Response. Return All Orders
     */
    @RolesAllowed({"ADMIN_ROLE", "USER_ROLE"})
    @GET
    public Response getOrders() {
        servletContext.log("retrieving all orders ...");
        List<OrderPojo> orders = customerServiceBean.getAllOrders();
        orders.forEach(s->{
            s.setOrderlines(null);
        });
        Response response = Response.ok(orders).build();
        return response;
    }

    /**
     * Get Order By Id
     * 
     * @param id. Order id
     * @return Response. Returns found order by Id
     */
    @RolesAllowed({"ADMIN_ROLE", "USER_ROLE"})
    @GET
    @Path("{id}")
    public Response getOrderById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to retrieve specific order " + id);
        Response response = null;
        OrderPojo order = null;
        order = customerServiceBean.getOrderById(id);
        order.getOrderlines().forEach(s-> {
            s.setOwningOrder(null);
        });
        response = Response.status( order == null ? NOT_FOUND : OK).entity(order).build();
        return response;
    }

    /**
     * Delete Order by Id
     * 
     * @param id. Order Id
     * @return Response. Returns result of order deleting(true - SUCCESS, false - FAILED)
     */
    @RolesAllowed({"ADMIN_ROLE"})
    @DELETE
    @Path("{id}")
    public Response deleteOrderById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to delete specific order " + id);
        Response response = null;
        Boolean result = true;

        result = customerServiceBean.deleteOrderById(id);
        response = Response.status( result == false ? NOT_FOUND : OK).entity(result).build();
        return response;
    }

    
    /**
     * 
     * Add new Order
     * 
     * @param newOrder. Order for saving
     * @return Response. Returns saved Order
     */
    @RolesAllowed({"ADMIN_ROLE", "USER_ROLE"})
    @POST
    public Response addOrder(OrderPojo newOrder) {
      Response response = null;
      WrappingCallerPrincipal wCallerPrincipal = (WrappingCallerPrincipal)sc.getCallerPrincipal();
      SecurityUser sUser = (SecurityUser)wCallerPrincipal.getWrapped();
      if(sUser != null) {
          CustomerPojo cust = sUser.getCustomer();
          if(cust != null) {
              newOrder.setOwningCustomer(cust);
          }
      }
      OrderPojo savedOrder = customerServiceBean.persistOrder(newOrder);
      response = Response.ok(savedOrder).build();
      return response;
    }

    /**
     * Add new Order Line
     * 
     * @param productId. Product Id for adding to an order Line.
     * @param orderId. Order Id.
     * @param amount. Amount
     * @return Response. Returns new created order line
     */
    @RolesAllowed({"ADMIN_ROLE", "USER_ROLE"})
    @PUT
    @Path("/addOrderLineToOder/{productId}/{orderId}/{amount}")
    public Response addOrderLine(@PathParam(RESOURCE_PATH_PRODUCT_ID_ELEMENT) int productId,
        @PathParam(RESOURCE_PATH_ORDER_ID_ELEMENT) int orderId,
        @PathParam(RESOURCE_PATH_AMOUNT_ELEMENT) double amount) {
      Response response = null;
      OrderLinePojo savedOrderLine = customerServiceBean.persistOrderLine(productId, orderId, amount);
      if(savedOrderLine != null) {
          savedOrderLine.setOwningOrder(null);
      }
      response = Response.ok(savedOrderLine).build();
      return response;
    }

    /**
     * Delete Order Line
     * 
     * @param orderLineNumber
     * @param orderId
     * @return Response. Result of deleting(true - SUCCESS, false - FAILED)
     */
    @RolesAllowed({"ADMIN_ROLE", "USER_ROLE"})
    @DELETE
    @Path("/deleteOrderLineToOder/{orderLineNumber}/{orderId}")
    public Response deleteOrderLine(@PathParam(RESOURCE_PATH_ORDER_LINE_NUMBER_ELEMENT) int orderLineNumber,
        @PathParam(RESOURCE_PATH_ORDER_ID_ELEMENT) int orderId) {
      Response response = null;
      Boolean result = customerServiceBean.deleteOrderLine(orderLineNumber, orderId);
      response = Response.ok(result).build();
      return response;
    }


    /**
     * 
     * @param orderLineNumber. Order Line number
     * @param orderId. Order Id
     * @param amount
     * @return Response. Returns updated Order Line
     */
    @RolesAllowed({"ADMIN_ROLE", "USER_ROLE"})
    @PUT
    @Path("/updateOrderLineToOder/{orderLineNumber}/{orderId}/{amount}")
    public Response updateOrderLine(@PathParam(RESOURCE_PATH_ORDER_LINE_NUMBER_ELEMENT) int orderLineNumber,
        @PathParam(RESOURCE_PATH_ORDER_ID_ELEMENT) int orderId, @PathParam(RESOURCE_PATH_ORDER_LINE_AMOUNT_ELEMENT) double amount) {
      Response response = null;
      OrderLinePojo result = customerServiceBean.updateOrderLine(orderLineNumber, orderId, amount);
      if(result != null) {
          result.setOwningOrder(null);
      }
      response = Response.ok(result).build();
      return response;
    }

    
    /**
     * Get all Order Lines by Order Id
     * 
     * @param orderId. Order Id
     * @return Response. Returns all order lines for order Id
     */
    @RolesAllowed({"ADMIN_ROLE", "USER_ROLE"})
    @GET
    @Path("/orderLines/{orderId}")
    public Response getAllOrderLines(@PathParam(RESOURCE_PATH_ORDER_ID_ELEMENT) int orderId) {
      Response response = null;
      List<OrderLinePojo> orderLines = customerServiceBean.getAllOrderLines(orderId);
      orderLines.forEach(
          s-> {
              s.setOwningOrder(null);
          });
      response = Response.ok(orderLines).build();
      return response;
    }

    /**
     * 
     * Get order line by Order Id and Order Line Number
     * 
     * @param orderId. Order Id
     * @param orderLineNumber. Order Line Number
     * @return Response. Returns found Order Line
     */
    @RolesAllowed({"ADMIN_ROLE", "USER_ROLE"})
    @GET
    @Path("/orderLine/{orderId}/{orderLineNumber}")
    public Response getOrderLine(@PathParam(RESOURCE_PATH_ORDER_ID_ELEMENT) int orderId,
        @PathParam(RESOURCE_PATH_ORDER_LINE_NUMBER_ELEMENT) int orderLineNumber) {
      Response response = null;
      OrderLinePojo orderLine = customerServiceBean.getOrderLineByOrderAndNumber(orderId, orderLineNumber);
      if(orderLine != null) {
          orderLine.setOwningOrder(null);
      }
      response = Response.ok(orderLine).build();
      return response;
    }

}