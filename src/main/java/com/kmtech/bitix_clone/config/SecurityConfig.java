package com.kmtech.bitix_clone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/VAADIN/**", "/api/**", "/logout", "/**") // Отключаем CSRF только для Vaadin и logout
                )
                .authorizeHttpRequests(authorize -> authorize
                        // Разрешаем ресурсы Vaadin
                        .requestMatchers("/VAADIN/**", "/frontend/**", "/webjars/**", "/connect/**").permitAll()
                        // Разрешаем маршруты для входа и выхода
                        .requestMatchers("/login", "/error", "/logout").permitAll()
                        // Разрешаем запросы Vaadin (например, /?v-r=init, /?v-r=uidl и т.д.)
                        .requestMatchers(new AntPathRequestMatcher("/**", "GET", false)).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/**", "POST", false)).permitAll()
                        // Закрытые страницы — только для пользователей с ролью USER
                        .requestMatchers("/requests", "/clients", "/equipment", "/send-email", "/equipment/maintenance", "/add-equipment", "/create-request").hasRole("USER")
                        // Все остальные запросы требуют аутентификации
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(oidcUserService()))
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(oidcLogoutSuccessHandler())
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return http.build();
    }

    @Bean
    public OidcUserService oidcUserService() {
        System.out.println("Creating OidcUserService bean");

        OidcUserService oidcUserService = new OidcUserService();

        return new OidcUserService() {
            @Override
            public OidcUser loadUser(org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest userRequest) {
                System.out.println("OidcUserService.loadUser called");

                OidcUser oidcUser = oidcUserService.loadUser(userRequest);

                System.out.println("User attributes: " + oidcUser.getAttributes());

                List<String> roles = List.of();
                Object realmAccess = oidcUser.getAttribute("realm_access");
                if (realmAccess instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> realmAccessMap = (Map<String, Object>) realmAccess;
                    Object rolesObj = realmAccessMap.get("roles");
                    if (rolesObj instanceof List) {
                        @SuppressWarnings("unchecked")
                        List<String> extractedRoles = (List<String>) rolesObj;
                        roles = extractedRoles;
                    } else {
                        System.out.println("Roles field is not a List: " + rolesObj);
                    }
                } else {
                    System.out.println("realm_access is not a Map: " + realmAccess);
                }

                System.out.println("Extracted roles: " + roles);

                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                authorities.addAll(oidcUser.getAuthorities().stream()
                        .map(auth -> new SimpleGrantedAuthority(auth.getAuthority()))
                        .collect(Collectors.toList()));

                System.out.println("Final authorities: " + authorities);

                return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo(), "preferred_username");
            }
        };
    }

    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        return (request, response, authentication) -> {
            String keycloakLogoutUrl = "http://localhost:8082/realms/serviceapp/protocol/openid-connect/logout" +
                    "?client_id=service-app-client" +
                    "&post_logout_redirect_uri=" + URLEncoder.encode("http://localhost:8080/login?logout", StandardCharsets.UTF_8);

            if (authentication != null) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof OidcUser) {
                    String idToken = ((OidcUser) principal).getIdToken().getTokenValue();
                    keycloakLogoutUrl += "&id_token_hint=" + URLEncoder.encode(idToken, StandardCharsets.UTF_8);
                    System.out.println("Added id_token_hint to Keycloak logout URL: " + idToken);
                } else {
                    System.out.println("Authentication principal is not an OidcUser: " + principal);
                }
            } else {
                System.out.println("Authentication is null, skipping id_token_hint");
            }

            System.out.println("Redirecting to Keycloak logout URL: " + keycloakLogoutUrl);
            response.sendRedirect(keycloakLogoutUrl);
            System.out.println("Redirect to Keycloak completed");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            throw new UnsupportedOperationException("UserDetailsService не используется, так как настроен Keycloak.");
        };
    }
}