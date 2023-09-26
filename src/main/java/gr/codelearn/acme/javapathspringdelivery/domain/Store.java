package gr.codelearn.acme.javapathspringdelivery.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "STORES", indexes = {@Index(columnList = "phoneNumber")})
@SequenceGenerator(name = "idGenerator", sequenceName = "STORES_SEQ", initialValue = 1, allocationSize = 1)
public class Store extends BaseModel{
    @NotNull(message = "Store name cannot be empty!")
    @Column(length = 30, nullable = false)
    private String name;
    @NotNull(message = "Phone number is required!")
    private String phoneNumber;
    @NotNull(message = "Store address is mandatory!")
    private String address;
    @ToString.Exclude
    @ManyToMany
    private List<Order> users;
    @ToString.Exclude
    @OneToMany
    private Set<Product> products = new HashSet<>();
}
