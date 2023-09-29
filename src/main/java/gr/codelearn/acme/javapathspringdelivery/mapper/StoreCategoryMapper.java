package gr.codelearn.acme.javapathspringdelivery.mapper;

import gr.codelearn.acme.javapathspringdelivery.domain.StoreCategory;
import gr.codelearn.acme.javapathspringdelivery.transfer.resource.StoreCategoryResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = IgnoreUnmappedConfig.class)
public interface StoreCategoryMapper extends BaseMapper<StoreCategory, StoreCategoryResource>{
}
