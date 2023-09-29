package gr.codelearn.acme.javapathspringdelivery.transfer.resource;

import gr.codelearn.acme.javapathspringdelivery.domain.ProductCategory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString(callSuper = true)
public class ProductResource extends BaseResource{
    private String name;
    private String description;
    private BigDecimal price;
    @ToString.Exclude
    private ProductCategory productCategory;
}
