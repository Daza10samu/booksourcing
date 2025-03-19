package org.dazai.booksourcing.main.standalone.security

import org.dazai.booksourcing.shared.auth.BasicJwtProvider
import org.dazai.booksourcing.shared.auth.JwtFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class WebSecurityConfig {
    @Bean
    fun jwtFilter(jwtProvider: BasicJwtProvider): JwtFilter {
        return JwtFilter(
            jwtProvider
        )
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity, jwtFilter: JwtFilter): SecurityFilterChain? {
        http
            .csrf { csrf -> csrf.disable() }
            .httpBasic { basic -> basic.disable() }
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { requests ->
                requests.requestMatchers(
                    "websocket-endpoint",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                )
                .permitAll()
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .anyRequest()
                    .authenticated()
            }
        return http.build()
    }
}