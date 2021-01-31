package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A DeviceConfiguration.
 */
@Entity
@Table(name = "device_configuration")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "deviceconfiguration")
public class DeviceConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "primary_hostname")
    private String primaryHostname;

    @Column(name = "secondary_hostname")
    private String secondaryHostname;

    @Column(name = "port")
    private Integer port;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrimaryHostname() {
        return primaryHostname;
    }

    public DeviceConfiguration primaryHostname(String primaryHostname) {
        this.primaryHostname = primaryHostname;
        return this;
    }

    public void setPrimaryHostname(String primaryHostname) {
        this.primaryHostname = primaryHostname;
    }

    public String getSecondaryHostname() {
        return secondaryHostname;
    }

    public DeviceConfiguration secondaryHostname(String secondaryHostname) {
        this.secondaryHostname = secondaryHostname;
        return this;
    }

    public void setSecondaryHostname(String secondaryHostname) {
        this.secondaryHostname = secondaryHostname;
    }

    public Integer getPort() {
        return port;
    }

    public DeviceConfiguration port(Integer port) {
        this.port = port;
        return this;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceConfiguration)) {
            return false;
        }
        return id != null && id.equals(((DeviceConfiguration) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceConfiguration{" +
            "id=" + getId() +
            ", primaryHostname='" + getPrimaryHostname() + "'" +
            ", secondaryHostname='" + getSecondaryHostname() + "'" +
            ", port=" + getPort() +
            "}";
    }
}
