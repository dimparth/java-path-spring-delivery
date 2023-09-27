package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.Product;

public interface ProductService extends BaseService<Product,Long>{
    Product getByName(String name);
}
