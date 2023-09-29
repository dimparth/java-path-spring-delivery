package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.Store;
import gr.codelearn.acme.javapathspringdelivery.domain.StoreCategory;
import gr.codelearn.acme.javapathspringdelivery.domain.StoreType;
import gr.codelearn.acme.javapathspringdelivery.repository.StoreCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class StoreCategoryServiceImpl extends BaseServiceImpl<StoreCategory> implements StoreCategoryService{
    private final StoreCategoryRepository storeCategoryRepository;
    @Override
    public JpaRepository<StoreCategory, Long> getRepository() {
        return storeCategoryRepository;
    }

    @Override
    public StoreCategory getStoreCategoryByStoreType(String storeType) {
        return storeCategoryRepository.findByStoreType(getStoreType(storeType));
    }
    private StoreType getStoreType(String input){
        for (StoreType storeType : StoreType.values()) {
            if (storeType.name().equals(input.toUpperCase())) {
                return storeType;
            }
        }
        throw new NoSuchElementException("specified input was not found");
    }
}
