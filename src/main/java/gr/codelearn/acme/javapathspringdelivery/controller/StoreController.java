package gr.codelearn.acme.javapathspringdelivery.controller;

import gr.codelearn.acme.javapathspringdelivery.domain.Store;
import gr.codelearn.acme.javapathspringdelivery.mapper.BaseMapper;
import gr.codelearn.acme.javapathspringdelivery.mapper.StoreMapper;
import gr.codelearn.acme.javapathspringdelivery.service.StoreService;
import gr.codelearn.acme.javapathspringdelivery.service.BaseService;
import gr.codelearn.acme.javapathspringdelivery.transfer.resource.StoreResource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController extends BaseController<Store, StoreResource>{
    private final StoreService storeService;
    private final StoreMapper storeMapper;
    @Override
    protected BaseService<Store, Long> getBaseService() {
        return storeService;
    }

    @Override
    protected BaseMapper<Store, StoreResource> getMapper() {
        return storeMapper;
    }

}
