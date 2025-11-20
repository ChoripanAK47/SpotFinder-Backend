package cl.spotfinder.usuarios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.spotfinder.usuarios.dto.Usuario;
import cl.spotfinder.usuarios.service.UsuarioService;

@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService servicio;

    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return servicio.getAllUsuarios();
    }

    @PostMapping("/add")
    public Usuario saveUsuario(@RequestBody Usuario usuario) {
        return servicio.saveUsuario(usuario);
    }
    
}
