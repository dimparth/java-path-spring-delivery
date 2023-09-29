package gr.codelearn.acme.javapathspringdelivery.bootstrap;


import gr.codelearn.acme.javapathspringdelivery.base.BaseComponent;
import gr.codelearn.acme.javapathspringdelivery.domain.*;
import gr.codelearn.acme.javapathspringdelivery.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
@Order(1)
@Profile("sample")
@RequiredArgsConstructor
public class SampleDataInitializer extends BaseComponent implements CommandLineRunner {
    private final UserService userService;
    private final StoreService storeService;
    private final ProductCategoryService productCategoryService;
    private final OrderService orderService;
    @Override
    public void run(String... args) {
        addSampleUsers();
        addSampleStores();
    }
    private void addSampleUsers(){

    }
    private void addSampleStores(){
        var users = List.of(User.builder().firstname("Dimitris").lastname("Parthenis").age(28).email("dim@sample.com").phoneNumber("21012345").address("Address 81").build(),
            User.builder().firstname("Giorgos").lastname("George").age(25).email("giorgos@sample.com").phoneNumber("21101234").address("Address 28").build());
        users.forEach(user -> userService.create(user));
        var burgerCategory = productCategoryService.create(ProductCategory.builder().productType(ProductType.BURGER).build());
        var souvlakiCategory = productCategoryService.create(ProductCategory.builder().productType(ProductType.SOUVLAKI).build());
        var fastFoodCategory = StoreCategory.builder().storeType(StoreType.FAST_FOOD).build();

        var products = Set.of(Product.builder().name("BigKahunaBurger").price(new BigDecimal(5)).description("This is a tasty burger").productCategory(burgerCategory).build(),
                Product.builder().name("RoyaleWithCheese").price(new BigDecimal(3)).description("Because of the metric system").productCategory(burgerCategory).build(),
                Product.builder().name("BigMac").price(new BigDecimal(6)).description("Le Big Mac").productCategory(burgerCategory).build());

        var productsSouvlakia = Set.of(Product.builder().name("Skepasti").productCategory(souvlakiCategory).description("Skepasti me souvlaki xoirino")
                .price(new BigDecimal(10)).build());
        var storeBurger = Store.builder().name("Goodik").address("Address 18").phoneNumber("21019083").storeCategory(fastFoodCategory).products(products).build();
        var storeSouvlaki = Store.builder().name("Souvlatzidiko").products(productsSouvlakia).address("Address 30").phoneNumber("21089789").storeCategory(fastFoodCategory).build();

        storeBurger = storeService.createNewStoreWithProducts(storeBurger);
        storeService.createNewStoreWithProducts(storeSouvlaki);
        var foundStore = storeService.getStoreByName("Goodik");
        logger.trace("Store found by name: {}", foundStore);
        var foundStore2 = storeService.getStoreByCategory("FAST_FOOD");
        logger.trace("found stores by cat: {}", foundStore2);
        var usr = User.builder().email("dim@sample.com").build();
        usr.setId(1L);
        var order2 = orderService.initiateOrderForUser("dim@sample.com","Goodik", List.of("BigKahunaBurger","BigMac"));
        var checkedout = orderService.checkoutOrder(order2);
        logger.trace("checked out: {}", checkedout);
        logger.trace("order: {}", order2);
        var str = storeService.getPopularStores();
        logger.trace("poplar stores: {}", str);
        var popstr = storeService.getPopularStoresPerCategory2();
        logger.trace("stores per cat: {}", popstr);
        var allordes = orderService.findAll();
        logger.trace("orders: {}", allordes);
    }
}
