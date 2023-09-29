package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.Order;
import gr.codelearn.acme.javapathspringdelivery.domain.User;

import java.util.List;

public interface OrderService extends BaseService<Order,Long> {
    //Order initiateOrder(Order order);
    Order initiateOrderForUser(String userEmail, String storeName, List<String> productNames);
    List<Order> getOrdersForUser(String email);
    Order checkoutOrder(Order order);
}
