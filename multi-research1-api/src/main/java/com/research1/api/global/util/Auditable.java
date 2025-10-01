package com.research1.api.global.util;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public class Auditable {

    @Column(updatable = false)
    protected LocalDateTime createYmd;

    @PrePersist
    protected void onCreate() {
        this.createYmd = LocalDateTime.now();
    }
}
