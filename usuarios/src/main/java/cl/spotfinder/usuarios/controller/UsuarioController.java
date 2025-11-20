package cl.spotfinder.usuarios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cl.spotfinder.usuarios.dto.Usuario;
import cl.spotfinder.usuarios.service.UsuarioService;

@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService servicio;

    @GetMapping
    public ResponseEntity<?> getAllUsuarios() {
        List<Usuario> usuarios = servicio.getAllUsuarios();
        if (usuarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron usuarios.");
        }
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/id")
    public ResponseEntity<?> findById(@RequestParam int id) {
        Usuario usuario = servicio.findById(id);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/add")
    public ResponseEntity<?> saveUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = servicio.saveUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al guardar el usuario: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioActualizado = servicio.updateUsuario(usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado para actualizar.");
        }
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteUsuario(@RequestParam int id) {
        try {
            servicio.deleteUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado para eliminar.");
        }
    }
}

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<String> handleNotFound(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
    }
}
