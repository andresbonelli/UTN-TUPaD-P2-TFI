package utn.programacion2.TPintegrador.service;

import utn.programacion2.TPintegrador.config.DatabaseConnection;
import utn.programacion2.TPintegrador.dao.GenericDAO;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Clase abstracta para evitar repetir codigo en los servicios concretos.
 * @param <T> la entidad correspondiente.
 */
public abstract class AbstractService<T> implements GenericService<T> {

    protected final GenericDAO<T> dao;
    private final Class<T> clazz;

    @SuppressWarnings("unchecked")
    protected AbstractService(GenericDAO<T> dao) {
        this.dao = dao;
        this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public T insertar(T entidad) throws Exception {
        try (Connection conn = DatabaseConnection.conectarDB()) {
            conn.setAutoCommit(false);
            T creada = dao.crear(entidad, conn);
            conn.commit();
            return creada;
        } catch (SQLException e) {
            throw new SQLException("Error en transacción de inserción: " + e.getMessage(), e);
        }
    }

    @Override
    public T actualizar(T entidad) throws Exception {
        try (Connection conn = DatabaseConnection.conectarDB()) {
            conn.setAutoCommit(false);
            dao.actualizar(entidad, conn);
            conn.commit();
            return entidad;
        } catch (SQLException e) {
            throw new SQLException("Error en transacción de actualización: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(long id) {
        try (Connection conn = DatabaseConnection.conectarDB()) {
            conn.setAutoCommit(false);
            boolean eliminado = dao.eliminar(id, conn);
            conn.commit();
            return eliminado;
        } catch (SQLException e) {
            System.out.println("Error eliminando "+clazz.getSimpleName()+": " + e.getMessage());
        }
        return false;
    }

    @Override
    public T getById(long id) {
        try {
            var entidad = dao.leer(id);
            if (null == entidad) {
                System.out.println(clazz.getSimpleName() +" con id '"+id+"' no existente");
            } else {
                return entidad;
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener entidad "+ clazz.getSimpleName() +" por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<T> getAll() {
        try {
            var result = dao.leerTodos();
            if (null == result || result.isEmpty()) {
                System.out.println("Registro de "+ clazz.getSimpleName() +" vacio.");
            } else {
                return result;
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todas las entidades "+ clazz.getSimpleName() +" : " + e.getMessage());
        }
        return List.of();
    }
}
