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
@Table(name="economic")
@Getter
@Setter
@NoArgsConstructor
public class Economic implements Serializable {


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

    // TODO: other fields

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
