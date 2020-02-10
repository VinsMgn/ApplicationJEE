/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfWorking;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
/**
 *
 * @author gmartin
 */
@Named(value = "imageToPDF")
@Dependent
public class imageToPDF {

    /**
     * Creates a new instance of imageToPDF
     */
    public imageToPDF() {
    }

    public void transformImageToPDF() {
       /* PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
 
        Table table = new Table(UnitValue.createPercentArray(new float[] {10, 90}));
        Image img = new Image(ImageDataFactory.create(IMG));
        table.addCell(img.setAutoScale(true)); 
        doc.add(table);
        doc.close();*/
    }
}
