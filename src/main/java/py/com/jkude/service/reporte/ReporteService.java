package py.com.jkude.service.reporte;

import jakarta.enterprise.context.ApplicationScoped;
import net.sf.jasperreports.engine.*;
import javax.sql.DataSource;
import jakarta.inject.Inject;
import py.com.jkude.util.QRCodeGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class ReporteService {

    @Inject
    DataSource dataSource;

    public byte[] generarKude(String cdc) {

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("cdc", cdc);

            // RUTA BASE ABSOLUTA
            String basePath = new File("reports").getAbsolutePath();
            params.put("report_path", basePath);


            // QR ABSOLUTO
            /*
            String qrPath = basePath + "/qr/qr.png";
            params.put("qrFilePath", qrPath);
            */

            // GENERAR QR DINÁMICO
            String qrData = "https://ekuatia.set.gov.py/consultas/qr?Id=" + cdc;

            // Carpeta donde guardar el PNG
            String qrFolder = basePath + "/qr";

            // Nombre final del archivo
            String qrFileName = cdc + ".png";

            // Generar QR usando tu utilidad
            String qrAbsolutePath = QRCodeGenerator.generateQRCode(
                    qrData,
                    qrFolder,
                    qrFileName
            );

            // Pasamos la ruta generada al Jasper
            params.put("qrFilePath", qrAbsolutePath);




            // JRXML ABSOLUTO
            File jrxmlFile = new File(basePath + "/kude.jrxml");
            if (!jrxmlFile.exists()) {
                throw new RuntimeException("No se encontró kude.jrxml en: " + jrxmlFile.getAbsolutePath());
            }
            InputStream jrxml = new FileInputStream(jrxmlFile);

            // Obtener conexión real de Quarkus + SQLite
            Connection conn = dataSource.getConnection();

            JasperReport reporte = JasperCompileManager.compileReport(jrxml);

            JasperPrint print = JasperFillManager.fillReport(
                    reporte,
                    params,
                    conn
            );

            return JasperExportManager.exportReportToPdf(print);

        } catch (Exception e) {
            throw new RuntimeException("Error generando KUDE", e);
        }
    }
}
