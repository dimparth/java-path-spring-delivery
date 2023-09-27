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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Order(1)
@Profile("sample")
@RequiredArgsConstructor
public class SampleDataInitializer extends BaseComponent implements CommandLineRunner {
    private final UserService userService;
    private final StoreService storeService;
    private final ProductService productService;
    private final StoreCategoryService storeCategoryService;
    private final ProductCategoryService productCategoryService;
    @Override
    public void run(String... args) throws Exception {
        addSampleUsers();
        addSampleStores();
    }
    private void addSampleUsers(){
        var users = List.of(User.builder().firstname("Dimitris").lastname("Parthenis").age(28).email("dim@sample.com").phoneNumber("21012345").address("Address 81").build(),
                User.builder().firstname("Giorgos").lastname("George").age(25).email("giorgos@sample.com").phoneNumber("21101234").address("Address 28").build());
        users.forEach(user -> userService.create(user));
    }
    private void addSampleStores(){
        var burgerCategory = productCategoryService.create(ProductCategory.builder().productType(ProductType.BURGER).build());
        var souvlakiCategory = productCategoryService.create(ProductCategory.builder().productType(ProductType.SOUVLAKI).build());
        var storeCategoryFastFood = storeCategoryService.create(StoreCategory.builder().storeType(StoreType.FAST_FOOD).build());

        var products = productService.createAll(List.of(Product.builder().name("BigKahunaBurger").price(new BigDecimal(5)).description("This is a tasty burger").productCategory(burgerCategory).build(),
                Product.builder().name("RoyaleWithCheese").price(new BigDecimal(3)).description("Because of the metric system").productCategory(burgerCategory).build(),
                Product.builder().name("BigMac").price(new BigDecimal(6)).description("Le Big Mac").productCategory(burgerCategory).build()));

        var productsSouvlakia = productService.createAll(List.of(Product.builder().name("Skepasti").productCategory(souvlakiCategory).description("Skepasti me souvlaki xoirino").price(new BigDecimal(10)).build()));
        var prodSet1 = new HashSet<>(products);
        var prodSet2 = new HashSet<>(productsSouvlakia);
        var storeBurger = Store.builder().name("Goodik").address("Address 18").phoneNumber("21019083").storeCategory(storeCategoryFastFood).products(prodSet1).build();
        var storeSouvlaki = Store.builder().name("Souvlatzidiko").products(prodSet2).address("Address 30").phoneNumber("21089789").storeCategory(storeCategoryFastFood).build();

        storeService.create(storeBurger);
        storeService.create(storeSouvlaki);
    }
}
