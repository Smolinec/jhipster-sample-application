package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A PushNotificationToken.
 */
@Entity
@Table(name = "push_notification_token")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "pushnotificationtoken")
public class PushNotificationToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "token")
    private String token;

    @Column(name = "timestamp")
    private ZonedDateTime timestamp;

    @ManyToMany(mappedBy = "pushNotificationTokens")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<WebUser> webUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public PushNotificationToken token(String token) {
        this.token = token;
        return this;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public PushNotificationToken timestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Set<WebUser> getWebUsers() {
        return webUsers;
    }

    public PushNotificationToken webUsers(Set<WebUser> webUsers) {
        this.webUsers = webUsers;
        return this;
    }

    public PushNotificationToken addWebUser(WebUser webUser) {
        this.webUsers.add(webUser);
        webUser.getPushNotificationTokens().add(this);
        return this;
    }

    public PushNotificationToken removeWebUser(WebUser webUser) {
        this.webUsers.remove(webUser);
        webUser.getPushNotificationTokens().remove(this);
        return this;
    }

    public void setWebUsers(Set<WebUser> webUsers) {
        this.webUsers = webUsers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PushNotificationToken)) {
            return false;
        }
        return id != null && id.equals(((PushNotificationToken) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PushNotificationToken{" +
            "id=" + getId() +
            ", token='" + getToken() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }
}
