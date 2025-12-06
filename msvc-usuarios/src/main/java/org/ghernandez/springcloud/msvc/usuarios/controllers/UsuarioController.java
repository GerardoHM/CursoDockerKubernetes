package org.ghernandez.springcloud.msvc.usuarios.controllers;

import java.util.*;

import jakarta.validation.Valid;
import org.ghernandez.springcloud.msvc.usuarios.models.entity.Usuario;
import org.ghernandez.springcloud.msvc.usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService userService;

    @GetMapping("/")
    public Map<String, List<Usuario>> list(){
        return Collections.singletonMap("usuarios", userService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id){
        Optional<Usuario> userOptional = userService.findById(id);
        if (userOptional.isPresent()){
            return ResponseEntity.ok(userOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Usuario user, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return validate(bindingResult);
        }
        if (!user.getEmail().isEmpty() && userService.existByEmail(user.getEmail())){
            return ResponseEntity.badRequest()
                    .body(Collections
                            .singletonMap("mensaje", "Ya existe un usuario con ese email."));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@Valid @RequestBody Usuario user, BindingResult bindingResult, @PathVariable Long id){
        if (bindingResult.hasErrors()){
            return validate(bindingResult);
        }

        Optional<Usuario> userOptional = userService.findById(id);
        if (userOptional.isPresent()){
            if (!user.getEmail().isEmpty() &&
                    !user.getEmail().equalsIgnoreCase(userOptional.get().getEmail()) &&
                    userService.existByEmail(user.getEmail())){
                return ResponseEntity.badRequest()
                        .body(Collections
                                .singletonMap("mensaje", "Ya existe un usuario con ese email."));
            }

            Usuario usuarioDb = userOptional.get();
            usuarioDb.setNombre(user.getNombre());
            usuarioDb.setEmail(user.getEmail());
            usuarioDb.setPassword(user.getPassword());

            return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(usuarioDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Optional<Usuario> userOptional = userService.findById(id);
        if (userOptional.isPresent()){
            userService.deleteUserById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<?> getStudentsByIds(@RequestParam List<Long> ids){
        return ResponseEntity.ok(userService.findAllByIds(ids));
    }

    private static ResponseEntity<Map<String, String>> validate(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }
}
