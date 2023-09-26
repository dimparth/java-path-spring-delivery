package gr.codelearn.acme.javapathspringdelivery.controller;

import gr.codelearn.acme.javapathspringdelivery.domain.User;
import gr.codelearn.acme.javapathspringdelivery.mapper.BaseMapper;
import gr.codelearn.acme.javapathspringdelivery.mapper.UserMapper;
import gr.codelearn.acme.javapathspringdelivery.service.BaseService;
import gr.codelearn.acme.javapathspringdelivery.service.UserService;
import gr.codelearn.acme.javapathspringdelivery.transfer.resource.UserResource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController extends BaseController<User, UserResource>{
    private final UserService userService;
    private final UserMapper userMapper;
    @Override
    protected BaseService<User, Long> getBaseService() {
        return userService;
    }

    @Override
    protected BaseMapper<User, UserResource> getMapper() {
        return userMapper;
    }
}
