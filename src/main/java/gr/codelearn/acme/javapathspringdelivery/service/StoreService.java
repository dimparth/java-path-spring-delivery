package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.Order;
import gr.codelearn.acme.javapathspringdelivery.domain.Store;
import gr.codelearn.acme.javapathspringdelivery.transfer.PopularStoreDto;
import gr.codelearn.acme.javapathspringdelivery.transfer.PopularStoresPerCategoryDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface StoreService extends BaseService<Store,Long> {
    CompletableFuture<Store> createNewStoreWithProducts(Store store);
    CompletableFuture<Store> getStoreByName(String name);
    CompletableFuture<List<Store>> getStoreByCategory(String storeCategory);
    CompletableFuture<List<PopularStoreDto>> getPopularStores();
    CompletableFuture<List<PopularStoresPerCategoryDto>> getPopularStoresPerCategory();
    Store addOrderToStore(Store store, Order order);
}
