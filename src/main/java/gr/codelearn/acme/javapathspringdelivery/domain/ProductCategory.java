package gr.codelearn.acme.javapathspringdelivery.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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
    /*@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Product> products = new HashSet<>();*/
}
