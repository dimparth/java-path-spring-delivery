package gr.codelearn.acme.javapathspringdelivery.controller;

import gr.codelearn.acme.javapathspringdelivery.domain.Order;
import gr.codelearn.acme.javapathspringdelivery.mapper.BaseMapper;
import gr.codelearn.acme.javapathspringdelivery.mapper.CustomFormMapper;
import gr.codelearn.acme.javapathspringdelivery.mapper.OrderMapper;
import gr.codelearn.acme.javapathspringdelivery.service.BaseService;
import gr.codelearn.acme.javapathspringdelivery.service.OrderService;
import gr.codelearn.acme.javapathspringdelivery.transfer.ApiResponse;
import gr.codelearn.acme.javapathspringdelivery.transfer.CheckoutOrderForm;
import gr.codelearn.acme.javapathspringdelivery.transfer.CreateOrderForm;
import gr.codelearn.acme.javapathspringdelivery.transfer.resource.OrderResource;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
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
    private final CustomFormMapper customFormMapper;
    @Override
    protected BaseService<Order, Long> getBaseService() {
        return orderService;
    }

    @Override
    protected BaseMapper<Order, OrderResource> getMapper() {
        return orderMapper;
    }

    @PostMapping("/initialize")
    @TimeLimiter(name = "basicTimeout")
    public CompletableFuture<ResponseEntity<ApiResponse<OrderResource>>> initializeOrder(@RequestBody CreateOrderForm createOrderForm){
        return CompletableFuture.supplyAsync(()->{
            return new ResponseEntity<>(
                    ApiResponse
                            .<OrderResource>builder()
                            .data(orderMapper.toResource(
                                    orderService.initiateOrderForUser(orderMapper.toDomain(customFormMapper.mapCreateOrderFormToOrderResource(createOrderForm))))).build(),
                    HttpStatus.CREATED);
        });

    }
    @PostMapping("/checkout")
    @TimeLimiter(name = "basicTimeout")
    public CompletableFuture<ResponseEntity<ApiResponse<OrderResource>>> checkoutOrder(@RequestBody CheckoutOrderForm checkoutOrderForm){
        return CompletableFuture.supplyAsync(()->{
            return new ResponseEntity<>(ApiResponse.<OrderResource>builder()
                    .data(orderMapper.toResource(
                            orderService.checkoutOrder(
                                    orderMapper.toDomain(
                                            customFormMapper.mapCheckoutOrderFormToOrderResource(checkoutOrderForm)
                                    )
                            )
                    )
                    ).build(),HttpStatus.ACCEPTED);
        });
    }
}
