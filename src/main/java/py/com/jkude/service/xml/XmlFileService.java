package py.com.jkude.service.xml;

import jakarta.enterprise.context.ApplicationScoped;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@ApplicationScoped
public class XmlFileService {

    public void guardarXmlEnArchivo(String xml, JSONObject json) {
        try {
            String id = extraerIdDelJson(json);

            if (id == null || id.isEmpty()) {
                System.err.println("No se pudo encontrar el ID en el JSON");
                return;
            }

            Path resourcesPath = Paths.get("src/main/resources");
            Path xmlFolder = resourcesPath.resolve("xml");
            Files.createDirectories(xmlFolder);

            Path xmlFile = xmlFolder.resolve(id + ".xml");
            Files.write(xmlFile, xml.getBytes(StandardCharsets.UTF_8));

            System.out.println("XML guardado en: " + xmlFile.toString());

        } catch (Exception e) {
            System.err.println("Error al guardar el XML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String extraerIdDelJson(JSONObject json) {
        String id = null;

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
        } else if (json.has("rDE")) {
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