package gr.codelearn.acme.javapathspringdelivery.repository;

import gr.codelearn.acme.javapathspringdelivery.domain.Order;
import gr.codelearn.acme.javapathspringdelivery.domain.Store;
import gr.codelearn.acme.javapathspringdelivery.domain.StoreCategory;
import gr.codelearn.acme.javapathspringdelivery.transfer.PopularStoresPerCategoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store,Long> {
    @Query("SELECT s FROM Store s " +
            "left JOIN FETCH s.orders o " +
            "left JOIN FETCH s.products p " +
            "left join fetch s.storeCategory " +
            "left JOIN FETCH p.productCategory " +
            "left join fetch o.user " +
            "left join fetch o.orderItems "
    )
    List<Store> findAllFetching();
    Store findByName(String name);
    List<Store> findByStoreCategory(StoreCategory storeCategory);
    @Query("SELECT s FROM Store s LEFT JOIN FETCH s.orders o GROUP BY s.id ORDER BY COUNT(o) DESC")
    List<Store> findMostFamousStores();
    @Query("SELECT s, sc, COUNT(o) AS orderCount FROM Store s " +
            "LEFT JOIN FETCH s.orders o " +
            "JOIN s.storeCategory sc " +
            "GROUP BY s, sc " +
            "ORDER BY orderCount DESC")
    List<PopularStoresPerCategoryDto> findMostFamousStoresByCategory();

    @Query("SELECT new gr.codelearn.acme.javapathspringdelivery.transfer.PopularStoresPerCategoryDto(s AS store, sc AS storeCategory, COUNT(o) AS orderCount) FROM Store s " +
            "LEFT JOIN FETCH s.orders o " +
            "JOIN s.storeCategory sc " +
            "GROUP BY s, sc " +
            "ORDER BY orderCount DESC")
    List<PopularStoresPerCategoryDto> findMostFamousStoresByCategory2();
}
