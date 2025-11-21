package py.com.jkude.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.json.JSONObject;
import org.json.JSONArray;
import py.com.jkude.model.FacturaCabecera;
import py.com.jkude.model.FacturaDetalle;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class KudeMapper {

    public FacturaCabecera mapCabecera(JSONObject json) {

        FacturaCabecera cab = new FacturaCabecera();
        cab.id = json.query("/rLoteDE/rDE/DE/Id").toString();

        JSONObject rDE = json
                .getJSONObject("rLoteDE")
                .getJSONObject("rDE")
                .getJSONObject("DE");

        FacturaCabecera c = new FacturaCabecera();

        // ID único de la factura
        c.id = rDE.optString("Id");

        /*
        // Fecha emisión
        c.fechaEmision = rDE
                .getJSONObject("gDatGralOpe")
                .optString("dFeEmiDE");

        // Datos del emisor
        JSONObject emis = rDE
                .getJSONObject("gDatGralOpe")
                .getJSONObject("gEmis");

        c.rucEmisor = emis.optLong("dRucEm");
        c.dvEmisor = emis.optInt("dDVEmi");
        c.nombreEmisor = emis.optString("dNomEmi");
        c.direccionEmisor = emis.optString("dDirEmi");
        c.telefonoEmisor = emis.optString("dTelEmi");
        c.emailEmisor = emis.optString("dEmailE");

        // Datos del receptor / cliente
        JSONObject rec = rDE
                .getJSONObject("gDatGralOpe")
                .getJSONObject("gDatRec");

        c.nombreCliente = rec.optString("dNomRec");
        c.rucCliente = rec.optLong("dRucRec");
        c.dvCliente = rec.optInt("dDVRec");
        c.emailCliente = rec.optString("dEmailRec");

        // Totales
        JSONObject tot = rDE.getJSONObject("gTotSub");

        c.total = tot.optDouble("dTotGralOpe");
        c.totalIva = tot.optDouble("dTotIVA");
        c.totalExenta = tot.optDouble("dSubExe");
        c.totalGrav10 = tot.optDouble("dSub10");
*/
        return c;
    }


    public List<FacturaDetalle> mapDetalles(JSONObject json) {

        JSONObject rDE = json
                .getJSONObject("rLoteDE")
                .getJSONObject("rDE")
                .getJSONObject("DE");

        List<FacturaDetalle> lista = new ArrayList<>();

        // Según el XML, gCamItem puede ser:
        // - un solo objeto
        // - o un array de items
        Object rawItem = rDE
                .getJSONObject("gDtipDE")
                .opt("gCamItem");

        if (rawItem instanceof JSONObject itemObj) {
            lista.add(mapDetalle(itemObj));
        }
        else if (rawItem instanceof JSONArray array) {
            for (int i = 0; i < array.length(); i++) {
                lista.add(mapDetalle(array.getJSONObject(i)));
            }
        }

        return lista;
    }


    private FacturaDetalle mapDetalle(JSONObject itemObj) {

        FacturaDetalle d = new FacturaDetalle();
/*
        d.codigo = itemObj.optInt("dCodInt");
        d.descripcion = itemObj.optString("dDesProSer");
        d.cantidad = itemObj.optDouble("dCantProSer");
        d.unidadMedida = itemObj.optString("dDesUniMed");

        // valores del item
        JSONObject val = itemObj.getJSONObject("gValorItem");

        d.precioUnitario = val.optDouble("dPUniProSer");

        JSONObject iva = itemObj.getJSONObject("gCamIVA");

        d.gravadaIva10 = iva.optDouble("dBasGravIVA");
        d.iva10 = iva.optDouble("dLiqIVAItem");

        d.totalItem = val
                .getJSONObject("gValorRestaItem")
                .optDouble("dTotOpeItem");
*/
        return d;
    }
}
