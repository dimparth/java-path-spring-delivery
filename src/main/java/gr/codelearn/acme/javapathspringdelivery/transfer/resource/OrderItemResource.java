package gr.codelearn.acme.javapathspringdelivery.transfer.resource;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString(callSuper = true)
public class OrderItemResource {
    private ProductResource product;
    private Integer quantity;
    private BigDecimal amount;
}
