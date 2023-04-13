package cs.vsu.businessservice.service.impl;

import cs.vsu.businessservice.exception.BadGetterOrSetterException;
import cs.vsu.businessservice.service.ReflectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ReflectionServiceImpl implements ReflectionService {
    private static final String SERVICE_PATH = "cs.vsu.businessservice.service.";
    private final ApplicationContext context;

    @Override
    public <T, U> T modifyEntity(T modifiedEntity, U editingEntity, String prefix, String[] unmodifiableFieldNames) {
        var fieldNameIndex = 3;
        var modifiedClass = modifiedEntity.getClass();
        var editingClass = editingEntity.getClass();
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
                            var name = getter.getName();
                            try {
                                var returnType = getter.getReturnType();
                                var value = returnType.cast(getter.invoke(editingEntity));
                                if (value != null
                                        && !(Collection.class.isAssignableFrom(returnType)
                                        && ((Collection<?>) value).isEmpty())
                                ) {
                                    if (name.endsWith("Id") && Number.class.isAssignableFrom(returnType)) {
                                        if (!name.substring(fieldNameIndex)
                                                .replaceFirst(prefix, "")
                                                .startsWith("Id")
                                        ) {
                                            var entityName = String.valueOf(name.charAt(0)).toUpperCase()
                                                    + name.substring(1, name.length() - 3);
                                            var serviceClass = Class.forName(SERVICE_PATH
                                                    + entityName
                                                    + "Service"
                                            );
                                            var service = serviceClass.cast(context.getBean(serviceClass));
                                            var entity = returnType.cast(
                                                    serviceClass
                                                            .getDeclaredMethod("get" + entityName, returnType)
                                                            .invoke(service, value)
                                            );

                                            modifiedClass
                                                    .getDeclaredMethod("set" + name.substring(fieldNameIndex), returnType)
                                                    .invoke(modifiedEntity, entity);
                                        }
                                    } else {
                                        modifiedClass
                                                .getDeclaredMethod("set" + name.substring(fieldNameIndex), returnType)
                                                .invoke(modifiedEntity, value);
                                    }
                                }
                            } catch (NoSuchMethodException e) {
                                throw new BadGetterOrSetterException(
                                        HttpStatus.NOT_FOUND,
                                        modifiedClass.getSimpleName()
                                                + " entity doesn't have getter or setter for "
                                                + name + " field"
                                );
                            } catch (InvocationTargetException e) {
                                throw new BadGetterOrSetterException(
                                        HttpStatus.NOT_FOUND,
                                        "Can not invoke " +
                                                modifiedClass.getSimpleName()
                                                + " entity's getter or setter for "
                                                + name + " field"
                                );
                            } catch (IllegalAccessException e) {
                                throw new BadGetterOrSetterException(
                                        HttpStatus.NOT_FOUND,
                                        "Can not get access to " +
                                                modifiedClass.getSimpleName()
                                                + " entity's getter or setter for "
                                                + name + " field"
                                );
                            } catch (ClassNotFoundException e) {
                                //TODO:
                                throw new RuntimeException(e);
                            }
                        }
                );
        return modifiedEntity;
    }
}
