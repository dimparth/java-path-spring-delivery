package gr.codelearn.acme.javapathspringdelivery.controller;


import gr.codelearn.acme.javapathspringdelivery.transfer.ApiResponse;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/demo")
public class DemoController {
    @GetMapping
    @TimeLimiter(name = "basicTimeout")
    public CompletableFuture<ResponseEntity<ApiResponse<String>>> timeoutDemo(){
        return CompletableFuture.supplyAsync(()->{
            Long i = 2L;
            while (i>0){
                i++;
            }
            return ResponseEntity.ok(ApiResponse.<String>builder().data("Hopefully no one will ever see this message").build());
        });
    }
}
