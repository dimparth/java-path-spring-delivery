package gr.codelearn.acme.javapathspringdelivery.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class InstanceCounter {
    private String name;
    private Integer count;
}
