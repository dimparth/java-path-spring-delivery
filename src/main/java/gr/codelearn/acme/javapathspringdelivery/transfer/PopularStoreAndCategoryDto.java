package gr.codelearn.acme.javapathspringdelivery.transfer;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PopularStoreAndCategoryDto {
    private String category;
    private Integer totalOrderCount;
    private List<PopularStoreDto> store = new ArrayList<>();
}
