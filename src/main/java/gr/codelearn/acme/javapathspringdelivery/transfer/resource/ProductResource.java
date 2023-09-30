package gr.codelearn.acme.javapathspringdelivery.transfer.resource;

import gr.codelearn.acme.javapathspringdelivery.domain.ProductCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString(callSuper = true)
public class ProductResource extends BaseResource{
    private String serialNo;
    private String name;
    private String description;
    private BigDecimal price;
    private String store;
    @ToString.Exclude
    private ProductCategory productCategory;
}
