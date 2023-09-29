package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.Order;
import gr.codelearn.acme.javapathspringdelivery.domain.Store;
import gr.codelearn.acme.javapathspringdelivery.domain.StoreCategory;
import gr.codelearn.acme.javapathspringdelivery.repository.StoreRepository;
import gr.codelearn.acme.javapathspringdelivery.transfer.PopularStoresPerCategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl extends BaseServiceImpl<Store> implements StoreService {
    private final StoreRepository storeRepository;
    private final StoreCategoryService storeCategoryService;
    private final ProductService productService;
    @Override
    public JpaRepository<Store,Long> getRepository() {
        return storeRepository;
    }

    @Override
    public List<Store> findAll(){
        return storeRepository.findAllFetching();
    }

    public List<Store> getPopularStores(){
        return storeRepository.findMostFamousStores();
    }

    @Override
    public List<PopularStoresPerCategoryDto> getPopularStoresPerCategory() {
        return storeRepository.findMostFamousStoresByCategory();
    }

    @Override
    public List<PopularStoresPerCategoryDto> getPopularStoresPerCategory2() {
        return storeRepository.findMostFamousStoresByCategory2();
    }

    @Override
    public Store createNewStoreWithProducts( Store store) {

        StoreCategory storeCategoryByStoreType = storeCategoryService.getStoreCategoryByStoreType(store.getStoreCategory().getStoreType().toString());
        if (storeCategoryByStoreType == null){
            storeCategoryByStoreType = storeCategoryService.create(store.getStoreCategory());
        }
        store.setStoreCategory(storeCategoryByStoreType);
        store =  storeRepository.save(store);
        for (var prod:store.getProducts()
        ) {
            prod.setStore(store);
        }
        var products = new HashSet<>(productService.createAll(store.getProducts().stream().toList()));
        store.setProducts(products);
        /*for (var prod:products
             ) {
            prod.setStore(store);
            productService.create(prod);
        }*/
        return storeRepository.save(store);

    }

    public Store getStoreByName(String name){
        return storeRepository.findByName(name);
    }
    public List<Store> getStoreByCategory(String storeCategory){
        var category = storeCategoryService.getStoreCategoryByStoreType(storeCategory);
        return storeRepository.findByStoreCategory(category);
    }
    public Store addOrderToStore(Store store, Order order){
        var storeOrders = store.getOrders();
        storeOrders.add(order);
        store.setOrders(storeOrders);
        return store;
    }
}
