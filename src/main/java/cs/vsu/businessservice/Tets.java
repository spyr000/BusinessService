package cs.vsu.businessservice;

import cs.vsu.businessservice.entity.Project;
import cs.vsu.businessservice.entity.Role;
import cs.vsu.businessservice.entity.User;
import cs.vsu.businessservice.exception.BadGetterOrSetterException;
import org.springframework.http.HttpStatus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Tets {
    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        var user = User.builder().username("sam").email("sam@email").role(Role.USER).password("123").build();
//        var editingEntity = User.builder().username("ben").password("78788").build();
//        editingEntity.setProjects(new HashSet<>(List.of(new Project[]{new Project()})));
        var entity = Project.builder().creationTime(LocalDateTime.now()).user(user).name("GOVNO").build();
        var editingEntity = Project.builder().creationTime(LocalDateTime.MIN).name("HUINYA").build();
//        Field[] fields = Stream
//                .concat(Arrays.stream(entity.getClass().getSuperclass().getDeclaredFields()),
//                        Arrays.stream(entity.getClass().getDeclaredFields())).sorted(Comparator.comparing(Field::getName))
//                .toArray(Field[]::new);
//        Field[] fields = Arrays.stream(entity.getClass())
//        System.out.println(Arrays.toString(fields));
        System.out.println(getModifiedEntity(entity, editingEntity, new String[]{"User"}));
    }

    public static <T> T getModifiedEntity(T oldEntity, T editingEntity, String[] staticFields) {
        var stream = Arrays.stream(oldEntity.getClass().getDeclaredMethods())
                .filter(method ->
                        Arrays.stream(staticFields)
                                .noneMatch(
                                        field -> method.getName().endsWith(field)
                                ) && (
                                method.getName().startsWith("get")
                                        && !method.getName().endsWith("Id")
                        )
                );
        stream.forEach(
                method -> {
                    System.out.println(method.getName());
                    var name = method.getName();
                    try {
                        var returnType = method.getReturnType();
                        var value = returnType.cast(method.invoke(editingEntity));
                        if (value != null
                                && !(Collection.class.isAssignableFrom(returnType)
                                && ((Collection<?>) value).isEmpty())
                        ) {
                            oldEntity.getClass().getDeclaredMethod("set" + name.substring(3), returnType).invoke(oldEntity, value);
                        }
                    } catch (NoSuchMethodException e) {
                        throw new BadGetterOrSetterException(
                                HttpStatus.NOT_FOUND,
                                oldEntity.getClass().getSimpleName()
                                        + " entity doesn't have getter or setter for "
                                        + name + " field"
                        );
                    } catch (InvocationTargetException e) {
                        throw new BadGetterOrSetterException(
                                HttpStatus.NOT_FOUND,
                                "Can not invoke " +
                                        oldEntity.getClass().getSimpleName()
                                        + " entity's getter or setter for "
                                        + name + " field"
                        );
                    } catch (IllegalAccessException e) {
                        throw new BadGetterOrSetterException(
                                HttpStatus.NOT_FOUND,
                                "Can not get access to " +
                                        oldEntity.getClass().getSimpleName()
                                        + " entity's getter or setter for "
                                        + name + " field"
                        );
                    }
                }
        );
        return oldEntity;
    }
}
