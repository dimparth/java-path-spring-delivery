package gr.codelearn.acme.javapathspringdelivery.transfer.resource;

import gr.codelearn.acme.javapathspringdelivery.domain.StoreCategory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
public class StoreResource extends BaseResource{
    private String name;
    private String phoneNumber;
    private String address;
    private Long orders;
    @ToString.Exclude
    private Set<ProductResource> products = new HashSet<>();
    private StoreCategory storeCategory;
}
