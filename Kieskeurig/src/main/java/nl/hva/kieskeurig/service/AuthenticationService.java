package nl.hva.kieskeurig.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import nl.hva.kieskeurig.config.APIConfig;
import nl.hva.kieskeurig.exception.NotAcceptableException;
import nl.hva.kieskeurig.model.Account;
import nl.hva.kieskeurig.repository.EntityRepository;
import nl.hva.kieskeurig.security.JWToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Service class responsible for authenticating users and issuing JWT tokens.
 * Validates credentials and returns a secure token if successful.
 */
@Service
public class AuthenticationService {

    @Autowired
    private APIConfig apiConfig;

    @Autowired
    private EntityRepository<Account> accountsRepo;

    /**
     * Authenticates a user using their email and password.
     * If valid, returns a JWT token in the Authorization header.
     *
     * @param signInInfo JSON object with "email" and "password"
     * @param request the HTTP request used to extract the IP address
     * @return a ResponseEntity containing the authenticated account and JWT
     * @throws NotAcceptableException if credentials are invalid or IP is unknown
     */
    public ResponseEntity<Account> authenticate(ObjectNode signInInfo, HttpServletRequest request) {
        String email = signInInfo.get("email").asText().trim().toLowerCase();
        String password = signInInfo.get("password").asText();

        List<Account> accounts = accountsRepo.findByQuery("Accounts_find_by_email", email);
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
                .body(account);
    }
}