package br.com.fiap.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import br.com.fiap.IntegrationTest;
import br.com.fiap.domain.Sensor;
import br.com.fiap.repository.SensorRepository;
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
 * Integration tests for the {@link SensorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class SensorResourceIT {

    private static final String DEFAULT_VARIABLE = "AAAAAAAAAA";
    private static final String UPDATED_VARIABLE = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sensors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Sensor sensor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sensor createEntity(EntityManager em) {
        Sensor sensor = new Sensor().variable(DEFAULT_VARIABLE).unit(DEFAULT_UNIT).value(DEFAULT_VALUE);
        return sensor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sensor createUpdatedEntity(EntityManager em) {
        Sensor sensor = new Sensor().variable(UPDATED_VARIABLE).unit(UPDATED_UNIT).value(UPDATED_VALUE);
        return sensor;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Sensor.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        sensor = createEntity(em);
    }

    @Test
    void createSensor() throws Exception {
        int databaseSizeBeforeCreate = sensorRepository.findAll().collectList().block().size();
        // Create the Sensor
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sensor))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll().collectList().block();
        assertThat(sensorList).hasSize(databaseSizeBeforeCreate + 1);
        Sensor testSensor = sensorList.get(sensorList.size() - 1);
        assertThat(testSensor.getVariable()).isEqualTo(DEFAULT_VARIABLE);
        assertThat(testSensor.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testSensor.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    void createSensorWithExistingId() throws Exception {
        // Create the Sensor with an existing ID
        sensor.setId(1L);

        int databaseSizeBeforeCreate = sensorRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sensor))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll().collectList().block();
        assertThat(sensorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllSensors() {
        // Initialize the database
        sensorRepository.save(sensor).block();

        // Get all the sensorList
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
            .value(hasItem(sensor.getId().intValue()))
            .jsonPath("$.[*].variable")
            .value(hasItem(DEFAULT_VARIABLE))
            .jsonPath("$.[*].unit")
            .value(hasItem(DEFAULT_UNIT))
            .jsonPath("$.[*].value")
            .value(hasItem(DEFAULT_VALUE));
    }

    @Test
    void getSensor() {
        // Initialize the database
        sensorRepository.save(sensor).block();

        // Get the sensor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, sensor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(sensor.getId().intValue()))
            .jsonPath("$.variable")
            .value(is(DEFAULT_VARIABLE))
            .jsonPath("$.unit")
            .value(is(DEFAULT_UNIT))
            .jsonPath("$.value")
            .value(is(DEFAULT_VALUE));
    }

    @Test
    void getNonExistingSensor() {
        // Get the sensor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewSensor() throws Exception {
        // Initialize the database
        sensorRepository.save(sensor).block();

        int databaseSizeBeforeUpdate = sensorRepository.findAll().collectList().block().size();

        // Update the sensor
        Sensor updatedSensor = sensorRepository.findById(sensor.getId()).block();
        updatedSensor.variable(UPDATED_VARIABLE).unit(UPDATED_UNIT).value(UPDATED_VALUE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedSensor.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedSensor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll().collectList().block();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
        Sensor testSensor = sensorList.get(sensorList.size() - 1);
        assertThat(testSensor.getVariable()).isEqualTo(UPDATED_VARIABLE);
        assertThat(testSensor.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testSensor.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    void putNonExistingSensor() throws Exception {
        int databaseSizeBeforeUpdate = sensorRepository.findAll().collectList().block().size();
        sensor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, sensor.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sensor))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll().collectList().block();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSensor() throws Exception {
        int databaseSizeBeforeUpdate = sensorRepository.findAll().collectList().block().size();
        sensor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sensor))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll().collectList().block();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSensor() throws Exception {
        int databaseSizeBeforeUpdate = sensorRepository.findAll().collectList().block().size();
        sensor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sensor))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll().collectList().block();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSensorWithPatch() throws Exception {
        // Initialize the database
        sensorRepository.save(sensor).block();

        int databaseSizeBeforeUpdate = sensorRepository.findAll().collectList().block().size();

        // Update the sensor using partial update
        Sensor partialUpdatedSensor = new Sensor();
        partialUpdatedSensor.setId(sensor.getId());

        partialUpdatedSensor.variable(UPDATED_VARIABLE).unit(UPDATED_UNIT).value(UPDATED_VALUE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSensor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSensor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll().collectList().block();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
        Sensor testSensor = sensorList.get(sensorList.size() - 1);
        assertThat(testSensor.getVariable()).isEqualTo(UPDATED_VARIABLE);
        assertThat(testSensor.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testSensor.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    void fullUpdateSensorWithPatch() throws Exception {
        // Initialize the database
        sensorRepository.save(sensor).block();

        int databaseSizeBeforeUpdate = sensorRepository.findAll().collectList().block().size();

        // Update the sensor using partial update
        Sensor partialUpdatedSensor = new Sensor();
        partialUpdatedSensor.setId(sensor.getId());

        partialUpdatedSensor.variable(UPDATED_VARIABLE).unit(UPDATED_UNIT).value(UPDATED_VALUE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSensor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSensor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll().collectList().block();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
        Sensor testSensor = sensorList.get(sensorList.size() - 1);
        assertThat(testSensor.getVariable()).isEqualTo(UPDATED_VARIABLE);
        assertThat(testSensor.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testSensor.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    void patchNonExistingSensor() throws Exception {
        int databaseSizeBeforeUpdate = sensorRepository.findAll().collectList().block().size();
        sensor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, sensor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(sensor))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll().collectList().block();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSensor() throws Exception {
        int databaseSizeBeforeUpdate = sensorRepository.findAll().collectList().block().size();
        sensor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(sensor))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll().collectList().block();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSensor() throws Exception {
        int databaseSizeBeforeUpdate = sensorRepository.findAll().collectList().block().size();
        sensor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(sensor))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll().collectList().block();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSensor() {
        // Initialize the database
        sensorRepository.save(sensor).block();

        int databaseSizeBeforeDelete = sensorRepository.findAll().collectList().block().size();

        // Delete the sensor
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, sensor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Sensor> sensorList = sensorRepository.findAll().collectList().block();
        assertThat(sensorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
