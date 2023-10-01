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
        var orderingUser = userService.getUserByEmail(order.getUser().getEmail());
        var activeOrder = getActiveOrdersForUser(orderingUser.getEmail());
        activeOrder.setUser(orderingUser);


        //activeOrder.setStore(storeService.addOrderToStore(storeService.create(store), activeOrder));
        Set<Product> products = new HashSet<>(getProductsForOrder(order.getOrderItems()));
        Set<OrderItem> orderItems = getOrderItems(products, activeOrder);
        var areyouastore = products.stream().findFirst().orElseThrow().getStore();
        String activeStoreName = areyouastore.getName();
        var activeOi = activeOrder.getOrderItems();
        if (activeOi != null){
            activeStoreName = activeOi.stream().findFirst().isPresent() ? activeOrder.getOrderItems().stream().findFirst().get().getProduct().getStore().getName() : "";
        }
        var persistedOrder = create(activeOrder);
        if(areyouastore.getName().equals(activeStoreName)){
            var store = storeService.addOrderToStore(areyouastore, persistedOrder);
            activeOrder.setOrderItems(orderItems);
            storeService.create(store);
            return create(persistedOrder);
        }else {
            delete(persistedOrder);
            var nOrder = initiateOrder();
            nOrder.setUser(orderingUser);
            var newPersistedOrder = create(nOrder);
            var store2 = storeService.addOrderToStore(areyouastore, newPersistedOrder);
            //storeService.delete(areyouastore);
            //store2.setId(null);
            storeService.create(store2);
            newPersistedOrder.setOrderItems(getOrderItems(products, newPersistedOrder));
            return create(newPersistedOrder);
            /*persistedOrder=null;
            //storeService.delete(areyouastore);
            logger.info("User {} had a non-completed order to store {}. Deleted the order and initialized a new one for store {}",orderingUser.getEmail(), activeStoreName, areyouastore.getName());
            persistedOrder= initiateOrder();
            persistedOrder.setUser(orderingUser);
            //newOrder.setOrderItems(getOrderItems(products, newOrder));
            var newPersistedOrder = create(persistedOrder);
            var store2 = storeService.addOrderToStore(areyouastore, newPersistedOrder);
            //storeService.delete(areyouastore);
            //store2.setId(null);
            storeService.create(store2);
            newPersistedOrder.setOrderItems(getOrderItems(products, persistedOrder));
            return create(newPersistedOrder);*/
        }
        /*var store = storeService.addOrderToStore(areyouastore, activeOrder);
        storeService.create(store);
        activeOrder.setOrderItems(orderItems);
        return create(activeOrder);*/
    }

    public Order checkoutOrder(Order order){
        var orderToCheckout = getActiveOrdersForUser(order.getUser().getEmail());
        orderToCheckout.setOrderStatus(OrderStatus.COMPLETED);
        return create(orderToCheckout);
    }

    private Order initiateOrder(){
        return Order.builder().orderStatus(OrderStatus.ACTIVE)
                .orderingDate(new Date())
                .paymentMethod(PaymentMethod.CASH).build();
    }

    private Order clearOrder(Order order){
        for (Iterator<OrderItem> iterator = order.getOrderItems().iterator(); iterator.hasNext();) {
            var s =  iterator.next();
                iterator.remove();
        }
        order.setOrderingDate(new Date());
        order.setOrderStatus(OrderStatus.ACTIVE);
        order.setUser(null);
        order.setPaymentMethod(PaymentMethod.CASH);
        return order;
    }

    private Order getActiveOrdersForUser(String userEmail){
        return  orderRepository.
                findOrdersByUserEmail(userEmail).stream().filter(order -> order.getOrderStatus() == OrderStatus.ACTIVE)
                .findFirst()
                .orElse(initiateOrder());
    }
    private List<Product> getProductsForOrder(Set<OrderItem> productNames){
        List<Product> producstForOrder = new ArrayList<>();
        for (var prod:productNames
             ) {
            var s = prod.getProduct().getName();
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
