package com.backend.clinicaodontologica.repository;

import java.util.List;

public interface IDao<T> {
    T registrar(T t);
    T buscarPorId(long id);
    T eliminarPorId(long id);
    List<T> listarTodos();

}
