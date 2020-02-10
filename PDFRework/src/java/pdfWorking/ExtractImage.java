/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfWorking;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.element.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.imageio.ImageIO;

/**
 *
 * @author gmartin
 */
@Named(value = "extractImage")
@SessionScoped
public class ExtractImage implements Serializable {

    /**
     * Creates a new instance of extractImage
     */
    public ExtractImage() {
    }
    private String DEST = "./target/sandbox/parse";

    public String getDEST() {
        return DEST;
    }

    public void setDEST(String DEST) {
        this.DEST = DEST;
    }

    private String SRC = "./src/test/resources/pdfs/image.pdf";

    public String getSRC() {
        return SRC;
    }

    public void setSRC(String SRC) {
        this.SRC = SRC;
    }

    public void onload(String fileName) {
        UploadHelper uploadHelper = new UploadHelper();
        String context = uploadHelper.getContextPath("");
        this.setDEST(context + "tst.pdf");
        this.setSRC(context + fileName);
        //new File(DEST).getParentFile().mkdirs();

    }

    public void extractImageFromPDF() throws IOException {
        //File file = new File(DEST);
        //file.mkdirs();
        this.manipulatePdf();
    }

    protected void manipulatePdf() throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));

        // Assume that there is a single XObject in the source document
        // and this single object is an image.
        PdfDictionary pageDict = pdfDoc.getFirstPage().getPdfObject();
        PdfDictionary resources = pageDict.getAsDictionary(PdfName.Resources);
        PdfDictionary xObjects = resources.getAsDictionary(PdfName.XObject);
        PdfName imgRef = xObjects.keySet().iterator().next();
        PdfStream stream = xObjects.getAsStream(imgRef);
        Image img = convertToBlackAndWhitePng(new PdfImageXObject(stream));
        // Replace the original image with the grayscale image
        xObjects.put(imgRef, img.getXObject().getPdfObject());

        pdfDoc.close();
        /* PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC));
 
        int numberOfPdfObjects = pdfDoc.getNumberOfPdfObjects();
        for (int i = 1; i <= numberOfPdfObjects; i++) {
            PdfObject obj = pdfDoc.getPdfObject(i);
            if (obj != null && obj.isStream()) {
                byte[] b;
                try {
 
                    // Get decoded stream bytes.
                    b = ((PdfStream) obj).getBytes();
                } catch (PdfException exc) {
 
                    // Get originally encoded stream bytes
                    b = ((PdfStream) obj).getBytes(false);
                }
                
                try (FileOutputStream fos = new FileOutputStream(String.format(DEST + "/extract_streams%s.dat", i))) {
                    
                    fos.write(b);
                }
            }
        }
 
        pdfDoc.close();*/

    }

    private Image convertToBlackAndWhitePng(PdfImageXObject image) throws IOException {
        BufferedImage bi = image.getBufferedImage();
        BufferedImage newBi = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_USHORT_GRAY);
        newBi.getGraphics().drawImage(bi, 0, 0, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(newBi, "png", baos);
        return new Image(ImageDataFactory.create(baos.toByteArray()));
    }
}
