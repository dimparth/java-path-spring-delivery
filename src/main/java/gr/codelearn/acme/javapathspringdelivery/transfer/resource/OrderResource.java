package gr.codelearn.acme.javapathspringdelivery.transfer.resource;

import gr.codelearn.acme.javapathspringdelivery.domain.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
@Builder
public class OrderResource extends BaseResource{
    private UserResource user;
    @ToString.Exclude
    private Set<OrderItemResource> orderItems = new HashSet<>();
    private PaymentMethod paymentMethod;
    private Date orderingDate;
    private String store;
    private OrderStatus orderStatus;

}
