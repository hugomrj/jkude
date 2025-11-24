package py.com.jkude.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "factura_detalle")
public class FacturaDetalle extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "factura_id")
    public String facturaId; // CDC (FK)

    public String producto_codigo;
    public String producto_descripcion;
    public String unidad_medida;

    public Double cantidad;
    public Double precio_unitario;


    public Double exenta;
    public Double gravada5;
    public Double gravada10;
    public Double subtotal;
    public Double iva;


    public Double descuento_item;
    public Double porcentaje_descuento;
    public Double descuento_global_item;
    public Double total_bruto_item;
    public Double base_gravada_iva;
    public Double liquidacion_iva_item;
    public Double base_exenta;
    public Integer tasa_iva;
    public String afectacion_iva;
    public String desc_afectacion_iva;
    public Double proporcion_iva;
}