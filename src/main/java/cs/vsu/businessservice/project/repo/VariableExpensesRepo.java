package cs.vsu.businessservice.project.repo;

import cs.vsu.businessservice.project.entity.VariableExpenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface VariableExpensesRepo extends JpaRepository<VariableExpenses, Long> {
    Optional<VariableExpenses> findVariableExpensesByProjectId(Long id);
    @Override
    @NonNull
    Optional<VariableExpenses> findById(@NonNull Long id);
}
