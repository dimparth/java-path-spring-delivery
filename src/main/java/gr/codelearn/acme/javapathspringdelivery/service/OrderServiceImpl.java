package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.*;
import gr.codelearn.acme.javapathspringdelivery.repository.OrderRepository;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends BaseServiceImpl<Order> implements OrderService {
    private final OrderRepository orderRepository;
    private final UserServiceImpl userService;
    private final StoreService storeService;
    private final ProductService productService;

    @Override
    public JpaRepository<Order, Long> getRepository() {
        return orderRepository;
    }

    @Override
    @TimeLimiter(name = "basicTimeout")
    public CompletableFuture<List<Order>> findAll() {
        return CompletableFuture.supplyAsync(() -> {
            logger.trace("Retrieving all Orders");
            return orderRepository.findAllFetching();
        });
    }

    @Override
    @TimeLimiter(name = "basicTimeout")
    public CompletableFuture<List<Order>> findOrdersByUserEmail(final String userEmail) {
        return CompletableFuture.supplyAsync(()-> {
            logger.trace("Fetching all orders for user {}", userEmail);
            return orderRepository.findOrdersByUserEmail(userEmail);
        });
    }

    public Order checkoutOrder(Order order) {
        var orderToCheckout = getActiveOrdersForUser(order.getUser().getEmail());
        if (orderToCheckout.isEmpty()) {
            throw new RuntimeException();
        }
        logger.trace("Checking out order with id: {} for user: {}", orderToCheckout.get().getId(), orderToCheckout.get().getUser().getEmail());
        orderToCheckout.get().setOrderStatus(OrderStatus.COMPLETED);
        return create(orderToCheckout.get());
    }



    public Order initiateOrderForUser(Order order) {
        logger.trace("Initializing order for user {}, with {} products and payment method {}", order.getUser().getEmail(), order.getOrderItems().size(), order.getPaymentMethod());
        var orderingUser = userService.getUserByEmail(order.getUser().getEmail());
        var activeOrder = getActiveOrdersForUser(orderingUser.getEmail());
        activeOrder.ifPresent(this::delete);
        var presentActiveOrder = activeOrder.orElse(initiateOrder(orderingUser.getEmail(), order.getPaymentMethod()));
        var instances = countInstancesOfProducts(order.getOrderItems());
        Set<Product> products = new HashSet<>(getProductsForOrder(instances));
        var storeOfProductsRequestedByUser = products.stream().findFirst().orElseThrow().getStore();
        var persistedOrder = create(initiateOrder(orderingUser.getEmail(), order.getPaymentMethod()));
        if (!storeOfProductsRequestedByUser.getName().equals(getActiveStoreName(presentActiveOrder, storeOfProductsRequestedByUser))) {
            return orderForChangedStore(order, orderingUser, instances, products, storeOfProductsRequestedByUser, persistedOrder);
        }
        var store = storeService.addOrderToStore(storeOfProductsRequestedByUser, persistedOrder);
        persistedOrder.setOrderItems(getOrderItems(products, persistedOrder, instances, presentActiveOrder));
        storeService.create(store);
        return create(persistedOrder);
    }

    private Order orderForChangedStore(Order order, User orderingUser, List<InstanceCounter> instances, Set<Product> products, Store storeOfProductsRequestedByUser, Order persistedOrder) {
        logger.trace("User {} changed store, will clear the order first", orderingUser.getEmail());
        delete(persistedOrder);
        var newPersistedOrder = create(initiateOrder(order.getUser().getEmail(), order.getPaymentMethod()));
        var store2 = storeService.addOrderToStore(storeOfProductsRequestedByUser, newPersistedOrder);
        storeService.create(store2);
        newPersistedOrder.setOrderItems(getOrderItems(products, newPersistedOrder, instances));
        return create(newPersistedOrder);
    }

    private String getActiveStoreName(Order presentActiveOrder, Store storeOfProductsRequestedByUser) {
        String activeStoreName = storeOfProductsRequestedByUser.getName();
        var activeOi = presentActiveOrder.getOrderItems();
        if (activeOi != null) {
            activeStoreName = activeOi.stream().findFirst().isPresent() ? presentActiveOrder.getOrderItems().stream().findFirst().get().getProduct().getStore().getName() : "";
        }
        return activeStoreName;
    }

    private Order initiateOrder(String userEmail, PaymentMethod paymentMethod) {
        return Order.builder().orderStatus(OrderStatus.ACTIVE)
                .orderingDate(new Date())
                .paymentMethod(paymentMethod)
                .user(userService.getUserByEmail(userEmail))
                .build();
    }

    private Optional<Order> getActiveOrdersForUser(String userEmail) {
        return orderRepository.
                findOrdersByUserEmail(userEmail).stream().filter(order -> order.getOrderStatus() == OrderStatus.ACTIVE)
                .findFirst();
    }

    private List<Product> getProductsForOrder(List<InstanceCounter> productNames) {
        var producstForOrder = new ArrayList<Product>();
        for (var prod : productNames
        ) {
            producstForOrder.add(productService.getByName(prod.getName()));
        }
        return producstForOrder;
    }

    private Set<OrderItem> getOrderItems(Set<Product> products, Order order, List<InstanceCounter> productInstances, Order oldOrder) {
        var existingOrderItems = oldOrder.getOrderItems() != null ? oldOrder.getOrderItems() : new HashSet<OrderItem>();
        existingOrderItems.forEach(x -> {
            x.setOrder(order);
            x.setId(null);
        });
        var orderItems = new HashSet<>(existingOrderItems);
        for (var product : products) {
            processOrderItems(order, productInstances, existingOrderItems, orderItems, product);
        }
        return orderItems;
    }

    private Set<OrderItem> getOrderItems(Set<Product> products, Order order, List<InstanceCounter> productInstances) {
        var existingOrderItems = order.getOrderItems() != null ? order.getOrderItems() : new HashSet<OrderItem>();
        var orderItems = new HashSet<>(existingOrderItems);
        for (var product : products) {
            processOrderItems(order, productInstances, existingOrderItems, orderItems, product);
        }
        return orderItems;
    }

    private void processOrderItems(Order order, List<InstanceCounter> productInstances, Set<OrderItem> existingOrderItems, HashSet<OrderItem> orderItems, Product product) {
        var oi = OrderItem.builder().product(product).order(order).build();
        var instance = productInstances.stream().filter(x -> Objects.equals(x.getName(), product.getName())).findFirst().orElse(new InstanceCounter(product.getName(), 1));
        var foundInExisting = existingOrderItems.stream().filter(x -> x.getProduct().getName().equals(oi.getProduct().getName())).findFirst().orElse(OrderItem.builder().quantity(0).build());
        var updatedQuantity = foundInExisting.getQuantity() + instance.getCount();
        oi.setQuantity(updatedQuantity);
        oi.setAmount(product.getPrice().multiply(BigDecimal.valueOf(updatedQuantity)));
        orderItems.remove(foundInExisting);
        orderItems.add(oi);
    }

    private List<InstanceCounter> countInstancesOfProducts(Set<OrderItem> orderItems) {
        var countMap = orderItems.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getProduct().getName(),
                        Collectors.counting()
                ));
        return countMap.entrySet().stream()
                .map(entry -> new InstanceCounter(entry.getKey(), entry.getValue().intValue()))
                .collect(Collectors.toList());
    }
}
