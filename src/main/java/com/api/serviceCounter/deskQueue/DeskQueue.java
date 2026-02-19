package com.api.serviceCounter.deskQueue;

import com.api.serviceCounter.serviceDesk.ServiceDesk;
import com.api.serviceCounter.shared.domain.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
    name = "queue",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_queue_code_per_desk",
        columnNames = {"service_desk_id", "queue_code"}
    )
)
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DeskQueue extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "queue_id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_desk_id")
    private ServiceDesk serviceDesk;

    @Enumerated(EnumType.STRING)
    @Column(name = "queue_code", nullable = false, length = 32)
    private QueueCode code;

    @Column(name = "queue_name", nullable = false, length = 120)
    private String name;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
}