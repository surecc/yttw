package com.karoliinaoksanen.qrreader;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by surecc on 6/13/16.
 */
public class Pdf2IMG {
    public static void main(String[] args) {
        System.out.println("test");
//        pdf2img("/Users/surecc/Desktop/1.pdf");

//        pdf2img2("/Users/surecc/Desktop/1.pdf");
        getDestImgName("/Users/surecc/Documents/Program/ytty/建2所/b第1份/000001.pdf");
    }

    public static String pdf2img(String sourcePdf) {
        try{
//            PDDocument pdDocument = new PDDocument(sourcePdf);
            File fileImg = new File(sourcePdf);
            if (fileImg.isFile()){
                String sourcePdfPath = fileImg.getParent();
                String sourcePdfName = fileImg.getName();
                System.out.println(sourcePdfPath + "\n" + sourcePdfName);
            }
            return sourcePdf;
//            pdDocument.save();
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
        }
        finally {
            return null;
        }
    }

    public static String getDestImgName(String sourceName) {
        try {
            File fileImg = new File(sourceName);
            if (fileImg.isAbsolute()){
                String sourcePdfPath = fileImg.getParent();
                String sourcePdfName = fileImg.getName();

                String destImgName = sourcePdfPath + File.separator + sourcePdfName + ".jpg";
                System.out.println(destImgName);
                return destImgName;
            }
            else {
                System.out.println("[ERROR]Can't Read the Pdf Filename!");
                return null;
            }
        }
        catch (Exception ex){
            System.out.println(ex.toString());
            return null;
        }
    }

    public static String pdf2img2(String sourcePdf) {
        try {
            PDDocument doc = PDDocument.load(sourcePdf);
            List pages = doc.getDocumentCatalog().getAllPages();

            String imgName = getDestImgName(sourcePdf);
            for(int i=0;i<pages.size();i++){
                PDPage page = (PDPage)pages.get(i);
                BufferedImage image = page.convertToImage();
                Iterator iter = ImageIO.getImageWritersBySuffix("jpg");
                ImageWriter writer = (ImageWriter)iter.next();

                File outFile = new File(imgName);
                FileOutputStream out = new FileOutputStream(outFile);
                ImageOutputStream outImage = ImageIO.createImageOutputStream(out);
                writer.setOutput(outImage);
                writer.write(new IIOImage(image,null,null));
            }
            doc.close();
            System.out.println("[Change PDF2IMG]" + imgName);
            return imgName;
        }
        catch (IOException ex){
            System.out.println(ex.toString());
            return null;
        }
    }

}