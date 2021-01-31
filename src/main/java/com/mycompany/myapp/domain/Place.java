package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Place.
 */
@Entity
@Table(name = "place")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "place")
public class Place implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "place_web_user",
               joinColumns = @JoinColumn(name = "place_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "web_user_id", referencedColumnName = "id"))
    private Set<WebUser> webUsers = new HashSet<>();

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

    public Place name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<WebUser> getWebUsers() {
        return webUsers;
    }

    public Place webUsers(Set<WebUser> webUsers) {
        this.webUsers = webUsers;
        return this;
    }

    public Place addWebUser(WebUser webUser) {
        this.webUsers.add(webUser);
        webUser.getPlaces().add(this);
        return this;
    }

    public Place removeWebUser(WebUser webUser) {
        this.webUsers.remove(webUser);
        webUser.getPlaces().remove(this);
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
        if (!(o instanceof Place)) {
            return false;
        }
        return id != null && id.equals(((Place) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Place{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
