package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.Product;
import gr.codelearn.acme.javapathspringdelivery.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends BaseServiceImpl<Product> implements ProductService{
    private final ProductRepository productRepository;
    @Override
    public JpaRepository<Product,Long> getRepository() {
        return productRepository;
    }
    public Product getByName(String name){
        return productRepository.getByName(name);
    }
}
