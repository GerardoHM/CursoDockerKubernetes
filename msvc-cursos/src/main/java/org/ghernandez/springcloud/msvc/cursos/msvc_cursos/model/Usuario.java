package org.ghernandez.springcloud.msvc.cursos.msvc_cursos.model;

import lombok.Data;

@Data
public class Usuario {
    private long id;

    private String nombre;

    private String email;

    private String password;
}
