package py.com.jkude.controller.xml;

import org.jboss.resteasy.reactive.PartType;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;

public class XmlUploadForm {

    @FormParam("file")
    @PartType(MediaType.APPLICATION_XML)
    public byte[] file;
}
