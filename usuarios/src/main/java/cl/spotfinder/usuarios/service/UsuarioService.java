package cl.spotfinder.usuarios.service;

import cl.spotfinder.usuarios.dto.Usuario;
import cl.spotfinder.usuarios.repository.UsuarioRepository;
import cl.spotfinder.usuarios.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;

    public List<Usuario> getAllUsuarios() {
        return repository.findAll();
    }

    public Usuario saveUsuario(Usuario usuario) {
        // ENCRIPTAR la contraseña antes de guardar
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        return repository.save(usuario);
    }

    public String login(String email, String passwordRaw) {
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        // Verificar si la contraseña coincide con la encriptada
        if (passwordEncoder.matches(passwordRaw, usuario.getContrasena())) {
            // Generar y retornar Token
            return jwtUtil.generateToken(usuario.getEmail(), usuario.getRol());
        } else {
            throw new RuntimeException("Contraseña incorrecta");
        }
    }
    
    public Usuario findById(int id) {
        return repository.findById(id).orElse(null);
    }   
    
    public Usuario findByEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }

    public Usuario findByNombre(String nombre) {
        return repository.findByNombre(nombre).orElse(null);
    }

    public Usuario findByGenero (String genero) {
        return repository.findByGenero(genero).orElse(null);
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
