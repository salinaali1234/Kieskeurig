package nl.hva.kieskeurig.controller;


import nl.hva.kieskeurig.model.Account;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import nl.hva.kieskeurig.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller that handles authentication-related operations such as login and logout.
 *
 * Exposes endpoints to authenticate users and optionally perform logout logic.
 * Issues JWT tokens upon successful login.
 */
@RestController
@RequestMapping("/authentication")
@CrossOrigin(origins = "*", exposedHeaders = "Authorization")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Authenticates a user using provided login credentials.
     *
     * @param signInInfo JSON object containing "email" and "password" fields
     * @param request HTTP request used to extract IP address for token generation
     * @return ResponseEntity with the authenticated Account and a JWT token in the Authorization header
     */

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> authenticateAccount(@RequestBody ObjectNode signInInfo, HttpServletRequest request) {
        return authenticationService.authenticate(signInInfo, request);
    }

    /**
     * Handles user logout.
     * Currently a placeholder – can be extended with JWT token blacklisting if required.
     *
     * @param authHeader Authorization header containing the JWT token
     * @return 204 No Content response
     */

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        // Extra: hier kan je in de toekomst een JWT-blacklist logica inbouwen
        return ResponseEntity.noContent().build();
    }
}