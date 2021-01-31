package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A WebUser.
 */
@Entity
@Table(name = "web_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "webuser")
public class WebUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "web_user_push_notification_token",
               joinColumns = @JoinColumn(name = "web_user_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "push_notification_token_id", referencedColumnName = "id"))
    private Set<PushNotificationToken> pushNotificationTokens = new HashSet<>();

    @ManyToMany(mappedBy = "webUsers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Place> places = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public WebUser firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public WebUser lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public WebUser email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public WebUser password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<PushNotificationToken> getPushNotificationTokens() {
        return pushNotificationTokens;
    }

    public WebUser pushNotificationTokens(Set<PushNotificationToken> pushNotificationTokens) {
        this.pushNotificationTokens = pushNotificationTokens;
        return this;
    }

    public WebUser addPushNotificationToken(PushNotificationToken pushNotificationToken) {
        this.pushNotificationTokens.add(pushNotificationToken);
        pushNotificationToken.getWebUsers().add(this);
        return this;
    }

    public WebUser removePushNotificationToken(PushNotificationToken pushNotificationToken) {
        this.pushNotificationTokens.remove(pushNotificationToken);
        pushNotificationToken.getWebUsers().remove(this);
        return this;
    }

    public void setPushNotificationTokens(Set<PushNotificationToken> pushNotificationTokens) {
        this.pushNotificationTokens = pushNotificationTokens;
    }

    public Set<Place> getPlaces() {
        return places;
    }

    public WebUser places(Set<Place> places) {
        this.places = places;
        return this;
    }

    public WebUser addPlace(Place place) {
        this.places.add(place);
        place.getWebUsers().add(this);
        return this;
    }

    public WebUser removePlace(Place place) {
        this.places.remove(place);
        place.getWebUsers().remove(this);
        return this;
    }

    public void setPlaces(Set<Place> places) {
        this.places = places;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WebUser)) {
            return false;
        }
        return id != null && id.equals(((WebUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WebUser{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
