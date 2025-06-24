package Test;

import Util.QRCodeGenerator;
import com.google.zxing.WriterException;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class QRCodeGeneratorTest {

    @Test
    public void testGenerateQRCodeImage_NotNull() throws WriterException {
        String text = "testtext";
        int width = 150;
        int height = 150;

        BufferedImage qrImage = QRCodeGenerator.generateQRCodeImage(text, width, height);

        assertNotNull(qrImage);
    }
}
