package gr.codelearn.acme.javapathspringdelivery.transfer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PopularStoresPerCategoryDto {
    private String storeName;
    private String phoneNumber;
    private String storeCategory;
    private Long orderCount;
}
