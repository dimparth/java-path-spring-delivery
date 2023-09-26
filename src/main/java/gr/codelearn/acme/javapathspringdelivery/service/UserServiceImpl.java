package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.User;
import gr.codelearn.acme.javapathspringdelivery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService{
    private final UserRepository userRepository;

    @Override
    public JpaRepository<User, Long> getRepository() {
        return userRepository;
    }
}
