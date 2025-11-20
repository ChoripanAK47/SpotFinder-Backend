package cl.spotfinder.usuarios.controller;

import cl.spotfinder.usuarios.dto.Usuario;
import cl.spotfinder.usuarios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/usuarios")
@CrossOrigin(origins = "*") // Permitir conexiones desde cualquier lado (ajustar en prod)
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

    @PostMapping("/add") // Registro
    public ResponseEntity<?> saveUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = servicio.saveUsuario(usuario);
            // No devolver la contraseña en la respuesta
            nuevoUsuario.setContrasena(null); 
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login") // Nuevo endpoint de Login
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        try {
            String token = servicio.login(credenciales.get("email"), credenciales.get("password"));
            return ResponseEntity.ok(Map.of("token", token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
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
