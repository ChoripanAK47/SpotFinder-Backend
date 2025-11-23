package cl.spotfinder.usuarios.service;

import cl.spotfinder.usuarios.dto.Usuario; 
import cl.spotfinder.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// IMPORTA ESTO:
import org.springframework.security.crypto.password.PasswordEncoder; // <--- NUEVO

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock // <--- AGREGA ESTE MOCK
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;
    //1
    @Test
    void deberiaGuardarUsuarioExitosamente() {
        
        Usuario usuarioPrueba = new Usuario();
        usuarioPrueba.setNombre("Usuario Test"); 
        usuarioPrueba.setContrasena("123"); 

        when(passwordEncoder.encode(any())).thenReturn("clave-encriptada-falsa"); 
       
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioPrueba);

        // --- 2. ACT ---
        Usuario resultado = usuarioService.saveUsuario(usuarioPrueba);

        // --- 3. ASSERT ---
        assertNotNull(resultado);
        assertEquals("Usuario Test", resultado.getNombre());
        
        verify(usuarioRepository).save(any(Usuario.class));
    }
   

    @Mock 
    private cl.spotfinder.usuarios.util.JwtUtil jwtUtil;

    @Test
    void deberiaHacerLoginExitoso() {
        // 1. ARRANGE
        String email = "juan@test.com";
        String pass = "123";
        Usuario usuarioSimulado = new Usuario();
        usuarioSimulado.setEmail(email);
        usuarioSimulado.setContrasena("clave-encriptada"); 
        usuarioSimulado.setRol("USER");

        when(usuarioRepository.findByEmail(email)).thenReturn(java.util.Optional.of(usuarioSimulado));
        
        when(passwordEncoder.matches(pass, "clave-encriptada")).thenReturn(true);
        
        when(jwtUtil.generateToken(email, "USER")).thenReturn("token-falso-123");

        String token = usuarioService.login(email, pass);

        assertEquals("token-falso-123", token);
    }

    @Test
    void deberiaLanzarErrorSiPasswordEsIncorrecta() {
        // 1. ARRANGE
        String email = "juan@test.com";
        Usuario usuarioSimulado = new Usuario();
        usuarioSimulado.setEmail(email);
        usuarioSimulado.setContrasena("clave-encriptada");

        when(usuarioRepository.findByEmail(email)).thenReturn(java.util.Optional.of(usuarioSimulado));
        
        when(passwordEncoder.matches("clave-erronea", "clave-encriptada")).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.login(email, "clave-erronea");
        });

        assertEquals("Contraseña incorrecta", exception.getMessage());
    }
    @Test
    void deberiaLanzarErrorSiElEmailYaExiste() {
        // --- 1. ARRANGE ---
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setEmail("repetido@test.com");
        nuevoUsuario.setContrasena("123456");

        when(usuarioRepository.findByEmail("repetido@test.com"))
            .thenReturn(java.util.Optional.of(new Usuario()));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.saveUsuario(nuevoUsuario);
        });

        assertEquals("El email ya está registrado", exception.getMessage());
        
        verify(usuarioRepository, org.mockito.Mockito.never()).save(any(Usuario.class));
    }
  
    @Test
    void deberiaEncontrarUsuarioPorId() {
        // 1. ARRANGE
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Buscado");
        
        when(usuarioRepository.findById(1)).thenReturn(java.util.Optional.of(usuario));

        // 2. ACT
        Usuario resultado = usuarioService.findById(1);

        // 3. ASSERT
        assertNotNull(resultado);
        assertEquals("Buscado", resultado.getNombre());
    }

    @Test
    void deberiaRetornarNullSiIdNoExiste() {
        // 1. ARRANGE
        when(usuarioRepository.findById(99)).thenReturn(java.util.Optional.empty());

        // 2. ACT
        Usuario resultado = usuarioService.findById(99);

        // 3. ASSERT
        assertNull(resultado, "Debería retornar null si no existe");
    }

    @Test
    void deberiaListarTodosLosUsuarios() {
        // 1. ARRANGE
        java.util.List<Usuario> listaSimulada = java.util.Arrays.asList(new Usuario(), new Usuario());
        when(usuarioRepository.findAll()).thenReturn(listaSimulada);

        // 2. ACT
        java.util.List<Usuario> resultados = usuarioService.getAllUsuarios();

        // 3. ASSERT
        assertNotNull(resultados);
        assertEquals(2, resultados.size(), "Debería traer 2 usuarios");
    }

    @Test
    void deberiaEliminarUsuario() {
        // 1. ACT
        usuarioService.deleteUsuario(5);

        // 2. ASSERT
        verify(usuarioRepository).deleteById(5);
    }

    @Test
    void deberiaActualizarUsuarioYEncriptarClave() {
        // 1. ARRANGE
        Usuario usuarioAntiguo = new Usuario();
        usuarioAntiguo.setId(1);
        usuarioAntiguo.setNombre("Viejo Nombre");
        usuarioAntiguo.setContrasena("clave-vieja-encriptada");

        Usuario datosNuevos = new Usuario();
        datosNuevos.setId(1);
        datosNuevos.setNombre("Nuevo Nombre");
        datosNuevos.setContrasena("nueva123"); 

        // Simulamos que existe el usuario antiguo
        when(usuarioRepository.findById(1)).thenReturn(java.util.Optional.of(usuarioAntiguo));
        
        // Simulamos la encriptación de la nueva clave
        when(passwordEncoder.encode("nueva123")).thenReturn("clave-nueva-encriptada-falsa");
        
        // Simulamos el guardado
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 2. ACT
        Usuario resultado = usuarioService.updateUsuario(datosNuevos);

        // 3. ASSERT
        assertNotNull(resultado);
        assertEquals("Nuevo Nombre", resultado.getNombre()); // Nombre cambio
        assertEquals("clave-nueva-encriptada-falsa", resultado.getContrasena()); // Clave se encripto
    }
}