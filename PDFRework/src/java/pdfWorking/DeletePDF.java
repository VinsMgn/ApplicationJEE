package pdfWorking;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author vince
 */
@Named(value = "deletePDF")
@SessionScoped
public class DeletePDF implements Serializable {

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

    public DeletePDF() {

    }

    public void deletePages() throws FileNotFoundException, IOException {
        if (this.pagesNumber != null && !this.pagesNumber.equals("")) {
            String[] list = this.pagesNumber.split(" ");
            UploadHelper uploadHelper = new UploadHelper();
            String filePath = uploadHelper.getContextPath(this.getFileName());
            PdfReader reader = new PdfReader(filePath);
            File file = new File(filePath);
            PdfDocument document = new PdfDocument(reader);
            int nbOfPages = document.getNumberOfPages();
            ArrayList<Integer> pageNumbers = this.createPageNumberArray(nbOfPages);
            if (list.length == 0) {
                System.out.println("Saisissez quelque chose");
            } else if (list.length < nbOfPages) {
                for (int i = 0; i < list.length; i++) {
                    int selectedPage = Integer.parseInt(list[i]);
                    if (selectedPage <= nbOfPages) {
                        pageNumbers.remove(pageNumbers.indexOf(selectedPage));

                    } else {
                        System.out.println("Page trop élevée");
                    }
                }
                PdfWriter writer = new PdfWriter(filePath.replace(".pdf", "-copy.pdf"));
                try (PdfDocument destination = new PdfDocument(writer)) {
                    document.copyPagesTo(pageNumbers, destination);
                }
                document.close();
                File copiedFile = new File(filePath.replace(".pdf", "-copy.pdf"));
                file.delete();
                System.out.println("FILE PATH : " + filePath);
                File fileRenamed = new File(filePath);
                copiedFile.renameTo(fileRenamed);
            } else {
                System.out.println("Vous avez selectionné TROP de pages");

            }

            System.out.println(pageNumbers.toString() + pageNumbers.size());

        }

    }
    
    public ArrayList<Integer> createPageNumberArray(int arrayLength) {
        ArrayList<Integer> pageNumbers;
        pageNumbers = new ArrayList<>();
        for (int i = 1; i <= arrayLength; i++) {
            pageNumbers.add(i);
        }
        return pageNumbers;
    }
}
