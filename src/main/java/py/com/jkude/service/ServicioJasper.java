package py.com.jkude.service;

import jakarta.enterprise.context.ApplicationScoped;
import net.sf.jasperreports.engine.*;
import javax.sql.DataSource;
import jakarta.inject.Inject;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class ServicioJasper {

    @Inject
    DataSource dataSource;

    public byte[] generarKude(String cdc) {

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("cdc", cdc);
            System.out.println("CDC :   "  + cdc);


            // RUTA BASE COMO URL (IMPORTANTE)
            String reportPath = getClass().getResource("/reports").toString();
            params.put("report_path", reportPath);

            // QR COMO URL
            String qrPath = getClass().getResource("/reports/qr/qr.png").toString();
            params.put("qrFilePath", qrPath);

            // CARGAR JRXML (ruta correcta)
            InputStream jrxml = getClass().getResourceAsStream("/reports/kude.jrxml");
            if (jrxml == null) {
                throw new RuntimeException("No se encontró kude.jrxml");
            }

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
