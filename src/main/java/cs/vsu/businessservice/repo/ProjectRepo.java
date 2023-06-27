package cs.vsu.businessservice.repo;

import cs.vsu.businessservice.entity.Project;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.Set;

public interface ProjectRepo extends JpaRepository<Project, Long> {
    @Override
    @NonNull
    Optional<Project> findById(@NonNull Long id);

    @Transactional
    Set<Project> findByUserId(Long userId);
}
