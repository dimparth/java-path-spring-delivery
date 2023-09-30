package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.Order;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface OrderService extends BaseService<Order,Long> {
    //Order initiateOrder(Order order);
    Order initiateOrderForUser(Order order);
    CompletableFuture<List<Order>> getOrdersForUser(String email);
    Order checkoutOrder(Order order);
}
