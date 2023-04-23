package cs.vsu.businessservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="variable_expenses")
@Getter
@Setter
@NoArgsConstructor
public class VariableExpenses implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "variable_expenses_id", nullable = false)
    private Long id;

    @Column(name = "logistics_per_month_cost")
    private Double logisticsCost;

    @Column(name = "other_expenses_per_month_cost")
    private Double otherExpensesCost;

    @Column(name = "equipment_cost")
    private Double equipmentCost;

    @Column(name = "office_tools_cost")
    private Double officeToolsCost;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        VariableExpenses project = (VariableExpenses) o;
        return getId() != null && Objects.equals(getId(), project.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
