package gr.codelearn.acme.javapathspringdelivery.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PRODUCT_CATEGORIES", indexes = {@Index(columnList = "productType")})
@SequenceGenerator(name = "idGenerator", sequenceName = "PRODCAT_SEQ", initialValue = 1, allocationSize = 1)
public class ProductCategory extends BaseModel {
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private ProductType productType;
}
