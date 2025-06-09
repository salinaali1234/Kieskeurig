package nl.hva.kieskeurig.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Configuration
@EnableWebSecurity
public class APIConfig {

    public static final String IP_FORWARDED_FOR = "X-Forwarded-For";
    public static final Set<String> SECURED_PATHS = Set.of("/accounts");
    private static final double REBOOT_CODE = 63.0427;

    // JWT-instellingen (kan @Value gebruiken als je dat wilt)
    private String issuer = "private company";
    private String passphrase = "This is very secret information for my private encryption key. extra words blalala salina is cool.";
    private int tokenDurationOfValidity = 1200; // 20 min

    public static String getHostIPAddressPattern() {
        try {
            return "http://" + Inet4Address.getLocalHost().getHostAddress() + ":*";
        } catch (UnknownHostException ignored) {
        }
        return "http://192.168.*.*:*";
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // endpoints die wél vrij zijn:
                        .requestMatchers(HttpMethod.POST, "/authentication/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/authentication/logout").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/public/**", "/h2-console/**").permitAll()

                        // álle overige GET/POST/etc kun je hier expliciet openzetten:
                        .requestMatchers("/api/partiesInfo/**").permitAll()
                        .requestMatchers("/api/candidates/**").permitAll()
                        .requestMatchers("/api/constituencies/**").permitAll()
                        .requestMatchers("/api/party/**").permitAll()
                        .requestMatchers("/api/provinces/**").permitAll()
                        .requestMatchers("/api/xml/**").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/api-docs"
                        ).permitAll()


                        // alleen /accounts/** moet beveiligd:
                        .requestMatchers("/api/accounts/**").authenticated()

                        // en als je écht klaar bent, laat je de rest óók open (of je gooit 'm weg):
                        .anyRequest().permitAll()
                )
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOriginPatterns(List.of(
                "http://localhost:*",
                getHostIPAddressPattern(),
                "http://*.hva.nl:*"
        ));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                IP_FORWARDED_FOR
        ));
        cfg.setExposedHeaders(List.of(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                IP_FORWARDED_FOR
        ));
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return src;
    }


    // getters/setters voor JWT-instellingen …

    public String getIssuer() {
        return String.format("%s-%f", this.issuer, REBOOT_CODE);
    }

    public String getPassphrase() {
        return passphrase;
    }

    public int getTokenDurationOfValidity() {
        return tokenDurationOfValidity;
    }

}