package cl.spotfinder.usuarios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.spotfinder.usuarios.dto.Usuario;
import cl.spotfinder.usuarios.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public List<Usuario> getAllUsuarios() {
        List<Usuario> usuarios = repository.findAll();
        return usuarios != null ? usuarios : List.of();
    }

    public Usuario saveUsuario(Usuario usuario) {
        if (usuario == null || usuario.getNombre() == null || usuario.getEmail() == null) {
            throw new IllegalArgumentException("Usuario o campos requeridos no pueden ser nulos");
        }
        return repository.save(usuario);
    }
}
