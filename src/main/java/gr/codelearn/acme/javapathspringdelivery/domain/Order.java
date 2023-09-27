package gr.codelearn.acme.javapathspringdelivery.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORDERS")
@SequenceGenerator(name = "idGenerator", sequenceName = "ORDERS_SEQ", initialValue = 1, allocationSize = 1)
public class Order extends BaseModel{
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private User user;
    @ToString.Exclude
    @OneToMany()
    private Set<Product> products = new HashSet<>();
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private PaymentMethod paymentMethod;
    @NotNull
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date orderingDate;
    @ManyToOne
    private Store store;
}
