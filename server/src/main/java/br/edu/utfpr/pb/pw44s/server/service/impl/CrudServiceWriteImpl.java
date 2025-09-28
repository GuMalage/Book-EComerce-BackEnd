package br.edu.utfpr.pb.pw44s.server.service.impl;

import br.edu.utfpr.pb.pw44s.server.service.ICrudServiceWrite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;


public abstract class CrudServiceWriteImpl<T, ID extends Serializable> implements ICrudServiceWrite<T, ID> {

    protected abstract JpaRepository<T, ID> getRepository();

    @Override
    public T save(T entity) {
        return getRepository().save(entity);
    }

    @Override
    public void deleteById(ID id) {
        getRepository().deleteById(id);
    }

    @Override
    public void delete(Iterable<? extends T> iterable) {
        getRepository().deleteAll(iterable);
    }

} 
