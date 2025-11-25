package py.com.jkude.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QRCodeGenerator {

    public static String generateQRCode(String data, String folderPath, String fileName) {
        try {
            int width = 400;
            int height = 400;

            // Crear carpeta si no existe
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            Path path = FileSystems.getDefault().getPath(folderPath, fileName);
            BitMatrix matrix = new MultiFormatWriter().encode(
                    data, BarcodeFormat.QR_CODE, width, height
            );

            MatrixToImageWriter.writeToPath(matrix, "PNG", path);

            return path.toString(); // ruta del archivo generado

        } catch (WriterException | IOException e) {
            throw new RuntimeException("Error generando el QR", e);
        }
    }
}
