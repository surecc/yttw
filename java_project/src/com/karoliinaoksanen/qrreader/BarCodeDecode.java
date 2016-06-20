package com.karoliinaoksanen.qrreader;

/**
 * Created by surecc on 6/16/16.
 */
//import com.sun.java.util.jar.pack.Package.File;



public class BarCodeDecode
{
    /**
     * @param args
     */
//    public static void main(String[] args)
//    {
//        try
//        {
//            String tmpImgFile = "D:\\FormCode128.TIF";
//
//            Map<DecodeHintType,Object> tmpHintsMap = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
//            tmpHintsMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
//            tmpHintsMap.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.allOf(BarcodeFormat.class));
//            tmpHintsMap.put(DecodeHintType.PURE_BARCODE, Boolean.FALSE);
//
//            File tmpFile = new File(tmpImgFile);
//            String tmpRetString = BarCodeUtil.decode(tmpFile, tmpHintsMap);
//            //String tmpRetString = BarCodeUtil.decode(tmpFile, null);
//            System.out.println(tmpRetString);
//        }
//        catch (Exception tmpExpt)
//        {
//            System.out.println("main: " + "Excpt err! (" + tmpExpt.getMessage() + ")");
//        }
//        System.out.println("main: " + "Program end.");
//    }

}

class BarCodeUtil
{
//    private static BarcodeFormat DEFAULT_BARCODE_FORMAT = BarcodeFormat.CODE_128;
//
//    /**
//     * Decode method used to read image or barcode itself, and recognize the barcode,
//     * get the encoded contents and returns it.
//     * @param whatFile image that need to be read.
//     * @param config configuration used when reading the barcode.
//     * @return decoded results from barcode.
//     */
//    public static String decode(File whatFile, Map<DecodeHintType, Object> whatHints) throws Exception
//    {
//        // check the required parameters
//        if (whatFile == null || whatFile.getName().trim().isEmpty())
//            throw new IllegalArgumentException("File not found, or invalid file name.");
//        BufferedImage tmpBfrImage;
//        try
//        {
//            tmpBfrImage = ImageIO.read(whatFile);
//        }
//        catch (IOException tmpIoe)
//        {
//            throw new Exception(tmpIoe.getMessage());
//        }
//        if (tmpBfrImage == null)
//            throw new IllegalArgumentException("Could not decode image.");
//        LuminanceSource tmpSource = new BufferedImageLuminanceSource(tmpBfrImage);
//        BinaryBitmap tmpBitmap = new BinaryBitmap(new HybridBinarizer(tmpSource));
//        MultiFormatReader tmpBarcodeReader = new MultiFormatReader();
//        Result tmpResult;
//        String tmpFinalResult;
//        try
//        {
//            if (whatHints != null && ! whatHints.isEmpty())
//                tmpResult = tmpBarcodeReader.decode(tmpBitmap, whatHints);
//            else
//                tmpResult = tmpBarcodeReader.decode(tmpBitmap);
//            // setting results.
//            tmpFinalResult = String.valueOf(tmpResult.getText());
//        }
//        catch (Exception tmpExcpt)
//        {
//            throw new Exception("BarCodeUtil.decode Excpt err - " + tmpExcpt.toString() + " - " + tmpExcpt.getMessage());
//        }
//        return tmpFinalResult;
//    }
}

