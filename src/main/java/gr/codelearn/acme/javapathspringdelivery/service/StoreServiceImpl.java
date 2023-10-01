package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.*;
import gr.codelearn.acme.javapathspringdelivery.repository.StoreRepository;
import gr.codelearn.acme.javapathspringdelivery.transfer.PopularStoreDto;
import gr.codelearn.acme.javapathspringdelivery.transfer.PopularStoresPerCategoryDto;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl extends BaseServiceImpl<Store> implements StoreService {
    private final StoreRepository storeRepository;
    private final StoreCategoryService storeCategoryService;
    private final ProductService productService;
    @Override
    public JpaRepository<Store,Long> getRepository() {
        return storeRepository;
    }

    @Override
    public List<Store> findAll(){
        var res = storeRepository.findAllFetching();
        return res;
    }

    //@TimeLimiter(name = "basicTimeout")
    public CompletableFuture<List<PopularStoreDto>> getPopularStores() {
        return CompletableFuture.supplyAsync(()->{
            var res = storeRepository.findMostFamousStores();
            return res;
        });
    }

    @Override
    @TimeLimiter(name = "basicTimeout")
    public CompletableFuture<List<PopularStoresPerCategoryDto>> getPopularStoresPerCategory(){
        return CompletableFuture.supplyAsync(()->{
            var res = storeRepository.findPopularStoresPerCategory();
            return res;
        });
    }


    /*public CompletableFuture<Store> createStore2(String storeName, String storeCategory, List<String> products){
        var newStore = initializeStore(storeName, storeCategory, products);
    }*/
    @Override
    public CompletableFuture<Store> createNewStoreWithProducts(final Store store) {
        return CompletableFuture.supplyAsync(()->{
            var initializedStore = initializeStore(store);


            store.getProducts().forEach(product -> product.setStore(initializedStore));
            Set<Product> products = new HashSet<>(productService.createAll(store.getProducts().stream().toList()));


            initializedStore.setProducts(products);


            return create(initializedStore);
        });
        /*var initializedStore = initializeStore(store);

        // Set the store for each product and create the products
        store.getProducts().forEach(product -> product.setStore(initializedStore));
        Set<Product> products = new HashSet<>(productService.createAll(store.getProducts().stream().toList()));

        // Set the products for the store
        initializedStore.setProducts(products);

        // Save the store with updated products
        return storeRepository.save(initializedStore);*/
        /*StoreCategory storeCategoryByStoreType = storeCategoryService.getStoreCategoryByStoreType(store.getStoreCategory().getStoreType().toString());
        if (storeCategoryByStoreType == null){
            storeCategoryByStoreType = storeCategoryService.create(store.getStoreCategory());
        }
        store.setStoreCategory(storeCategoryByStoreType);
        store =  storeRepository.save(store);
        for (var prod:store.getProducts()
        ) {
            prod.setStore(store);
        }
        var products = new HashSet<>(productService.createAll(store.getProducts().stream().toList()));
        store.setProducts(products);
        *//*for (var prod:products
             ) {
            prod.setStore(store);
            productService.create(prod);
        }*//*
        return storeRepository.save(store);*/
    }

    public CompletableFuture<Store> getStoreByName(final String name){
        return CompletableFuture.supplyAsync(()->{
            var res = storeRepository.findByName(name);
            return res;
        });
    }
    public List<Store> getStoreByCategory(String storeCategory){
        var category = storeCategoryService.getStoreCategoryByStoreType(storeCategory).orElseThrow(NoSuchElementException::new);
        return storeRepository.findByStoreCategory(category);
    }
    public Store addOrderToStore(Store store, Order order){
        var storeOrders = store.getOrders();//.stream().findFirst().isPresent() ? store.getOrders() : new ArrayList<Order>();
        /*if (storeOrders == null){
            storeOrders = new ArrayList<Order>();
        }*/
        storeOrders.add(order);
        store.setOrders(storeOrders);
        return store;
    }
    private Store initializeStore(final Store store){
        StoreCategory storeCategoryByStoreType = storeCategoryService.getStoreCategoryByStoreType(store.getStoreCategory().getStoreType().toString())
                .orElse(storeCategoryService.create(store.getStoreCategory()));
        var newStore = Store.builder().name(store.getName()).storeCategory(storeCategoryByStoreType).address(store.getAddress()).phoneNumber(store.getPhoneNumber()).build();
        return create(newStore);
    }
}
