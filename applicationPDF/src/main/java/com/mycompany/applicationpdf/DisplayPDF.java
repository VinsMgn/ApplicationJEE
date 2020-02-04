/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.applicationpdf;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import javax.inject.Named;
import java.io.IOException;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author vince
 */
@Named(value = "pdfReader")
@RequestScoped
public class DisplayPDF {
    String document = "E:\\EPSI\\B3\\PLANNING_B3 19-20 CDA.pdf";
    PdfDocument pdfDocument;
    PdfReader readerFile = new PdfReader(this.document);

    /**
     * Creates a new instance of pdfReader
     * @throws java.io.IOException
     */
    public DisplayPDF() throws IOException {
        this.pdfDocument = new PdfDocument(readerFile);
        pdfDocument.removePage(10);
    }

    public void displayFile() throws IOException {

    }
}
