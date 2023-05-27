package cs.vsu.businessservice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "fixed_expenses")
@Getter
@Setter
@NoArgsConstructor
public class FixedExpenses implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "fixed_expenses_id", nullable = false)
    private Long id;

    @Column(name = "marketing_cost")
    private Double marketingCost;

    @Column(name = "office_rental_cost")
    private Double officeRentalCost;

    @Column(name = "wage_fund_cost")
    private Double wageFundCost;

    @Column(name = "income_tax_percent")
    private Double incomeTaxPercent;

    @Column(name = "insurance_cost")
    private Double insuranceCost;

    @Column(name = "equipment_service_cost")
    private Double equipmentServiceCost;

    @Column(name = "public_utilities_cost")
    private Double publicUtilitiesCost;

    @OneToOne(mappedBy = "fixedExpenses", orphanRemoval = true)
    private Project project;

    @Builder
    public FixedExpenses(Double marketingCost,
                         Double officeRentalCost,
                         Double wageFundCost,
                         Double incomeTaxPercent,
                         Double insuranceCost,
                         Double equipmentServiceCost,
                         Double publicUtilitiesCost,
                         Project project
    ) {
        this.marketingCost = marketingCost;
        this.officeRentalCost = officeRentalCost;
        this.wageFundCost = wageFundCost;
        this.incomeTaxPercent = incomeTaxPercent;
        this.insuranceCost = insuranceCost;
        this.equipmentServiceCost = equipmentServiceCost;
        this.publicUtilitiesCost = publicUtilitiesCost;
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FixedExpenses project = (FixedExpenses) o;
        return getId() != null && Objects.equals(getId(), project.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
