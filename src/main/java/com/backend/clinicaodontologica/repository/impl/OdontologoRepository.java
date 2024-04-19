package com.backend.clinicaodontologica.repository.impl;

import com.backend.clinicaodontologica.dbconnection.H2Connection;
import com.backend.clinicaodontologica.entity.Domicilio;
import com.backend.clinicaodontologica.entity.Odontologo;
import com.backend.clinicaodontologica.entity.Paciente;
import com.backend.clinicaodontologica.repository.IDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

public class OdontologoRepository implements IDao<Odontologo> {
    private final Logger LOGGER = LoggerFactory.getLogger(OdontologoRepository.class);

    @Override
    public Odontologo registrar(Odontologo odontologo) {
        Connection connection = null;
        Odontologo odontologoRegistrado = null;

        try {
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ODONTOLOGOS (NOMBRE, APELLIDO, MATRICULA) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, odontologo.getNombre());
            preparedStatement.setString(2, odontologo.getApellido());
            preparedStatement.setString(3, odontologo.getMatricula());
            preparedStatement.execute();

            odontologoRegistrado = new Odontologo(odontologo.getMatricula(), odontologo.getNombre(), odontologo.getApellido());

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while(resultSet.next()) {
                odontologoRegistrado.setId(resultSet.getLong("id"));
            }

            connection.commit();
            LOGGER.info("Se ha registrado el odontologo: " + odontologoRegistrado);



        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                    LOGGER.info("Tuvimos un problema");
                    LOGGER.error(e.getMessage());
                    e.printStackTrace();
                } catch (SQLException exception) {
                    LOGGER.error(exception.getMessage());
                    exception.printStackTrace();
                }
            }


        } finally {
            {
                try {
                    connection.close();
                } catch (Exception ex) {
                    LOGGER.error("No se pudo cerrar la conexion: " + ex.getMessage());
                }
            }


        }

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
    public void eliminarPorId(long id) {

    }
}
