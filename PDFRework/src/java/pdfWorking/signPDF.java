/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfWorking;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author gmartin
 */
@Named(value = "signPDF")
@SessionScoped
public class signPDF implements Serializable {

    private String signature;
    private String fileName;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void onload(String urlParam) {
        this.setFileName(urlParam);

    }

    /**
     * Creates a new instance of signPDF
     */
    public signPDF() {

    }

    public void signMyPdf() throws IOException {
        UploadHelper uploadHelper = new UploadHelper();
        String filePath = uploadHelper.getContextPath(this.getFileName());
                PdfDocument pdfWriter = new PdfDocument(new PdfWriter(filePath.replace(".pdf", "-temp.pdf")));

        PdfDocument pdf = new PdfDocument(new PdfReader(filePath), new PdfWriter(filePath.replace(".pdf", "-temp.pdf")));        
        
        
        System.out.println("NUMBER OF PAGES" + pdf.getNumberOfPages());        
                System.out.println("NUMBER OF PAGES" + pdf.getLastPage());
        
        PdfPage page = pdf.getLastPage();
        Rectangle pageSize = page.getMediaBox();
        PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdf);
        System.out.println("signature : "+this.signature);
        new Canvas(pdfCanvas, pdf, pageSize).setFontColor(ColorConstants.GREEN)
              .showTextAligned(this.signature, pageSize.getWidth() - 60, 10, TextAlignment.CENTER, VerticalAlignment.MIDDLE, 0);
        pdf.close();
        File file = new File(filePath);        

        File fileToRename = new File(filePath.replace(".pdf", "-temp.pdf"));
        file.delete();
        fileToRename.renameTo(file);
        
    }

}
