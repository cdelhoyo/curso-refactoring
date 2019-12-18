package es.cdelhoyo.refactoring.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class GrupoDeEdadTest {

    @Test
    public void createGrupoDeEdad() {
        assertThat(GrupoDeEdad.createGrupoDeEdad(34), equalTo(GrupoDeEdad.JOVEN));
        assertThat(GrupoDeEdad.createGrupoDeEdad(35), equalTo(GrupoDeEdad.ADULTO));
        assertThat(GrupoDeEdad.createGrupoDeEdad(64), equalTo(GrupoDeEdad.ADULTO));
        assertThat(GrupoDeEdad.createGrupoDeEdad(65), equalTo(GrupoDeEdad.JUBILADO));

    }
}