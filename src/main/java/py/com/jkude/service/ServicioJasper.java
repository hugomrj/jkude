package py.com.jkude.service;

import jakarta.enterprise.context.ApplicationScoped;
import net.sf.jasperreports.engine.*;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class ServicioJasper {

    public byte[] generarKude(Long idCabecera) {

        try {
            // Parámetros para el reporte
            Map<String, Object> params = new HashMap<>();
            params.put("ID_CAB", idCabecera);

            // Cargar el archivo jasper desde resources
            var stream = getClass().getResourceAsStream("/reportes/kude.jasper");

            JasperPrint print = JasperFillManager.fillReport(
                    stream,
                    params,
                    new JREmptyDataSource() // o tu conexión JDBC
            );

            return JasperExportManager.exportReportToPdf(print);

        } catch (Exception e) {
            throw new RuntimeException("Error generando KUDE", e);
        }
    }
}

