package cs.vsu.businessservice.repo;

import cs.vsu.businessservice.entity.Project;
import cs.vsu.businessservice.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface ProjectRepo extends JpaRepository<Project, Integer> {
    Optional<Project> findById(Long id);

    @Transactional
    Set<Project> findByUserId(Long userId);
}
