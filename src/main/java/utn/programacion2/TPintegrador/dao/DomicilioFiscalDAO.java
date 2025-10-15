package utn.programacion2.TPintegrador.dao;

import utn.programacion2.TPintegrador.config.DatabaseConnection;
import utn.programacion2.TPintegrador.entities.DomicilioFiscal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DomicilioFiscalDAO implements GenericDAO<DomicilioFiscal> {

    private static final String INSERT =
            "INSERT INTO domicilio_fiscal (eliminado, calle, numero, ciudad, provincia, codigo_postal, pais) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID =
            "SELECT * FROM domicilio_fiscal WHERE id = ? AND eliminado = false";

    private static final String SELECT_ALL =
            "SELECT * FROM domicilio_fiscal WHERE eliminado = false";

    private static final String UPDATE =
            "UPDATE domicilio_fiscal SET eliminado = ?, calle = ?, numero = ?, ciudad = ?, " +
                    "provincia = ?, codigo_postal = ?, pais = ? WHERE id = ?";

    private static final String DELETE_LOGICAL =
            "UPDATE domicilio_fiscal SET eliminado = true WHERE id = ?";

    @Override
    public DomicilioFiscal crear(DomicilioFiscal entity) throws SQLException {
        try (Connection conn = DatabaseConnection.conectarDB()) {
            return crear(entity, conn);
        }
    }

    @Override
    public DomicilioFiscal crear(DomicilioFiscal entity, Connection connection) throws SQLException {
        if (null == entity) {
            throw new IllegalArgumentException("La entidad no puede ser null");
        }

        try (PreparedStatement stmt = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setBoolean(1, null != entity.getEliminado() ? entity.getEliminado() : false);
            stmt.setString(2, entity.getCalle());
            stmt.setObject(3, entity.getNumero());
            stmt.setString(4, entity.getCiudad());
            stmt.setString(5, entity.getProvincia());
            stmt.setString(6, entity.getCodigoPostal());
            stmt.setString(7, entity.getPais());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al crear el domicilio fiscal, no se insertó ninguna fila");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Error al crear el domicilio fiscal, no se obtuvo el ID");
                }
            }
        }
        return entity;
    }

    @Override
    public DomicilioFiscal leer(long id) throws SQLException {
        try (Connection conn = DatabaseConnection.conectarDB()) {
            return leer(id, conn);
        }
    }

    @Override
    public DomicilioFiscal leer(long id, Connection connection) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_BY_ID)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return desdeResultSet(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<DomicilioFiscal> leerTodos() throws SQLException {
        try (Connection conn = DatabaseConnection.conectarDB()) {
            return leerTodos(conn);
        }
    }

    @Override
    public List<DomicilioFiscal> leerTodos(Connection connection) throws SQLException {
        List<DomicilioFiscal> domicilios = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                domicilios.add(desdeResultSet(rs));
            }
        }
        return domicilios;
    }

    @Override
    public boolean actualizar(DomicilioFiscal entity) throws SQLException {
        try (Connection conn = DatabaseConnection.conectarDB()) {
            return actualizar(entity, conn);
        }
    }

    @Override
    public boolean actualizar(DomicilioFiscal entity, Connection connection) throws SQLException {
        if (null == entity || null == entity.getId()) {
            throw new IllegalArgumentException("La entidad y su ID no pueden ser null");
        }

        try (PreparedStatement stmt = connection.prepareStatement(UPDATE)) {
            stmt.setBoolean(1, null != entity.getEliminado() ? entity.getEliminado() : false);
            stmt.setString(2, entity.getCalle());
            stmt.setObject(3, entity.getNumero());
            stmt.setString(4, entity.getCiudad());
            stmt.setString(5, entity.getProvincia());
            stmt.setString(6, entity.getCodigoPostal());
            stmt.setString(7, entity.getPais());
            stmt.setLong(8, entity.getId());

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

    /**
     * Mapea un ResultSet a una entidad DomicilioFiscal
     */
    private DomicilioFiscal desdeResultSet(ResultSet rs) throws SQLException {
        DomicilioFiscal df = new DomicilioFiscal();
        df.setId(rs.getLong("id"));
        df.setEliminado(rs.getBoolean("eliminado"));
        df.setCalle(rs.getString("calle"));
        df.setNumero(rs.getString("numero"));
        df.setCiudad(rs.getString("ciudad"));
        df.setProvincia(rs.getString("provincia"));
        df.setCodigoPostal(rs.getString("codigo_postal"));
        df.setPais(rs.getString("pais"));

        return df;
    }
}
