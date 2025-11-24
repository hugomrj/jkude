package py.com.jkude.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import py.com.jkude.model.FacturaCabecera;

import java.util.List;

@ApplicationScoped
public class FacturaRepository implements PanacheRepository<FacturaCabecera> {

    public List<FacturaCabecera> findByRucEmisor(String ruc) {
        return find("ruc_emisor", ruc).list();
    }

    public List<FacturaCabecera> findByRucReceptor(Integer ruc) {
        return find("ruc_receptor", ruc).list();
    }


    public List<FacturaCabecera> findRecientes() {
        return find("order by fecha_emision desc")
                .page(0, 10)
                .list();
    }

    public FacturaCabecera findById(String id) {
        return find("id", id).firstResult();
    }

}