package gr.codelearn.acme.javapathspringdelivery.mapper;

import gr.codelearn.acme.javapathspringdelivery.domain.Product;
import gr.codelearn.acme.javapathspringdelivery.transfer.resource.ProductResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {StoreMapper.class, OrderMapper.class},config = IgnoreUnmappedConfig.class)
public interface ProductMapper extends BaseMapper<Product, ProductResource>{
}
