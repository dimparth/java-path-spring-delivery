package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.Order;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OrderService extends BaseService<Order,Long> {
    Order initiateOrderForUser(Order order);
    Order checkoutOrder(Order order);
    CompletableFuture<List<Order>> findOrdersByUserEmail(String userEmail);

}
