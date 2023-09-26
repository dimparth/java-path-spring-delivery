package gr.codelearn.acme.javapathspringdelivery.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "STORE_CATEGORIES", indexes = {@Index(columnList = "name")})
@SequenceGenerator(name = "idGenerator", sequenceName = "STORESCAT_SEQ", initialValue = 1, allocationSize = 1)
public class StoreCategory extends BaseModel{
    @Column(nullable = false)
    private String name;
    @ManyToMany
    private List<Store> stores;
}
