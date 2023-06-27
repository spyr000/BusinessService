package cs.vsu.businessservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "investments")
@Getter
@Setter
@NoArgsConstructor
public class Investments implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "investments_id", nullable = false)
    private Long id;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "financing_cost_percent")
    private Double financingCostPercent;

    @Column(name = "showing_cost")
    private Double showingCost;

    @Column(name = "click_conversion_percent")
    private Double clickConversionPercent;

    @Column(name = "conversion_to_applications_percent")
    private Double conversionToApplicationsPercent;

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

    @JsonIgnore
    @OneToOne(mappedBy = "investments", orphanRemoval = true)
    private Project project;

    @Builder
    public Investments(Double amount,
                       Double financingCostPercent,
                       Double showingCost,
                       Double clickConversionPercent,
                       Double conversionToApplicationsPercent,
                       Integer customerGrowth,
                       Double customerCost,
                       Double monthGrowth,
                       Double requestsToPurchasesConversionPercent,
                       Double customerServiceCost,
                       Project project
    ) {
        this.amount = amount;
        this.financingCostPercent = financingCostPercent;
        this.showingCost = showingCost;
        this.clickConversionPercent = clickConversionPercent;
        this.conversionToApplicationsPercent = conversionToApplicationsPercent;
        this.customerGrowth = customerGrowth;
        this.customerCost = customerCost;
        this.monthGrowth = monthGrowth;
        this.requestsToPurchasesConversionPercent = requestsToPurchasesConversionPercent;
        this.customerServiceCost = customerServiceCost;
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Investments project = (Investments) o;
        return getId() != null && Objects.equals(getId(), project.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
