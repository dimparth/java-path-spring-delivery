package gr.codelearn.acme.javapathspringdelivery.controller;

import gr.codelearn.acme.javapathspringdelivery.base.BaseComponent;
import gr.codelearn.acme.javapathspringdelivery.bootstrap.SampleDataInitializer;
import gr.codelearn.acme.javapathspringdelivery.domain.BaseModel;
import gr.codelearn.acme.javapathspringdelivery.mapper.BaseMapper;
import gr.codelearn.acme.javapathspringdelivery.service.BaseService;
import gr.codelearn.acme.javapathspringdelivery.transfer.ApiResponse;
import gr.codelearn.acme.javapathspringdelivery.transfer.resource.BaseResource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class BaseController<T extends BaseModel, R extends BaseResource> extends BaseComponent {

    protected abstract BaseService<T, Long> getBaseService();

    protected abstract BaseMapper<T, R> getMapper();

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<R>> get(@PathVariable("id") final Long id) {
        return ResponseEntity.ok(
                ApiResponse.<R>builder().data(getMapper().toResource(getBaseService().get(id))).build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<R>>> findAll() {

        return ResponseEntity.ok(
                ApiResponse.<List<R>>builder().data(getMapper().toResources(getBaseService().findAll())).build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<R>> create(@Valid @RequestBody final R resource) {
        return new ResponseEntity<>(ApiResponse.<R>builder()
                .data(getMapper().toResource(
                        getBaseService().create(getMapper().toDomain(resource))))
                .build(), HttpStatus.CREATED);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody final R resource) {
        getBaseService().update(getMapper().toDomain(resource));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") final Long id) {
        getBaseService().deleteById(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Valid @RequestBody final R resource) {
        if (getBaseService().exists(getMapper().toDomain(resource))) {
            getBaseService().delete(getMapper().toDomain(resource));
        }
    }

}