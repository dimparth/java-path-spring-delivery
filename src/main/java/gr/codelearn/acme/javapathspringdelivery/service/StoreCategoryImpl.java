package gr.codelearn.acme.javapathspringdelivery.service;

import gr.codelearn.acme.javapathspringdelivery.domain.StoreCategory;
import gr.codelearn.acme.javapathspringdelivery.repository.StoreCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreCategoryImpl extends BaseServiceImpl<StoreCategory> implements StoreCategoryService{
    private final StoreCategoryRepository storeCategoryRepository;
    @Override
    public JpaRepository<StoreCategory, Long> getRepository() {
        return storeCategoryRepository;
    }
}
