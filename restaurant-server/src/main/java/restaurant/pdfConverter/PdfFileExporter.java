package restaurant.pdfConverter;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map;

public class PdfFileExporter {

    public void exportPdfFile(String templateFileName, Map<String, Object> data, String pdfFileName) {
        String htmlContent = generateHtml(templateFileName, data);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(pdfFileName));
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private String generateHtml(String templateFileName, Map<String, Object> data) {
        TemplateEngine templateEngine = createTemplateEngine();
        Context context = new Context();
        context.setVariables(data);
        return templateEngine.process(templateFileName, context);
    }

    private TemplateEngine createTemplateEngine() {
        ClassLoaderTemplateResolver pdfTemplateResolver = new ClassLoaderTemplateResolver();
        pdfTemplateResolver.setPrefix("templates/");
        pdfTemplateResolver.setSuffix(".html");
        pdfTemplateResolver.setTemplateMode("HTML");
        pdfTemplateResolver.setCharacterEncoding("UTF-8");
        pdfTemplateResolver.setOrder(1);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(pdfTemplateResolver);
        return templateEngine;
    }

}