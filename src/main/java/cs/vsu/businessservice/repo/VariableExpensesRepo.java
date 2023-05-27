package cs.vsu.businessservice.repo;

import cs.vsu.businessservice.entity.VariableExpenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;

import java.util.Optional;

public interface VariableExpensesRepo extends JpaRepository<VariableExpenses, Long> {
    @Override
    @NonNull
    Optional<VariableExpenses> findById(@NonNull Long id);
}
