package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.ProductCategory;
import gr.codelearn.acme.javapathspringdelivery.domain.ProductType;
import gr.codelearn.acme.javapathspringdelivery.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

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
    public ProductCategory getProductCategoryByProductType(ProductType productType) {
        return productCategoryRepository.findByProductType(productType);
    }
}
