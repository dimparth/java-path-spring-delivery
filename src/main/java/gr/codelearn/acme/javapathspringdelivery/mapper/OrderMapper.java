package gr.codelearn.acme.javapathspringdelivery.mapper;

import com.fasterxml.jackson.databind.ser.Serializers;
import gr.codelearn.acme.javapathspringdelivery.domain.Order;
import gr.codelearn.acme.javapathspringdelivery.domain.Product;
import gr.codelearn.acme.javapathspringdelivery.transfer.resource.OrderResource;
import gr.codelearn.acme.javapathspringdelivery.transfer.resource.ProductResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, OrderItemMapper.class}, config = IgnoreUnmappedConfig.class)
public interface OrderMapper extends BaseMapper<Order, OrderResource> {
    /*@Mapping(source = "store.name", target = "store")
    OrderResource toResource(Order order);

    @Mapping(source = "store", target = "store.name")
    Order toDomain(OrderResource resource);*/
}
