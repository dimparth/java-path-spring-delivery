package gr.codelearn.acme.javapathspringdelivery.bootstrap;


import gr.codelearn.acme.javapathspringdelivery.base.BaseComponent;
import gr.codelearn.acme.javapathspringdelivery.domain.User;
import gr.codelearn.acme.javapathspringdelivery.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(1)
@Profile("sample")
@RequiredArgsConstructor
public class SampleDataInitializer extends BaseComponent implements CommandLineRunner {
    private final UserService userService;
    @Override
    public void run(String... args) throws Exception {
        addSampleUsers();
    }
    private void addSampleUsers(){
        var users = List.of(User.builder().firstname("Dimitris").lastname("Parthenis").age(28).email("dim@sample.com").phoneNumber("21012345").address("Address 81").build(),
                User.builder().firstname("Giorgos").lastname("George").age(25).email("giorgos@sample.com").phoneNumber("21101234").address("Address 28").build());
        users.forEach(user -> userService.create(user));
    }
}
