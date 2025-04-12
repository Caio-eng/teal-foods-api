package br.com.foods.teal.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Classe reponsável por manitorar a segurança da aplicação
 * 
 * @author Caio Pereira Leal
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/**
	 * Classe responsábel por configurar os cors
	 * 
	 * @param http
	 * 			{@link HttpSecurity http}
	 * 
	 * @return o bulid do http
	 * 
	 * @throws Exception
	 * 				Exceção caso ocorra erro
	 */
    @SuppressWarnings("removal")
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                		"/h2-console/**",
                		"/products/images/**",
                		"/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs",
                        "/v3/api-docs/**"
                		).permitAll() 
                .anyRequest().permitAll())  
            .headers(headers -> 
                headers
                    .frameOptions().sameOrigin()  
                    .disable()
            );

        return http.build();
    }

    /**
     * Metódo responsável por liberar os cors para comunicação de API
     * 
     * @return source configurado
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
