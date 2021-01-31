package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * A Temperature.
 */
@Entity
@Table(name = "temperature")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "temperature")
public class Temperature implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "create_timestamp")
    private ZonedDateTime createTimestamp;

    @Column(name = "last_update_timestamp")
    private ZonedDateTime lastUpdateTimestamp;

    @ManyToOne
    @JsonIgnoreProperties(value = "temperatures", allowSetters = true)
    private Device device;

    @ManyToMany(mappedBy = "temperatures")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Value> values = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Temperature name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public Temperature address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ZonedDateTime getCreateTimestamp() {
        return createTimestamp;
    }

    public Temperature createTimestamp(ZonedDateTime createTimestamp) {
        this.createTimestamp = createTimestamp;
        return this;
    }

    public void setCreateTimestamp(ZonedDateTime createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public ZonedDateTime getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public Temperature lastUpdateTimestamp(ZonedDateTime lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
        return this;
    }

    public void setLastUpdateTimestamp(ZonedDateTime lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    public Device getDevice() {
        return device;
    }

    public Temperature device(Device device) {
        this.device = device;
        return this;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Set<Value> getValues() {
        return values;
    }

    public Temperature values(Set<Value> values) {
        this.values = values;
        return this;
    }

    public Temperature addValue(Value value) {
        this.values.add(value);
        value.getTemperatures().add(this);
        return this;
    }

    public Temperature removeValue(Value value) {
        this.values.remove(value);
        value.getTemperatures().remove(this);
        return this;
    }

    public void setValues(Set<Value> values) {
        this.values = values;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Temperature)) {
            return false;
        }
        return id != null && id.equals(((Temperature) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Temperature{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", createTimestamp='" + getCreateTimestamp() + "'" +
            ", lastUpdateTimestamp='" + getLastUpdateTimestamp() + "'" +
            "}";
    }
}
