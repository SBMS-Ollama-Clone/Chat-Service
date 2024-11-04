package com.kkimleang.chatservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tb_chats", uniqueConstraints = {
        @UniqueConstraint(columnNames = "share_id")
})
public class Chat {
    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;

    public Chat() {
        this.id = UUID.randomUUID().toString();
    }

    @Column(name = "used_id", nullable = false)
    private UUID userId;
    private String title;
    @Column(name = "created_at")
    private Instant createdAt;
    @Column(name = "updated_at")
    private Instant updatedAt;
    @Column(name = "share_id")
    private String shareId;
    private boolean archived = false;
    private boolean pinned = false;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
}
