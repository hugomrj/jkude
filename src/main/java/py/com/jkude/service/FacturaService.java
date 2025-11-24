package py.com.jkude.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import py.com.jkude.model.FacturaCabecera;
import py.com.jkude.repository.FacturaRepository;
import py.com.jkude.util.PagedUtil;

import java.util.List;

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

}