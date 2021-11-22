package ru.amogus.bot;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import java.awt.image.BufferedImage;

public class BarcodeReader {

    private static final String charset = "UTF-8"; // or "ISO-8859-1";

    public String readBarcode(BufferedImage image)
            throws NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(image)));

        Result barcodeResult = new MultiFormatReader().decode(binaryBitmap);
        return barcodeResult.getText();
    }
}
