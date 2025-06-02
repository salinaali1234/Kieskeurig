package nl.hva.kieskeurig.controller;


import nl.hva.kieskeurig.model.Account;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import nl.hva.kieskeurig.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/authentication")
@CrossOrigin(origins = "*", exposedHeaders = "Authorization")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> authenticateAccount(@RequestBody ObjectNode signInInfo, HttpServletRequest request) {
        return authenticationService.authenticate(signInInfo, request);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        // Extra: hier kan je in de toekomst een JWT-blacklist logica inbouwen
        return ResponseEntity.noContent().build();
    }
}