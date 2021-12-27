package com.example.rssfeedanalyzer.appl.domain;

import javax.persistence.*;
import java.time.Instant;

/**
 * User: pelesic
 */
@MappedSuperclass
abstract public class EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ")
    @SequenceGenerator(name = "SEQ", sequenceName = "SEQ", allocationSize = 50)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false, name = "CHANGE_TIME")
    private Instant changeTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Instant getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Instant changeTime) {
        this.changeTime = changeTime;
    }

    @PrePersist
    private void pre() {
        changeTime = Instant.now();
    }
}