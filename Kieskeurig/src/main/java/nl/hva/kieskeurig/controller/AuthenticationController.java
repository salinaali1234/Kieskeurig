package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.config.APIConfig;
import nl.hva.kieskeurig.exception.NotAcceptableException;
import nl.hva.kieskeurig.model.Account;
import nl.hva.kieskeurig.repository.EntityRepository;
import nl.hva.kieskeurig.security.JWToken;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", exposedHeaders = "Authorization")
@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    @Autowired
    APIConfig apiConfig;

    @Autowired
    private EntityRepository<Account> accountsRepo;

    @PostMapping(path = "/login")
    public ResponseEntity<Account> authenticateAccount(
            @RequestBody ObjectNode signInInfo,
            HttpServletRequest request) {

        String email = signInInfo.get("email").asText();
        String password = signInInfo.get("password").asText();

        // check whether we need and have the source IP-address of the user
        String ipAddress = JWToken.getIpAddress(request);
        if (ipAddress == null) {
            throw new NotAcceptableException("Cannot identify your source IP-Address.");
        }

        List<Account> accounts = accountsRepo.findByQuery("Accounts_find_by_email", email);
        Account account = !accounts.isEmpty() ? accounts.getFirst() : null;
        System.out.println(accounts);

        if (account == null || !account.verfiyPassword(password)) {
            throw new NotAcceptableException("Cannot authenticate account with email=" + email);
        }

        // Issue a token for the account, valid for some time
        JWToken jwToken = new JWToken(account.getName(), account.getId(), account.getRole(), ipAddress);
        String tokenString = jwToken.encode(this.apiConfig.getIssuer(),
                this.apiConfig.getPassphrase(),
                this.apiConfig.getTokenDurationOfValidity());

        return ResponseEntity.accepted()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenString)
                .body(account);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        // TODO: voeg dit token toe aan een blacklist (in-memory of Redis e.d.)
        return ResponseEntity.noContent().build();
    }
}
