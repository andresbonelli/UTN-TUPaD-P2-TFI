package utn.programacion2.TPintegrador.service;

import utn.programacion2.TPintegrador.config.DatabaseConnection;
import utn.programacion2.TPintegrador.dao.EmpresaDAO;
import utn.programacion2.TPintegrador.entities.Empresa;

import java.sql.Connection;
import java.util.regex.Pattern;

public class EmpresaService extends AbstractService<Empresa> {

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private final EmpresaDAO empresaDAO;

    public EmpresaService() {
        super(new EmpresaDAO());
        empresaDAO = (EmpresaDAO) this.dao;
    }

    @Override
    public Empresa insertar(Empresa empresa) {
        try (Connection conn = DatabaseConnection.conectarDB()) {
            validarDatos(empresa);
            conn.setAutoCommit(false);

            try {
                var creada = empresaDAO.crear(empresa, conn);
                conn.commit();
                return creada;
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            System.out.println("Error al crear la empresa: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Empresa actualizar(Empresa empresa) {

        try (Connection conn = DatabaseConnection.conectarDB()) {
            validarDatos(empresa);
            conn.setAutoCommit(false);

            try {
                empresaDAO.actualizar(empresa, conn);
                conn.commit();
                return empresa;
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            System.out.println("Error al actualizar empresa: " + e.getMessage());
        }
        return null;
    }

    private void validarDatos(Empresa e) {
        if (null == e)
            throw new IllegalArgumentException("La empresa no puede ser null");

        if (null == e.getRazonSocial() || e.getRazonSocial().isBlank())
            throw new IllegalArgumentException("La razón social es obligatoria");

        if (null == e.getCuit() || e.getCuit().isBlank())
            throw new IllegalArgumentException("El CUIT es obligatorio");

        if (null != e.getEmail() && !EMAIL_REGEX.matcher(e.getEmail()).matches())
            throw new IllegalArgumentException("El formato de email es inválido");

        if (null == e.getDomicilioFiscal())
            throw new IllegalArgumentException("Debe asociarse un domicilio fiscal");
    }
}
