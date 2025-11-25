package py.com.jkude.service.factura;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import py.com.jkude.model.FacturaCabecera;
import py.com.jkude.repository.FacturaRepository;
import py.com.jkude.util.PagedUtil;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class FacturaService {

    @Inject
    FacturaRepository facturaRepository;  // ← Cambia EntityManager por Repository

    // Ya no necesitas @Transactional aquí, el repository lo maneja
    public PagedUtil<FacturaCabecera> findPaged(int page, int size) {
        List<FacturaCabecera> facturas = facturaRepository.findAll()
                .page(page - 1, size)
                .list();

        long total = facturaRepository.count();

        return new PagedUtil<>(facturas, total, size, page);
    }


    public List<FacturaCabecera> findByRucEmisor(String ruc) {
        return facturaRepository.findByRucEmisor(ruc);
    }

    public List<FacturaCabecera> findByRucReceptor(Integer ruc) {
        return facturaRepository.findByRucReceptor(ruc);
    }


    public List<FacturaCabecera> findRecientes() {
        return facturaRepository.findRecientes();
    }

    public FacturaCabecera findById(String id) {
        return facturaRepository.findById(id);
    }


    public List<Map<String, Object>> simple(int page, int size) {

        EntityManager em = facturaRepository.getEntityManager();
        List<Object[]> rows = em.createQuery(
                        """
                        SELECT 
                            f.id, f.fecha_emision, f.total_monto
                        FROM 
                            FacturaCabecera f
                        ORDER BY 
                            f.fecha_emision DESC
                        """,
                        Object[].class
                )
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();


        return rows.stream().map(r -> Map.of(
                "cdc", r[0],
                "fecha_emision", r[1],
                "total_monto", r[2]
        )).toList();
    }


}