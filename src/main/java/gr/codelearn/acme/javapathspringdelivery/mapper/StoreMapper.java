package gr.codelearn.acme.javapathspringdelivery.mapper;

import gr.codelearn.acme.javapathspringdelivery.domain.Order;
import gr.codelearn.acme.javapathspringdelivery.domain.Store;
import gr.codelearn.acme.javapathspringdelivery.transfer.resource.StoreResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductMapper.class}, config = IgnoreUnmappedConfig.class)
public interface StoreMapper extends BaseMapper<Store, StoreResource> {
    @Mapping(target = "orders", expression = "java(countOrders(domain.getOrders()))")
    StoreResource toResource(Store domain);

    @Mapping(target = "orders", ignore = true)
    Store toDomain(StoreResource resource);


    default Long countOrders(List<Order> orders) {
        System.out.println("orders in mapper: "+ orders);
        return (long) orders.stream().distinct().toList().size();
    }
}
