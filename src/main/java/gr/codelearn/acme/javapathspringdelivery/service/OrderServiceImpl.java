package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.*;
import gr.codelearn.acme.javapathspringdelivery.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    public List<Order> getOrdersForUser(String email){
        return orderRepository.findOrdersByUserEmail(email);
    }

    @Override
    public List<Order> findAll(){
        var orders = orderRepository.findAllFetching();
        return orders;
    }
    public Order initiateOrderForUser(String userEmail, String storeName, List<String> productNames){
        var orderingUser = userService.getUserByEmail(userEmail);
        var store = storeService.getStoreByName(storeName);
        var newOrder = getActiveOrdersForUser(orderingUser.getEmail()).isPresent() ? getActiveOrdersForUser(orderingUser.getEmail()).get() : initiateOrder();
        newOrder.setUser(orderingUser);
        var products = new HashSet<>(getProductsForOrder(productNames));
        newOrder = create(newOrder);
        store = storeService.create(store);
        newOrder.setStore(storeService.addOrderToStore(store, newOrder));
        newOrder.setOrderItems(getOrderItems(products,newOrder));
        newOrder = create(newOrder);
        return newOrder;
    }

    public Order checkoutOrder(Order order){
        var orderToCheckout = getActiveOrdersForUser(order.getUser().getEmail()).isPresent()? getActiveOrdersForUser(order.getUser().getEmail()).get() : initiateOrderForUser(order.getUser().getEmail(), order.getStore().getName()
        , order.getOrderItems().stream().map(x->x.getProduct().getName()).collect(Collectors.toList()));
        orderToCheckout.setOrderStatus(OrderStatus.COMPLETED);
        return create(orderToCheckout);
    }

    private Order cancelOrder(Order order){
        var orderToCancel = getActiveOrdersForUser(order.getUser().getEmail()).isPresent()? getActiveOrdersForUser(order.getUser().getEmail()).get() : initiateOrderForUser(order.getUser().getEmail(), order.getStore().getName()
                , order.getOrderItems().stream().map(x->x.getProduct().getName()).collect(Collectors.toList()));
        orderToCancel.setOrderStatus(OrderStatus.CANCELED);
        return create(orderToCancel);
    }

    private Order initiateOrder(){
        return Order.builder().orderStatus(OrderStatus.ACTIVE).store(Store.builder().name("").build()).orderingDate(new Date()).paymentMethod(PaymentMethod.CASH).build();
    }
    private Optional<Order> getActiveOrdersForUser(String userEmail){
        return orderRepository.findOrdersByUserEmail(userEmail).stream().filter(order -> order.getOrderStatus() == OrderStatus.ACTIVE).findFirst();
    }
    private List<Product> getProductsForOrder(List<String> productNames){
        List<Product> producstForOrder = new ArrayList<>();
        for (var prod:productNames
             ) {
            producstForOrder.add(productService.getByName(prod));
        }
        return producstForOrder;
    }
    private Set<OrderItem> getOrderItems(HashSet<Product> products, Order order){
        Set<OrderItem> orderItems = new HashSet<>();
        for (var product:products
             ) {
            orderItems.add(OrderItem.builder()
                    .product(product).amount(product.getPrice()).order(order).build());
        }
        return orderItems;
    }
}
