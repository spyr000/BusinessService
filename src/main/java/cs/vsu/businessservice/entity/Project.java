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
@Table(name="projects")
@Getter
@Setter
@NoArgsConstructor
public class Project implements Serializable {

    @Builder
    public Project(String name, LocalDateTime creationTime, User user) {
        this.name = name;
        this.lastEditingTime = creationTime;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "project_id", nullable = false)
    private Long id;

    @Column(name = "project_name", nullable = false)
    private String name;

    @Column(name = "project_last_editing_time", nullable = false)
    private LocalDateTime lastEditingTime;

    @ManyToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    // TODO: other fields

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Project project = (Project) o;
        return getId() != null && Objects.equals(getId(), project.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
