package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.ProductCategory;
import gr.codelearn.acme.javapathspringdelivery.domain.ProductType;
import gr.codelearn.acme.javapathspringdelivery.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl extends BaseServiceImpl<ProductCategory> implements ProductCategoryService{
    private final ProductCategoryRepository productCategoryRepository;
    @Override
    public JpaRepository<ProductCategory, Long> getRepository() {
        return productCategoryRepository;
    }

    @Override
    public ProductCategory create(final ProductCategory item) {
        logger.trace("Creating {}.", item);
        return getRepository().save(item);
    }
    @Override
    public ProductCategory getProductCategoryByProductType(String productType) {
        return productCategoryRepository.findByProductType(getProductType(productType));
    }
    public ProductCategory getNonPersistentProductCategory(String productType){
        logger.trace("Fetching product category {}", productType);
        return ProductCategory.builder().productType(getProductType(productType)).build();
    }
    private ProductType getProductType(String input){
        for (ProductType productType : ProductType.values()) {
            if (productType.name().equals(input.toUpperCase())) {
                return productType;
            }
        }
        throw new NoSuchElementException("specified input was not found");
    }
}
