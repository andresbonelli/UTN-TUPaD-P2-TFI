package utn.programacion2.TPintegrador.service;

import utn.programacion2.TPintegrador.config.DatabaseConnection;
import utn.programacion2.TPintegrador.dao.DomicilioFiscalDAO;
import utn.programacion2.TPintegrador.entities.DomicilioFiscal;

import java.sql.SQLException;
import java.util.List;

import java.sql.Connection;

public class DomicilioFiscalService implements GenericService<DomicilioFiscal> {

    private final DomicilioFiscalDAO domicilioFiscalDAO;

    public DomicilioFiscalService() {
        domicilioFiscalDAO = new DomicilioFiscalDAO();
    }

    /**
     * Inserta un nuevo domicilio fiscal manejando transacción y validaciones.
     */
    @Override
    public DomicilioFiscal insertar(DomicilioFiscal domicilio) throws Exception {
        validarDatos(domicilio);

        try (Connection conn = DatabaseConnection.conectarDB()) {
            conn.setAutoCommit(false);
            try {
                DomicilioFiscal creada = domicilioFiscalDAO.crear(domicilio, conn);
                conn.commit();
                return creada;
            } catch (Exception ex) {
                conn.rollback();
                throw new SQLException("Error al insertar domicilio fiscal: " + ex.getMessage(), ex);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    /**
     * Actualiza un domicilio fiscal existente.
     */
    @Override
    public DomicilioFiscal actualizar(DomicilioFiscal domicilio) throws Exception {
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
                throw new SQLException("Error al actualizar el domicilio fiscal: " + ex.getMessage(), ex);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    /**
     * Elimina lógicamente el domicilio fiscal.
     */
    @Override
    public boolean eliminar(long id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido para eliminación de domicilio fiscal");
        }
        try (Connection conn = DatabaseConnection.conectarDB()) {
            conn.setAutoCommit(false);
            try {
                boolean eliminado = domicilioFiscalDAO.eliminar(id, conn);
                conn.commit();
                return eliminado;
            } catch (Exception ex) {
                conn.rollback();
                throw new SQLException("Error al eliminar domicilio fiscal: " + ex.getMessage(), ex);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    /**
     * Devuelve un domicilio fiscal por su ID.
     */
    @Override
    public DomicilioFiscal getById(long id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido");
        }
        return domicilioFiscalDAO.leer(id);
    }

    /**
     * Devuelve todos los domicilios fiscales activos (no eliminados).
     */
    @Override
    public List<DomicilioFiscal> getAll() throws SQLException {
        return domicilioFiscalDAO.leerTodos();
    }

    /**
     * Realiza validaciones de negocio y formato sobre el domicilio.
     */
    private void validarDatos(DomicilioFiscal df) throws Exception {
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
