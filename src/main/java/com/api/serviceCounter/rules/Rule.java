package com.api.serviceCounter.rules;

import com.api.serviceCounter.shared.domain.BaseCreatedEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "rule",
       indexes = @Index(name = "idx_rule_ruleset_order", columnList = "rule_set_id, rule_order"))
@Getter
@Setter
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Rule extends BaseCreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "rule_id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rule_set_id", nullable = false)
    private RuleSet ruleSet;

    @Column(name = "rule_order", nullable = false)
    private int order;

    @Column(name = "condition_json", nullable = false, columnDefinition = "jsonb")
    private String conditionJson;

    @Column(name = "action_json", nullable = false, columnDefinition = "jsonb")
    private String actionJson;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}