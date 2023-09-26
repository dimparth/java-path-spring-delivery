package gr.codelearn.acme.javapathspringdelivery.domain;

import java.util.HashSet;
import java.util.Set;

public class ProductCategory {
    private ProductType name;
    private Set<Product> products = new HashSet<>();
}
