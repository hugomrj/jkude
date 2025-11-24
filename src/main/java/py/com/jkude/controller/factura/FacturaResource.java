package py.com.jkude.controller.factura;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import py.com.jkude.model.FacturaCabecera;
import py.com.jkude.service.FacturaService;
import py.com.jkude.util.PagedUtil;

import java.util.List;

@Path("/api/facturas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FacturaResource {

    @Inject
    FacturaService service;

    @GET
    public Response listPaged(@QueryParam("page") @DefaultValue("1") int page,
                              @QueryParam("size") @DefaultValue("20") int size) {
        PagedUtil<FacturaCabecera> result = service.findPaged(page, size);
        return Response.ok(result).build();
    }

    @GET
    @Path("/recientes")
    public Response getRecientes() {
        List<FacturaCabecera> facturas = service.findRecientes();
        return Response.ok(facturas).build();
    }

    @GET
    @Path("/emisor/{ruc}")
    public Response getByRucEmisor(@PathParam("ruc") String ruc) {
        List<FacturaCabecera> facturas = service.findByRucEmisor(ruc);
        return Response.ok(facturas).build();
    }

    @GET
    @Path("/receptor/{ruc}")
    public Response getByRucReceptor(@PathParam("ruc") Integer ruc) {
        List<FacturaCabecera> facturas = service.findByRucReceptor(ruc);
        return Response.ok(facturas).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        FacturaCabecera factura = service.findById(id);
        if (factura != null) {
            return Response.ok(factura).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Factura no encontrada con ID: " + id)
                    .build();
        }
    }
}