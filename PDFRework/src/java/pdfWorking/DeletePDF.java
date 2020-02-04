package pdfWorking;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author vince
 */
@Named(value = "deletePDF")
@SessionScoped
public class DeletePDF implements Serializable {

    public static final String SRC = "E:\\EPSI\\B3\\applicationPDFJAVA\\ApplicationJEE\\PDFRework\\web\\ressources\\Competence_Inutile.pdf";
    public static final String DEST = "E:\\EPSI\\B3\\applicationPDFJAVA\\ApplicationJEE\\PDFRework\\web\\ressources\\Competence_Inutile_2.pdf";

    public DeletePDF() {
    }

    public String deleteFile() throws FileNotFoundException, IOException {
        System.out.println(SRC);
        PdfReader reader = new PdfReader(SRC);
        PdfWriter writer = new PdfWriter(SRC);
        PdfDocument document = new PdfDocument(reader, writer);
        document.removePage(1);
        document.close();
        return "";
    }
}
