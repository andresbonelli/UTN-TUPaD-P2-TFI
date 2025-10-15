package utn.programacion2.TPintegrador.dao;

import utn.programacion2.TPintegrador.config.DatabaseConnection;
import utn.programacion2.TPintegrador.entities.DomicilioFiscal;
import utn.programacion2.TPintegrador.entities.Empresa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpresaDAO implements GenericDAO<Empresa> {

    private final DomicilioFiscalDAO domicilioFiscalDAO;

    private static final String INSERT =
            "INSERT INTO empresa (eliminado, razon_social, cuit, actividad_principal, email, domicilio_fiscal_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID =
            "SELECT * FROM empresa WHERE id = ? AND eliminado = false";

    private static final String SELECT_BY_CUIT =
            "SELECT * FROM empresa WHERE cuit = ? AND eliminado = false";

    private static final String SELECT_BY_RAZON_SOCIAL =
            "SELECT * FROM empresa WHERE razon_social LIKE ? AND eliminado = false";

    private static final String SELECT_BY_DOMICILIO_FISCAL =
            "SELECT * FROM empresa WHERE domicilio_fiscal_id = ? AND eliminado = false";

    private static final String SELECT_ALL =
            "SELECT * FROM empresa WHERE eliminado = false";

    private static final String UPDATE =
            "UPDATE empresa SET eliminado = ?, razon_social = ?, cuit = ?, actividad_principal = ?, " +
                    "email = ?, domicilio_fiscal_id = ? WHERE id = ?";

    private static final String DELETE_LOGICAL =
            "UPDATE empresa SET eliminado = true WHERE id = ?";

    public EmpresaDAO() {
        this.domicilioFiscalDAO = new DomicilioFiscalDAO();
    }

    @Override
    public Empresa crear(Empresa entity) throws SQLException {
        try (Connection conn = DatabaseConnection.conectarDB()) {
            return crear(entity, conn);
        }
    }

    @Override
    public Empresa crear(Empresa entity, Connection connection) throws SQLException {
        if (null == entity) {
            throw new IllegalArgumentException("La entidad no puede ser null");
        }

        verificarDomicilioExistente(entity, connection);

        try (PreparedStatement stmt = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setBoolean(1, null != entity.getEliminado() ? entity.getEliminado() : false);
            stmt.setString(2, entity.getRazonSocial());
            stmt.setString(3, entity.getCuit());
            stmt.setString(4, entity.getActividadPrincipal());
            stmt.setString(5, entity.getEmail());

            if (null != entity.getDomicilioFiscal() && null != entity.getDomicilioFiscal().getId()) {
                stmt.setLong(6, entity.getDomicilioFiscal().getId());
            } else {
                stmt.setNull(6, Types.BIGINT);
            }

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al crear la empresa, no se insertó ninguna fila");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Error al crear la empresa, no se obtuvo el ID");
                }
            }
        }
        return entity;
    }

    @Override
    public Empresa leer(long id) throws SQLException {
        try (Connection conn = DatabaseConnection.conectarDB()) {
            return leer(id, conn);
        }
    }

    @Override
    public Empresa leer(long id, Connection connection) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_BY_ID)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return desdeResultSet(rs, connection);
                }
            }
        }
        return null;
    }

    @Override
    public List<Empresa> leerTodos() throws SQLException {
        try (Connection conn = DatabaseConnection.conectarDB()) {
            return leerTodos(conn);
        }
    }

    @Override
    public List<Empresa> leerTodos(Connection connection) throws SQLException {
        List<Empresa> empresas = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                empresas.add(desdeResultSet(rs, connection));
            }
        }
        return empresas;
    }

    @Override
    public boolean actualizar(Empresa entity) throws SQLException {
        try (Connection conn = DatabaseConnection.conectarDB()) {
            return actualizar(entity, conn);
        }
    }

    @Override
    public boolean actualizar(Empresa entity, Connection connection) throws SQLException {
        if (null == entity || null == entity.getId()) {
            throw new IllegalArgumentException("La entidad y su ID no pueden ser null");
        }

        // Si tiene domicilio fiscal, lo actualizamos o creamos
        if (entity.getDomicilioFiscal() != null) {
            if (entity.getDomicilioFiscal().getId() != null) {
                verificarDomicilioExistente(entity, connection);
                domicilioFiscalDAO.actualizar(entity.getDomicilioFiscal(), connection);
            } else {
                domicilioFiscalDAO.crear(entity.getDomicilioFiscal(), connection);
            }
        }

        try (PreparedStatement stmt = connection.prepareStatement(UPDATE)) {
            stmt.setBoolean(1, null != entity.getEliminado() ? entity.getEliminado() : false);
            stmt.setString(2, entity.getRazonSocial());
            stmt.setString(3, entity.getCuit());
            stmt.setString(4, entity.getActividadPrincipal());
            stmt.setString(5, entity.getEmail());

            if (null != entity.getDomicilioFiscal() && null != entity.getDomicilioFiscal().getId()) {
                stmt.setLong(6, entity.getDomicilioFiscal().getId());
            } else {
                stmt.setNull(6, Types.BIGINT);
            }

            stmt.setLong(7, entity.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(long id) throws SQLException {
        try (Connection conn = DatabaseConnection.conectarDB()) {
            return eliminar(id, conn);
        }
    }

    @Override
    public boolean eliminar(long id, Connection connection) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_LOGICAL)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public Empresa buscarPorDomicilioFiscal(long domicilioFiscalId) throws SQLException {
        try (Connection conn = DatabaseConnection.conectarDB()) {
            return buscarPorDomicilioFiscal(domicilioFiscalId, conn);
        }
    }

    public Empresa buscarPorDomicilioFiscal(long domicilioFiscalId, Connection connection) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_BY_DOMICILIO_FISCAL)) {
            stmt.setLong(1, domicilioFiscalId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return desdeResultSet(rs, connection);
                }
            }
        }
        return null;
    }

    public Empresa buscarPorCuit(String cuit) throws SQLException {
        try (Connection conn = DatabaseConnection.conectarDB()) {
            return buscarPorCuit(cuit, conn);
        }
    }

    public Empresa buscarPorCuit(String cuit, Connection connection) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_BY_CUIT)) {
            stmt.setString(1, cuit);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return desdeResultSet(rs, connection);
                }
            }
        }
        return null;
    }

    public List<Empresa> buscarPorRazonSocial(String razonSocial) throws SQLException {
        try (Connection conn = DatabaseConnection.conectarDB()) {
            return buscarPorRazonSocial(razonSocial, conn);
        }
    }

    public List<Empresa> buscarPorRazonSocial(String razonSocial, Connection connection) throws SQLException {
        List<Empresa> empresas = new ArrayList<>();
        String likeRazonSocial = "%" + razonSocial + "%";
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_BY_RAZON_SOCIAL)) {
            stmt.setString(1, likeRazonSocial);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    empresas.add(desdeResultSet(rs, connection));
                }
            }
        }
        return empresas;
    }

    /**
     * Mapea un ResultSet a una entidad Empresa, incluyendo su DomicilioFiscal
     */
    private Empresa desdeResultSet(ResultSet rs, Connection connection) throws SQLException {
        Empresa e = new Empresa();
        e.setId(rs.getLong("id"));
        e.setEliminado(rs.getBoolean("eliminado"));
        e.setRazonSocial(rs.getString("razon_social"));
        e.setCuit(rs.getString("cuit"));
        e.setActividadPrincipal(rs.getString("actividad_principal"));
        e.setEmail(rs.getString("email"));

        // Cargar el domicilio fiscal asociado
        long domicilioFiscalId = rs.getLong("domicilio_fiscal_id");
        if (!rs.wasNull()) {
            DomicilioFiscal domicilio = domicilioFiscalDAO.leer(domicilioFiscalId, connection);
            e.setDomicilioFiscal(domicilio);
        }

        return e;
    }

    /**
     *  Nos aseguramos que exista el domicilio y que no haya sido asignado a otra empresa
     */
    private void verificarDomicilioExistente(Empresa entity, Connection connection) throws SQLException {
        var domicilioExistente = domicilioFiscalDAO.leer(entity.getDomicilioFiscal().getId(), connection);
        if (null == domicilioExistente) {
            throw new SQLException(
                    "El domicilio fiscal con ID " + entity.getDomicilioFiscal().getId() +
                            " no existe en la base de datos"
            );
        }
        if (null != buscarPorDomicilioFiscal(domicilioExistente.getId(), connection)) {
            throw new SQLException(
                    "El domicilio fiscal con ID " + entity.getDomicilioFiscal().getId() +
                            " ya está asignado a otra empresa"
            );
        }
    }
}
