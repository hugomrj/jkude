package py.com.jkude.controller.kude;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;
import org.json.XML;
import py.com.jkude.model.FacturaCabecera;
import py.com.jkude.model.FacturaDetalle;
import py.com.jkude.service.KudeMapper;
import py.com.jkude.service.ServicioJasper;

import java.util.List;

@Path("/kude")
@Transactional
public class KudeResource {

    @Inject
    KudeMapper mapper;

    @Inject
    ServicioJasper servicioJasper;

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces("application/pdf")
    public Response procesarFactura(String xml) {

        // 1) XML → JSON
        JSONObject json = XML.toJSONObject(xml);

        // 2) Mapear datos
        FacturaCabecera cab = mapper.mapCabecera(json);
        List<FacturaDetalle> detalles = mapper.mapDetalles(json);

        // EXTRA IMPORTANTE:
        // cab.id YA trae el CDC porque viene mapeado desde el XML.
        // Usamos ese id para eliminar registros anteriores.

        // 0) Eliminar registros previos (cabecera + detalle)
        if (cab.id != null) {
            // Borrar detalles donde facturaId = id del CDC
            FacturaDetalle.delete("facturaId", cab.id);

            // Borrar cabecera con ese id
            FacturaCabecera.deleteById(cab.id);
        }

        // 3) Guardar cabecera nueva
        cab.persist();

        // 4) Guardar detalles nuevos
        for (FacturaDetalle det : detalles) {
            det.facturaId = cab.id;  // asignar FK
            det.persist();
        }

        // 5) Generar PDF (por ahora vacío)
        byte[] pdf = new byte[0];
        // byte[] pdf = servicioJasper.generarKude(cab.id);

        // 6) Respuesta PDF
        return Response.ok(pdf)
                .header("Content-Disposition", "inline; filename=kude.pdf")
                .build();
    }
}