package com.api.serviceCounter.ticket;

import com.api.serviceCounter.deskQueue.DeskQueue;
import com.api.serviceCounter.serviceDesk.ServiceDesk;
import com.api.serviceCounter.shared.domain.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
    name = "ticket",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_ticket_public_code_per_desk",
        columnNames = {"service_desk_id", "ticket_public_code"}
    ),
    indexes = {
        @Index(name = "idx_ticket_desk_status", columnList = "service_desk_id, ticket_status"),
        @Index(name = "idx_ticket_queue_status", columnList = "queue_id, ticket_status")
    }
)
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Ticket extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ticket_id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_desk_id", nullable = false)
    private ServiceDesk desk;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "queue_id", nullable = false)
    private DeskQueue queue;

    @Column(name = "ticket_public_code", nullable = false, length = 16)
    private String publicCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_status", nullable = false, length = 24)
    private TicketStatus status;

    @Column(name = "ticket_priority_score", nullable = false)
    private int priorityScore;

    @Column(name = "ticket_triage_level")
    private Integer triageLevel;

    @Column(name = "ticket_flags_json", columnDefinition = "jsonb")
    private String flagsJson;

    @Column(name = "ticket_called_at")
    private Instant calledAt;

    @Column(name = "ticket_sla_due_at")
    private Instant slaDueAt;
}