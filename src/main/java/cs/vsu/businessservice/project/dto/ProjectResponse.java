package cs.vsu.businessservice.project.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import cs.vsu.businessservice.project.entity.Project;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProjectResponse {
    @Builder
    public ProjectResponse(Long projectId, String projectName, LocalDateTime projectCreationTime) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectCreationTime = projectCreationTime;
    }

    private Long projectId;
    private String projectName;
    private LocalDateTime projectCreationTime;

    public static ProjectResponse fromProject(Project project) {
        return ProjectResponse.builder()
                .projectName(project.getName())
                .projectId(project.getId())
                .projectCreationTime(project.getLastEditingTime())
                .build();
    }
}
