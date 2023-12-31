package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.ProductCategory;

public interface ProductCategoryService extends BaseService<ProductCategory,Long>{
    ProductCategory getProductCategoryByProductType(String productType);
    ProductCategory getNonPersistentProductCategory(String productType);
}
