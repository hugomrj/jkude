package py.com.jkude.controller.kude;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


import py.com.jkude.service.KudeService;


@Path("/kude")
@Transactional
public class KudeResource {

    @Inject
    KudeService kudeService;

    @POST
    @Path("/pdf")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces("application/pdf")
    public Response procesarFactura(String xml) {
        return kudeService.procesarDesdeXml(xml);
    }


    @GET
    @Path("/cdc/{cdc}")
    @Produces("application/pdf")
    public Response generarPorCdc(@PathParam("cdc") String cdc) {
        return kudeService.generarPorCdc(cdc);
    }
}
