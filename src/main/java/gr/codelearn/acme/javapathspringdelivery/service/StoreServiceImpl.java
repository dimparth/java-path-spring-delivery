package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.Order;
import gr.codelearn.acme.javapathspringdelivery.domain.Product;
import gr.codelearn.acme.javapathspringdelivery.domain.Store;
import gr.codelearn.acme.javapathspringdelivery.domain.StoreCategory;
import gr.codelearn.acme.javapathspringdelivery.repository.StoreRepository;
import gr.codelearn.acme.javapathspringdelivery.transfer.PopularCategoriesDto;
import gr.codelearn.acme.javapathspringdelivery.transfer.PopularStoreAndCategoryDto;
import gr.codelearn.acme.javapathspringdelivery.transfer.PopularStoreDto;
import gr.codelearn.acme.javapathspringdelivery.transfer.PopularStoresPerCategoryDto;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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
    @TimeLimiter(name = "basicTimeout")
    public CompletableFuture<List<Store>> findAll(){
        logger.trace("Retrieving all Stores");
        return CompletableFuture.supplyAsync(storeRepository::findAllFetching);
    }

    @TimeLimiter(name = "basicTimeout")
    public CompletableFuture<List<PopularCategoriesDto>> getMostPopularCategories(){
        logger.trace("retrieving most popular categories");
        return CompletableFuture.supplyAsync(storeRepository::findPopularCategories);
    }

    @TimeLimiter(name = "basicTimeout")
    public CompletableFuture<List<PopularStoreAndCategoryDto>> getPopular(){
        logger.trace("retrieving most popular stores grouped by category");
        return CompletableFuture.supplyAsync(()->{
            try {
                Map<String, List<Store>> map = findAll().get().stream().collect(Collectors.groupingBy(x->x.getStoreCategory().getStoreType().toString()));
                var result = new ArrayList<PopularStoreAndCategoryDto>();
                for (Map.Entry<String, List<Store>> entry : map.entrySet()) {
                    var category = entry.getKey();
                    var stores = entry.getValue();
                    var res = new PopularStoreAndCategoryDto();
                    res.setCategory(category);
                    var tot = (Integer) stores.stream().mapToInt(x -> x.getOrders().stream().distinct().toList().size()).sum();
                    res.setTotalOrderCount(tot);
                    for(var store: stores){
                        var st = new PopularStoreDto();
                        st.setStoreName(store.getName());
                        st.setOrderCount((long) store.getOrders().stream().distinct().toList().size());
                        res.getStore().add(st);
                    }
                    result.add(res);
                }
                Comparator<PopularStoreAndCategoryDto> comparator = Comparator.comparingLong(PopularStoreAndCategoryDto::getTotalOrderCount);
                result.sort(comparator.reversed());
                return result;
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @TimeLimiter(name = "basicTimeout")
    public CompletableFuture<List<PopularStoreDto>> getPopularStores() {
        logger.trace("Fetching most popular stores");
        return CompletableFuture.supplyAsync(storeRepository::findMostFamousStores);
    }

    @Override
    @TimeLimiter(name = "basicTimeout")
    public CompletableFuture<List<PopularStoresPerCategoryDto>> getPopularStoresPerCategory(){
        logger.trace("Fetching most popular stores and categories");
        return CompletableFuture.supplyAsync(storeRepository::findPopularStoresPerCategory);
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
        logger.trace("Searching for store with name {}", name);
        return CompletableFuture.supplyAsync(()->{
            var res = storeRepository.findByName(name);
            return res;
        });
    }
    @TimeLimiter(name = "basicTimeout")
    public CompletableFuture<List<Store>> getStoreByCategory(final String storeCategory){
        logger.trace("Searching for stores belonging to category {}", storeCategory);
        return CompletableFuture.supplyAsync(()->{
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
        logger.trace("initializing store {}", store.getName());
        StoreCategory storeCategoryByStoreType = storeCategoryService.getStoreCategoryByStoreType(store.getStoreCategory().getStoreType().toString())
                .orElse(storeCategoryService.create(store.getStoreCategory()));
        var newStore = Store.builder().name(store.getName()).storeCategory(storeCategoryByStoreType).address(store.getAddress()).phoneNumber(store.getPhoneNumber()).build();
        return create(newStore);
    }
}
