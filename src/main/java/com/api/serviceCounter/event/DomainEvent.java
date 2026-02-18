package com.api.serviceCounter.event;

import com.api.serviceCounter.serviceDesk.ServiceDesk;
import com.api.serviceCounter.shared.domain.BaseCreatedEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "domain_event",
       indexes = @Index(name = "idx_event_desk_created", columnList = "service_desk_id, created_at"))
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DomainEvent extends BaseCreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "domain_event_id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_desk_id", nullable = false)
    private ServiceDesk desk;

    @Column(name = "event_type", nullable = false, length = 80)
    private String type;

    @Column(name = "entity_id")
    private UUID entityId;

    @Column(name = "payload_json", nullable = false, columnDefinition = "jsonb")
    private String payloadJson;
}