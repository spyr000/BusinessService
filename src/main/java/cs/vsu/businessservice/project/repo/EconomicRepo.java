package cs.vsu.businessservice.project.repo;

import cs.vsu.businessservice.project.entity.Economic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface EconomicRepo extends JpaRepository<Economic, Long> {
    @Override
    @NonNull
    Optional<Economic> findById(@NonNull Long id);

    Optional<Economic> findEconomicByProjectId(@NonNull Long projectId);
}
