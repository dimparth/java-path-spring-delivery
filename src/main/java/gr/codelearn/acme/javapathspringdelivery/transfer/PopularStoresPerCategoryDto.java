package gr.codelearn.acme.javapathspringdelivery.transfer;

import gr.codelearn.acme.javapathspringdelivery.domain.Store;
import gr.codelearn.acme.javapathspringdelivery.domain.StoreCategory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PopularStoresPerCategoryDto {
    private Store store;
    private StoreCategory storeCategory;
    private long orderCount;
}
