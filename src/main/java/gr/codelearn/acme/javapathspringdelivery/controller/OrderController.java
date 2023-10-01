package gr.codelearn.acme.javapathspringdelivery.controller;

import gr.codelearn.acme.javapathspringdelivery.domain.Order;
import gr.codelearn.acme.javapathspringdelivery.domain.ProductCategory;
import gr.codelearn.acme.javapathspringdelivery.mapper.BaseMapper;
import gr.codelearn.acme.javapathspringdelivery.mapper.OrderMapper;
import gr.codelearn.acme.javapathspringdelivery.service.BaseService;
import gr.codelearn.acme.javapathspringdelivery.service.OrderService;
import gr.codelearn.acme.javapathspringdelivery.transfer.ApiResponse;
import gr.codelearn.acme.javapathspringdelivery.transfer.CreateOrderForm;
import gr.codelearn.acme.javapathspringdelivery.transfer.resource.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController extends BaseController<Order, OrderResource>{
    private final OrderService orderService;
    private final OrderMapper orderMapper;
    @Override
    protected BaseService<Order, Long> getBaseService() {
        return orderService;
    }

    @Override
    protected BaseMapper<Order, OrderResource> getMapper() {
        return orderMapper;
    }


    @PostMapping("/initialize")
    public ResponseEntity<ApiResponse<OrderResource>> initializeOrder(@RequestBody CreateOrderForm createOrderForm){
            return new ResponseEntity<>(
                    ApiResponse
                            .<OrderResource>builder()
                            .data(orderMapper.toResource(
                                    orderService.initiateOrderForUser(orderMapper.toDomain(orderFormToResource(createOrderForm))))).build(),
                    HttpStatus.CREATED);
    }
    private OrderResource orderFormToResource(CreateOrderForm createOrderForm){
        var orderItems = new HashSet<OrderItemResource>();
        for (var product:createOrderForm.getProducts()
             ) {
            var pr = new ProductResource();
            pr.setName(product);
            var oir = new OrderItemResource();
            oir.setProduct(pr);
            orderItems.add(oir);
        }
        var ur = new UserResource();
        ur.setEmail(createOrderForm.getUserEmail());
        var or = new OrderResource();
        or.setUser(ur);
        //or.setStore(createOrderForm.getStoreName());
        or.setOrderItems(orderItems);

        return or;
    }
}
