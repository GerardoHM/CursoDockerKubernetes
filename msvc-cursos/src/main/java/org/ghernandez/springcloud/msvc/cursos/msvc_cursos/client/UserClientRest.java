package org.ghernandez.springcloud.msvc.cursos.msvc_cursos.client;

import org.ghernandez.springcloud.msvc.cursos.msvc_cursos.model.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-usuarios", url = "${msvc.usuarios.url}")
public interface UserClientRest {

    @GetMapping("/{id}")
    Usuario detail(@PathVariable Long id);

    @PostMapping("/")
    Usuario create(@RequestBody Usuario user);

    @GetMapping("/usuarios-por-curso")
    List<Usuario> getStudentsByIds(@RequestParam Iterable<Long> ids);
}
