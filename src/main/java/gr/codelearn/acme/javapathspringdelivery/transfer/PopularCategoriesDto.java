package gr.codelearn.acme.javapathspringdelivery.transfer;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PopularCategoriesDto {
    private String storeCategory;
    private Long orderCount;
}
