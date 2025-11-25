package py.com.jkude.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;
import org.json.XML;
import py.com.jkude.model.FacturaCabecera;
import py.com.jkude.model.FacturaDetalle;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@ApplicationScoped
public class KudeService {

    @Inject
    KudeMapper mapper;

    @Inject
    ServicioJasper servicioJasper;

    public Response procesarDesdeXml(String xml) {

        JSONObject json = XML.toJSONObject(xml);
        guardarXmlEnArchivo(xml, json);

        FacturaCabecera cab = mapper.mapCabecera(json);
        List<FacturaDetalle> detalles = mapper.mapDetalles(json);

        if (cab.id != null) {
            FacturaDetalle.delete("facturaId", cab.id);
            FacturaCabecera.deleteById(cab.id);
        }

        cab.persist();

        for (FacturaDetalle det : detalles) {
            det.facturaId = cab.id;
            det.persist();
        }

        byte[] pdf = servicioJasper.generarKude(cab.id);

        return Response.ok(pdf)
                .header("Content-Disposition", "inline; filename=kude.pdf")
                .build();
    }




    public Response generarPorCdc(String cdc) {
        FacturaCabecera cab = FacturaCabecera.findById(cdc);

        if (cab == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("El CDC no existe")
                    .build();
        }

        byte[] pdf = servicioJasper.generarKude(cdc);

        return Response.ok(pdf)
                .header("Content-Disposition",
                        "inline; filename=factura-" + cdc + ".pdf")
                .build();
    }


    private void guardarXmlEnArchivo(String xml, JSONObject json) {
        try {
            // Extraer el ID del JSON - maneja ambas estructuras
            String id = extraerIdDelJson(json);

            if (id == null || id.isEmpty()) {
                System.err.println("No se pudo encontrar el ID en el JSON");
                return;
            }

            // Obtener el path de resources
            Path resourcesPath = Paths.get("src/main/resources");

            // Crear la carpeta xml si no existe
            Path xmlFolder = resourcesPath.resolve("xml");
            Files.createDirectories(xmlFolder);

            // Crear el archivo con el nombre del ID
            Path xmlFile = xmlFolder.resolve(id + ".xml");

            // Escribir el contenido XML (sobrescribe si ya existe)
            Files.write(xmlFile, xml.getBytes(StandardCharsets.UTF_8));

            System.out.println("XML guardado en: " + xmlFile.toString());

        } catch (Exception e) {
            System.err.println("Error al guardar el XML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String extraerIdDelJson(JSONObject json) {
        String id = null;

        // Primero buscar en estructura con lote: rLoteDE > rDE > DE
        if (json.has("rLoteDE")) {
            JSONObject rLoteDE = json.optJSONObject("rLoteDE");
            if (rLoteDE != null) {
                JSONObject rDE = rLoteDE.optJSONObject("rDE");
                if (rDE != null) {
                    JSONObject DE = rDE.optJSONObject("DE");
                    if (DE != null) {
                        id = DE.optString("Id");
                    }
                }
            }
        }
        // Si no encontró, buscar en estructura directa: rDE > DE
        else if (json.has("rDE")) {
            JSONObject rDE = json.optJSONObject("rDE");
            if (rDE != null) {
                JSONObject DE = rDE.optJSONObject("DE");
                if (DE != null) {
                    id = DE.optString("Id");
                }
            }
        }

        return id;
    }





}
