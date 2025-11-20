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
        return repository.findAll();
    }

    public Usuario saveUsuario(Usuario usuario) {
        return repository.save(usuario);
    }
    
    public Usuario findById(int id) {
        return repository.findById(id).orElse(null);
    }   

    public Usuario updateUsuario(Usuario usuario) {
        Usuario existingUsuario = repository.findById(usuario.getId()).orElse(null);
        existingUsuario.setNombre(usuario.getNombre());
        existingUsuario.setApellido(usuario.getApellido());
        existingUsuario.setEmail(usuario.getEmail());
        existingUsuario.setContrasena(usuario.getContrasena());
        return repository.save(existingUsuario);
    }

    public void deleteUsuario(int id) {
        repository.deleteById(id);
    }
}
