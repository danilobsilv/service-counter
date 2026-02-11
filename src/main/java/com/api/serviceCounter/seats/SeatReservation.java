package com.api.serviceCounter.seats;

import com.api.serviceCounter.shared.domain.BaseAuditEntity;
import com.api.serviceCounter.shared.domain.BaseCreatedEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
    name = "seat_reservation",
    indexes = {
        @Index(name = "idx_reservation_seat", columnList = "seat_id"),
        @Index(name = "idx_reservation_event", columnList = "event_id")
    }
)
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SeatReservation extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "seat_reservation_id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Column(name = "reserved_by", nullable = false, length = 120)
    private String reservedBy;

    @Column(name = "reservation_status", nullable = false, length = 24)
    private String status;

    @Column(name = "reserved_until", nullable = false)
    private Instant reservedUntil;
}