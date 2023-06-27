package cs.vsu.businessservice.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import cs.vsu.businessservice.dto.result.doubleresults.DoubleResultsResponse;
import cs.vsu.businessservice.dto.result.investmentsresults.InvestmentsResultsResponse;
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
            BOLD_MARKER + "Income",
            "Cost price",
            BOLD_MARKER + "Gross approx",
            "Marketing and Advertising",
            "Rent",
            "Logistics",
            "Wage fund",
            "Other expenses",
            "Income tax",
            BOLD_MARKER + "Net profit"
    };

    private static final String[] DOUBLE_RESULTS_ROWS = {
            "New clients retaining",
            "New equipment",
            "Logistics cost",
            "Wage fund increasing",
            "Minimal investments"
    };

    private static final String[] INVESTMENTS_RESULT_HEADER = {
            "Month", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    private static final String[] INVESTMENTS_RESULTS_ROWS = {
            "Brought customers cost",
            "Recruitment client cost",
            "Subscriber acquisition costs",
            "Marketing expenses"
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

    @Value("${pdf.fonts.title_size}")
    private String titleFontSizeString;

    @Value("${pdf.fonts.subtitle_size}")
    private String subtitleFontSizeString;
    @Value("${pdf.fonts.encoding}")
    private String fontEncoding;

    private PdfPTable fillNormalResultsTable(PdfPTable table, NormalResultsResponse results) {
        var rowHeaderCounter = 0;
        var data = results.getNormalResultsData().getData();
        var percents = results.getNormalResultsPercents().getData();
        var fontSize = Integer.parseInt(fontSizeString);

        Font classicFont = FontFactory.getFont(
                FontFactory.TIMES_ROMAN,
                fontEncoding,
                BaseFont.EMBEDDED, fontSize
        );
        var boldFont = FontFactory.getFont(
                FontFactory.TIMES_BOLD,
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

    private PdfPTable fillDoubleResultsTable(PdfPTable table, DoubleResultsResponse results) {
        var rowHeaderCounter = 0;
        var data = results.getDoubleResultsData().getData();
        var fontSize = Integer.parseInt(fontSizeString);

        Font classicFont = FontFactory.getFont(
                FontFactory.TIMES_ROMAN,
                fontEncoding,
                BaseFont.EMBEDDED, fontSize
        );
        for (int i = 0; i < data.length * 2; i++) {
            var cell = new PdfPCell();
            if (i % 2 == 0) {
                var text = DOUBLE_RESULTS_ROWS[rowHeaderCounter];
                cell.setPhrase(new Phrase(text, classicFont));
                table.addCell(cell);

            } else {
                cell.setPhrase(
                        new Phrase(
                                String.format("%.2f", data[rowHeaderCounter])
                                        .replace(',', '.'), classicFont)
                );
                table.addCell(cell);
                rowHeaderCounter++;
            }
        }
        return table;
    }

    private void fillInvestmentsResultsTable(Document document,
                                                  InvestmentsResultsResponse results,
                                                  Paragraph blank,
                                                  Font classicFont,
                                                  Font subtitleFont) throws DocumentException {
        var rowHeaderCounter = 0;
        var data = results.getInvestmentsResultsData().getData();

        document.add(blank);
        var title = new Paragraph("Investments", subtitleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(blank);
        document.add(new Paragraph("Direct advertising", classicFont));
        document.add(blank);
        var table = new PdfPTable(13);
        for (String s : INVESTMENTS_RESULT_HEADER) {
            table.addCell(new Phrase(s, classicFont));
        }

        for (int i = 0; i < 2; i++) {
            table.addCell(new Phrase(INVESTMENTS_RESULTS_ROWS[i], classicFont));
            for (int j = 0; j < 12; j++) {
                table.addCell(new Phrase(String.valueOf(data[i]), classicFont));
            }
        }
        document.add(table);
        table = new PdfPTable(13);
        document.add(blank);
        document.add(new Paragraph("Content marketing", classicFont));
        document.add(blank);
        for (int i = 2; i < 4; i++) {
            table.addCell(new Phrase(INVESTMENTS_RESULTS_ROWS[i], classicFont));
            for (int j = 0; j < 12; j++) {
                table.addCell(new Phrase(String.valueOf(data[i]), classicFont));
            }
        }
        document.add(table);

    }

    @Override
    public void makeResultsPdf(Project project, HttpServletResponse response) {
        var document = new Document(PageSize.A4);
        var blank = new Paragraph(" ");
        var results = resultsCalculatorService.calculateNormalResults(project.getId());
        var doubleResults = resultsCalculatorService.calculateDoubleResults(project.getId());
        var investmentsResults = resultsCalculatorService.calculateInvestmentsResults(project.getId());
        var textSize = Integer.parseInt(fontSizeString);
        var titleSize = Integer.parseInt(titleFontSizeString);
        var subtitleSize = Integer.parseInt(subtitleFontSizeString);
        var textFont = FontFactory.getFont(
                FontFactory.TIMES_ROMAN,
                fontEncoding,
                BaseFont.EMBEDDED, textSize
        );
        var titleFont = FontFactory.getFont(
                FontFactory.TIMES_BOLD,
                fontEncoding,
                BaseFont.EMBEDDED, titleSize
        );
        var subtitleFont = FontFactory.getFont(
                FontFactory.TIMES_BOLD,
                fontEncoding,
                BaseFont.EMBEDDED, subtitleSize
        );
        var title = new Paragraph("Project business plan: \"" + project.getName() + "\"", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        var subtitle = new Paragraph("Project description: " + project.getDesc(), subtitleFont);
        var res = new Paragraph("Main results", subtitleFont);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            document.add(title);
            document.add(blank);
            document.add(subtitle);
            document.add(blank);
            document.add(res);
            document.add(blank);
            var table = fillNormalResultsTable(new PdfPTable(3), results);
            document.add(table);
            table = fillDoubleResultsTable(new PdfPTable(2), doubleResults);
            document.add(blank);
            document.add(new Paragraph("For doubling income per year", subtitleFont));
            document.add(blank);
            document.add(table);
            fillInvestmentsResultsTable(document, investmentsResults, blank, textFont, subtitleFont);

        } catch (DocumentException | IOException e) {
            throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            document.close();
        }


    }


}
