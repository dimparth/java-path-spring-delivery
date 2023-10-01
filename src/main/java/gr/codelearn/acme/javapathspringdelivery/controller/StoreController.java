package gr.codelearn.acme.javapathspringdelivery.controller;

import gr.codelearn.acme.javapathspringdelivery.domain.Store;
import gr.codelearn.acme.javapathspringdelivery.mapper.BaseMapper;
import gr.codelearn.acme.javapathspringdelivery.mapper.StoreMapper;
import gr.codelearn.acme.javapathspringdelivery.service.BaseService;
import gr.codelearn.acme.javapathspringdelivery.service.StoreService;
import gr.codelearn.acme.javapathspringdelivery.transfer.*;
import gr.codelearn.acme.javapathspringdelivery.transfer.resource.StoreResource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

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

    @GetMapping(params = {"name"})
    public ResponseEntity<ApiResponse<StoreResource>> getByStoreName(@RequestParam("name") final String name) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(
                ApiResponse.<StoreResource>builder().data(getMapper().toResource(storeService.getStoreByName(name).get())).build());
    }

    @GetMapping(params = {"category"})
    public ResponseEntity<ApiResponse<List<StoreResource>>> getStoresByCategory(@RequestParam("category") final String category) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(
                ApiResponse.<List<StoreResource>>builder().data(getMapper().toResources(storeService.getStoreByCategory(category).get())).build()
        );
    }
    @GetMapping(headers = "action=storesByPopularity")
    public ResponseEntity<ApiResponse<List<PopularStoreDto>>> getStoresByPopularity() throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(
                ApiResponse.<List<PopularStoreDto>>builder().data(storeService.getPopularStores().get()).build()
        );
    }

    @GetMapping(headers = "action=storesByPopularityAndCategory")
    public ResponseEntity<ApiResponse<List<PopularStoresPerCategoryDto>>> getStoresByPopularityAndCategory() throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(
                ApiResponse.<List<PopularStoresPerCategoryDto>>builder().data(storeService.getPopularStoresPerCategory().get()).build()
        );
    }

    @GetMapping(headers = "action=popularCategories")
    public ResponseEntity<ApiResponse<List<PopularCategoriesDto>>> getPopularCategories() throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(
                ApiResponse.<List<PopularCategoriesDto>>builder().data(storeService.getMostPopularCategories().get()).build()
        );
    }

    @GetMapping(headers = "action=popularStoresGroupedByCategory")
    public ResponseEntity<ApiResponse<List<PopularStoreAndCategoryDto>>> getPopularStoresPerCategory() throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(
                ApiResponse.<List<PopularStoreAndCategoryDto>>builder().data(storeService.getPopular().get()).build()
        );
    }
}
