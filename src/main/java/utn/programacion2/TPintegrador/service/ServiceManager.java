package utn.programacion2.TPintegrador.service;

import utn.programacion2.TPintegrador.dao.DomicilioFiscalDAO;
import utn.programacion2.TPintegrador.dao.EmpresaDAO;

/**
 * Clase que inicializa los servicios y gestiona la inyeccion de dependencias.
 * Funciona como 'singleton' (solo puede existir una unica instancia por ejecucion)
 * Los servicios independientes solo pueden ser instanciados por el ServiceManager
 */
public class ServiceManager {

    private static ServiceManager INSTANCE;

    private final DomicilioFiscalService domicilioFiscalService;
    private final EmpresaService empresaService;

    private ServiceManager() {
        var domicilioFiscalDAO = new DomicilioFiscalDAO();
        var empresaDAO = new EmpresaDAO(domicilioFiscalDAO);
        this.domicilioFiscalService = new DomicilioFiscalService(domicilioFiscalDAO);
        this.empresaService = new EmpresaService(empresaDAO, domicilioFiscalService);
    }

    public static ServiceManager getInstance() {
        if (null == INSTANCE) {
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
