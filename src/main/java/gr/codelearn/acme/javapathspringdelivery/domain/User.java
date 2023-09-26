package gr.codelearn.acme.javapathspringdelivery.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS", indexes = {@Index(columnList = "email")})
@SequenceGenerator(name = "idGenerator", sequenceName = "USERS_SEQ", initialValue = 1, allocationSize = 1)
public class User extends BaseModel{
    @NotNull(message = "Empty email is not allowed!")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Provided email is in wrong format!")
    @Column(length = 50, nullable = false, unique = true)
    private String email;
    @NotNull(message = "First name cannot be empty!")
    @Column(length = 30, nullable = false)
    private String firstname;
    @NotNull(message = "Last name cannot be empty!")
    @Column(length = 30, nullable = false)
    private String lastname;
    @Min(value = 10, message = "User is underage!")
    @Max(value = 120, message = "User is too old!")
    private Integer age;
    @NotNull(message = "How are you going to order without specifying an address?")
    private String address;
    @NotNull(message = "Phone number is required!")
    private String phoneNumber;
}
