package utn.programacion2.TPintegrador.service;

import java.util.List;

public interface GenericService<T> {

    T insertar(T entity) throws Exception;

    T actualizar(T entity) throws Exception;

    void eliminarLogico(long id) throws Exception;

    T getById(long id) throws Exception;

    List<T> getAll() throws Exception;

}
