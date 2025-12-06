package org.ghernandez.springcloud.msvc.cursos.msvc_cursos.services;

import org.ghernandez.springcloud.msvc.cursos.msvc_cursos.model.Usuario;
import org.ghernandez.springcloud.msvc.cursos.msvc_cursos.model.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {
    List<Curso> list();
    Optional<Curso> findById(Long id);
    Optional<Curso> findByIdWithUsers(Long id);
    Curso save(Curso curso);
    void delete(Long id);
    void deleteCursoUsuarioById(Long id);

    Optional<Usuario> assignUser(Usuario user, Long cursoId);
    Optional<Usuario> createUser(Usuario user, Long cursoId);
    Optional<Usuario> deleteUser(Usuario user, Long cursoId);
}
