package org.mcezario.oauth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping( "/api" )
public class SampleController {

    @GetMapping( "/public" )
    public String publicEndpoint() {
        return "This is a public endpoint";
    }

    @GetMapping( "/user" )
    public Map<String, Object> userEndpoint(@AuthenticationPrincipal Jwt jwt) {
        return jwt.getClaims();
    }

    @GetMapping( "/user-role" )
    @PreAuthorize( "hasRole('USER')" )
    public String checkRoleViaMethod() {
        return "User has the ROLE_USER assigned";
    }

    @GetMapping( "/admin" )
    public String adminEndpoint() {
        return "Admin access granted!";
    }

}
