package br.com.fiap.web.rest;

import br.com.fiap.domain.SubscriptionSensor;
import br.com.fiap.repository.SubscriptionSensorRepository;
import br.com.fiap.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link br.com.fiap.domain.SubscriptionSensor}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SubscriptionSensorResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionSensorResource.class);

    private static final String ENTITY_NAME = "subscriptionSensor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionSensorRepository subscriptionSensorRepository;

    public SubscriptionSensorResource(SubscriptionSensorRepository subscriptionSensorRepository) {
        this.subscriptionSensorRepository = subscriptionSensorRepository;
    }

    /**
     * {@code POST  /subscription-sensors} : Create a new subscriptionSensor.
     *
     * @param subscriptionSensor the subscriptionSensor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionSensor, or with status {@code 400 (Bad Request)} if the subscriptionSensor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/subscription-sensors")
    public Mono<ResponseEntity<SubscriptionSensor>> createSubscriptionSensor(@RequestBody SubscriptionSensor subscriptionSensor)
        throws URISyntaxException {
        log.debug("REST request to save SubscriptionSensor : {}", subscriptionSensor);
        if (subscriptionSensor.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionSensor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return subscriptionSensorRepository
            .save(subscriptionSensor)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/subscription-sensors/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /subscription-sensors/:id} : Updates an existing subscriptionSensor.
     *
     * @param id the id of the subscriptionSensor to save.
     * @param subscriptionSensor the subscriptionSensor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionSensor,
     * or with status {@code 400 (Bad Request)} if the subscriptionSensor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionSensor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/subscription-sensors/{id}")
    public Mono<ResponseEntity<SubscriptionSensor>> updateSubscriptionSensor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SubscriptionSensor subscriptionSensor
    ) throws URISyntaxException {
        log.debug("REST request to update SubscriptionSensor : {}, {}", id, subscriptionSensor);
        if (subscriptionSensor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionSensor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return subscriptionSensorRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return subscriptionSensorRepository
                        .save(subscriptionSensor)
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                        .map(
                            result ->
                                ResponseEntity
                                    .ok()
                                    .headers(
                                        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString())
                                    )
                                    .body(result)
                        );
                }
            );
    }

    /**
     * {@code PATCH  /subscription-sensors/:id} : Partial updates given fields of an existing subscriptionSensor, field will ignore if it is null
     *
     * @param id the id of the subscriptionSensor to save.
     * @param subscriptionSensor the subscriptionSensor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionSensor,
     * or with status {@code 400 (Bad Request)} if the subscriptionSensor is not valid,
     * or with status {@code 404 (Not Found)} if the subscriptionSensor is not found,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionSensor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/subscription-sensors/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<SubscriptionSensor>> partialUpdateSubscriptionSensor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SubscriptionSensor subscriptionSensor
    ) throws URISyntaxException {
        log.debug("REST request to partial update SubscriptionSensor partially : {}, {}", id, subscriptionSensor);
        if (subscriptionSensor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionSensor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return subscriptionSensorRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<SubscriptionSensor> result = subscriptionSensorRepository
                        .findById(subscriptionSensor.getId())
                        .map(
                            existingSubscriptionSensor -> {
                                if (subscriptionSensor.getTelegramId() != null) {
                                    existingSubscriptionSensor.setTelegramId(subscriptionSensor.getTelegramId());
                                }
                                if (subscriptionSensor.getName() != null) {
                                    existingSubscriptionSensor.setName(subscriptionSensor.getName());
                                }

                                return existingSubscriptionSensor;
                            }
                        )
                        .flatMap(subscriptionSensorRepository::save);

                    return result
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                        .map(
                            res ->
                                ResponseEntity
                                    .ok()
                                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                    .body(res)
                        );
                }
            );
    }

    /**
     * {@code GET  /subscription-sensors} : get all the subscriptionSensors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionSensors in body.
     */
    @GetMapping("/subscription-sensors")
    public Mono<List<SubscriptionSensor>> getAllSubscriptionSensors() {
        log.debug("REST request to get all SubscriptionSensors");
        return subscriptionSensorRepository.findAll().collectList();
    }

    /**
     * {@code GET  /subscription-sensors} : get all the subscriptionSensors as a stream.
     * @return the {@link Flux} of subscriptionSensors.
     */
    @GetMapping(value = "/subscription-sensors", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<SubscriptionSensor> getAllSubscriptionSensorsAsStream() {
        log.debug("REST request to get all SubscriptionSensors as a stream");
        return subscriptionSensorRepository.findAll();
    }

    /**
     * {@code GET  /subscription-sensors/:id} : get the "id" subscriptionSensor.
     *
     * @param id the id of the subscriptionSensor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionSensor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/subscription-sensors/{id}")
    public Mono<ResponseEntity<SubscriptionSensor>> getSubscriptionSensor(@PathVariable Long id) {
        log.debug("REST request to get SubscriptionSensor : {}", id);
        Mono<SubscriptionSensor> subscriptionSensor = subscriptionSensorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(subscriptionSensor);
    }

    /**
     * {@code DELETE  /subscription-sensors/:id} : delete the "id" subscriptionSensor.
     *
     * @param id the id of the subscriptionSensor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/subscription-sensors/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteSubscriptionSensor(@PathVariable Long id) {
        log.debug("REST request to delete SubscriptionSensor : {}", id);
        return subscriptionSensorRepository
            .deleteById(id)
            .map(
                result ->
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
            );
    }
}
