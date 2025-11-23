package cl.spotfinder.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioProfile {
    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
    private String genero;
}
