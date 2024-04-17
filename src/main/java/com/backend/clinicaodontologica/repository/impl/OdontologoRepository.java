package com.backend.clinicaodontologica.repository.impl;

import com.backend.clinicaodontologica.entity.Odontologo;
import com.backend.clinicaodontologica.repository.IDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;

public class OdontologoRepository implements IDao<Odontologo> {
    private final Logger LOGGER = LoggerFactory.getLogger(OdontologoRepository.class);

    @Override
    public Odontologo registrar(Odontologo odontologo) {
        Connection connection = null;


        return null;
    }

    @Override
    public Odontologo buscarPorId(long id) {
        return null;
    }

    @Override
    public List<Odontologo> listarTodos() {
        return null;
    }

    @Override
    public Odontologo eliminarPorId(long id) {
        return null;
    }
}
