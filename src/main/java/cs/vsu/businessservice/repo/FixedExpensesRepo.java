package cs.vsu.businessservice.repo;

import cs.vsu.businessservice.entity.FixedExpenses;
import cs.vsu.businessservice.entity.VariableExpenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface FixedExpensesRepo extends JpaRepository<FixedExpenses, Long> {
    @Override
    @NonNull
    Optional<FixedExpenses> findById(@NonNull Long id);

    Optional<FixedExpenses> findFixedExpensesByProjectId(Long projectId);
}
