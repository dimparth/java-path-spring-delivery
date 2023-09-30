package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.StoreCategory;
import gr.codelearn.acme.javapathspringdelivery.domain.StoreType;
import gr.codelearn.acme.javapathspringdelivery.repository.StoreCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreCategoryServiceImpl extends BaseServiceImpl<StoreCategory> implements StoreCategoryService{
    private final StoreCategoryRepository storeCategoryRepository;
    @Override
    public JpaRepository<StoreCategory, Long> getRepository() {
        return storeCategoryRepository;
    }

    public StoreCategory getStoreCategory(String storeType){
        return StoreCategory.builder().storeType(getStoreType(storeType)).build();
    }
    @Override
    public Optional<StoreCategory> getStoreCategoryByStoreType(String storeType) {
        return Optional.ofNullable(storeCategoryRepository.findByStoreType(getStoreType(storeType)));
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
