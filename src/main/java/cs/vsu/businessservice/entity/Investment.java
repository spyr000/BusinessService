package cs.vsu.businessservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="investments")
@Getter
@Setter
@NoArgsConstructor
public class Investment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "investment_id", nullable = false)
    private Long id;

    @Column(name = "investment_amt")
    private Double investmentAmt;

    @Column(name = "financing_cost_percent")
    private Double financingCostPercent;

    @Column(name = "showing_cost")
    private Double showingCost;

    @Column(name = "click_conversion_percent")
    private Integer incomeTaxPercent;

    @Column(name = "conversion_to_applications_percent")
    private Integer conversionToApplicationsPercent;

    @Column(name = "customer_growth_per_month")
    private Integer customerGrowth;

    @Column(name = "customer_cost")
    private Double customerCost;

    @Column(name = "month_growth")
    private Double monthGrowth;

    @Column(name = "requests_to_purchases_conversion_percent")
    private Double requestsToPurchasesConversionPercent;

    @Column(name = "customer_service_cost")
    private Double customerServiceCost;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Investment project = (Investment) o;
        return getId() != null && Objects.equals(getId(), project.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
