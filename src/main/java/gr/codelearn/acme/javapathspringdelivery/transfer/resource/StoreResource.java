package gr.codelearn.acme.javapathspringdelivery.transfer.resource;

import gr.codelearn.acme.javapathspringdelivery.domain.Order;
import gr.codelearn.acme.javapathspringdelivery.domain.Product;
import gr.codelearn.acme.javapathspringdelivery.domain.StoreCategory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
public class StoreResource extends BaseResource{
    private String name;
    private String phoneNumber;
    private String address;
    @ToString.Exclude
    private List<Order> orders;
    @ToString.Exclude
    private Set<Product> products = new HashSet<>();
    private StoreCategory storeCategory;
}
