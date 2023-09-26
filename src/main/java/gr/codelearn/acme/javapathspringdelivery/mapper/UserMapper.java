package gr.codelearn.acme.javapathspringdelivery.mapper;

import gr.codelearn.acme.javapathspringdelivery.domain.User;
import gr.codelearn.acme.javapathspringdelivery.transfer.resource.UserResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = IgnoreUnmappedConfig.class)
public interface UserMapper extends BaseMapper<User, UserResource>{
}
