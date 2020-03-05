package net.atlassian.cmathtutor.adaptive.repository;

import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CrudRepository<T, ID> extends org.springframework.data.repository.CrudRepository<T, ID> {

    @Override
    List<T> findAll();

    @Override
    List<T> findAllById(Iterable<ID> ids);

    @Override
    <S extends T> List<S> saveAll(Iterable<S> entities);
}
