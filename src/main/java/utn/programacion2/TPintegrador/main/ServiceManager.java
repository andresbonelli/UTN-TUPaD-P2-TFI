package utn.programacion2.TPintegrador.main;

import utn.programacion2.TPintegrador.dao.DomicilioFiscalDAO;
import utn.programacion2.TPintegrador.dao.EmpresaDAO;
import utn.programacion2.TPintegrador.service.DomicilioFiscalService;
import utn.programacion2.TPintegrador.service.EmpresaService;

public class ServiceManager {

    private static ServiceManager INSTANCE;

    private final DomicilioFiscalDAO domicilioFiscalDAO;
    private final EmpresaDAO empresaDAO;
    private final DomicilioFiscalService domicilioFiscalService;
    private final EmpresaService empresaService;

    private ServiceManager() {
        // Inicialización de servicios con inyeccion de dependencias
        this.domicilioFiscalDAO = new DomicilioFiscalDAO();
        this.empresaDAO = new EmpresaDAO(domicilioFiscalDAO);
        this.domicilioFiscalService = new DomicilioFiscalService(domicilioFiscalDAO);
        this.empresaService = new EmpresaService(empresaDAO, domicilioFiscalService);
    }

    public static ServiceManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServiceManager();
        }
        return INSTANCE;
    }

    public DomicilioFiscalService getDomicilioFiscalService() {
        return domicilioFiscalService;
    }

    public EmpresaService getEmpresaService() {
        return empresaService;
    }
}
