package cl.spotfinder.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.spotfinder.usuarios.dto.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByNombre(String nombre);
    Optional<Usuario> findByGenero(String genero);
}