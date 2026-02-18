package com.api.serviceCounter.seats;

import com.api.serviceCounter.shared.domain.BaseAuditEntity;
import com.api.serviceCounter.shared.domain.BaseCreatedEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
    name = "seat",
    uniqueConstraints = @UniqueConstraint(name = "uq_seat_code_per_event", columnNames = {"event_id", "seat_code"}),
    indexes = @Index(name = "idx_seat_event", columnList = "event_id")
)
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Seat extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "seat_id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @Column(name = "seat_code", nullable = false, length = 32)
    private String seatCode;

    @Column(name = "seat_status", nullable = false, length = 24)
    private String status;
}