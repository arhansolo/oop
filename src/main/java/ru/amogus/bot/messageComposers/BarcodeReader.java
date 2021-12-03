package ru.amogus.bot.messageComposers;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;

public class BarcodeReader {

    public static boolean isRotated45 = false;

    public String readBarcode(BufferedImage image) throws NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));

        try {
            Result barcodeResult = new MultiFormatReader().decode(binaryBitmap);
            return barcodeResult.getText();
        }

        catch (NotFoundException e) {
            String result = rotateBitmap(binaryBitmap);

            if (result == null) {
                return new MultiFormatReader().decode(binaryBitmap).getText();
            }

            return result;
        }
    }

    @Nullable
    private String rotateBitmap (BinaryBitmap binaryBitmap) throws NotFoundException {
        for (int i = 0; i < 4; i++) {
            if (binaryBitmap.isRotateSupported()) {
                binaryBitmap = binaryBitmap.rotateCounterClockwise();
            }

            try {
                Result barcodeResult = new MultiFormatReader().decode(binaryBitmap);
                return barcodeResult.getText();
            }

            catch (NotFoundException e){
                if (isRotated45) continue;

                binaryBitmap = binaryBitmap.rotateCounterClockwise45();
                isRotated45 = true;

                try {
                    Result barcodeResult = new MultiFormatReader().decode(binaryBitmap);
                    return barcodeResult.getText();
                }

                catch (NotFoundException ignored){
                }
            }
        }

        return null;
    }
}
