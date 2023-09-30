package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.*;
import gr.codelearn.acme.javapathspringdelivery.repository.OrderRepository;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends BaseServiceImpl<Order> implements OrderService{
    private final OrderRepository orderRepository;
    private final UserServiceImpl userService;
    private final StoreService storeService;
    private final ProductService productService;
    @Override
    public JpaRepository<Order, Long> getRepository() {
        return orderRepository;
    }
    @TimeLimiter(name="basicTimeout")
    public CompletableFuture<List<Order>> getOrdersForUser(final String email){
        return CompletableFuture.supplyAsync(()->{
            var res = orderRepository.findOrdersByUserEmail(email);
            return res;
        });
    }

    @Override
    public List<Order> findAll(){
        return orderRepository.findAllFetching();
    }

    public Order initiateOrderForUser(Order order){
        User orderingUser = userService.getUserByEmail(order.getUser().getEmail());
        Store store = getStoreForOrder(order.getStore().getName());
        Order activeOrder = getActiveOrdersForUser(orderingUser.getEmail());
        activeOrder.setUser(orderingUser);
        activeOrder.setStore(storeService.addOrderToStore(storeService.create(store), activeOrder));
        Set<Product> products = new HashSet<>(getProductsForOrder(order.getOrderItems()));
        Set<OrderItem> orderItems = getOrderItems(products, activeOrder);
        var persistedOrder = create(activeOrder);
        persistedOrder.setOrderItems(orderItems);
        return create(persistedOrder);
    }

    public Order checkoutOrder(Order order){
        var orderToCheckout = getActiveOrdersForUser(order.getUser().getEmail());
        orderToCheckout.setOrderStatus(OrderStatus.COMPLETED);
        return create(orderToCheckout);
    }

    private Order cancelOrder(Order order){
        var orderToCancel = getActiveOrdersForUser(order.getUser().getEmail());
        orderToCancel.setOrderStatus(OrderStatus.CANCELED);
        return create(orderToCancel);
    }

    private Order initiateOrder(){
        return Order.builder().orderStatus(OrderStatus.ACTIVE)
                .store(Store.builder().name("").build()).orderingDate(new Date())
                .paymentMethod(PaymentMethod.CASH).build();
    }
    private Order getActiveOrdersForUser(String userEmail){
        return orderRepository.
                findOrdersByUserEmail(userEmail).stream().filter(order -> order.getOrderStatus() == OrderStatus.ACTIVE)
                .findFirst()
                .orElse(initiateOrder());
    }
    private List<Product> getProductsForOrder(Set<OrderItem> productNames){
        List<Product> producstForOrder = new ArrayList<>();
        for (var prod:productNames
             ) {
            producstForOrder.add(productService.getByName(prod.getProduct().getName()));
        }
        return producstForOrder;
    }
    private Set<OrderItem> getOrderItems(Set<Product> products, Order order){
        Set<OrderItem> orderItems = new HashSet<>();
        for (var product:products
             ) {
            orderItems.add(OrderItem.builder()
                    .product(product).amount(product.getPrice()).order(order).build());
        }
        return orderItems;
    }
    private Store getStoreForOrder(String storeName){
        try {
            return storeService.getStoreByName(storeName).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
