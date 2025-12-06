package org.ghernandez.springcloud.msvc.cursos.msvc_cursos.services;

import org.ghernandez.springcloud.msvc.cursos.msvc_cursos.client.UserClientRest;
import org.ghernandez.springcloud.msvc.cursos.msvc_cursos.model.Usuario;
import org.ghernandez.springcloud.msvc.cursos.msvc_cursos.model.entity.Curso;
import org.ghernandez.springcloud.msvc.cursos.msvc_cursos.model.entity.CursoUsuario;
import org.ghernandez.springcloud.msvc.cursos.msvc_cursos.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepo;
    private final UserClientRest userClientRest;

    @Autowired
    public CursoServiceImpl(CursoRepository cursoRepo, UserClientRest userClientRest){
        this.cursoRepo = cursoRepo;
        this.userClientRest = userClientRest;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Curso> list() {
        return (List<Curso>) cursoRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> findById(Long id) {
        return cursoRepo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> findByIdWithUsers(Long id) {
        Optional<Curso> cursoOptional = cursoRepo.findById(id);
        if (cursoOptional.isPresent()){
            Curso curso = cursoOptional.get();
            if (!curso.getCursoUsuarios().isEmpty()){
                List<Long> ids = curso.getCursoUsuarios().stream().
                        map(CursoUsuario::getUsuarioId).toList();
                List<Usuario> usuarios = userClientRest.getStudentsByIds(ids);
                curso.setUsuarios(usuarios);
            }
            return  Optional.of(curso);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Curso save(Curso curso) {
        return cursoRepo.save(curso);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        cursoRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteCursoUsuarioById(Long id) {
        cursoRepo.deleteCursoUsuarioById(id);
    }

    @Override
    @Transactional
    public Optional<Usuario> assignUser(Usuario user, Long cursoId) {
        Optional<Curso> cursoOptional = cursoRepo.findById(cursoId);
        if (cursoOptional.isPresent()){
            Usuario userMscv = userClientRest.detail(user.getId());

            Curso curso = cursoOptional.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(userMscv.getId());

            curso.addCursoUsuarios(cursoUsuario);
            cursoRepo.save(curso);
            return Optional.of(userMscv);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> createUser(Usuario user, Long cursoId) {
        Optional<Curso> cursoOptional = cursoRepo.findById(cursoId);
        if (cursoOptional.isPresent()){
            Usuario userNewMscv = userClientRest.create(user);

            Curso curso = cursoOptional.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(userNewMscv.getId());

            curso.addCursoUsuarios(cursoUsuario);
            cursoRepo.save(curso);
            return Optional.of(userNewMscv);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> deleteUser(Usuario user, Long cursoId) {
        Optional<Curso> cursoOptional = cursoRepo.findById(cursoId);
        if (cursoOptional.isPresent()){
            Usuario userMscv = userClientRest.detail(user.getId());

            Curso curso = cursoOptional.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(userMscv.getId());

            curso.removeCursoUsuarios(cursoUsuario);
            cursoRepo.save(curso);
            return Optional.of(userMscv);
        }

        return Optional.empty();
    }
}
