package gr.codelearn.acme.javapathspringdelivery.repository;

import gr.codelearn.acme.javapathspringdelivery.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
