package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.Product;

import java.util.List;

public interface ProductService extends BaseService<Product,Long>{
    Product getByName(String name);
    List<Product> createOrUpdateProducts(List<String> productNames);
}
