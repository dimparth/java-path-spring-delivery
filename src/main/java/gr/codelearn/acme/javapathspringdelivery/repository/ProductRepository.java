package gr.codelearn.acme.javapathspringdelivery.repository;

import gr.codelearn.acme.javapathspringdelivery.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Product getByName(String name);
}
