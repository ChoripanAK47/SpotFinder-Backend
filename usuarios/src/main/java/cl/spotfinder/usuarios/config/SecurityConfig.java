package cl.spotfinder.usuarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthorizationFilter
     jwtAuthorizationFilter;

    public SecurityConfig(JwtAuthorizationFilter jwtAuthorizationFilter) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para APIs REST
            // Añadir nuestro filtro de JWT antes del filtro de autenticación estándar
            .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
                // Permitir acceso total a login y registro
                .requestMatchers("/api/v1/usuarios/add", "/api/v1/usuarios/login").permitAll()
                .anyRequest().authenticated() // El resto requiere autenticación
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // El estándar de oro para encriptar passwords
    }
}