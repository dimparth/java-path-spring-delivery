package gr.codelearn.acme.javapathspringdelivery.transfer;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ApiError {
    private Integer status;
    private String message;
    private String path;
}
