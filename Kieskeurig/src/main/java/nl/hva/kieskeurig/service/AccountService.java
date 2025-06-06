package nl.hva.kieskeurig.service;

import lombok.Getter;
import lombok.Setter;
import nl.hva.kieskeurig.config.APIConfig;
import nl.hva.kieskeurig.exception.ResourceNotFoundException;
import nl.hva.kieskeurig.exception.UnAuthorizedException;
import nl.hva.kieskeurig.model.Account;
import nl.hva.kieskeurig.repository.EntityRepository;
import nl.hva.kieskeurig.security.JWToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Service class that handles business logic related to user accounts.
 * Provides methods for retrieving, creating, and deleting accounts.
 * Also handles authorization for sensitive actions like deletion.
 */
@Getter
@Setter
@Service
public class AccountService {

    @Autowired
    private EntityRepository<Account> accountsRepo;

    @Getter
    @Setter
    @Autowired
    private APIConfig apiConfig;

    /**
     * Retrieves all registered accounts from the repository.
     *
     * @return a list of all accounts
     */
    public List<Account> getAllAccounts() {
        return accountsRepo.findAll();
    }

    /**
     * Retrieves a single account based on its ID.
     *
     * @param id the unique identifier of the account
     * @return the account object if found
     * @throws ResourceNotFoundException if the account does not exist
     */
    public Account getAccountById(long id) {
        Account account = accountsRepo.findById(id);
        if (account == null) {
            throw new ResourceNotFoundException("Cannot provide an account with id=" + id);
        }
        return account;
    }

    /**
     * Deletes an account, only if the user making the request has admin privileges.
     *
     * @param id the ID of the account to delete
     * @param jwtInfo token containing user information and roles
     * @return the deleted account
     * @throws UnAuthorizedException if the user is not an admin
     * @throws ResourceNotFoundException if the account does not exist
     */
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

    /**
     * Registers a new account.
     * Hashes the password and saves the account in the repository.
     *
     * @param account the new account to register
     * @return the saved account
     * @throws IllegalArgumentException if no password is provided
     */
    public Account registerAccount(Account account) {
        if (account.getPassword() == null || account.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }
        account.setId(0L); // Ensure it's treated as a new account
        Account saved = accountsRepo.save(account);
        saved.setPassword(account.getPassword()); // Hash password
        return accountsRepo.save(saved);
    }

}