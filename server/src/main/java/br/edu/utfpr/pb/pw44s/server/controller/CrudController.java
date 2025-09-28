package br.edu.utfpr.pb.pw44s.server.controller;

import br.edu.utfpr.pb.pw44s.server.service.ICrudServiceRead;
import br.edu.utfpr.pb.pw44s.server.service.ICrudServiceWrite;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CrudController<T, D, R, ID extends Serializable> {

    protected abstract ICrudServiceWrite<T, ID> getWriteService();
    protected abstract ICrudServiceRead<T, ID> getReadService();

    protected abstract ModelMapper getModelMapper();

    private final Class<T> typeClass;
    private final Class<D> typeDtoClass;
    private final Class<R> typeResponseClass;

    public CrudController(Class<T> typeClass, Class<D> typeDtoClass, Class<R> typeResponseClass) {
        this.typeClass = typeClass;
        this.typeDtoClass = typeDtoClass;
        this.typeResponseClass = typeResponseClass;
    }

    private D convertToDto(T entity) {
        return getModelMapper().map(entity, this.typeDtoClass);
    }

    private R convertToResponseDto(T entity) {
        return getModelMapper().map(entity, this.typeResponseClass);
    }

    private T convertToEntity(D entityDto) {
        return getModelMapper().map(entityDto, this.typeClass);
    }

    @GetMapping //http://ip.api:port/classname
    public ResponseEntity<List<R>> findAll() {
        return ResponseEntity.ok(
                getReadService().findAll().stream().map(this::convertToResponseDto).collect(Collectors.toList())
        );
    }

    @GetMapping("page")  //http://ip.api:port/classname/page
    public ResponseEntity<Page<R>> findAll(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) Boolean asc
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        if (order != null && asc != null) {
            pageRequest = PageRequest.of(page, size,
                    asc ? Sort.Direction.ASC : Sort.Direction.DESC, order);
        }
        return ResponseEntity.ok(
                getReadService().findAll(pageRequest).map(this::convertToResponseDto)
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<R> findOne(@PathVariable ID id) {
        T entity = getReadService().findOne(id);
        if (entity != null) {
            return ResponseEntity.ok(convertToResponseDto(entity));
        } else {
            return ResponseEntity.noContent().build();
        }
    }


    @PostMapping
    public ResponseEntity<R> create(@RequestBody @Valid D entityDto) {
        T entity = convertToEntity(entityDto);
        T savedEntity = getWriteService().save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponseDto(savedEntity));
    }

    @PutMapping("{id}")
    public ResponseEntity<D> update(@PathVariable ID id, @RequestBody @Valid D entity) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(convertToDto(getWriteService().save(convertToEntity(entity))));
    }

    @GetMapping("exists/{id}")
    public ResponseEntity<Boolean> exists(@PathVariable ID id) {
        return ResponseEntity.ok(getReadService().exists(id));
    }

    @GetMapping("count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(getReadService().count());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        getWriteService().delete((Iterable<? extends T>) id);
        return ResponseEntity.noContent().build();
    }


}