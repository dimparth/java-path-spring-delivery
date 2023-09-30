package gr.codelearn.acme.javapathspringdelivery.mapper;

import gr.codelearn.acme.javapathspringdelivery.domain.Product;
import gr.codelearn.acme.javapathspringdelivery.transfer.resource.ProductResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = IgnoreUnmappedConfig.class)
public interface ProductMapper extends BaseMapper<Product, ProductResource>{
    @Mapping(source = "store.name", target = "store")
    ProductResource toResource(Product product);

    @Mapping(source = "store", target = "store.name")
    Product toDomain(ProductResource resource);
}
