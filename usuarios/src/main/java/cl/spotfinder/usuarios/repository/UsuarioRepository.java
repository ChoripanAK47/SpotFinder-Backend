package cl.spotfinder.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.spotfinder.usuarios.dto.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario,Integer>{

}
