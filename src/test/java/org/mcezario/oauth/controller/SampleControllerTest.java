package org.mcezario.oauth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class SampleControllerTest {

    private static final Jwt DUMMY_JWT_TOKEN = Jwt.withTokenValue("fake-token")
            .header("alg", "none")
            .claim("sub", "123456")
            .claim("preferred_username", "fake-user")
            .claim("realm_access", Map.of("roles", List.of("ROLE_USER")))
            .build();

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser( username = "user", authorities = {"ROLE_USER"} )
    void shouldAuthorizeUsersWithRoleUser() throws Exception {
        // Given
        JwtAuthenticationToken token = new JwtAuthenticationToken(DUMMY_JWT_TOKEN, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(token);

        // When authenticated user has the USER_ROLE role and accesses the /api/user endpoint, then authorizes the request
        mockMvc.perform(get("/api/user").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(DUMMY_JWT_TOKEN.getClaims())));
    }

    @Test
    @WithMockUser( username = "user", authorities = {"ROLE_USER"} )
    void shouldNotAuthorizeUsersWithoutRoleAdmin() throws Exception {
        // When authenticated user doesn't have the USER_ADMIN role and accesses the /api/admin endpoint, then denies the request
        mockMvc.perform(get("/api/admin").accept(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser( username = "admin", authorities = {"ROLE_ADMIN"} )
    void shouldAuthorizeUsersWithRoleAdmin() throws Exception {
        // When authenticated user has the ROLE_ADMIN role and accesses the /api/admin endpoint, then authorizes the request
        mockMvc.perform(get("/api/admin").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Admin access granted!"));
    }

    @Test
    @WithMockUser( username = "admin", authorities = {"ROLE_ADMIN"} )
    void shouldNotAuthorizeUsersWithoutRoleUser() throws Exception {
        // When authenticated user doesn't have the USER_ROLE ole and accesses the /api/user endpoint, then denies the request
        mockMvc.perform(get("/api/user").accept(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenUnauthenticated_thenAccessIsDenied() throws Exception {
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/admin"))
                .andExpect(status().isUnauthorized());
    }
}
