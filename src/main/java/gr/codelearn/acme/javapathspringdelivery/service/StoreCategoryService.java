package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.StoreCategory;

import java.util.Optional;

public interface StoreCategoryService extends BaseService<StoreCategory,Long>{
    Optional<StoreCategory> getStoreCategoryByStoreType(String storeType);
    StoreCategory getStoreCategory(String storeType);
}
