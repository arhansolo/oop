package ru.amogus.bot;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;

public class BarcodeReader {

    private static final String charset = "UTF-8"; // or "ISO-8859-1";

    public String readBarcode(BufferedImage image)
            throws NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(image)));
        try
        {
            Result barcodeResult = new MultiFormatReader().decode(binaryBitmap);
            return barcodeResult.getText();
        }
        catch (NotFoundException e)
        {
            String result = rotateBitmap(binaryBitmap);
            if (result == null) return new MultiFormatReader().decode(binaryBitmap).getText();
            return result;
        }
    }

    @Nullable
    public String rotateBitmap (BinaryBitmap binaryBitmap) throws NotFoundException {
        int rotate45Count = 0;
        for (int i = 0; i < 4; i++) {
            if (binaryBitmap.isRotateSupported()) {
                binaryBitmap = binaryBitmap.rotateCounterClockwise();
            }
            try {
                Result barcodeResult = new MultiFormatReader().decode(binaryBitmap);
                return barcodeResult.getText();
            }
            catch (NotFoundException e){
                if (rotate45Count>0) continue;
                binaryBitmap = binaryBitmap.rotateCounterClockwise45();
                rotate45Count+=1;

                try {
                    Result barcodeResult = new MultiFormatReader().decode(binaryBitmap);
                    return barcodeResult.getText();
                }
                catch (NotFoundException exception){
                    //
                }
            }
        }
        return null;
    }
}
