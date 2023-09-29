package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.Order;
import gr.codelearn.acme.javapathspringdelivery.domain.Store;
import gr.codelearn.acme.javapathspringdelivery.domain.StoreCategory;
import gr.codelearn.acme.javapathspringdelivery.transfer.PopularStoresPerCategoryDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface StoreService extends BaseService<Store,Long> {
    Store createNewStoreWithProducts(Store store);
    Store getStoreByName(String name);
    List<Store> getStoreByCategory(String storeCategory);
    List<Store> getPopularStores();
    List<PopularStoresPerCategoryDto> getPopularStoresPerCategory();
    List<PopularStoresPerCategoryDto> getPopularStoresPerCategory2();
    Store addOrderToStore(Store store, Order order);
}
