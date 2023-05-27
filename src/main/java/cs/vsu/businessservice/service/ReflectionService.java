package cs.vsu.businessservice.service;

import cs.vsu.businessservice.entity.Project;

public interface ReflectionService {
    <T extends Project, U> T modifyEntity(T modifiedEntity, U editingEntity, String[] unmodifiableFieldNames);
}
