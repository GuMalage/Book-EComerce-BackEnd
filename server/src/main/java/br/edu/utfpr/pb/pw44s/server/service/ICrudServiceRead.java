package br.edu.utfpr.pb.pw44s.server.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;

public interface ICrudServiceRead <T, ID extends Serializable> {
    List<T> findAll();

    Page<T> findAll(Pageable pageable);

    boolean exists(ID id);

    long count();

    T findOne(ID id);
}
