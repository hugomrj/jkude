package py.com.jkude.controller.kude;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;
import org.json.XML;
import py.com.jkude.model.FacturaCabecera;
import py.com.jkude.model.FacturaDetalle;
import py.com.jkude.service.KudeMapper;
import py.com.jkude.service.ServicioJasper;

import java.util.List;

@Path("/kude/pdf")
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
        //byte[] pdf = new byte[0];
        byte[] pdf = servicioJasper.generarKude(cab.id);

        // 6) Respuesta PDF
        return Response.ok(pdf)
                .header("Content-Disposition", "inline; filename=kude.pdf")
                .build();
    }


    @GET
    @Path("/{cdc}")
    @Produces("application/pdf")
    public Response generarPorCdc(@PathParam("cdc") String cdc) {

        // 1) Buscar cabecera ya existente
        FacturaCabecera cab = FacturaCabecera.findById(cdc);

        if (cab == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("El CDC no existe")
                    .build();
        }

        // 2) Buscar detalles asociados
        List<FacturaDetalle> detalles = FacturaDetalle.list("facturaId", cdc);

        // 3) Generar el PDF usando el CDC
        byte[] pdf = servicioJasper.generarKude(cdc);

        // 4) Devolverlo
        return Response.ok(pdf)
                .header("Content-Disposition",
                        "inline; filename=factura-" + cdc + ".pdf")
                .build();
    }

}