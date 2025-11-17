package utn.programacion2.TPintegrador.service;

import utn.programacion2.TPintegrador.config.DatabaseConnection;
import utn.programacion2.TPintegrador.dao.EmpresaDAO;
import utn.programacion2.TPintegrador.entities.Empresa;

import java.sql.Connection;
import java.util.List;
import java.util.regex.Pattern;

public class EmpresaService extends AbstractService<Empresa> {

    // Regex mejorados y más estrictos
    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
                    Pattern.CASE_INSENSITIVE);

    private static final Pattern CUIT_REGEX =
            Pattern.compile("^\\d{11}$"); // exactamente 11 dígitos numéricos

    private final EmpresaDAO empresaDAO;
    private final DomicilioFiscalService domicilioFiscalService;

    EmpresaService(EmpresaDAO dao, DomicilioFiscalService domicilioFiscalService) {
        super(dao);
        this.empresaDAO = (EmpresaDAO) this.dao;
        this.domicilioFiscalService = domicilioFiscalService;
    }

    @Override
    public Empresa insertar(Empresa empresa) {
        try (Connection conn = DatabaseConnection.conectarDB()) {

            validarDatos(empresa);

            conn.setAutoCommit(false);
            System.out.println("[INFO] Iniciando transacción para crear empresa...");

            try {
                if (empresa.getDomicilioFiscal() != null
                        && empresa.getDomicilioFiscal().getId() == null) {

                    System.out.println("[INFO] Insertando domicilio fiscal...");
                    var nuevoDomicilio =
                            domicilioFiscalService.insertar(empresa.getDomicilioFiscal(), conn);

                    empresa.setDomicilioFiscal(nuevoDomicilio);
                }

                var creada = empresaDAO.crear(empresa, conn);

                conn.commit();
                System.out.println("[INFO] Empresa creada exitosamente con ID: " + creada.getId());

                return creada;

            } catch (Exception ex) {
                System.err.println("[ERROR] Fallo en creación, haciendo rollback: " + ex.getMessage());
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (Exception e) {
            System.err.println("[ERROR] Error general al crear la empresa: " + e.getMessage());
        }

        return null;
    }

    @Override
    public Empresa actualizar(Empresa empresa) {
        try (Connection conn = DatabaseConnection.conectarDB()) {

            validarDatos(empresa);

            conn.setAutoCommit(false);
            System.out.println("[INFO] Iniciando transacción para actualizar empresa...");

            try {

                if (empresa.getDomicilioFiscal() != null) {
                    if (empresa.getDomicilioFiscal().getId() != null) {
                        System.out.println("[INFO] Actualizando domicilio fiscal...");
                        domicilioFiscalService.actualizar(empresa.getDomicilioFiscal(), conn);
                    } else {
                        System.out.println("[INFO] Creando nuevo domicilio fiscal...");
                        var nuevoDom =
                                domicilioFiscalService.insertar(empresa.getDomicilioFiscal(), conn);
                        empresa.setDomicilioFiscal(nuevoDom);
                    }
                }

                empresaDAO.actualizar(empresa, conn);
                conn.commit();

                System.out.println("[INFO] Empresa actualizada correctamente.");
                return empresa;

            } catch (Exception ex) {
                System.err.println("[ERROR] Fallo en actualización, haciendo rollback: " + ex.getMessage());
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (Exception e) {
            System.err.println("[ERROR] Error general al actualizar empresa: " + e.getMessage());
        }

        return null;
    }

    public Empresa buscarPorCuit(String cuit) {
        try {
            var empresa = empresaDAO.buscarPorCuit(cuit);
            if (empresa == null) {
                System.out.println("[INFO] Empresa con CUIT '" + cuit + "' no encontrada.");
                return null;
            }
            return empresa;
        } catch (Exception e) {
            System.err.println("[ERROR] Error al buscar empresa por CUIT: " + e.getMessage());
            return null;
        }
    }

    public List<Empresa> buscarPorRazonSocial(String razonSocial) {
        try {
            var empresas = empresaDAO.buscarPorRazonSocial(razonSocial);
            if (empresas == null || empresas.isEmpty()) {
                System.out.println("[INFO] No se encontraron empresas con razón social: " + razonSocial);
                return List.of();
            }
            return empresas;
        } catch (Exception e) {
            System.err.println("[ERROR] Error al buscar empresas por razón social: " + e.getMessage());
            return List.of();
        }
    }

    public boolean existePorDomicilioFiscal(long id) {
        try {
            return empresaDAO.buscarPorDomicilioFiscal(id) != null;
        } catch (Exception e) {
            System.err.println("[ERROR] Error buscando empresa por domicilio fiscal: " + e.getMessage());
            return false;
        }
    }

    private void validarDatos(Empresa e) {

        if (e == null)
            throw new IllegalArgumentException("La empresa no puede ser null.");

        if (e.getRazonSocial() == null || e.getRazonSocial().isBlank())
            throw new IllegalArgumentException("La razón social es obligatoria.");

        if (e.getCuit() == null || e.getCuit().isBlank())
            throw new IllegalArgumentException("El CUIT es obligatorio.");

        if (!CUIT_REGEX.matcher(e.getCuit()).matches())
            throw new IllegalArgumentException("El CUIT debe tener exactamente 11 dígitos.");

        if (e.getEmail() != null && !EMAIL_REGEX.matcher(e.getEmail()).matches())
            throw new IllegalArgumentException("Formato de email inválido.");

        if (e.getDomicilioFiscal() == null)
            throw new IllegalArgumentException("Debe asociarse un domicilio fiscal.");

        if (e.getActividadPrincipal() != null && e.getActividadPrincipal().length() > 80)
            throw new IllegalArgumentException("La actividad principal no puede superar los 80 caracteres.");
    }
}
