package gr.codelearn.acme.javapathspringdelivery.transfer.resource;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class UserResource extends BaseResource {
    private String email;
    private String firstname;
    private String lastname;
    private Integer age;
    private String address;
    private String phoneNumber;
}
