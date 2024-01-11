package com.pri.petcationbackend;

import com.pri.petcationbackend.auth.JwtAuthorizationFilter;
import com.pri.petcationbackend.auth.JwtUtil;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final JwtUtil jwtUtil;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder bCryptPasswordEncoder)
            throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/login/**").permitAll()
                                .requestMatchers("/signup/**").permitAll()
                                .requestMatchers("/user").authenticated()
                                .requestMatchers("/logout").authenticated()
                                .requestMatchers("/deletePet/**").authenticated()
                                .requestMatchers("/usersPets/**").authenticated()
                                .requestMatchers("/addModifyPet").authenticated()
                                .requestMatchers("/deleteReservation").authenticated()
                                .requestMatchers("/reservation").authenticated()
                                .requestMatchers("/modifyUser").authenticated()
                                .requestMatchers("/changePassword").authenticated()
                                .requestMatchers("/rejectReservation").authenticated()
                                .requestMatchers("/currentReservations").authenticated()
                                .requestMatchers("/allReservations").authenticated()
                                .requestMatchers("/addHotelRate").authenticated()
                                .requestMatchers("/addRate").authenticated()
                                .requestMatchers("/getConflictedReservations").authenticated()
                                .requestMatchers("/deleteImage").authenticated()
                                .requestMatchers("/addImages").authenticated()
                                .requestMatchers("/acceptReservation").authenticated()
                                .requestMatchers("/**").permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .addLogoutHandler((request, response, auth) -> {
                            String token = jwtUtil.resolveToken(request);
                            jwtUtil.refreshToken(token);
                            if(request.getCookies() != null) {
                                for (Cookie cookie : request.getCookies()) {
                                    String cookieName = cookie.getName();
                                    Cookie cookieToDelete = new Cookie(cookieName, null);
                                    cookieToDelete.setMaxAge(0);
                                    response.addCookie(cookieToDelete);
                                }
                            }
                        })
                        .invalidateHttpSession(true)
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)))
                ;

        return http.build();
    }

}
