package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.Order;
import gr.codelearn.acme.javapathspringdelivery.domain.Store;
import gr.codelearn.acme.javapathspringdelivery.domain.StoreCategory;
import gr.codelearn.acme.javapathspringdelivery.transfer.PopularStoresPerCategoryDto;
import gr.codelearn.acme.javapathspringdelivery.transfer.PopularStoresPerCategoryDto2;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface StoreService extends BaseService<Store,Long> {
    CompletableFuture<Store> createNewStoreWithProducts(Store store);
    CompletableFuture<Store> getStoreByName(String name);
    List<Store> getStoreByCategory(String storeCategory);
    CompletableFuture<List<Store>> getPopularStores();
    CompletableFuture<List<PopularStoresPerCategoryDto2>> getPopularStoresPerCategory();
    CompletableFuture<List<PopularStoresPerCategoryDto>> getPopularStoresPerCategory2();
    Store addOrderToStore(Store store, Order order);
}
