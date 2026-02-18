package com.api.serviceCounter.rules;

import com.api.serviceCounter.serviceDesk.ServiceDesk;
import com.api.serviceCounter.shared.domain.BaseCreatedEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "rule_set")
@Getter
@Setter
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RuleSet extends BaseCreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "rule_set_id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_desk_id", nullable = false)
    private ServiceDesk desk;

    @Column(name = "rule_set_name", nullable = false, length = 120)
    private String name;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "version", nullable = false)
    private int version;
}