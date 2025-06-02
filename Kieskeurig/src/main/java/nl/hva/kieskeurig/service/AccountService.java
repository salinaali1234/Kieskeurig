package nl.hva.kieskeurig.service;

import nl.hva.kieskeurig.config.APIConfig;
import nl.hva.kieskeurig.exception.NotAcceptableException;
import nl.hva.kieskeurig.exception.ResourceNotFoundException;
import nl.hva.kieskeurig.exception.UnAuthorizedException;
import nl.hva.kieskeurig.model.Account;
import nl.hva.kieskeurig.repository.EntityRepository;
import nl.hva.kieskeurig.security.JWToken;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private EntityRepository<Account> accountsRepo;

    @Autowired
    private APIConfig apiConfig;

    public List<Account> getAllAccounts() {
        return accountsRepo.findAll();
    }

    public Account getAccountById(long id) {
        Account account = accountsRepo.findById(id);
        if (account == null) {
            throw new ResourceNotFoundException("Cannot provide a account with id=" + id);
        }
        return account;
    }

    public Account deleteAccount(long id, JWToken jwtInfo) {
        if (jwtInfo == null || !jwtInfo.isAdmin()) {
            throw new UnAuthorizedException("Admin role is required to remove an account");
        }
        Account account = accountsRepo.deleteById(id);
        if (account == null) {
            throw new ResourceNotFoundException("Cannot delete an account with id=" + id);
        }
        return account;
    }

    public Account registerAccount(Account account) {
        if (account.getPassword() == null || account.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Wachtwoord is verplicht.");
        }
        account.setId(0L);
        Account saved = accountsRepo.save(account);
        saved.setPassword(account.getPassword());
        return accountsRepo.save(saved);
    }

    public ResponseEntity<Account> login(ObjectNode signInInfo, HttpServletRequest request) {
        String email = signInInfo.get("email").asText().trim().toLowerCase();
        String password = signInInfo.get("password").asText();

        List<Account> accounts = accountsRepo.findByQuery("Accounts_find_by_email", email);
        if (accounts.isEmpty()) {
            throw new NotAcceptableException("Invalid credentials");
        }

        Account account = accounts.get(0);
        if (!account.verifyPassword(password)) {
            throw new NotAcceptableException("Invalid credentials");
        }

        String ipAddress = JWToken.getIpAddress(request);
        if (ipAddress == null) {
            throw new NotAcceptableException("Cannot identify source IP");
        }

        JWToken jwToken = new JWToken(account.getName(), account.getId(), account.getRole(), ipAddress);
        String tokenString = jwToken.encode(apiConfig.getIssuer(), apiConfig.getPassphrase(), apiConfig.getTokenDurationOfValidity());

        return ResponseEntity.accepted()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenString)
                .body(account);
    }
}