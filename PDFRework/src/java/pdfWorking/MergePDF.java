/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfWorking;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.servlet.http.Part;

/**
 *
 * @author gmartin
 */
@Named(value = "mergePDF")
@SessionScoped
public class MergePDF implements Serializable {

    /**
     * Creates a new instance of MergePDF
     */
    public MergePDF() {
    }

    private Part file;
    private String mergedFilePath;
    private String fileToMergePath;
    private String storedUrlParam;

    public void onload(String urlParam) {
        System.out.println("HERERERERERE : "+urlParam);
        UploadHelper uploadHelper = new UploadHelper();
        this.setStoredUrlParam(urlParam);
        String filePath = uploadHelper.getContextPath(urlParam);
        this.setMergedFilePath(filePath);
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public String getMergedFilePath() {
        return mergedFilePath;
    }

    public void setMergedFilePath(String mergedFilePath) {
        this.mergedFilePath = mergedFilePath;
    }

    public String getStoredUrlParam() {
        return storedUrlParam;
    }

    public void setStoredUrlParam(String storedUrlParam) {
        this.storedUrlParam = storedUrlParam;
    }

    public String getFileToMergePath() {
        return fileToMergePath;
    }

    public void setFileToMergePath(String filePath) {
        this.fileToMergePath = filePath;
    }

    @PostConstruct
    public void init() {
    }

    public void upload() throws IOException {
        if (file != null) {
            System.out.println("HELLO");
            UploadHelper uploadHelper = new UploadHelper();
            String fileNameToMerge = uploadHelper.getFileNameFromPart(file).replace(".pdf", "-to-merge.pdf");
            uploadHelper.uploadFileOnPath(fileNameToMerge, file);
            this.setFileToMergePath(uploadHelper.getContextPath(fileNameToMerge));
        }

    }

    public void mergePDF() throws IOException {

        PdfDocument pdf = new PdfDocument(new PdfWriter(this.getMergedFilePath().replace(".pdf", "-temp.pdf")));
        PdfMerger merger = new PdfMerger(pdf);

        //Add pages from the first document
        PdfDocument firstSourcePdf = new PdfDocument(new PdfReader(this.getMergedFilePath()));
        merger.merge(firstSourcePdf, 1, firstSourcePdf.getNumberOfPages());

        //Add pages from the second pdf document
        PdfDocument secondSourcePdf = new PdfDocument(new PdfReader(this.getFileToMergePath()));

        merger.merge(secondSourcePdf, 1, secondSourcePdf.getNumberOfPages());

        firstSourcePdf.close();
        secondSourcePdf.close();
        pdf.close();

        File mergedFile = new File(this.getMergedFilePath());
        File renamedFile = new File(this.getMergedFilePath());
        mergedFile.delete();
        File fileToMerge = new File(this.getFileToMergePath());
        fileToMerge.delete();
        File fileToRename = new File(this.getMergedFilePath().replace(".pdf", "-temp.pdf"));
        fileToRename.renameTo(renamedFile);
    }
}
