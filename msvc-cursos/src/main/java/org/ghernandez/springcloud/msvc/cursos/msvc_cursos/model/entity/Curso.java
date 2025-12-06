package org.ghernandez.springcloud.msvc.cursos.msvc_cursos.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.ghernandez.springcloud.msvc.cursos.msvc_cursos.model.Usuario;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cursos")
@Data
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "curso_id")
    private List<CursoUsuario> cursoUsuarios;

    @Transient
    private List<Usuario> usuarios;

    @NotBlank
    private String nombre;

    public Curso(){
        cursoUsuarios = new ArrayList<>();
        usuarios= new ArrayList<>();
    }

    public void addCursoUsuarios(CursoUsuario cursoUsuario){
        cursoUsuarios.add(cursoUsuario);
    }

    public void removeCursoUsuarios(CursoUsuario cursoUsuario){
        cursoUsuarios.remove(cursoUsuario);
    }
}
