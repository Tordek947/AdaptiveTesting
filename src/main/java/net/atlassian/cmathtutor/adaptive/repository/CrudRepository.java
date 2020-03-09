package net.atlassian.cmathtutor.adaptive.repository;

import java.util.Collection;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CrudRepository<T, ID> extends org.springframework.data.repository.CrudRepository<T, ID> {

    @Override
    Collection<T> findAll();

    @Override
    Collection<T> findAllById(Iterable<ID> ids);

    @Override
    <S extends T> Collection<S> saveAll(Iterable<S> entities);

}
