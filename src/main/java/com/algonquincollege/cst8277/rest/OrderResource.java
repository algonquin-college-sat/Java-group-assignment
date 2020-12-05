/*****************************************************************c******************o*******v******id********
 * File: CustomerResource.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * update by : Hanna Bernyk 040904190
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

import java.util.List;

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

import com.algonquincollege.cst8277.ejb.CustomerService;
import com.algonquincollege.cst8277.models.OrderLinePojo;
import com.algonquincollege.cst8277.models.OrderPojo;
import com.algonquincollege.cst8277.models.ProductPojo;

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
        order = customerServiceBean.getOrderById(id);
        response = Response.status( order == null ? NOT_FOUND : OK).entity(order).build();
        return response;
    }

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

    
    @POST
    public Response addOrder(OrderPojo newOrder) {
      Response response = null;
      OrderPojo savedOrder = customerServiceBean.persistOrder(newOrder);
      response = Response.ok(savedOrder).build();
      return response;
    }

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

    @PUT
    @Path("/deleteOrderLineToOder/{orderLineNumber}/{orderId}")
    public Response addOrderLine(@PathParam(RESOURCE_PATH_ORDER_LINE_NUMBER_ELEMENT) int orderLineNumber,
        @PathParam(RESOURCE_PATH_ORDER_ID_ELEMENT) int orderId) {
      Response response = null;
      Boolean result = customerServiceBean.deleteOrderLine(orderLineNumber, orderId);
      response = Response.ok(result).build();
      return response;
    }

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
    
}