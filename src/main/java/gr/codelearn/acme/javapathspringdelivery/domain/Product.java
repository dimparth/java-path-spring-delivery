package gr.codelearn.acme.javapathspringdelivery.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PRODUCTS")
@SequenceGenerator(name = "idGenerator", sequenceName = "PRODUCTS_SEQ", initialValue = 1, allocationSize = 1)
public class Product extends BaseModel{
    @NotNull(message = "Product name is mandatory!")
    private String name;
    private String description;
    @ManyToOne
    @JoinColumn
    private Store store;
    @NotNull(message = "Product price is mandatory!")
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;
    @ManyToOne
    @JoinColumn
    private ProductCategory productCategory;
}
