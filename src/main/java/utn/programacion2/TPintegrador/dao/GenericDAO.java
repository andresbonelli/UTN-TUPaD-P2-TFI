package utn.programacion2.TPintegrador.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface GenericDAO<T> {

    /**
     * Crea una nueva entidad en la base de datos
     * @param entity entidad a crear
     * @return entidad creada con ID asignado
     * @throws SQLException si ocurre un error en la operación
     */
    T crear(T entity) throws SQLException;

    /**
     * Crea una nueva entidad usando una conexión externa (transaccional)
     * @param entity entidad a crear
     * @param connection conexión externa
     * @return entidad creada con ID asignado
     * @throws SQLException si ocurre un error en la operación
     */
    T crear(T entity, Connection connection) throws SQLException;

    /**
     * Lee una entidad por su ID
     * @param id identificador de la entidad
     * @return entidad encontrada o null si no existe
     * @throws SQLException si ocurre un error en la operación
     */
    T leer(long id) throws SQLException;

    /**
     * Lee una entidad usando una conexión externa (transaccional)
     * @param id identificador de la entidad
     * @param connection conexión externa
     * @return entidad encontrada o null si no existe
     * @throws SQLException si ocurre un error en la operación
     */
    T leer(long id, Connection connection) throws SQLException;

    /**
     * Lee todas las entidades no eliminadas
     * @return lista de entidades
     * @throws SQLException si ocurre un error en la operación
     */
    List<T> leerTodos() throws SQLException;

    /**
     * Lee todas las entidades usando una conexión externa (transaccional)
     * @param connection conexión externa
     * @return lista de entidades
     * @throws SQLException si ocurre un error en la operación
     */
    List<T> leerTodos(Connection connection) throws SQLException;

    /**
     * Actualiza una entidad existente
     * @param entity entidad con datos actualizados
     * @return true si se actualizó correctamente
     * @throws SQLException si ocurre un error en la operación
     */
    boolean actualizar(T entity) throws SQLException;

    /**
     * Actualiza una entidad usando una conexión externa (transaccional)
     * @param entity entidad con datos actualizados
     * @param connection conexión externa
     * @return true si se actualizó correctamente
     * @throws SQLException si ocurre un error en la operación
     */
    boolean actualizar(T entity, Connection connection) throws SQLException;

    /**
     * Elimina lógicamente una entidad (marca eliminado = true)
     * @param id identificador de la entidad
     * @return true si se eliminó correctamente
     * @throws SQLException si ocurre un error en la operación
     */
    boolean eliminar(long id) throws SQLException;

    /**
     * Elimina lógicamente una entidad usando una conexión externa (transaccional)
     * @param id identificador de la entidad
     * @param connection conexión externa
     * @return true si se eliminó correctamente
     * @throws SQLException si ocurre un error en la operación
     */
    boolean eliminar(long id, Connection connection) throws SQLException;
}
