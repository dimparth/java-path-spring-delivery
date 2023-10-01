package gr.codelearn.acme.javapathspringdelivery.repository;

import gr.codelearn.acme.javapathspringdelivery.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("select p from Product p " +
            "left join fetch p.store s " +
            "left join fetch p.productCategory " +
            "left join fetch s.orders o " +
            "left join fetch o.user " +
            "left join fetch o.orderItems oi ")
    List<Product> findAllFetching();

    @Query("select p from Product p " +
            "left join fetch p.store s " +
            "left join fetch p.productCategory " +
            "left join fetch s.orders o " +
            "left join fetch o.user " +
            "left join fetch o.orderItems oi " +
            "where p.name = :name")
    Product getByName(@Param("name") String name);
}
