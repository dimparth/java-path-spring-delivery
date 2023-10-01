package gr.codelearn.acme.javapathspringdelivery.transfer;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopularStoreDto {
    private String storeName;
    private Long orderCount;
}
