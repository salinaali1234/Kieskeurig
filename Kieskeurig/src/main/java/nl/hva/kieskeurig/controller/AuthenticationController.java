package nl.hva.kieskeurig.controller;


import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import nl.hva.kieskeurig.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller that handles authentication-related operations such as login and logout.
 * Exposes endpoints to authenticate users and optionally perform logout logic.
 * Issues JWT tokens upon successful login.
 */
@RestController
@RequestMapping("/authentication")
@CrossOrigin(origins = "*", exposedHeaders = HttpHeaders.AUTHORIZATION)
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Authenticates a user using provided login credentials.
     *
     * @param signInInfo JSON object with "email" and "password"
     * @param request HTTP request (for IP logging or token tracking)
     * @return LoginResponse (account + token), or 401 if invalid
     */
    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateAccount(@RequestBody ObjectNode signInInfo, HttpServletRequest request) {
        try {
            ResponseEntity<AuthenticationService.LoginResponse> result = authenticationService.authenticate(signInInfo, request);
            AuthenticationService.LoginResponse loginResponse = result.getBody();

            if (loginResponse == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid email or password.");
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .header(HttpHeaders.AUTHORIZATION, result.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                    .body(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login error: " + e.getMessage());
        }
    }

    /**
     * Handles user logout.
     *
     * @param authHeader JWT token in Authorization header
     * @return 204 No Content
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        // In de toekomst kun je hier een blacklist of audit log bijhouden
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}