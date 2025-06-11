package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.model.Account;
import nl.hva.kieskeurig.security.JWToken;
import nl.hva.kieskeurig.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing user accounts.
 *
 * Provides endpoints to:
 * - Retrieve all accounts
 * - Retrieve a single account by ID
 * - Delete an account (admin only)
 * - Register a new account
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/accounts")
public class AccountsController {

    @Autowired
    private AccountService accountService;
    /**
     * Retrieves a list of all accounts.
     *
     * @return List of Account objects
     */
    @GetMapping(path = "", produces = "application/json")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }
    /**
     * Retrieves a specific account by ID.
     *
     * @param id ID of the account to fetch
     * @return ResponseEntity with the requested Account, or 404 if not found
     */
    @GetMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Account> getOneAccount(@PathVariable long id) {
        Account account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }
    /**
     * Deletes an account by ID.
     * Only users with the "admin" role are authorized.
     *
     * @param id ID of the account to delete
     * @param jwtInfo JWT token info extracted from the request (used to verify role)
     * @return The deleted Account
     */
    @DeleteMapping(path = "{id}")
    public Account deleteOneAccount(@PathVariable long id,
                                    @RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo) {
        return accountService.deleteAccount(id, jwtInfo);
    }
    /**
     * Registers a new account.
     * The password is hashed and stored securely.
     *
     * @param account Account object from the request body
     * @return The newly created Account with ID assigned
     */
    @PostMapping(path = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        Account registered = accountService.registerAccount(account);
        return ResponseEntity.ok(registered);
    }
}