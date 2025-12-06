package org.ghernandez.springcloud.msvc.usuarios.services;

import java.lang.Override;
import java.util.List;
import java.util.Optional;

import org.ghernandez.springcloud.msvc.usuarios.clients.CourseClientRest;
import org.ghernandez.springcloud.msvc.usuarios.models.entity.Usuario;
import org.ghernandez.springcloud.msvc.usuarios.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    private final UsuarioRepository userRepo;
    private final CourseClientRest courseClientRest;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository userRepo, CourseClientRest courseClientRest){
        this.userRepo = userRepo;
        this.courseClientRest = courseClientRest;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> list() {
        return (List<Usuario>) userRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long id) {
        return userRepo.findById(id);
    }

    @Override
    @Transactional
    public Usuario save(Usuario usuario) {
        return userRepo.save(usuario);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepo.deleteById(id);
        courseClientRest.deleteCursoUsuarioById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAllByIds(Iterable<Long> ids) {
        return (List<Usuario>) userRepo.findAllById(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existByEmail(String email) {
        return userRepo.existsByEmail(email);
    }
}
