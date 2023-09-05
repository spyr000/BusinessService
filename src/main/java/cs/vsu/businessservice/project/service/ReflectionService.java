package cs.vsu.businessservice.project.service;

import cs.vsu.businessservice.project.entity.Project;

public interface ReflectionService {
    <T extends Project, U> T modifyEntity(T modifiedEntity, U editingEntity, String[] unmodifiableFieldNames);
}
