package gr.codelearn.acme.javapathspringdelivery.transfer.resource;

import gr.codelearn.acme.javapathspringdelivery.domain.StoreType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class StoreCategoryResource extends BaseResource{
    private StoreType storeType;
}
