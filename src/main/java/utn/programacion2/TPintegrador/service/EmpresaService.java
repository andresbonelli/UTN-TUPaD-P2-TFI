package utn.programacion2.TPintegrador.service;

import utn.programacion2.TPintegrador.config.DatabaseConnection;
import utn.programacion2.TPintegrador.dao.EmpresaDAO;
import utn.programacion2.TPintegrador.entities.Empresa;

import java.sql.Connection;
import java.util.List;
import java.util.regex.Pattern;

public class EmpresaService extends AbstractService<Empresa> {

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private final EmpresaDAO empresaDAO;
    private final DomicilioFiscalService domicilioFiscalService;

    public EmpresaService() {
        super(new EmpresaDAO());
        empresaDAO = (EmpresaDAO) this.dao;
        domicilioFiscalService = new DomicilioFiscalService();
    }

    @Override
    public Empresa insertar(Empresa empresa) {
        try (Connection conn = DatabaseConnection.conectarDB()) {
            validarDatos(empresa);
            conn.setAutoCommit(false);
            try {
                // Si trae domicilio fiscal pero no tiene ID asociada, lo creamos primero
                if (null != empresa.getDomicilioFiscal() && null == empresa.getDomicilioFiscal().getId()) {
                    var nuevoDomicilio = domicilioFiscalService.insertar(empresa.getDomicilioFiscal(), conn);
                    empresa.setDomicilioFiscal(nuevoDomicilio);
                }
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
                // Si tiene domicilio fiscal, lo actualizamos o creamos
                if (empresa.getDomicilioFiscal() != null) {
                    if (empresa.getDomicilioFiscal().getId() != null) {
                        domicilioFiscalService.actualizar(empresa.getDomicilioFiscal(), conn);
                    } else {
                        var nuevoDomicilio = domicilioFiscalService.insertar(empresa.getDomicilioFiscal(), conn);
                        empresa.setDomicilioFiscal(nuevoDomicilio);
                    }
                }
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

    public Empresa buscarPorCuit(String cuit) {
        try {
            var empresa = empresaDAO.buscarPorCuit(cuit);
            if (null == empresa) {
                System.out.println("Empresa con cuit '"+cuit+"' no existente");
            } else {
                return empresa;
            }
        } catch (Exception e) {
            System.out.println("Error al buscar empresa por CUIT: " + e.getMessage());
        }
        return null;
    }

    public List<Empresa> buscarPorRazonSocial(String razonSocial) {
        try {
            var empresas = empresaDAO.buscarPorRazonSocial(razonSocial);
            if (null == empresas || empresas.isEmpty()) {
                System.out.println("No se encontraron empresas con la razón social: " + razonSocial);
            } else {
                return empresas;
            }
        } catch (Exception e) {
            System.out.println("Error al buscar empresas por razón social: " + e.getMessage());
        }
        return List.of();
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
