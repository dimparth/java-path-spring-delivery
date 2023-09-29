package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.Product;
import gr.codelearn.acme.javapathspringdelivery.domain.ProductCategory;
import gr.codelearn.acme.javapathspringdelivery.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends BaseServiceImpl<Product> implements ProductService{
    private final ProductRepository productRepository;
    private final ProductCategoryService productCategoryService;
    @Override
    public JpaRepository<Product,Long> getRepository() {
        return productRepository;
    }
    @Override
    public List<Product> createAll(final List<Product> items) {
        for (var item:items) {
            ProductCategory productCategoryByProductType = productCategoryService.getProductCategoryByProductType(item.getProductCategory().getProductType());
            if (productCategoryByProductType != null){
                productCategoryByProductType = productCategoryService.create(productCategoryByProductType);
            }
            item.setProductCategory(productCategoryByProductType);
        }
        return getRepository().saveAll(items);
    }
    public Product getByName(String name){
        return productRepository.getByName(name);
    }
}
