package cs.vsu.businessservice.project.repo;

import cs.vsu.businessservice.project.entity.Investments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface InvestmentsRepo extends JpaRepository<Investments, Long> {
    @Override
    @NonNull
    Optional<Investments> findById(@NonNull Long id);

    Optional<Investments> findInvestmentsByProjectId(Long projectId);
}
