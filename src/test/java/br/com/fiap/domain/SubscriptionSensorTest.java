package br.com.fiap.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.fiap.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubscriptionSensorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionSensor.class);
        SubscriptionSensor subscriptionSensor1 = new SubscriptionSensor();
        subscriptionSensor1.setId(1L);
        SubscriptionSensor subscriptionSensor2 = new SubscriptionSensor();
        subscriptionSensor2.setId(subscriptionSensor1.getId());
        assertThat(subscriptionSensor1).isEqualTo(subscriptionSensor2);
        subscriptionSensor2.setId(2L);
        assertThat(subscriptionSensor1).isNotEqualTo(subscriptionSensor2);
        subscriptionSensor1.setId(null);
        assertThat(subscriptionSensor1).isNotEqualTo(subscriptionSensor2);
    }
}
