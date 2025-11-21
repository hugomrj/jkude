package py.com.jkude.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;


@Entity
@Table(name = "factura_cabecera")
public class FacturaCabecera extends PanacheEntityBase {

    @Id
    @Column(name = "id")
    public String id;   // CDC

    public String ruc_emisor;
    public Integer dv_emisor;
    public String nombre_emisor;
    public String fantasia_emisor;
    public String direccion_emisor;
    public String departamento_emisor;
    public String ciudad_emisor;
    public String telefono_emisor;
    public String email_emisor;

    public Integer numero_timbrado;
    public String establecimiento;
    public String punto_expedicion;
    public String numero_documento;
    public String fecha_inicio_vigencia;

    public String fecha_emision;
    public String condicion_operacion;
    public String moneda;

    public Integer ruc_receptor;
    public Integer dv_receptor;
    public String nombre_receptor;
    public String email_receptor;

    public Double total_iva;
    public Double total_monto;
    public Double total_gravada10;
    public Double total_exenta;
    public Double total_gravada5;
}
