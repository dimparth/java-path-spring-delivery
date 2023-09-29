package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.StoreCategory;
import gr.codelearn.acme.javapathspringdelivery.domain.StoreType;

public interface StoreCategoryService extends BaseService<StoreCategory,Long>{
    StoreCategory getStoreCategoryByStoreType(String storeType);
}
