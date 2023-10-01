package gr.codelearn.acme.javapathspringdelivery.mapper;

import gr.codelearn.acme.javapathspringdelivery.domain.Order;
import gr.codelearn.acme.javapathspringdelivery.transfer.resource.OrderResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, OrderItemMapper.class}, config = IgnoreUnmappedConfig.class)
public interface OrderMapper extends BaseMapper<Order, OrderResource> {
}
