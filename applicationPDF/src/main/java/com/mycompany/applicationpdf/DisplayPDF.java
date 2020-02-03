/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.applicationpdf;

import com.itextpdf.kernel.pdf.PdfReader;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import java.io.IOException;

/**
 *
 * @author vince
 */
@Named(value = "pdfReader")
@Dependent
public class DisplayPDF {

    PdfReader reader;
    String document = "E:\\EPSI\\B3\\PLANNING_B3 19-20 CDA.pdf";

    /**
     * Creates a new instance of pdfReader
     */
    public DisplayPDF() {
    }

    public void displayFile() throws IOException {

    }
}
