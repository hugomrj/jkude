package py.com.jkude.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;
import org.json.XML;
import py.com.jkude.model.FacturaCabecera;
import py.com.jkude.model.FacturaDetalle;

import java.util.List;

@ApplicationScoped
public class KudeService {

    @Inject
    KudeMapper mapper;

    @Inject
    ServicioJasper servicioJasper;

    public Response procesarDesdeXml(String xml) {

        JSONObject json = XML.toJSONObject(xml);

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
}
