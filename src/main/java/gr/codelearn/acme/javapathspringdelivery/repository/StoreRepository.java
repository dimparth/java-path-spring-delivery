package gr.codelearn.acme.javapathspringdelivery.repository;

import gr.codelearn.acme.javapathspringdelivery.domain.Store;
import gr.codelearn.acme.javapathspringdelivery.domain.StoreCategory;
import gr.codelearn.acme.javapathspringdelivery.transfer.PopularStoreDto;
import gr.codelearn.acme.javapathspringdelivery.transfer.PopularStoresPerCategoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store,Long> {


    @Query("SELECT DISTINCT s FROM Store s " +
            "LEFT JOIN FETCH s.orders o " +
            "LEFT JOIN FETCH s.products p " +
            "LEFT JOIN FETCH s.storeCategory " +
            "LEFT JOIN FETCH p.productCategory " +
            "LEFT JOIN FETCH o.user " +
            "LEFT JOIN FETCH o.orderItems"
    )
    List<Store> findAllFetching();
    @Query("SELECT DISTINCT s FROM Store s " +
            "LEFT JOIN FETCH s.orders o " +
            "LEFT JOIN FETCH s.products p " +
            "LEFT JOIN FETCH s.storeCategory " +
            "LEFT JOIN FETCH p.productCategory " +
            "LEFT JOIN FETCH o.user " +
            "LEFT JOIN FETCH o.orderItems " +
            "where s.name = :name "
    )
    Store findByName(@Param("name") String name);
    @Query("SELECT DISTINCT s FROM Store s " +
            "LEFT JOIN FETCH s.orders o " +
            "LEFT JOIN FETCH s.products p " +
            "LEFT JOIN FETCH s.storeCategory sc " +
            "LEFT JOIN FETCH p.productCategory " +
            "LEFT JOIN FETCH o.user " +
            "LEFT JOIN FETCH o.orderItems " +
            "where sc = :category "
    )
    List<Store> findByStoreCategory(@Param("category") StoreCategory storeCategory);

    @Query("SELECT new gr.codelearn.acme.javapathspringdelivery.transfer.PopularStoreDto(" +
            "s.name AS storeName, COUNT(o.id) AS orderCount)" +
            "FROM Store s " +
            "join s.orders o " +
            "group by s.name " +
            "ORDER BY orderCount DESC")
    List<PopularStoreDto> findMostFamousStores();

    @Query("SELECT " +
            "new gr.codelearn.acme.javapathspringdelivery.transfer.PopularStoresPerCategoryDto(" +
            "s.name AS storeName, s.phoneNumber AS phoneNumber, CAST(sc.storeType AS string) AS storeCategory, " +
            "COUNT(o.id) AS orderCount)" +
            " FROM Store s " +
            "LEFT JOIN s.storeCategory sc " +
            "LEFT JOIN s.orders o " +
            "GROUP BY s.name, s.phoneNumber, sc.storeType " +
            "ORDER BY orderCount DESC")
    List<PopularStoresPerCategoryDto> findPopularStoresPerCategory();
}
