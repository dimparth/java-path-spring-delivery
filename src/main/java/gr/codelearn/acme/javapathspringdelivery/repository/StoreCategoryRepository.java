package gr.codelearn.acme.javapathspringdelivery.repository;

import gr.codelearn.acme.javapathspringdelivery.domain.StoreCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreCategoryRepository extends JpaRepository<StoreCategory, Long> {

}
