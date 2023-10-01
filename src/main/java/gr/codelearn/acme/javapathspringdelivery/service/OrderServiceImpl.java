package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.*;
import gr.codelearn.acme.javapathspringdelivery.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
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

    @Override
    public CompletableFuture<List<Order>> findAll(){
        return CompletableFuture.supplyAsync(()->{
            logger.trace("Retrieving all Orders");
            return orderRepository.findAllFetching();
        });
    }

    public Order initiateOrderForUser(Order order){
        logger.trace("Initializing order for user {}, with {} products and payment method {}", order.getUser().getEmail(), order.getOrderItems().size(), order.getPaymentMethod());
        var orderingUser = userService.getUserByEmail(order.getUser().getEmail());
        var activeOrder = getActiveOrdersForUser(orderingUser.getEmail());
        activeOrder.ifPresent(this::delete);
        var presentActiveOrder = activeOrder.orElse(initiateOrder(orderingUser.getEmail(), order.getPaymentMethod()));
        var newOrder = initiateOrder(orderingUser.getEmail(), order.getPaymentMethod());
        newOrder.setUser(orderingUser);
        var instances = countInstancesOfProducts(order.getOrderItems());
        Set<Product> products = new HashSet<>(getProductsForOrder(instances));
        var areyouastore = products.stream().findFirst().orElseThrow().getStore();
        String activeStoreName = areyouastore.getName();
        var activeOi = presentActiveOrder.getOrderItems();
        if (activeOi != null){
            activeStoreName = activeOi.stream().findFirst().isPresent() ? presentActiveOrder.getOrderItems().stream().findFirst().get().getProduct().getStore().getName() : "";
        }
        var persistedOrder = create(newOrder);
        if(areyouastore.getName().equals(activeStoreName)){
            var store = storeService.addOrderToStore(areyouastore, persistedOrder);
            persistedOrder.setOrderItems(getOrderItems(products, persistedOrder, instances, presentActiveOrder));
            storeService.create(store);
            return create(persistedOrder);
        }else {
            delete(persistedOrder);
            var nOrder = initiateOrder(order.getUser().getEmail(), order.getPaymentMethod());
            nOrder.setUser(orderingUser);
            var newPersistedOrder = create(nOrder);
            var store2 = storeService.addOrderToStore(areyouastore, newPersistedOrder);
            storeService.create(store2);
            newPersistedOrder.setOrderItems(getOrderItems(products,newPersistedOrder,instances));
            return create(newPersistedOrder);
        }
    }

    public Order checkoutOrder(Order order){
        var orderToCheckout = getActiveOrdersForUser(order.getUser().getEmail());
        logger.trace("Checking out order with id: {} for user: {}", orderToCheckout);
        if (orderToCheckout.isEmpty()){
            throw new RuntimeException();
        }
        logger.trace("Checking out order with id: {} for user: {}", orderToCheckout.get().getId(), orderToCheckout.get().getUser().getEmail());
        orderToCheckout.get().setOrderStatus(OrderStatus.COMPLETED);
        return create(orderToCheckout.get());
    }

    private Order initiateOrder(String userEmail, PaymentMethod paymentMethod){
        return Order.builder().orderStatus(OrderStatus.ACTIVE)
                .orderingDate(new Date())
                .paymentMethod(paymentMethod)
                .user(userService.getUserByEmail(userEmail))
                .build();
    }

    private Optional<Order> getActiveOrdersForUser(String userEmail){
        return orderRepository.
                findOrdersByUserEmail(userEmail).stream().filter(order -> order.getOrderStatus() == OrderStatus.ACTIVE)
                .findFirst();
    }

    private List<Product> getProductsForOrder(List<InstanceCounter> productNames){
        List<Product> producstForOrder = new ArrayList<>();
        for (var prod:productNames
             ) {
            producstForOrder.add(productService.getByName(prod.getName()));
        }
        return producstForOrder;
    }

    private Set<OrderItem> getOrderItems(Set<Product> products, Order order, List<InstanceCounter> productInstances, Order oldOrder){
        var existingOrderItems = oldOrder.getOrderItems() != null ? oldOrder.getOrderItems() : new HashSet<OrderItem>();
        existingOrderItems.forEach(x->{
            x.setOrder(order);
            x.setId(null);
        });
        Set<OrderItem> orderItems = new HashSet<>(existingOrderItems);
        for (var product:products
             ) {
            var oi = OrderItem.builder()
                .product(product).order(order).build();
            InstanceCounter instance = productInstances.stream().filter(x -> Objects.equals(x.getName(), product.getName())).findFirst().orElse(new InstanceCounter(product.getName(),1));
            var foundInExisting = existingOrderItems.stream().filter(x->x.getProduct().getName().equals(oi.getProduct().getName())).findFirst().orElse(OrderItem.builder().quantity(0).build());
            var updatedQuantity = foundInExisting.getQuantity() + instance.getCount();
            oi.setQuantity(updatedQuantity);
            oi.setAmount(product.getPrice().multiply(BigDecimal.valueOf(updatedQuantity)));
            orderItems.remove(foundInExisting);
            orderItems.add(oi);
        }
        return orderItems;
    }

    private Set<OrderItem> getOrderItems(Set<Product> products, Order order, List<InstanceCounter> productInstances){
        var existingOrderItems = order.getOrderItems() != null ? order.getOrderItems() : new HashSet<OrderItem>();
        Set<OrderItem> orderItems = new HashSet<>(existingOrderItems);
        for (var product:products
        ) {
            var oi = OrderItem.builder()
                    .product(product).order(order).build();
            InstanceCounter instance = productInstances.stream().filter(x -> Objects.equals(x.getName(), product.getName())).findFirst().orElse(new InstanceCounter(product.getName(),1));
            var foundInExisting = existingOrderItems.stream().filter(x->x.getProduct().getName().equals(oi.getProduct().getName())).findFirst().orElse(OrderItem.builder().quantity(0).build());
            var updatedQuantity = foundInExisting.getQuantity() + instance.getCount();
            oi.setQuantity(updatedQuantity);
            oi.setAmount(product.getPrice().multiply(BigDecimal.valueOf(updatedQuantity)));
            removeOrderItem(order, foundInExisting);
            orderItems.remove(foundInExisting);
            orderItems.add(oi);
        }
        return orderItems;
    }

    private void removeOrderItem(Order order, OrderItem orderItem){
        if (order.getOrderItems()!=null){
            order.getOrderItems().remove(orderItem);
        }
    }
    private List<InstanceCounter> countInstancesOfProducts(Set<OrderItem> orderItems){
        Map<String, Long> countMap = orderItems.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getProduct().getName(),
                        Collectors.counting()
                ));
        return countMap.entrySet().stream()
                .map(entry -> new InstanceCounter(entry.getKey(), entry.getValue().intValue()))
                .collect(Collectors.toList());
    }
}
