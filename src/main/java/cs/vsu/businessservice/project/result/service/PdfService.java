package cs.vsu.businessservice.project.result.service;

import cs.vsu.businessservice.project.entity.Project;
import jakarta.servlet.http.HttpServletResponse;


public interface PdfService {
    void makeResultsPdf(Project project, HttpServletResponse response);
}
