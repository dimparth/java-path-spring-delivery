package gr.codelearn.acme.javapathspringdelivery.controller;

import gr.codelearn.acme.javapathspringdelivery.domain.Store;
import gr.codelearn.acme.javapathspringdelivery.mapper.BaseMapper;
import gr.codelearn.acme.javapathspringdelivery.mapper.StoreMapper;
import gr.codelearn.acme.javapathspringdelivery.service.BaseService;
import gr.codelearn.acme.javapathspringdelivery.service.StoreService;
import gr.codelearn.acme.javapathspringdelivery.transfer.ApiResponse;
import gr.codelearn.acme.javapathspringdelivery.transfer.PopularStoresPerCategoryDto;
import gr.codelearn.acme.javapathspringdelivery.transfer.PopularStoresPerCategoryDto2;
import gr.codelearn.acme.javapathspringdelivery.transfer.resource.StoreResource;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
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

    @GetMapping("/{name}")
    public ResponseEntity<ApiResponse<StoreResource>> getByStoreName(@PathVariable("name") final String name) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(
                ApiResponse.<StoreResource>builder().data(getMapper().toResource(storeService.getStoreByName(name).get())).build());
    }

    @GetMapping("/{category}")
    public ResponseEntity<ApiResponse<List<StoreResource>>> getStoresByCategory(@PathVariable("category") final String category){
        return ResponseEntity.ok(
                ApiResponse.<List<StoreResource>>builder().data(getMapper().toResources(storeService.getStoreByCategory(category))).build()
        );
    }
    @GetMapping(headers = "action=storesByPopularity")
    public ResponseEntity<ApiResponse<List<StoreResource>>> getStoresByPopularity() throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(
                ApiResponse.<List<StoreResource>>builder().data(getMapper().toResources(storeService.getPopularStores().get())).build()
        );
    }

    @GetMapping(headers = "action=storesByPopularityAndCategory")
    public ResponseEntity<ApiResponse<List<PopularStoresPerCategoryDto2>>> getStoresByPopularityAndCategory() throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(
                ApiResponse.<List<PopularStoresPerCategoryDto2>>builder().data(storeService.getPopularStoresPerCategory().get()).build()
        );
    }

    @GetMapping("/timeout")
    @TimeLimiter(name = "basicTimeout")
    public CompletableFuture<ResponseEntity<ApiResponse<List<StoreResource>>>> timeout(){
        return CompletableFuture.supplyAsync(()->{
            Long i =1L;
            while(i>0){
                i++;
            }
            return ResponseEntity.ok(
                    ApiResponse.<List<StoreResource>>builder().data(getMapper().toResources((storeService.findAll()))).build());
        });
    }
}
