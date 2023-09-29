package gr.codelearn.acme.javapathspringdelivery.transfer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CreateOrderForm {
    private String userEmail;
    private String storeName;
    private List<String> products;
}
