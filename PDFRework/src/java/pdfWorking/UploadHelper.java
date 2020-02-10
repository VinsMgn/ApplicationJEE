/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfWorking;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;

/**
 *
 * @author gmartin
 */
@Named(value = "uploadHelper")
@Dependent
public class UploadHelper {

    /**
     * Creates a new instance of UploadHelper
     */
    public UploadHelper() {
    }

    public String getFileNameFromPart(Part part) {
        return part.getSubmittedFileName();
    }

    public String uploadFileOnPath(String endPointPath, Part fileToUpload) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
        String path = servletContext.getRealPath("");
        String fullUploadedPath = null;
        boolean success = false;
        if (fileToUpload != null) {
            if (fileToUpload.getSize() > 0) {
                InputStream inputStream = null;
                OutputStream outputStream = null;
                System.out.println("File uploaded to : " + path + File.separator + "resources/" + endPointPath);
                File outputFile = new File(path + File.separator + "resources/" + endPointPath);
                try {
                    inputStream = fileToUpload.getInputStream();
                    outputStream = new FileOutputStream(outputFile);
                } catch (IOException e) {
                    System.err.println(e);
                }
                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                fullUploadedPath = path + File.separator + "resources/" + endPointPath;
                success = true;
            }
        }
        if (success && fullUploadedPath != null) {
            System.out.println("File uploaded");
            return fullUploadedPath;
        } else {
            System.out.println("Error, no pdf selected!");
            return null;
        }
    }

    public String getContextPath(String fileName) {
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
        String path = servletContext.getRealPath("");

        return path + File.separator + "resources/" + fileName;
    }
}
