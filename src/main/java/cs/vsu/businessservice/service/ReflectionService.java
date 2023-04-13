package cs.vsu.businessservice.service;

public interface ReflectionService {
    <T, U> T modifyEntity(T modifiedEntity, U editingEntity, String prefix, String[] unmodifiableFieldNames);
}
