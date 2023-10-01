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
@Table(name = "STORE_CATEGORIES", indexes = {@Index(columnList = "storeType")})
@SequenceGenerator(name = "idGenerator", sequenceName = "STORESCAT_SEQ", initialValue = 1, allocationSize = 1)
public class StoreCategory extends BaseModel {
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private StoreType storeType;
}
