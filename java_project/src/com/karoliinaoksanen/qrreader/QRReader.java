package com.karoliinaoksanen.qrreader;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.aztec.decoder.*;
import com.sun.javafx.collections.MappingChange;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by surecc on 6/13/16.
 */
public class QRReader {

    public static void main(String[] args) throws WriterException, IOException,
            NotFoundException {
        String charset = "UTF-8"; // or "ISO-8859-1"
        Map hintMap = new HashMap();
//        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hintMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);


//        createQRCode(qrCodeData, filePath, charset, hintMap, 200, 200);
//        System.out.println("QR Code image created successfully!");

        System.out.println("Data read from QR Code: "
                + readQRCode("/Users/surecc/Desktop/test/7.pdf.jpg", charset, hintMap));

    }

    public static String readQRCode2(String filePath)
        throws IOException {
        Decoder decoder = new Decoder();
        decoder.highLevelDecode();

        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(
                        ImageIO.read(new FileInputStream(filePath)))));
        Result result = decoder.decode(binaryBitmap);
    }

//    public Result decode(BinaryBitmap image,Map hints)
//            throws NotFoundException, ChecksumException, FormatException {
//        Result decoderResult;
//        ResultPoint[] points;
//        if (hints != null && hints.containsKey(DecodeHintType.PURE_BARCODE)) {
//            BitMatrix bits= extractPureBits(image.getBlackMatrix());
//            decoderResult=decoder.decode(bits,hints);
//            points=NO_POINTS;
//        }
//        else {
//            DetectorResult detectorResult=new Detector(image.getBlackMatrix()).detect(hints);
//            decoderResult=decoder.decode(detectorResult.getBits(),hints);
//            points=detectorResult.getPoints();
//        }
//        Result result=new Result(decoderResult.getText(),decoderResult.getRawBytes(),points,BarcodeFormat.QR_CODE);
//        if (decoderResult.getByteSegments() != null) {
//            result.putMetadata(ResultMetadataType.BYTE_SEGMENTS,decoderResult.getByteSegments());
//        }
//        if (decoderResult.getECLevel() != null) {
//            result.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL,decoderResult.getECLevel().toString());
//        }
//        return result;
//    }

    public static void createQRCode(String qrCodeData, String filePath,
                                    String charset, Map hintMap, int qrCodeheight, int qrCodewidth)
            throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(
                new String(qrCodeData.getBytes(charset), charset),
                BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
        MatrixToImageWriter.writeToFile(matrix, filePath.substring(filePath
                .lastIndexOf('.') + 1), new File(filePath));
    }

    public static String readQRCodePub(String filePath)
            throws IOException, NotFoundException {
        String charset = "UTF-8";
        Map hintMap = new HashMap();
        hintMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        return readQRCode(filePath, charset, hintMap);
    }

    private static String readQRCode(String filePath, String charset, Map hintMap)
            throws IOException, NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(
                        ImageIO.read(new FileInputStream(filePath)))));
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,
                hintMap);
        System.out.println(qrCodeResult.getText());
        return qrCodeResult.getText();
    }
}
//
//
///**
// * Created by surecc on 6/13/16.
// */
//public class QRReader {
////    // Recognize QR Code and other barcode symbols from local image file path.
////    public static BarcodeResult[] Scan(String filename);
////    // Scan and read QR Code and other barcode symbols from BufferedImage object in memory.
////    public static BarcodeResult[] Scan(BufferedImage image);
////    // Decode and read QR Code and other barcode symbols from InputStream object.
////    public static BarcodeResult[] Scan(InputStream imageStream);
////    // Define to read and detect QR Code symbol only from local image file path.
////    public static BarcodeResult[] Scan(String filename, BarCodeType barType);
////    // Define to scan and decode QR Code symbol only from BufferedImage object.
////    public static BarcodeResult[] Scan(BufferedImage image, BarCodeType barType);
////    // Define to read and recognize QR Code symbol only from InputStream object.
////    public static BarcodeResult[] Scan(InputStream imageStream, BarCodeType barType);
//    public static void main(String[] args)
//    {
//        try
//        {
//            // Load image source you need, and select to read QR Code only.
//            BarcodeResult[] results = BarcodeScanner.Scan("/Users/surecc/Desktop/qrcode.jpg", BarCodeType.QRCode);
//
//            for(int i = 0; i < results.length; i++)
//            {
//                // Show all decoded QR Code barcode symbol message
//                System.out.println(results[i].getData() + "--" + results[i].getBarType());
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//}
