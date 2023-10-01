package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.Order;

public interface OrderService extends BaseService<Order,Long> {
    //Order initiateOrder(Order order);
    Order initiateOrderForUser(Order order);
    Order checkoutOrder(Order order);
}
