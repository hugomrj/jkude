package py.com.jkude.controller.xml;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.json.XML;

@Path("/xml")
public class XmlToJsonResource {

    @POST
    @Path("/convert")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    public String convertXmlToJson(String xml) {
        return XML.toJSONObject(xml).toString(4);
    }
}
