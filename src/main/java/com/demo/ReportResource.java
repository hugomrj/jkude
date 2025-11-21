package com.demo;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import net.sf.jasperreports.engine.*;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Path("/reportes")
public class ReportResource {

    @GET
    @Path("/pdf")
    @Produces("application/pdf")
    public Response generar() throws Exception {

        InputStream jrxml = getClass().getResourceAsStream("/reports/report.jrxml");

        JasperReport report = JasperCompileManager.compileReport(jrxml);

        Map<String, Object> params = new HashMap<>();
        params.put("mensaje", "Hola desde Jasper!");

        JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource());

        byte[] pdf = JasperExportManager.exportReportToPdf(print);

        return Response.ok(pdf)
                .header("Content-Disposition", "inline; filename=reporte.pdf")
                .build();
    }
}
