package cs.vsu.businessservice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "economic")
@Getter
@Setter
@NoArgsConstructor
public class Economic implements Serializable {
    @Builder
    public Economic(Double averagePrice,
                    Long clientsAmt,
                    Long ordersCnt,
                    Double clientsOutflowPercent,
                    Double proceedPercent,
                    Double clientAttractionCost,
                    Project project
    ) {
        this.averagePrice = averagePrice;
        this.clientsAmt = clientsAmt;
        this.ordersCnt = ordersCnt;
        this.clientsOutflowPercent = clientsOutflowPercent;
        this.proceedPercent = proceedPercent;
        this.clientAttractionCost = clientAttractionCost;
        this.project = project;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "economic_id", nullable = false)
    private Long id;

    @Column(name = "avg_price")
    private Double averagePrice;

    @Column(name = "clients_amt")
    private Long clientsAmt;

    @Column(name = "avg_orders_cnt")
    private Long ordersCnt;

    @Column(name = "clients_outflow_percent")
    private Double clientsOutflowPercent;

    @Column(name = "proceed_percent")
    private Double proceedPercent;

    @Column(name = "client_attraction_cost")
    private Double clientAttractionCost;

    @OneToOne(mappedBy = "economic", orphanRemoval = true)
    private Project project;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Economic project = (Economic) o;
        return getId() != null && Objects.equals(getId(), project.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
