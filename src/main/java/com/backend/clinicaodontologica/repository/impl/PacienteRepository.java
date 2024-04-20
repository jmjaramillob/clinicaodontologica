package com.backend.clinicaodontologica.repository.impl;

import com.backend.clinicaodontologica.repository.IDao;
import com.backend.clinicaodontologica.dbconnection.H2Connection;
import com.backend.clinicaodontologica.entity.Domicilio;
import com.backend.clinicaodontologica.entity.Paciente;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PacienteRepository implements IDao<Paciente> {
    private Logger LOGGER = LoggerFactory.getLogger(PacienteRepository.class);
    private final DomicilioRepository domicilioRepository;

    @Autowired
    public PacienteRepository(DomicilioRepository domicilioRepository){
        this.domicilioRepository = domicilioRepository;
    }
    @Override
    public Paciente registrar(Paciente paciente) {
        Connection connection = null;
        Paciente pacienteRegistrado = null;

        try {
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);

            Domicilio domicilioRegistrado = domicilioRepository.registrar(paciente.getDomicilio());

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO PACIENTES (NOMBRE, APELLIDO, DNI, FECHA, DOMICILIO_ID) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, paciente.getNombre());
            preparedStatement.setString(2, paciente.getApellido());
            preparedStatement.setInt(3, paciente.getDni());
            preparedStatement.setDate(4, Date.valueOf(paciente.getFechaIngreso()));
            preparedStatement.setLong(5, domicilioRegistrado.getId());
            preparedStatement.execute();

            pacienteRegistrado = new Paciente(paciente.getNombre(), paciente.getApellido(), paciente.getDni(), paciente.getFechaIngreso(), domicilioRegistrado);

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while(resultSet.next()) {
                pacienteRegistrado.setId(resultSet.getLong("id"));
            }

            connection.commit();
            LOGGER.info("Se ha registrado el paciente: " + pacienteRegistrado);



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


        return pacienteRegistrado;
    }

    @Override
    public Paciente buscarPorId(long id) {
        Connection connection = null;
        PacienteRepository pacienteRepository = null;
        Paciente paciente = null;

        try {
            connection = H2Connection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT FROM PACIENTES WHERE ID = ?");
            preparedStatement.setInt(1, (int) id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                paciente = pacienteRepository.crearObjetoPaciente(resultSet);
            }

            connection.commit();
            LOGGER.info("Se ha encontrado el paciente con id {}: ", (int) id );



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


        return paciente;

    }

    @Override
    public List<Paciente> listarTodos() {
        Connection connection = null;
        List<Paciente> pacientes = new ArrayList<>();
        try{
            connection = H2Connection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM PACIENTES");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Paciente paciente = crearObjetoPaciente(resultSet);
                pacientes.add(paciente);
            }

            LOGGER.info("Listado de todos los pacientes: " + pacientes);



        }catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();

        } finally {
            try {
                connection.close();
            } catch (Exception ex) {
                LOGGER.error("Ha ocurrido un error al intentar cerrar la bdd. " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        return pacientes;
    }

    @Override
    public void eliminarPorId(long id){
        Connection connection = null;
        try{
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement("DELETE FROM PACIENTES WHERE ID = ?", (int) id);
            ps.execute();
            connection.commit();
            LOGGER.warn("Se ha eliminado un paciente.");
        }catch (Exception e){
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
            try {
                connection.close();
            } catch (Exception ex) {
                LOGGER.error("No se pudo cerrar la conexion: " + ex.getMessage());
            }
        }
    }

    private Paciente crearObjetoPaciente(ResultSet resultSet) throws SQLException {

        Domicilio domicilio = new DomicilioRepository().buscarPorId(resultSet.getInt("domicilio_id"));

        return new Paciente(resultSet.getLong("id"), resultSet.getString("nombre"), resultSet.getString("apellido"), resultSet.getInt("dni"), resultSet.getDate("fecha").toLocalDate(), domicilio);
    }
}
