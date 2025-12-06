package org.ghernandez.springcloud.msvc.usuarios.services;

import java.util.List;
import java.util.Optional;

import org.ghernandez.springcloud.msvc.usuarios.models.entity.Usuario;

public interface UsuarioService {
    List<Usuario> list();
    Optional<Usuario> findById(Long id);
    Usuario save(Usuario usuario);
    void deleteUserById(Long id);
    List<Usuario> findAllByIds(Iterable<Long> ids);

    Optional<Usuario> findByEmail(String email);
    boolean existByEmail(String email);
}
