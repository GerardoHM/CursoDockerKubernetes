package org.ghernandez.springcloud.msvc.cursos.msvc_cursos.controllers;

import feign.FeignException;
import jakarta.validation.Valid;
import org.ghernandez.springcloud.msvc.cursos.msvc_cursos.model.Usuario;
import org.ghernandez.springcloud.msvc.cursos.msvc_cursos.model.entity.Curso;
import org.ghernandez.springcloud.msvc.cursos.msvc_cursos.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class CursoController {

    @Autowired
    CursoService cursoService;

    @GetMapping
    public ResponseEntity<?> list(){
        return ResponseEntity.ok(cursoService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id){
        Optional<Curso> cursoOptional = cursoService.findByIdWithUsers(id); //cursoService.findById(id);
        if (cursoOptional.isPresent()){
            return ResponseEntity.ok(cursoOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@Valid @RequestBody Curso curso, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return validate(bindingResult);
        }

        Curso cursoDb = cursoService.save(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoDb);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Curso curso, BindingResult bindingResult, @PathVariable Long id){
        if (bindingResult.hasErrors()){
            return validate(bindingResult);
        }

        Optional<Curso> cursoOptional = cursoService.findById(id);
        if (cursoOptional.isPresent()){
            Curso cursoDb = cursoOptional.get();
            cursoDb.setNombre(curso.getNombre());

            return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.save(cursoDb));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Optional<Curso> cursoOptional = cursoService.findById(id);
        if (cursoOptional.isPresent()){
            cursoService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> assignUser(@RequestBody Usuario usuario, @PathVariable Long cursoId){
        Optional<Usuario> userOptional;
        try {
            userOptional = cursoService.assignUser(usuario, cursoId);
        } catch (FeignException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("mensaje", "No existe el usuario " +
                            "u ocurrio un error en la comunicación: " + e.getMessage()));
        }
        if (userOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(userOptional.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> createUser(@RequestBody Usuario usuario, @PathVariable Long cursoId){
        Optional<Usuario> userOptional;
        try {
            userOptional = cursoService.createUser(usuario, cursoId);
        } catch (FeignException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("mensaje", "No se pudo crear el usuario " +
                            "u ocurrio un error en la comunicación: " + e.getMessage()));
        }
        if (userOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(userOptional.get());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> deleteUser(@RequestBody Usuario usuario, @PathVariable Long cursoId){
        Optional<Usuario> userOptional;
        try {
            userOptional = cursoService.deleteUser(usuario, cursoId);
        } catch (FeignException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("mensaje", "No existe el usuario " +
                            "u ocurrio un error en la comunicación: " + e.getMessage()));
        }
        if (userOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(userOptional.get());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    public ResponseEntity<?> deleteCursoUsuarioById(@PathVariable Long id){
        cursoService.deleteCursoUsuarioById(id);
        return ResponseEntity.noContent().build();
    }

    private static ResponseEntity<Map<String, String>> validate(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }
}
