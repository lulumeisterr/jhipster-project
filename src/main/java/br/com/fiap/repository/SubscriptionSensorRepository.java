package br.com.fiap.repository;

import br.com.fiap.domain.SubscriptionSensor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the SubscriptionSensor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionSensorRepository extends R2dbcRepository<SubscriptionSensor, Long>, SubscriptionSensorRepositoryInternal {
    // just to avoid having unambigous methods
    @Override
    Flux<SubscriptionSensor> findAll();

    @Override
    Mono<SubscriptionSensor> findById(Long id);

    @Override
    <S extends SubscriptionSensor> Mono<S> save(S entity);
}

interface SubscriptionSensorRepositoryInternal {
    <S extends SubscriptionSensor> Mono<S> insert(S entity);
    <S extends SubscriptionSensor> Mono<S> save(S entity);
    Mono<Integer> update(SubscriptionSensor entity);

    Flux<SubscriptionSensor> findAll();
    Mono<SubscriptionSensor> findById(Long id);
    Flux<SubscriptionSensor> findAllBy(Pageable pageable);
    Flux<SubscriptionSensor> findAllBy(Pageable pageable, Criteria criteria);
}
