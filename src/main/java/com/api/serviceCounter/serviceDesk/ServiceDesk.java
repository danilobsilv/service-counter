package com.api.serviceCounter.serviceDesk;

import com.api.serviceCounter.shared.domain.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "service_desk")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class ServiceDesk extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "service_desk_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "service_desk_name", nullable = false, length = 120)
    private String name;

    @Column(name = "service_desk_timezone", nullable = false, length = 64)
    private String timezone;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
}

