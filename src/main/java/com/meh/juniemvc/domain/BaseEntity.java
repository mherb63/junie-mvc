package com.meh.juniemvc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Abstract base class for all entities in the system.
 * Provides common fields and methods to reduce code duplication.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    @Column(updatable = false)
    private LocalDateTime createdDate;
    
    private LocalDateTime updateDate;

    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updateDate = LocalDateTime.now();
    }
}