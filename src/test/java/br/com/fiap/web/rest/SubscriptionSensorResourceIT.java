package br.com.fiap.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import br.com.fiap.IntegrationTest;
import br.com.fiap.domain.SubscriptionSensor;
import br.com.fiap.repository.SubscriptionSensorRepository;
import br.com.fiap.service.EntityManager;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link SubscriptionSensorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class SubscriptionSensorResourceIT {

    private static final Integer DEFAULT_TELEGRAM_ID = 1;
    private static final Integer UPDATED_TELEGRAM_ID = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/subscription-sensors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubscriptionSensorRepository subscriptionSensorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private SubscriptionSensor subscriptionSensor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionSensor createEntity(EntityManager em) {
        SubscriptionSensor subscriptionSensor = new SubscriptionSensor().telegramId(DEFAULT_TELEGRAM_ID).name(DEFAULT_NAME);
        return subscriptionSensor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionSensor createUpdatedEntity(EntityManager em) {
        SubscriptionSensor subscriptionSensor = new SubscriptionSensor().telegramId(UPDATED_TELEGRAM_ID).name(UPDATED_NAME);
        return subscriptionSensor;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(SubscriptionSensor.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        subscriptionSensor = createEntity(em);
    }

    @Test
    void createSubscriptionSensor() throws Exception {
        int databaseSizeBeforeCreate = subscriptionSensorRepository.findAll().collectList().block().size();
        // Create the SubscriptionSensor
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriptionSensor))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the SubscriptionSensor in the database
        List<SubscriptionSensor> subscriptionSensorList = subscriptionSensorRepository.findAll().collectList().block();
        assertThat(subscriptionSensorList).hasSize(databaseSizeBeforeCreate + 1);
        SubscriptionSensor testSubscriptionSensor = subscriptionSensorList.get(subscriptionSensorList.size() - 1);
        assertThat(testSubscriptionSensor.getTelegramId()).isEqualTo(DEFAULT_TELEGRAM_ID);
        assertThat(testSubscriptionSensor.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void createSubscriptionSensorWithExistingId() throws Exception {
        // Create the SubscriptionSensor with an existing ID
        subscriptionSensor.setId(1L);

        int databaseSizeBeforeCreate = subscriptionSensorRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriptionSensor))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SubscriptionSensor in the database
        List<SubscriptionSensor> subscriptionSensorList = subscriptionSensorRepository.findAll().collectList().block();
        assertThat(subscriptionSensorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllSubscriptionSensorsAsStream() {
        // Initialize the database
        subscriptionSensorRepository.save(subscriptionSensor).block();

        List<SubscriptionSensor> subscriptionSensorList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(SubscriptionSensor.class)
            .getResponseBody()
            .filter(subscriptionSensor::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(subscriptionSensorList).isNotNull();
        assertThat(subscriptionSensorList).hasSize(1);
        SubscriptionSensor testSubscriptionSensor = subscriptionSensorList.get(0);
        assertThat(testSubscriptionSensor.getTelegramId()).isEqualTo(DEFAULT_TELEGRAM_ID);
        assertThat(testSubscriptionSensor.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void getAllSubscriptionSensors() {
        // Initialize the database
        subscriptionSensorRepository.save(subscriptionSensor).block();

        // Get all the subscriptionSensorList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(subscriptionSensor.getId().intValue()))
            .jsonPath("$.[*].telegramId")
            .value(hasItem(DEFAULT_TELEGRAM_ID))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME));
    }

    @Test
    void getSubscriptionSensor() {
        // Initialize the database
        subscriptionSensorRepository.save(subscriptionSensor).block();

        // Get the subscriptionSensor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, subscriptionSensor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(subscriptionSensor.getId().intValue()))
            .jsonPath("$.telegramId")
            .value(is(DEFAULT_TELEGRAM_ID))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME));
    }

    @Test
    void getNonExistingSubscriptionSensor() {
        // Get the subscriptionSensor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewSubscriptionSensor() throws Exception {
        // Initialize the database
        subscriptionSensorRepository.save(subscriptionSensor).block();

        int databaseSizeBeforeUpdate = subscriptionSensorRepository.findAll().collectList().block().size();

        // Update the subscriptionSensor
        SubscriptionSensor updatedSubscriptionSensor = subscriptionSensorRepository.findById(subscriptionSensor.getId()).block();
        updatedSubscriptionSensor.telegramId(UPDATED_TELEGRAM_ID).name(UPDATED_NAME);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedSubscriptionSensor.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedSubscriptionSensor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SubscriptionSensor in the database
        List<SubscriptionSensor> subscriptionSensorList = subscriptionSensorRepository.findAll().collectList().block();
        assertThat(subscriptionSensorList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionSensor testSubscriptionSensor = subscriptionSensorList.get(subscriptionSensorList.size() - 1);
        assertThat(testSubscriptionSensor.getTelegramId()).isEqualTo(UPDATED_TELEGRAM_ID);
        assertThat(testSubscriptionSensor.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void putNonExistingSubscriptionSensor() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionSensorRepository.findAll().collectList().block().size();
        subscriptionSensor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, subscriptionSensor.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriptionSensor))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SubscriptionSensor in the database
        List<SubscriptionSensor> subscriptionSensorList = subscriptionSensorRepository.findAll().collectList().block();
        assertThat(subscriptionSensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSubscriptionSensor() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionSensorRepository.findAll().collectList().block().size();
        subscriptionSensor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriptionSensor))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SubscriptionSensor in the database
        List<SubscriptionSensor> subscriptionSensorList = subscriptionSensorRepository.findAll().collectList().block();
        assertThat(subscriptionSensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSubscriptionSensor() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionSensorRepository.findAll().collectList().block().size();
        subscriptionSensor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriptionSensor))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SubscriptionSensor in the database
        List<SubscriptionSensor> subscriptionSensorList = subscriptionSensorRepository.findAll().collectList().block();
        assertThat(subscriptionSensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSubscriptionSensorWithPatch() throws Exception {
        // Initialize the database
        subscriptionSensorRepository.save(subscriptionSensor).block();

        int databaseSizeBeforeUpdate = subscriptionSensorRepository.findAll().collectList().block().size();

        // Update the subscriptionSensor using partial update
        SubscriptionSensor partialUpdatedSubscriptionSensor = new SubscriptionSensor();
        partialUpdatedSubscriptionSensor.setId(subscriptionSensor.getId());

        partialUpdatedSubscriptionSensor.telegramId(UPDATED_TELEGRAM_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSubscriptionSensor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSubscriptionSensor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SubscriptionSensor in the database
        List<SubscriptionSensor> subscriptionSensorList = subscriptionSensorRepository.findAll().collectList().block();
        assertThat(subscriptionSensorList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionSensor testSubscriptionSensor = subscriptionSensorList.get(subscriptionSensorList.size() - 1);
        assertThat(testSubscriptionSensor.getTelegramId()).isEqualTo(UPDATED_TELEGRAM_ID);
        assertThat(testSubscriptionSensor.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void fullUpdateSubscriptionSensorWithPatch() throws Exception {
        // Initialize the database
        subscriptionSensorRepository.save(subscriptionSensor).block();

        int databaseSizeBeforeUpdate = subscriptionSensorRepository.findAll().collectList().block().size();

        // Update the subscriptionSensor using partial update
        SubscriptionSensor partialUpdatedSubscriptionSensor = new SubscriptionSensor();
        partialUpdatedSubscriptionSensor.setId(subscriptionSensor.getId());

        partialUpdatedSubscriptionSensor.telegramId(UPDATED_TELEGRAM_ID).name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSubscriptionSensor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSubscriptionSensor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SubscriptionSensor in the database
        List<SubscriptionSensor> subscriptionSensorList = subscriptionSensorRepository.findAll().collectList().block();
        assertThat(subscriptionSensorList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionSensor testSubscriptionSensor = subscriptionSensorList.get(subscriptionSensorList.size() - 1);
        assertThat(testSubscriptionSensor.getTelegramId()).isEqualTo(UPDATED_TELEGRAM_ID);
        assertThat(testSubscriptionSensor.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void patchNonExistingSubscriptionSensor() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionSensorRepository.findAll().collectList().block().size();
        subscriptionSensor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, subscriptionSensor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriptionSensor))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SubscriptionSensor in the database
        List<SubscriptionSensor> subscriptionSensorList = subscriptionSensorRepository.findAll().collectList().block();
        assertThat(subscriptionSensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSubscriptionSensor() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionSensorRepository.findAll().collectList().block().size();
        subscriptionSensor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriptionSensor))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SubscriptionSensor in the database
        List<SubscriptionSensor> subscriptionSensorList = subscriptionSensorRepository.findAll().collectList().block();
        assertThat(subscriptionSensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSubscriptionSensor() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionSensorRepository.findAll().collectList().block().size();
        subscriptionSensor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriptionSensor))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SubscriptionSensor in the database
        List<SubscriptionSensor> subscriptionSensorList = subscriptionSensorRepository.findAll().collectList().block();
        assertThat(subscriptionSensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSubscriptionSensor() {
        // Initialize the database
        subscriptionSensorRepository.save(subscriptionSensor).block();

        int databaseSizeBeforeDelete = subscriptionSensorRepository.findAll().collectList().block().size();

        // Delete the subscriptionSensor
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, subscriptionSensor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<SubscriptionSensor> subscriptionSensorList = subscriptionSensorRepository.findAll().collectList().block();
        assertThat(subscriptionSensorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
