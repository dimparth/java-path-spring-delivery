package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.Product;
import gr.codelearn.acme.javapathspringdelivery.repository.ProductRepository;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    @TimeLimiter(name = "basicTimeout")
    public CompletableFuture<List<Product>> findAll(){
        return CompletableFuture.supplyAsync(()->{
            logger.trace("Retrieving all products");
            return productRepository.findAllFetching();
        });
    }
    @Override
    public List<Product> createAll(final List<Product> items) {
        for (var item:items) {
            var  productCategoryByProductType = productCategoryService.getProductCategoryByProductType(item.getProductCategory().getProductType().toString()) != null
                    ?
                    productCategoryService.getProductCategoryByProductType(item.getProductCategory().getProductType().toString())
                    :
                    productCategoryService.create(productCategoryService.getNonPersistentProductCategory(item.getProductCategory().getProductType().toString()));
            item.setProductCategory(productCategoryByProductType);
        }
        return getRepository().saveAll(items);
    }
    public Product getByName(String name){
        return productRepository.getByName(name);
    }
    public List<Product> createOrUpdateProducts(List<String> productNames){
        var storeProducts = new ArrayList<Product>();
        for (var product:productNames
        ) {
            storeProducts.add(getByName(product));
        }
        return createAll(storeProducts);
    }
}
