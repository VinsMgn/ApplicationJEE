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
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author gmartin
 */
@Named(value = "reorderPDF")
@SessionScoped
public class reorderPDF implements Serializable {

    private String pagesNumber;
    private String fileName;
    private Boolean isTooLong = false;
    private Boolean isTooShort = false;

    public String getPagesNumber() {
        return pagesNumber;
    }

    public void setPagesNumber(String pagesNumber) {
        this.pagesNumber = pagesNumber;
    }

    public Boolean getIsTooLong() {
        return isTooLong;
    }

    public void setIsTooLong(Boolean isTooLong) {
        this.isTooLong = isTooLong;
    }

    public Boolean getIsTooShort() {
        return isTooShort;
    }

    public void setIsTooShort(Boolean isTooShort) {
        this.isTooShort = isTooShort;
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
     * Creates a new instance of reorderPDF
     */
    public reorderPDF() {
    }

    public void reorderPages() throws FileNotFoundException, IOException {
                this.setIsTooShort(false);
        this.setIsTooLong(false);

        if (this.pagesNumber != null && !this.pagesNumber.equals("")) {
            String[] list = this.pagesNumber.split(" ");
            ArrayList<Integer> integerList = new ArrayList<>();
            UploadHelper uploadHelper = new UploadHelper();
            String filePath = uploadHelper.getContextPath(this.getFileName());
            PdfReader reader = new PdfReader(filePath);
            File file = new File(filePath);
            PdfDocument document = new PdfDocument(reader);
            int nbOfPages = document.getNumberOfPages();
            // Check if the number of pages selected is not enough big
            if (list.length < nbOfPages) {
                this.setIsTooShort(true);
                return;
            }

            // Check if there is too many pages selected compare to PDF pages number
            if (list.length > nbOfPages) {
                this.setIsTooLong(true);
                return;
            }

            // Check if one of pages doesn't exist + transform strings value to int.
            for (int i = 0; i < list.length; i++) {
                int integer = Integer.parseInt(list[i]);
                if (integer > list.length) {
                    this.setIsTooLong(true);
                    return;
                }
                integerList.add(integer);
            }

            this.doReorder(document, integerList, filePath, file);

        }

    }

    public void doReorder(PdfDocument document, ArrayList<Integer> integerList, String filePath, File file) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(filePath.replace(".pdf", "-copy.pdf"));

        try (PdfDocument destination = new PdfDocument(writer)) {
            document.copyPagesTo(integerList, destination);
            destination.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        document.close();
        this.renameFile(filePath, file);

    }

    public void renameFile(String filePath, File file) {
        File copiedFile = new File(filePath.replace(".pdf", "-copy.pdf"));
        file.delete();
        File fileRenamed = new File(filePath);
        copiedFile.renameTo(fileRenamed);
    }

}
