package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A Application.
 */
@Entity
@Table(name = "application")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "application")
public class Application implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Lob
    @Column(name = "data")
    private byte[] data;

    @Column(name = "data_content_type")
    private String dataContentType;

    @Column(name = "version")
    private String version;

    @Column(name = "timestamp")
    private ZonedDateTime timestamp;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public Application fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getData() {
        return data;
    }

    public Application data(byte[] data) {
        this.data = data;
        return this;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getDataContentType() {
        return dataContentType;
    }

    public Application dataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
        return this;
    }

    public void setDataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
    }

    public String getVersion() {
        return version;
    }

    public Application version(String version) {
        this.version = version;
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public Application timestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Application)) {
            return false;
        }
        return id != null && id.equals(((Application) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Application{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", data='" + getData() + "'" +
            ", dataContentType='" + getDataContentType() + "'" +
            ", version='" + getVersion() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }
}
