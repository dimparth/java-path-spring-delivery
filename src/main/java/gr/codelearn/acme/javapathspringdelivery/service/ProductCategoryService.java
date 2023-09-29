package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.ProductCategory;
import gr.codelearn.acme.javapathspringdelivery.domain.ProductType;

public interface ProductCategoryService extends BaseService<ProductCategory,Long>{
    ProductCategory getProductCategoryByProductType(ProductType productType);
}
