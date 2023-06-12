package cs.vsu.businessservice.service;

import cs.vsu.businessservice.entity.Project;
import jakarta.servlet.http.HttpServletResponse;


public interface PdfService {
    void makeResultsPdf(Project project, HttpServletResponse response);
}
