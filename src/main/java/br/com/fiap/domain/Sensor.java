package br.com.fiap.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Sensor.
 */
@Table("sensor")
public class Sensor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column("variable")
    private String variable;

    @Column("unit")
    private String unit;

    @Column("value")
    private String value;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sensor id(Long id) {
        this.id = id;
        return this;
    }

    public String getVariable() {
        return this.variable;
    }

    public Sensor variable(String variable) {
        this.variable = variable;
        return this;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String getUnit() {
        return this.unit;
    }

    public Sensor unit(String unit) {
        this.unit = unit;
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getValue() {
        return this.value;
    }

    public Sensor value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sensor)) {
            return false;
        }
        return id != null && id.equals(((Sensor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sensor{" +
            "id=" + getId() +
            ", variable='" + getVariable() + "'" +
            ", unit='" + getUnit() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
