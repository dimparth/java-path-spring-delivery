package gr.codelearn.acme.javapathspringdelivery.transfer;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PopularStoreAndCategoryDto {
    private String category;
    private PopularStoreDto store;
}
