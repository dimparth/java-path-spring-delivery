package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.Product;
import gr.codelearn.acme.javapathspringdelivery.domain.ProductCategory;
import gr.codelearn.acme.javapathspringdelivery.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
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
    public List<Product> findAll(){
        var res =productRepository.findAllFetching();
        return res;
    }
    @Override
    public List<Product> createAll(final List<Product> items) {
        for (var item:items) {
            var  productCategoryByProductType = productCategoryService.getProductCategoryByProductType(item.getProductCategory().getProductType().toString()) != null
                    ?
                    productCategoryService.getProductCategoryByProductType(item.getProductCategory().getProductType().toString())
                    :
                    productCategoryService.create(productCategoryService.getNonPersistentProductCategory(item.getProductCategory().getProductType().toString()));
            /*if (productCategoryByProductType != null){
                productCategoryByProductType = productCategoryService.create(productCategoryByProductType);
            }*/
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
