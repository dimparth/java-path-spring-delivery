package gr.codelearn.acme.javapathspringdelivery.controller;

import gr.codelearn.acme.javapathspringdelivery.domain.Store;
import gr.codelearn.acme.javapathspringdelivery.mapper.BaseMapper;
import gr.codelearn.acme.javapathspringdelivery.mapper.StoreMapper;
import gr.codelearn.acme.javapathspringdelivery.service.StoreService;
import gr.codelearn.acme.javapathspringdelivery.service.BaseService;
import gr.codelearn.acme.javapathspringdelivery.transfer.ApiResponse;
import gr.codelearn.acme.javapathspringdelivery.transfer.resource.StoreResource;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

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
    public ResponseEntity<ApiResponse<StoreResource>> getByStoreName(@PathVariable("name") final String name) {
        return ResponseEntity.ok(
                ApiResponse.<StoreResource>builder().data(getMapper().toResource(storeService.getStoreByName(name))).build());
    }

    @GetMapping("/{category}")
    public ResponseEntity<ApiResponse<List<StoreResource>>> getStoresByCategory(@PathVariable("category") final String category){
        return ResponseEntity.ok(
                ApiResponse.<List<StoreResource>>builder().data(getMapper().toResources(storeService.getStoreByCategory(category))).build()
        );
    }

    @GetMapping("/timeout")
    @TimeLimiter(name = "basicTimeout")
    public CompletableFuture<ResponseEntity<ApiResponse<StoreResource>>> timeout(){
        return CompletableFuture.supplyAsync(()->{
            Long i =1L;
            while(i>0){
                i++;
            }
            return ResponseEntity.ok(
                    ApiResponse.<StoreResource>builder().data(getMapper().toResource(storeService.getStoreByName("name"))).build());
        });
        /*int i =1;
        while(i>0){
            i++;
        }
        ResponseEntity.ok(
                ApiResponse.<StoreResource>builder().data(getMapper().toResource(storeService.getStoreByName("name"))).build());*/
    }
    protected HttpHeaders getTimeoutHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Keep-Alive", "timeout=1, max=1000");
        return headers;
    }
}
