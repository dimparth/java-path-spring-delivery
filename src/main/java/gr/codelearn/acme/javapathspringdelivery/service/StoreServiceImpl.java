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
    public CompletableFuture<List<Store>> findAll(){
        return CompletableFuture.supplyAsync(()->{
            logger.trace("Retrieving all Stores");
            var res = storeRepository.findAllFetching();
            return res;
        });
    }

    @TimeLimiter(name = "basicTimeout")
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
    @Override
    @TimeLimiter(name = "basicTimeout")
    public CompletableFuture<Store> createNewStoreWithProducts(final Store store) {
        return CompletableFuture.supplyAsync(()->{
            var initializedStore = initializeStore(store);
            store.getProducts().forEach(product -> product.setStore(initializedStore));
            Set<Product> products = new HashSet<>(productService.createAll(store.getProducts().stream().toList()));
            initializedStore.setProducts(products);
            return create(initializedStore);
        });
    }

    @TimeLimiter(name = "basicTimeout")
    public CompletableFuture<Store> getStoreByName(final String name){
        return CompletableFuture.supplyAsync(()->{
            logger.trace("Searching for store with name {}", name);
            var res = storeRepository.findByName(name);
            return res;
        });
    }
    @TimeLimiter(name = "basicTimeout")
    public CompletableFuture<List<Store>> getStoreByCategory(final String storeCategory){
        return CompletableFuture.supplyAsync(()->{
            logger.trace("Searching for stores belonging to category {}", storeCategory);
            var category = storeCategoryService.getStoreCategoryByStoreType(storeCategory).orElseThrow(NoSuchElementException::new);
            return storeRepository.findByStoreCategory(category);
        });
    }
    public Store addOrderToStore(Store store, Order order){
        var storeOrders = store.getOrders();
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
