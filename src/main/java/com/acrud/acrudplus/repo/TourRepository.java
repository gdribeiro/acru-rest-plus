package com.acrud.acrudplus.repo;

import com.acrud.acrudplus.domain.Tour;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

@Tag(name = "Tours", description = "The Tour API")
public interface TourRepository extends PagingAndSortingRepository<Tour,Integer> {

    Page<Tour> findByTourPackageCode(@Param("code") String code, Pageable pageable);

    @Override
    @RestResource(exported = false)
    <S extends Tour> S save(S entity);

    @Override
    @RestResource(exported = false)
    <S extends Tour> Iterable<S> saveAll(Iterable<S> entities);

    @Override
    @RestResource(exported = false)
    void deleteById(Integer integer);

    @Override
    @RestResource(exported = false)
    void delete(Tour entity);

    @Override
    @RestResource(exported = false)
    void deleteAllById(Iterable<? extends Integer> integers);

    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends Tour> entities);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
