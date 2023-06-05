package cs.vsu.businessservice.service.impl;

import cs.vsu.businessservice.entity.*;
import cs.vsu.businessservice.exception.BadGetterOrSetterException;
import cs.vsu.businessservice.exception.ParseJsonException;
import cs.vsu.businessservice.service.ReflectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ReflectionServiceImpl implements ReflectionService {

    @Override
    public <T extends Project, U> T modifyEntity(T modifiedEntity, U editingEntity, String[] unmodifiableFieldNames) {
        var fieldNameIndex = 3;
        var modifiedClass = modifiedEntity.getClass();
        var editingClass = editingEntity.getClass();

        Economic economic;
        if (modifiedEntity.getEconomic() == null) {
            economic = Economic.builder().build();
        } else {
            economic = modifiedEntity.getEconomic();
        }
        FixedExpenses fixedExpenses;
        if (modifiedEntity.getFixedExpenses() == null) {
            fixedExpenses = FixedExpenses.builder().build();
        } else {
            fixedExpenses = modifiedEntity.getFixedExpenses();
        }
        VariableExpenses variableExpenses;
        if (modifiedEntity.getVariableExpenses() == null) {
            variableExpenses = VariableExpenses.builder().build();
        } else {
            variableExpenses = modifiedEntity.getVariableExpenses();
        }
        Investments investments;
        if (modifiedEntity.getInvestments() == null) {
            investments = Investments.builder().build();
        } else {
            investments = modifiedEntity.getInvestments();
        }

        Arrays.stream(editingClass.getDeclaredMethods())
                .filter(method ->
                        Arrays.stream(unmodifiableFieldNames)
                                .noneMatch(
                                        field -> method.getName().endsWith(field)
                                ) && (
                                method.getName().startsWith("get")
                        )
                ).forEach(
                        getter -> {
                            var getterName = getter.getName();
                            var fieldName = getterName.substring(fieldNameIndex);
                            try {
                                var returnType = getter.getReturnType();
                                var value = returnType.cast(getter.invoke(editingEntity));
                                if (value != null
                                        && !(Collection.class.isAssignableFrom(returnType)
                                        && ((Collection<?>) value).isEmpty())
                                ) {
                                    var pattern = Pattern.compile("(^[A-Z][a-z]*(Expenses){0,1}[A-Z])");
                                    var matcher = pattern.matcher(fieldName);
                                    var entityName = "";
                                    if (matcher.find()) {
                                        entityName = fieldName.substring(matcher.start(), matcher.end() - 1);
                                    } else throw new ParseJsonException(HttpStatus.BAD_REQUEST, "Could not parse request json");

                                    switch (entityName) {
                                        case "Economic" -> Economic.class.getDeclaredMethod("set" + fieldName.substring(8), returnType)
                                                .invoke(economic, value);
                                        case "FixedExpenses" ->
                                                FixedExpenses.class.getDeclaredMethod("set" + fieldName.substring(13), returnType)
                                                        .invoke(fixedExpenses, value);
                                        case "VariableExpenses" ->
                                                VariableExpenses.class.getDeclaredMethod("set" + fieldName.substring(16), returnType)
                                                        .invoke(variableExpenses, value);
                                        case "Investments" -> Investments.class.getDeclaredMethod("set" + fieldName.substring(11), returnType)
                                                .invoke(investments, value);
                                        case "Project" -> modifiedClass.getDeclaredMethod("set" + fieldName.substring(7), returnType)
                                                .invoke(modifiedEntity, value);
                                    }
                                }
                            } catch (NoSuchMethodException e) {
                                throw new BadGetterOrSetterException(
                                        HttpStatus.NOT_FOUND,
                                        modifiedClass.getSimpleName()
                                                + " entity doesn't have getter or setter for "
                                                + fieldName + " field"
                                );
                            } catch (InvocationTargetException e) {
                                throw new BadGetterOrSetterException(
                                        HttpStatus.NOT_FOUND,
                                        "Can not invoke " +
                                                modifiedClass.getSimpleName()
                                                + " entity's getter or setter for "
                                                + getterName + " field"
                                );
                            } catch (IllegalAccessException e) {
                                throw new BadGetterOrSetterException(
                                        HttpStatus.NOT_FOUND,
                                        "Can not get access to " +
                                                modifiedClass.getSimpleName()
                                                + " entity's getter or setter for "
                                                + getterName + " field"
                                );
                            }
                        }
                );
        modifiedEntity.setEconomic(economic);
        modifiedEntity.setFixedExpenses(fixedExpenses);
        modifiedEntity.setVariableExpenses(variableExpenses);
        modifiedEntity.setInvestments(investments);
        modifiedEntity.setLastEditingTime(LocalDateTime.now());
        return modifiedEntity;
    }
}
