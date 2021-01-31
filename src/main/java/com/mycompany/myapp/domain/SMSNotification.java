package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.ZonedDateTime;

import com.mycompany.myapp.domain.enumeration.AlertType;

/**
 * A SMSNotification.
 */
@Entity
@Table(name = "sms_notification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "smsnotification")
public class SMSNotification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "tel_number")
    private String telNumber;

    @Column(name = "message")
    private String message;

    @Column(name = "created_timestamp")
    private ZonedDateTime createdTimestamp;

    @Column(name = "uuid_device")
    private String uuidDevice;

    @Column(name = "is_sending")
    private Boolean isSending;

    @Column(name = "sending_timestamp")
    private ZonedDateTime sendingTimestamp;

    @Column(name = "is_send")
    private Boolean isSend;

    @Column(name = "send_timestamp")
    private ZonedDateTime sendTimestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type")
    private AlertType alertType;

    @Column(name = "feature_send")
    private ZonedDateTime featureSend;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public SMSNotification telNumber(String telNumber) {
        this.telNumber = telNumber;
        return this;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getMessage() {
        return message;
    }

    public SMSNotification message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ZonedDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public SMSNotification createdTimestamp(ZonedDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
        return this;
    }

    public void setCreatedTimestamp(ZonedDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getUuidDevice() {
        return uuidDevice;
    }

    public SMSNotification uuidDevice(String uuidDevice) {
        this.uuidDevice = uuidDevice;
        return this;
    }

    public void setUuidDevice(String uuidDevice) {
        this.uuidDevice = uuidDevice;
    }

    public Boolean isIsSending() {
        return isSending;
    }

    public SMSNotification isSending(Boolean isSending) {
        this.isSending = isSending;
        return this;
    }

    public void setIsSending(Boolean isSending) {
        this.isSending = isSending;
    }

    public ZonedDateTime getSendingTimestamp() {
        return sendingTimestamp;
    }

    public SMSNotification sendingTimestamp(ZonedDateTime sendingTimestamp) {
        this.sendingTimestamp = sendingTimestamp;
        return this;
    }

    public void setSendingTimestamp(ZonedDateTime sendingTimestamp) {
        this.sendingTimestamp = sendingTimestamp;
    }

    public Boolean isIsSend() {
        return isSend;
    }

    public SMSNotification isSend(Boolean isSend) {
        this.isSend = isSend;
        return this;
    }

    public void setIsSend(Boolean isSend) {
        this.isSend = isSend;
    }

    public ZonedDateTime getSendTimestamp() {
        return sendTimestamp;
    }

    public SMSNotification sendTimestamp(ZonedDateTime sendTimestamp) {
        this.sendTimestamp = sendTimestamp;
        return this;
    }

    public void setSendTimestamp(ZonedDateTime sendTimestamp) {
        this.sendTimestamp = sendTimestamp;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public SMSNotification alertType(AlertType alertType) {
        this.alertType = alertType;
        return this;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public ZonedDateTime getFeatureSend() {
        return featureSend;
    }

    public SMSNotification featureSend(ZonedDateTime featureSend) {
        this.featureSend = featureSend;
        return this;
    }

    public void setFeatureSend(ZonedDateTime featureSend) {
        this.featureSend = featureSend;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SMSNotification)) {
            return false;
        }
        return id != null && id.equals(((SMSNotification) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SMSNotification{" +
            "id=" + getId() +
            ", telNumber='" + getTelNumber() + "'" +
            ", message='" + getMessage() + "'" +
            ", createdTimestamp='" + getCreatedTimestamp() + "'" +
            ", uuidDevice='" + getUuidDevice() + "'" +
            ", isSending='" + isIsSending() + "'" +
            ", sendingTimestamp='" + getSendingTimestamp() + "'" +
            ", isSend='" + isIsSend() + "'" +
            ", sendTimestamp='" + getSendTimestamp() + "'" +
            ", alertType='" + getAlertType() + "'" +
            ", featureSend='" + getFeatureSend() + "'" +
            "}";
    }
}
