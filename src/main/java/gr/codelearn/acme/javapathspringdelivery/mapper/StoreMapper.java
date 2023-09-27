package gr.codelearn.acme.javapathspringdelivery.mapper;

import gr.codelearn.acme.javapathspringdelivery.domain.Store;
import gr.codelearn.acme.javapathspringdelivery.transfer.resource.StoreResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = IgnoreUnmappedConfig.class)
public interface StoreMapper extends BaseMapper<Store, StoreResource> {

}
