package nl.hva.kieskeurig.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import nl.hva.kieskeurig.config.APIConfig;
import nl.hva.kieskeurig.exception.NotAcceptableException;
import nl.hva.kieskeurig.model.Account;
import nl.hva.kieskeurig.repository.AccountRepo;
import nl.hva.kieskeurig.security.JWToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for authenticating users and issuing JWT tokens.
 * It validates email and password against stored accounts, and returns a token upon success.
 */
@Service
public class AuthenticationService {

    @Autowired
    private APIConfig apiConfig;

    @Autowired
    private AccountRepo accountsRepo;

    /**
     * Authenticates a user using email and password.
     * If valid, returns an account and JWT token in the Authorization header.
     *
     * @param signInInfo JSON containing "email" and "password"
     * @param request HTTP request used to extract the client's IP address
     * @return ResponseEntity with LoginResponse (account + token) and Authorization header
     * @throws NotAcceptableException if email/password are invalid or IP is unknown
     */
    public ResponseEntity<LoginResponse> authenticate(ObjectNode signInInfo, HttpServletRequest request) {
        String email = signInInfo.get("email").asText().trim().toLowerCase();
        String password = signInInfo.get("password").asText();

        List<Account> accounts = accountsRepo.findByEmail(email);
        if (accounts.isEmpty()) {
            throw new NotAcceptableException("Invalid credentials");
        }

        Account account = accounts.getFirst();
        if (!account.verifyPassword(password)) {
            throw new NotAcceptableException("Invalid credentials");
        }

        String ipAddress = JWToken.getIpAddress(request);
        if (ipAddress == null) {
            throw new NotAcceptableException("Cannot identify source IP");
        }

        JWToken jwToken = new JWToken(account.getName(), account.getId(), account.getRole(), ipAddress);
        String tokenString = jwToken.encode(
                apiConfig.getIssuer(),
                apiConfig.getPassphrase(),
                apiConfig.getTokenDurationOfValidity()
        );

        return ResponseEntity.accepted()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenString)
                .body(new LoginResponse(account, tokenString));
    }

    /**
     * Simple DTO that holds an account and its associated JWT token.
     *
     * @param account The authenticated account
     * @param token The issued JWT token
     */
    public record LoginResponse(Account account, String token) {}
}