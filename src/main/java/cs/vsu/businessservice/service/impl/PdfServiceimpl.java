package cs.vsu.businessservice.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import cs.vsu.businessservice.dto.result.normalresults.NormalResultsData;
import cs.vsu.businessservice.dto.result.normalresults.NormalResultsResponse;
import cs.vsu.businessservice.entity.Project;
import cs.vsu.businessservice.exception.BaseException;
import cs.vsu.businessservice.service.PdfService;
import cs.vsu.businessservice.service.ResultsCalculatorService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PdfServiceimpl implements PdfService {
    private static final Character BOLD_MARKER = '$';
    private static final String[] NORMAL_RESULTS_ROWS = {
            BOLD_MARKER + "Выручка",
            "Себестоимость",
            BOLD_MARKER + "Валовая прибыль",
            "Маркетинг и реклама",
            "Аренда",
            "Логистика",
            "ФОТ",
            "Амортизация",
            "Остальные расходы",
            "Налог на прибыль",
            BOLD_MARKER + "Чистая прибыль"
    };

    private final ResultsCalculatorService resultsCalculatorService;

    @Value("${pdf.fonts.directory}")
    private String fontsDirectory;

    @Value("${pdf.fonts.classic}")
    private String classicFontFilename;

    @Value("${pdf.fonts.bold}")
    private String boldFontFilename;
    @Value("${pdf.fonts.size}")
    private String fontSizeString;

    @Value("${pdf.fonts.encoding}")
    private String fontEncoding;

    private PdfPTable fillNormalResultsTable(PdfPTable table, NormalResultsResponse results) {
        var rowHeaderCounter = 0;
        var data = results.getNormalResultsData().getData();
        var percents = results.getNormalResultsPercents().getData();
        var fontSize = Integer.parseInt(fontSizeString);
        Font classicFont = FontFactory.getFont(
                fontsDirectory + classicFontFilename,
                fontEncoding,
                BaseFont.EMBEDDED, fontSize
        );
        var boldFont = FontFactory.getFont(
                fontsDirectory + boldFontFilename,
                fontEncoding,
                BaseFont.EMBEDDED, fontSize
        );
        for (int i = 0; i < data.length * 3; i++) {
            var cell = new PdfPCell();
            if (i % 3 == 0) {
                var text = NORMAL_RESULTS_ROWS[rowHeaderCounter];
                var font = classicFont;
                if (text.charAt(0) == BOLD_MARKER) {
                    text = text.substring(1);
                    font = boldFont;
                }
                cell.setPhrase(new Phrase(text, font));
                table.addCell(cell);

            } else if (i % 3 == 1) {
                cell.setPhrase(
                        new Phrase(
                                String.format("%.2f", data[rowHeaderCounter])
                                        .replace(',', '.'), classicFont)
                );
                table.addCell(cell);
            } else {
                cell.setPhrase(
                        new Phrase(
                                String.valueOf(
                                        Math.round(
                                                percents[rowHeaderCounter] * 100)
                                                / 100.) + '%',
                                classicFont)
                );
                table.addCell(cell);
                rowHeaderCounter++;
            }
        }
        return table;
    }

    @Override
    public void makeResultsPdf(Project project, HttpServletResponse response) {
        var document = new Document(PageSize.A4);
        var results = resultsCalculatorService.calculateNormalResults(project.getId());
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            var table = fillNormalResultsTable(new PdfPTable(3), results);
            document.add(table);
        } catch (DocumentException | IOException e) {
            throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            document.close();
        }
    }


}
