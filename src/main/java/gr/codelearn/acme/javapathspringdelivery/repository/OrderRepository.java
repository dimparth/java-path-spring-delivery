package gr.codelearn.acme.javapathspringdelivery.repository;

import gr.codelearn.acme.javapathspringdelivery.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("SELECT o FROM Order o " +
            "left JOIN FETCH o.user " +
            "left JOIN FETCH o.orderItems oi " +
            "left join fetch oi.product p " +
            "left join fetch p.store s " +
            "left join fetch s.storeCategory " +
            "left join fetch p.productCategory ")
    List<Order> findAllFetching();
    @Query("SELECT o FROM Order o " +
            "left join fetch o.user u " +
            "left join fetch o.orderItems oi " +
            "left join fetch oi.product p " +
            "left join fetch p.store s " +
            "left join fetch s.storeCategory " +
            "left join fetch p.productCategory " +
            "WHERE u.email = :email")
    List<Order> findOrdersByUserEmail(@Param("email") String email);
}
