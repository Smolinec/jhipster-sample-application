package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Device.
 */
@Entity
@Table(name = "device")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "device")
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "app_version")
    private String appVersion;

    @Column(name = "id_updated")
    private Boolean idUpdated;

    @ManyToOne
    @JsonIgnoreProperties(value = "devices", allowSetters = true)
    private Place place;

    @ManyToMany(mappedBy = "devices")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<DeviceProfile> deviceProfiles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public Device uuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public Device appVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Boolean isIdUpdated() {
        return idUpdated;
    }

    public Device idUpdated(Boolean idUpdated) {
        this.idUpdated = idUpdated;
        return this;
    }

    public void setIdUpdated(Boolean idUpdated) {
        this.idUpdated = idUpdated;
    }

    public Place getPlace() {
        return place;
    }

    public Device place(Place place) {
        this.place = place;
        return this;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Set<DeviceProfile> getDeviceProfiles() {
        return deviceProfiles;
    }

    public Device deviceProfiles(Set<DeviceProfile> deviceProfiles) {
        this.deviceProfiles = deviceProfiles;
        return this;
    }

    public Device addDeviceProfile(DeviceProfile deviceProfile) {
        this.deviceProfiles.add(deviceProfile);
        deviceProfile.getDevices().add(this);
        return this;
    }

    public Device removeDeviceProfile(DeviceProfile deviceProfile) {
        this.deviceProfiles.remove(deviceProfile);
        deviceProfile.getDevices().remove(this);
        return this;
    }

    public void setDeviceProfiles(Set<DeviceProfile> deviceProfiles) {
        this.deviceProfiles = deviceProfiles;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Device)) {
            return false;
        }
        return id != null && id.equals(((Device) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Device{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", appVersion='" + getAppVersion() + "'" +
            ", idUpdated='" + isIdUpdated() + "'" +
            "}";
    }
}
