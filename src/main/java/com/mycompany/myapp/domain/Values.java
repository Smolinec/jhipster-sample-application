package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Values.
 */
@Entity
@Table(name = "values")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "values")
public class Values implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "value")
    private Double value;

    @Column(name = "timestamp")
    private ZonedDateTime timestamp;

    @ManyToOne
    @JsonIgnoreProperties(value = "values", allowSetters = true)
    private Temperature temperature;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "values_temperature",
               joinColumns = @JoinColumn(name = "values_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "temperature_id", referencedColumnName = "id"))
    private Set<Temperature> temperatures = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public Values value(Double value) {
        this.value = value;
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public Values timestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public Values temperature(Temperature temperature) {
        this.temperature = temperature;
        return this;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public Set<Temperature> getTemperatures() {
        return temperatures;
    }

    public Values temperatures(Set<Temperature> temperatures) {
        this.temperatures = temperatures;
        return this;
    }

    public Values addTemperature(Temperature temperature) {
        this.temperatures.add(temperature);
        temperature.getValues().add(this);
        return this;
    }

    public Values removeTemperature(Temperature temperature) {
        this.temperatures.remove(temperature);
        temperature.getValues().remove(this);
        return this;
    }

    public void setTemperatures(Set<Temperature> temperatures) {
        this.temperatures = temperatures;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Values)) {
            return false;
        }
        return id != null && id.equals(((Values) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Values{" +
            "id=" + getId() +
            ", value=" + getValue() +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }
}
