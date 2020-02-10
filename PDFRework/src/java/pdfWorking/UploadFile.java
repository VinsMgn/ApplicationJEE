/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfWorking;

import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.servlet.http.Part;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import javax.inject.Named;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

/**
 *
 * @author gmartin
 */
@Named(value = "uploadFile")
@SessionScoped
public class UploadFile implements Serializable {

    private Part file;
    private String destination;
    private String filePath;

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Creates a new instance of UploadPDF
     */
    public UploadFile() {
    }

    @PostConstruct
    public void init() {
    }

    public void upload() throws IOException {
        if (file != null) {
            if (file.getContentType().equals("application/pdf")) {
                UploadHelper uploadHelper = new UploadHelper();
                uploadHelper.uploadFileOnPath(uploadHelper.getFileNameFromPart(file), file);
                this.setFilePath(uploadHelper.getFileNameFromPart(file));
                System.out.println("It's a PDF");

            } else if (file.getContentType().contains("image")) {
                UploadHelper uploadHelper = new UploadHelper();

                String fileNameWithoutExtension = file.getSubmittedFileName().substring(0, file.getSubmittedFileName().lastIndexOf('.'));
                PdfDocument pdfDoc = new PdfDocument(new PdfWriter(uploadHelper.getContextPath(fileNameWithoutExtension + ".pdf")));
                Document doc = new Document(pdfDoc);
                System.out.println("CHEMIN : "+uploadHelper.getContextPath(fileNameWithoutExtension + ".pdf"));
                uploadHelper.uploadFileOnPath(uploadHelper.getFileNameFromPart(file), file);
                Table table = new Table(UnitValue.createPercentArray(new float[]{10, 10}));
                Image img = new Image(ImageDataFactory.create(uploadHelper.getContextPath(uploadHelper.getFileNameFromPart(file))));
                table.addCell(img.setAutoScale(true));
                doc.add(table);
                doc.close();
                this.setFilePath(fileNameWithoutExtension + ".pdf");
            } else {
                System.out.println("It isn't a PDF");

            }

        }

    }

}
