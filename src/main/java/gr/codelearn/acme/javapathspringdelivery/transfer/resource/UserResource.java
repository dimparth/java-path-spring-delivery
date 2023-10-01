package gr.codelearn.acme.javapathspringdelivery.transfer.resource;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
