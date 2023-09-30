package gr.codelearn.acme.javapathspringdelivery.repository;

import gr.codelearn.acme.javapathspringdelivery.domain.Order;
import gr.codelearn.acme.javapathspringdelivery.domain.Store;
import gr.codelearn.acme.javapathspringdelivery.domain.StoreCategory;
import gr.codelearn.acme.javapathspringdelivery.transfer.PopularStoresPerCategoryDto;
import gr.codelearn.acme.javapathspringdelivery.transfer.PopularStoresPerCategoryDto2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store,Long> {

    @Query("SELECT DISTINCT s FROM Store s " +
            "left JOIN FETCH s.orders  o " +
            "left JOIN FETCH s.products  p " +
            "left join fetch s.storeCategory  " +
            "left JOIN FETCH p.productCategory  " +
            "left join fetch o.user  " +
            "left join fetch o.orderItems  "
    )
    List<Store> findAllFetching();
    @Query("SELECT DISTINCT s FROM Store s " +
            "left JOIN FETCH s.orders  o " +
            "left JOIN FETCH s.products  p " +
            "left join fetch s.storeCategory  " +
            "left JOIN FETCH p.productCategory  " +
            "left join fetch o.user  " +
            "left join fetch o.orderItems " +
            "where s.name = :name "
    )
    Store findByName(@Param("name") String name);
    List<Store> findByStoreCategory(StoreCategory storeCategory);

    @Query("SELECT s FROM Store s " +
            "left JOIN FETCH s.orders o " +
            "left JOIN FETCH s.products p " +
            "left join fetch s.storeCategory sc " +
            "left JOIN FETCH p.productCategory pc " +
            "left join fetch o.user u " +
            "left join fetch o.orderItems oi " +
            "group by s.id, o.id, p.id, sc.id, pc.id, u.id, oi.id order by count(o.id) desc "
    )
    List<Store> findMostFamousStores();

    @Query("SELECT new gr.codelearn.acme.javapathspringdelivery.transfer.PopularStoresPerCategoryDto(s AS store, sc AS storeCategory, COUNT(o.id) AS orderCount) FROM Store s " +
            "LEFT JOIN FETCH s.orders o " +
            "JOIN s.storeCategory sc " +
            "GROUP BY s, sc " +
            "ORDER BY orderCount DESC")
    List<PopularStoresPerCategoryDto> findMostFamousStoresByCategory2();

    @Query("SELECT " +
            "new gr.codelearn.acme.javapathspringdelivery.transfer.PopularStoresPerCategoryDto2(" +
            "s.name AS storeName, s.phoneNumber AS phoneNumber, CAST(sc.storeType AS string) AS storeCategory, " +
            "(SELECT COUNT(o.id) FROM Order o WHERE o.store = s) AS orderCount)" +
            " FROM Store s " +
            "LEFT JOIN s.storeCategory sc " +
            "GROUP BY s.name, s.phoneNumber, sc.storeType " +
            "ORDER BY orderCount DESC")
    List<PopularStoresPerCategoryDto2> findPopularStoresPerCategory();
}
