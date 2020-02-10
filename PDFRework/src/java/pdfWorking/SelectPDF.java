/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfWorking;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author gmartin
 */
@Named(value = "selectPDF")
@SessionScoped
public class SelectPDF implements Serializable {

    private String pagesNumber;
    private String fileName;

    public String getPagesNumber() {
        return pagesNumber;
    }

    public void setPagesNumber(String pagesNumber) {
        this.pagesNumber = pagesNumber;
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
     * Creates a new instance of SelectPDF
     */
    public SelectPDF() {
    }

    public void selectPages() throws FileNotFoundException, IOException {
        if (this.pagesNumber != null && !this.pagesNumber.equals("")) {
            String[] list = this.pagesNumber.split(" ");
            ArrayList<Integer> integerList = new ArrayList<>();
            UploadHelper uploadHelper = new UploadHelper();
            String filePath = uploadHelper.getContextPath(this.getFileName());
            PdfReader reader = new PdfReader(filePath);
            File file = new File(filePath);
            PdfDocument document = new PdfDocument(reader);
            int nbOfPages = document.getNumberOfPages();
            
            for(int i = 0; i < list.length; i++){
                integerList.add(Integer.parseInt(list[i]));
            }
            
            if (list.length == 0) {
                System.out.println("Saisissez quelque chose");
            } else if (integerList.size() < nbOfPages) {
                PdfWriter writer = new PdfWriter(filePath.replace(".pdf", "-copy.pdf"));
                try (PdfDocument destination = new PdfDocument(writer)) {
                    document.copyPagesTo(integerList, destination);
                    destination.close();

                } catch (Exception e) {
                    System.out.println(e);
                }
                document.close();

                File copiedFile = new File(filePath.replace(".pdf", "-copy.pdf"));
                file.delete();
                File fileRenamed = new File(filePath);
                copiedFile.renameTo(fileRenamed);
            } else {
                System.out.println("Vous avez selectionné TROP de pages");

            }

        }

    }

}
