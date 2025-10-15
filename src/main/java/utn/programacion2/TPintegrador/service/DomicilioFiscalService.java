package utn.programacion2.TPintegrador.service;

import utn.programacion2.TPintegrador.config.DatabaseConnection;
import utn.programacion2.TPintegrador.dao.DomicilioFiscalDAO;
import utn.programacion2.TPintegrador.entities.DomicilioFiscal;

import java.sql.Connection;

public class DomicilioFiscalService extends AbstractService<DomicilioFiscal> {

    private final DomicilioFiscalDAO domicilioFiscalDAO;

    public DomicilioFiscalService() {
        super(new DomicilioFiscalDAO());
        this.domicilioFiscalDAO = new DomicilioFiscalDAO();
    }

    @Override
    public DomicilioFiscal insertar(DomicilioFiscal domicilio)  {
        validarDatos(domicilio);
        try (Connection conn = DatabaseConnection.conectarDB()) {
            conn.setAutoCommit(false);
            try {
                DomicilioFiscal creada = domicilioFiscalDAO.crear(domicilio, conn);
                conn.commit();
                return creada;
            } catch (Exception ex) {
                conn.rollback();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            System.out.println("Error al insertar domicilio fiscal: " + e.getMessage());
        }
        return null;
    }

    @Override
    public DomicilioFiscal actualizar(DomicilioFiscal domicilio) {
        if (domicilio == null || domicilio.getId() == null) {
            throw new IllegalArgumentException("El domicilio y su ID no pueden ser null");
        }

        validarDatos(domicilio);

        try (Connection conn = DatabaseConnection.conectarDB()) {
            conn.setAutoCommit(false);
            try {
                domicilioFiscalDAO.actualizar(domicilio, conn);
                conn.commit();
                return domicilio;
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            System.out.println("Error al actualizar domicilio fiscal: " + e.getMessage());
        }
        return null;
    }

    /**
     * Realiza validaciones de negocio y formato sobre el domicilio.
     */
    private void validarDatos(DomicilioFiscal df)  {
        if (null == df)
            throw new IllegalArgumentException("El domicilio no puede ser null");

        if (null == df.getCalle() || df.getCalle().isBlank())
            throw new IllegalArgumentException("La calle es obligatoria");

        if (null == df.getCiudad() || df.getCiudad().isBlank())
            throw new IllegalArgumentException("La ciudad es obligatoria");

        if (null == df.getProvincia() || df.getProvincia().isBlank())
            throw new IllegalArgumentException("La provincia es obligatoria");

        if (null == df.getPais() || df.getPais().isBlank())
            throw new IllegalArgumentException("El país es obligatorio");

        if (null != df.getCodigoPostal() && df.getCodigoPostal().length() > 8)
            throw new IllegalArgumentException("El código postal debe tener 8 caracteres como maximo");
    }
}
