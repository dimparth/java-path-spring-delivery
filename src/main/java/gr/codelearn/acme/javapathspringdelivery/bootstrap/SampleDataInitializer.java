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
import java.util.concurrent.ExecutionException;

@Component
@Order(1)
@Profile("sample")
@RequiredArgsConstructor
public class SampleDataInitializer extends BaseComponent implements CommandLineRunner {
    private final UserService userService;
    private final StoreService storeService;
    private final ProductCategoryService productCategoryService;
    private final OrderService orderService;
    private final ProductService productService;

    @Override
    public void run(String... args) throws ExecutionException, InterruptedException {
        generateSampleData();
    }

    private void generateSampleData() throws ExecutionException, InterruptedException {
        //user creation
        var users = buildSampleUsers();
        users = userService.createAll(users);
        logger.trace("Created users: {}", users);

        //category creation -- both for products and stores.
        var burgerCategory = productCategoryService.create(ProductCategory.builder().productType(ProductType.BURGER).build());
        var souvlakiCategory = productCategoryService.create(ProductCategory.builder().productType(ProductType.SOUVLAKI).build());
        var softDrinkCategory = productCategoryService.create(ProductCategory.builder().productType(ProductType.SOFT_DRINK).build());
        var alcoholCategory = productCategoryService.create(ProductCategory.builder().productType(ProductType.ALCOHOL).build());
        var dessertCategory = productCategoryService.create(ProductCategory.builder().productType(ProductType.DESSERT).build());
        var pastaCategory = productCategoryService.create(ProductCategory.builder().productType(ProductType.PASTA).build());
        var pizzaCategory = productCategoryService.create(ProductCategory.builder().productType(ProductType.PIZZA).build());
        var fastFoodCategory = StoreCategory.builder().storeType(StoreType.FAST_FOOD).build();
        var burgerHouseCategory = StoreCategory.builder().storeType(StoreType.BURGER_HOUSE).build();
        var italianStoreCategory = StoreCategory.builder().storeType(StoreType.ITALIAN).build();
        var traditionalStoreCategory = StoreCategory.builder().storeType(StoreType.TRADITIONAL).build();
        var patisserieStoreCategory = StoreCategory.builder().storeType(StoreType.PATISSERIE).build();
        logger.trace("Created all categories");


        var burgers = productService.createAll(buildBurgers(burgerCategory).stream().toList());
        var souvlakia = productService.createAll(buildSouvlakia(souvlakiCategory).stream().toList());
        var softDrinks = productService.createAll(buildSoftDrinks(softDrinkCategory).stream().toList());
        var alcoholDrinks = productService.createAll(buildAlcohol(alcoholCategory).stream().toList());
        var desserts = productService.createAll(buildDesserts(dessertCategory).stream().toList());
        var pasta = productService.createAll(buildPasta(pastaCategory).stream().toList());
        var morePasta = productService.createAll(buildMorePasta(pastaCategory).stream().toList());
        var pizza = productService.createAll(buildPizza(pizzaCategory).stream().toList());
        logger.trace("Created products for all categories");

        var burgerStoreProducts = new HashSet<>(burgers);


        var italianStoreProducts = new HashSet<>(pasta);
        italianStoreProducts.addAll(pizza);
        var moreItalianStoreProducts = new HashSet<>(morePasta);
        var fastFoodProducts = new HashSet<>(souvlakia);
        fastFoodProducts.addAll(softDrinks);

        var storeBurger = storeService.createNewStoreWithProducts(
                        Store.builder().name("Goodik").address("Address 18").phoneNumber("21019083").storeCategory(burgerHouseCategory).products(burgerStoreProducts).build())
                .get();
        logger.trace("Created store {}", storeBurger);

        var storeFastFood = storeService.createNewStoreWithProducts(
                Store.builder().name("Souvlatzidiko").products(fastFoodProducts).address("Address 30").phoneNumber("21089789").storeCategory(fastFoodCategory).build()
        );
        logger.trace("Created store {}", storeFastFood);

        var storeItalian = storeService.createNewStoreWithProducts(
                Store.builder().name("Joey's").products(italianStoreProducts).address("Address 89").phoneNumber("211389753").storeCategory(italianStoreCategory).build()
        );
        var anotherItalian = storeService.createNewStoreWithProducts(
                Store.builder().name("Vesuvio").products(moreItalianStoreProducts).address("Address 52").phoneNumber("2113895416").storeCategory(italianStoreCategory).build()
        );
        logger.trace("Created store {}", storeItalian);

        var storeDesserts = storeService.createNewStoreWithProducts(
                Store.builder().name("Patisserie").products(new HashSet<>(desserts)).address("Address 98").phoneNumber("211382734").storeCategory(patisserieStoreCategory).build()
        );
        logger.trace("Created store {}", storeDesserts);

        var ordersItalian = List.of(getOrderObject("dim@sample.com", "Joey's", List.of("Spaghetti Bolognese", "Spaghetti Bolognese"))
                //,
                //getOrderObject("nikos@sample.com", "Joey's", List.of("Spaghetti Napolitana")),
                //getOrderObject("giorgos@sample.com", "Joey's", List.of("Pizza di Pollo"))
        );
        ordersItalian.forEach(x -> {
                    logger.trace("Initiated order {}", x);
                    var o =orderService.initiateOrderForUser(x);
                    orderService.checkoutOrder(o);
                }
        );

        var moreOrdersItalian = List.of(getOrderObject("dim@sample.com", "Vesuvio", List.of("Lasagna"))
                ,
                getOrderObject("nikos@sample.com", "Vesuvio", List.of("Pasticcio")),
                getOrderObject("giorgos@sample.com", "Vesuvio", List.of("Pasticcio")),
                getOrderObject("vasilis@sample.com", "Vesuvio", List.of("Pasticcio"))
        );
        moreOrdersItalian.forEach(o->{
            logger.trace("Initiated order {}", o);
            var a =orderService.initiateOrderForUser(o);
            orderService.checkoutOrder(a);
        });

        var ordersBurger = List.of(getOrderObject("dim@sample.com", "Goodik", List.of("BigKahunaBurger", "RoyaleWithCheese"))
                ,
                getOrderObject("nikos@sample.com", "Goodik", List.of("BigMac")),
                getOrderObject("nikos@sample.com", "Goodik", List.of("BigMac"))
        );
        ordersBurger.forEach(order->{
            var o = orderService.initiateOrderForUser(order);
            orderService.checkoutOrder(o);
            logger.trace("Initiated order: {}", order);
        });

        var ordersSouvlakia = List.of(getOrderObject("dim@sample.com", "Souvlatzidiko", List.of("Skepasti")));
        ordersSouvlakia.forEach(or->{
            orderService.initiateOrderForUser(or);
            logger.trace("Initiated order: {}", or);
        });

        var ordersDessert = List.of(getOrderObject("dim@sample.com", "Patisserie", List.of("Tiramissou")));
        ordersDessert.forEach(or->{
            orderService.initiateOrderForUser(or);
            logger.trace("Initiated order: {}", or);
        });
    }

    private gr.codelearn.acme.javapathspringdelivery.domain.Order getOrderObject(String email, String storeName, List<String> products) {
        var orderItems = new HashSet<OrderItem>();
        for (var product : products
        ) {
            orderItems.add(OrderItem.builder().product(Product.builder().name(product).build()).build());
        }
        return gr.codelearn.acme.javapathspringdelivery.domain.Order.builder()
                .user(User.builder().email(email).build())
                .paymentMethod(PaymentMethod.CASH)
                .orderItems(orderItems)
                .build();
    }

    private List<User> buildSampleUsers() {
        return List.of(User.builder().firstname("Dimitris").lastname("Parthenis").age(28).email("dim@sample.com").phoneNumber("21012345").address("Address 81").build(),
                User.builder().firstname("Nikos").lastname("Nikolaou").age(25).email("nikos@sample.com").phoneNumber("21101234").address("Address 28").build(),
                User.builder().firstname("Vasilis").lastname("Vasiliou").age(30).email("vasilis@sample.com").phoneNumber("2113904").address("Address 31").build(),
                User.builder().firstname("Giorgos").lastname("Georgiou").age(30).email("giorgos@sample.com").phoneNumber("2113909").address("Address 33").build());
    }

    private Set<Product> buildBurgers(ProductCategory burgerCategory) {
        return Set.of(Product.builder().serialNo("SN-123").name("BigKahunaBurger").price(new BigDecimal(5)).description("This is a tasty burger").productCategory(burgerCategory).build(),
                Product.builder().serialNo("SN-1235").name("RoyaleWithCheese").price(new BigDecimal(3)).description("Because of the metric system").productCategory(burgerCategory).build(),
                Product.builder().serialNo("SN-1234").name("BigMac").price(new BigDecimal(6)).description("Le Big Mac").productCategory(burgerCategory).build());
    }

    private Set<Product> buildSouvlakia(ProductCategory souvlakiCategory) {
        var productsSouvlakia = Set.of(Product.builder().serialNo("SN-4238905673468").name("Skepasti").productCategory(souvlakiCategory).description("Skepasti me souvlaki xoirino")
                        .price(new BigDecimal(10)).build(),
                Product.builder().serialNo("SN-23857238956239").name("Souvlaki Xoirino").productCategory(souvlakiCategory).description("Paradosiako xoirino souvlaki")
                        .price(new BigDecimal(2)).build(),
                Product.builder().serialNo("SN-5806753467834578").name("Pita Gyros Xoirinos").productCategory(souvlakiCategory).description("Paradosiaki pita gyro xoirino")
                        .price(new BigDecimal(4)).build());
        return productsSouvlakia;
    }

    private Set<Product> buildSoftDrinks(ProductCategory softDrinkCategory) {
        var softDrinks = Set.of(Product.builder().serialNo("SN-34i0634580963478").name("Cola").productCategory(softDrinkCategory).description("our famous cola!")
                        .price(new BigDecimal(1)).build(),
                Product.builder().serialNo("SN-3456567457").name("Sprite").productCategory(softDrinkCategory).description("Tasty beverage to wash your food down!")
                        .price(new BigDecimal(2)).build(),
                Product.builder().serialNo("SN-2342354646").name("Sparkling Water").productCategory(softDrinkCategory).description("Plain sparkling water")
                        .price(new BigDecimal("0.5")).build());
        return softDrinks;
    }

    private Set<Product> buildAlcohol(ProductCategory alcoholCategory) {
        var alcohol = Set.of(Product.builder().serialNo("SN-547468685645").name("Beer").productCategory(alcoholCategory).description("Regular beer.")
                        .price(new BigDecimal(1)).build(),
                Product.builder().serialNo("SN-54745675373735").name("Wine").productCategory(alcoholCategory).description("Regular white wine")
                        .price(new BigDecimal(2)).build(),
                Product.builder().serialNo("SN-3757453634634567").name("Red Wine").productCategory(alcoholCategory).description("Our red wine")
                        .price(new BigDecimal("0.5")).build());
        return alcohol;
    }

    private Set<Product> buildDesserts(ProductCategory dessertCategory) {
        var desserts = Set.of(Product.builder().serialNo("SN-564856").name("Chocolate Cookie").productCategory(dessertCategory).description("Tasty chocolate cookies")
                        .price(new BigDecimal(5)).build(),
                Product.builder().serialNo("SN-1564564").name("BlackWhite Crepe").productCategory(dessertCategory).description("Crepe with black and white chocolate")
                        .price(new BigDecimal(12)).build(),
                Product.builder().serialNo("SN-124856").name("Tiramissou").productCategory(dessertCategory).description("Delicious tiramissou")
                        .price(new BigDecimal(10)).build());
        return desserts;
    }

    private Set<Product> buildPasta(ProductCategory pastaCategory) {
        var pasta = Set.of(Product.builder().serialNo("SN-5648748").name("Spaghetti Bolognese").productCategory(pastaCategory).description("Favourite dish of a famous Greek musician!")
                        .price(new BigDecimal(20)).build(),
                Product.builder().serialNo("SN-15645669").name("Spaghetti Carbonara").productCategory(pastaCategory).description("The traditional recipe with eggs and beef")
                        .price(new BigDecimal(15)).build(),
                Product.builder().serialNo("SN-1564896748").name("Spaghetti Napolitana").productCategory(pastaCategory).description("Delicious and light on calories!")
                        .price(new BigDecimal(10)).build());
        return pasta;
    }

    private Set<Product> buildMorePasta(ProductCategory pastaCategory){
        var morePasta = Set.of(Product.builder().serialNo("SN-235246745").name("Ravioli").productCategory(pastaCategory).description("Delicious ravioli.")
                        .price(new BigDecimal(20)).build(),
                Product.builder().serialNo("SN-125243578").name("Lasagna").productCategory(pastaCategory).description("Simply lovely")
                        .price(new BigDecimal(15)).build(),
                Product.builder().serialNo("SN-135467536").name("Pasticcio").productCategory(pastaCategory).description("Delicious!")
                        .price(new BigDecimal(10)).build());
        return morePasta;
    }

    private Set<Product> buildPizza(ProductCategory pizzaCategory) {
        var pizza = Set.of(Product.builder().serialNo("SN-15648964").name("Pizza di Pollo").productCategory(pizzaCategory).description("Pizza with parmesan cheese, tomato sauce and chicken")
                        .price(new BigDecimal(20)).build(),
                Product.builder().serialNo("SN-2352346346").name("Pizza BBQ").productCategory(pizzaCategory).description("Pizza with bbq sauce, gouda cheese and bacon")
                        .price(new BigDecimal(15)).build(),
                Product.builder().serialNo("SN-2353474567568").name("Pizza Margharita").productCategory(pizzaCategory).description("The simplest of pizzas, also light on calories")
                        .price(new BigDecimal(10)).build());
        return pizza;
    }
}
