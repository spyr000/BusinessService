package cs.vsu.businessservice.repo;

import cs.vsu.businessservice.entity.Economic;
import cs.vsu.businessservice.entity.Investments;
import cs.vsu.businessservice.entity.VariableExpenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface InvestmentsRepo extends JpaRepository<Investments, Long> {
    @Override
    @NonNull
    Optional<Investments> findById(@NonNull Long id);
}
