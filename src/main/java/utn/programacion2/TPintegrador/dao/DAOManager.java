package utn.programacion2.TPintegrador.dao;

/**
 * Clase que se encarga de crear los DAO y de devolverlos al ServiceManager
 * Funciona como 'singleton' (solo puede existir una unica instancia por ejecucion)
 * Los DAOs independientes solo pueden ser instanciados por el DAOManager
 */
public class DAOManager {

    private final DomicilioFiscalDAO domicilioFiscalDAO;
    private final EmpresaDAO empresaDAO;

    private static DAOManager INSTANCE;

    private DAOManager() {
        this.domicilioFiscalDAO = new DomicilioFiscalDAO();
        this.empresaDAO = new EmpresaDAO(domicilioFiscalDAO);
    }

    public static DAOManager getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new DAOManager();
        }
        return INSTANCE;
    }

    public DomicilioFiscalDAO getDomicilioFiscalDAO() {
        return domicilioFiscalDAO;
    }

    public EmpresaDAO getEmpresaDAO() {
        return empresaDAO;
    }
}
