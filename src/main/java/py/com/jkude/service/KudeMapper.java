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

        // Fecha emisión
        c.fecha_emision = rDE
                .getJSONObject("gDatGralOpe")
                .optString("dFeEmiDE");

        JSONObject ope = rDE
                .getJSONObject("gDatGralOpe")
                .getJSONObject("gOpeCom");

        c.condicion_operacion = ope.optString("dDesTipTra");
        c.moneda = ope.optString("cMoneOpe"); //

        // Datos del emisor
        JSONObject emisor = rDE
                .getJSONObject("gDatGralOpe")
                .getJSONObject("gEmis");

        c.ruc_emisor = String.valueOf(emisor.optLong("dRucEm"));
        c.dv_emisor = emisor.optInt("dDVEmi");
        c.nombre_emisor = emisor.optString("dNomEmi");
        c.fantasia_emisor = emisor.optString("dNomFanEmi", null);
        c.direccion_emisor = emisor.optString("dDirEmi");
        c.departamento_emisor = emisor.optString("dDesDepEmi", null);
        c.ciudad_emisor = emisor.optString("dDesCiuEmi", null);
        c.telefono_emisor = emisor.optString("dTelEmi");
        c.email_emisor = emisor.optString("dEmailE");

        // TIMBRADO
        JSONObject timb = rDE.getJSONObject("gTimb");

        c.numero_timbrado = timb.optInt("dNumTim");
        c.establecimiento = timb.optString("dEst");
        c.punto_expedicion = timb.optString("dPunExp");
        c.numero_documento = timb.optString("dNumDoc");
        c.fecha_inicio_vigencia = timb.optString("dFeIniT");



        // Datos del receptor / cliente
        JSONObject rec = rDE
                .getJSONObject("gDatGralOpe")
                .getJSONObject("gDatRec");

        c.ruc_receptor = rec.optInt("dRucRec", 0);
        c.dv_receptor = rec.optInt("dDVRec", 0);
        c.nombre_receptor = rec.optString("dNomRec", null);
        c.email_receptor = rec.optString("dEmailRec", null);


        // Totales
        JSONObject tot = rDE.getJSONObject("gTotSub");


        c.total_iva       = tot.optDouble("dTotIVA");
        c.total_monto     = tot.optDouble("dTotGralOpe");
        c.total_exenta    = tot.optDouble("dSubExe");

        // ELIMINAR estos duplicados - usaremos solo sub_exenta5 y sub_exenta10
        // c.total_gravada10 = tot.optDouble("dSub10");  // ← COMENTAR O ELIMINAR
        // c.total_gravada5  = tot.optDouble("dSub5");   // ← COMENTAR O ELIMINAR

        // Usar solo estos campos (aunque los nombres sean confusos)
        c.sub_exenta5        = tot.optDouble("dSub5", 0);      // En realidad es Gravada 5%
        c.sub_exenta10       = tot.optDouble("dSub10", 0);     // En realidad es Gravada 10%

        // Resto de campos (están correctos)
        c.total_operacion    = tot.optDouble("dTotOpe", 0);
        c.total_descuento    = tot.optDouble("dTotDesc", 0);
        c.total_desc_glotem  = tot.optDouble("dTotDescGlotem", 0);
        c.total_anticipo_item= tot.optDouble("dTotAntItem", 0);
        c.total_anticipo     = tot.optDouble("dTotAnt", 0);
        c.porc_desc_total    = tot.optDouble("dPorcDescTotal", 0);
        c.desc_total         = tot.optDouble("dDescTotal", 0);
        c.anticipo           = tot.optDouble("dAnticipo", 0);
        c.redondeo           = tot.optDouble("dRedon", 0);
        c.iva5               = tot.optDouble("dIVA5", 0);
        c.iva10              = tot.optDouble("dIVA10", 0);
        c.total_base_iva     = tot.optDouble("dTBasGraIVA", 0);


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

        // Datos básicos del producto
        d.producto_codigo = itemObj.optString("dCodInt");
        d.producto_descripcion = itemObj.optString("dDesProSer");
        d.unidad_medida = itemObj.optString("dDesUniMed");
        d.cantidad = itemObj.optDouble("dCantProSer");

        // ValorItem
        JSONObject val = itemObj.getJSONObject("gValorItem");
        d.precio_unitario = val.optDouble("dPUniProSer");
        d.total_bruto_item = val.optDouble("dTotBruOpeItem");

        // ValorRestaItem
        JSONObject valorResta = val.getJSONObject("gValorRestaItem");
        d.descuento_item = valorResta.optDouble("dDescItem");
        d.porcentaje_descuento = valorResta.optDouble("dPorcDesIt");
        d.descuento_global_item = valorResta.optDouble("dDescGloItem");
        d.subtotal = valorResta.optDouble("dTotOpeItem");

        // IVA datos
        JSONObject iva = itemObj.getJSONObject("gCamIVA");
        d.afectacion_iva = iva.optString("iAfecIVA");
        d.desc_afectacion_iva = iva.optString("dDesAfecIVA");
        d.proporcion_iva = iva.optDouble("dPropIVA");
        d.tasa_iva = iva.optInt("dTasaIVA");
        d.base_gravada_iva = iva.optDouble("dBasGravIVA");
        d.liquidacion_iva_item = iva.optDouble("dLiqIVAItem");
        d.base_exenta = iva.optDouble("dBasExe");

        // Determinar exenta/gravada5/gravada10 según la tasa de IVA
        if (d.tasa_iva == 5) {
            d.gravada5 = d.subtotal;
            d.gravada10 = 0.0;
            d.exenta = 0.0;
        } else if (d.tasa_iva == 10) {
            d.gravada10 = d.subtotal;
            d.gravada5 = 0.0;
            d.exenta = 0.0;
        } else {
            // Para tasa 0 o cualquier otra (exenta)
            d.exenta = d.subtotal;
            d.gravada5 = 0.0;
            d.gravada10 = 0.0;
        }

        d.iva = d.liquidacion_iva_item;

        return d;
    }

}
