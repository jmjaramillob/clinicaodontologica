package com.backend.clinicaodontologica;

import com.backend.clinicaodontologica.entity.Domicilio;
import com.backend.clinicaodontologica.entity.Paciente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class PacienteTest {

    @Test
    public void pacienteTest(){
        LocalDate date = LocalDate.now();
        Domicilio domi = new Domicilio(12L, "a", 1, "zz", "as");
        Paciente paciente = new Paciente(11L, "aa", "nbb", 123, date, domi);

        Assertions.assertEquals(paciente.getApellido(), "nbb");
    }
}
