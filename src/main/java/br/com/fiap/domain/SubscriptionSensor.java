package br.com.fiap.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A SubscriptionSensor.
 */
@Table("subscription_sensor")
public class SubscriptionSensor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column("telegram_id")
    private Integer telegramId;

    @Column("name")
    private String name;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubscriptionSensor id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getTelegramId() {
        return this.telegramId;
    }

    public SubscriptionSensor telegramId(Integer telegramId) {
        this.telegramId = telegramId;
        return this;
    }

    public void setTelegramId(Integer telegramId) {
        this.telegramId = telegramId;
    }

    public String getName() {
        return this.name;
    }

    public SubscriptionSensor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionSensor)) {
            return false;
        }
        return id != null && id.equals(((SubscriptionSensor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionSensor{" +
            "id=" + getId() +
            ", telegramId=" + getTelegramId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
