package com.api.serviceCounter.seats;

import com.api.serviceCounter.serviceDesk.ServiceDesk;
import com.api.serviceCounter.shared.domain.BaseAuditEntity;
import com.api.serviceCounter.shared.domain.BaseCreatedEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "event",
       indexes = @Index(name = "idx_event_desk_start", columnList = "service_desk_id, starts_at"))
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "event_id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_desk_id", nullable = false)
    private ServiceDesk desk;

    @Column(name = "event_name", nullable = false, length = 120)
    private String name;

    @Column(name = "starts_at", nullable = false)
    private Instant startsAt;
}