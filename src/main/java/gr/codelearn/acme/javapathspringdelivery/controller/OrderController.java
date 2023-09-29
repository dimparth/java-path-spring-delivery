package gr.codelearn.acme.javapathspringdelivery.controller;

import gr.codelearn.acme.javapathspringdelivery.domain.Order;
import gr.codelearn.acme.javapathspringdelivery.mapper.BaseMapper;
import gr.codelearn.acme.javapathspringdelivery.mapper.OrderMapper;
import gr.codelearn.acme.javapathspringdelivery.service.BaseService;
import gr.codelearn.acme.javapathspringdelivery.service.OrderService;
import gr.codelearn.acme.javapathspringdelivery.transfer.ApiResponse;
import gr.codelearn.acme.javapathspringdelivery.transfer.CreateOrderForm;
import gr.codelearn.acme.javapathspringdelivery.transfer.resource.OrderResource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

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
    public CompletableFuture<ResponseEntity<ApiResponse<OrderResource>>> initializeOrder(@RequestBody CreateOrderForm createOrderForm){
        return CompletableFuture.supplyAsync(()-> new ResponseEntity<>(
                ApiResponse
                        .<OrderResource>builder()
                        .data(orderMapper.toResource(
                                orderService.initiateOrderForUser(createOrderForm.getUserEmail(), createOrderForm.getStoreName(), createOrderForm.getProducts()))).build(),
                HttpStatus.CREATED));
    }
}
