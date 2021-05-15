package br.com.fiap.repository;

import br.com.fiap.domain.Sensor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Sensor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SensorRepository extends R2dbcRepository<Sensor, Long>, SensorRepositoryInternal {
    Flux<Sensor> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<Sensor> findAll();

    @Override
    Mono<Sensor> findById(Long id);

    @Override
    <S extends Sensor> Mono<S> save(S entity);
}

interface SensorRepositoryInternal {
    <S extends Sensor> Mono<S> insert(S entity);
    <S extends Sensor> Mono<S> save(S entity);
    Mono<Integer> update(Sensor entity);

    Flux<Sensor> findAll();
    Mono<Sensor> findById(Long id);
    Flux<Sensor> findAllBy(Pageable pageable);
    Flux<Sensor> findAllBy(Pageable pageable, Criteria criteria);
}
