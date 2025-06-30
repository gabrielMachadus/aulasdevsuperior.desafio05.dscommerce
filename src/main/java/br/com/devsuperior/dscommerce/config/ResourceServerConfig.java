package br.com.devsuperior.dscommerce.config;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Profile("test")
@Configuration
@EnableWebSecurity
public class ResourceServerConfig {

	private final Environment env;

	public ResourceServerConfig(Environment env) {
		this.env = env;
	}

	@Value("${cors.origins}")
	private String corsOrigins;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		// H2-console
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers(headers -> headers.frameOptions(frame -> frame.disable()));
			http.authorizeHttpRequests(auth -> auth.requestMatchers(PathRequest.toH2Console()).permitAll());
		}

		http.csrf(csrf -> csrf.disable());
		http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.authorizeHttpRequests(auth -> auth
				.requestMatchers(HttpMethod.GET, "/products/**").permitAll()
				.requestMatchers(HttpMethod.POST, "/products/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")
				.requestMatchers("/orders/**").authenticated()
				.requestMatchers(HttpMethod.GET, "/users/me").authenticated()
				.anyRequest().permitAll());
		http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

		return http.build();
	}

	private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
		authoritiesConverter.setAuthoritiesClaimName("authorities");
		authoritiesConverter.setAuthorityPrefix("");
		converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
		return converter;
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		String[] origins = corsOrigins.split(",");
		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedOriginPatterns(Arrays.asList(origins));
		corsConfig.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "PATCH"));
		corsConfig.setAllowCredentials(true);
		corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		return source;
	}
}