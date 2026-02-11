package com.api.serviceCounter.session;

import com.api.serviceCounter.serviceDesk.ServiceDesk;
import com.api.serviceCounter.shared.domain.BaseAuditEntity;
import com.api.serviceCounter.shared.domain.BaseCreatedEntity;
import com.api.serviceCounter.ticket.Ticket;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
    name = "service_session",
    uniqueConstraints = @UniqueConstraint(name = "uq_session_per_ticket", columnNames = "ticket_id"),
    indexes = {
        @Index(name = "idx_session_desk_status", columnList = "service_desk_id, session_status"),
        @Index(name = "idx_session_worker", columnList = "worker_id")
    }
)
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceSession extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "service_session_id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_desk_id", nullable = false)
    private ServiceDesk desk;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(name = "worker_id", nullable = false, length = 64)
    private String workerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "session_status", nullable = false, length = 24)
    private SessionStatus status;

    @Column(name = "called_at")
    private Instant calledAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "ended_at")
    private Instant endedAt;

    public void finishDone() {
        this.status = SessionStatus.DONE;
        this.endedAt = Instant.now();
    }

    public void finishNoShow() {
        this.status = SessionStatus.NO_SHOW;
        this.endedAt = Instant.now();
    }
}