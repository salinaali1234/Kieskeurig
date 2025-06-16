package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.model.Account;
import nl.hva.kieskeurig.security.JWToken;
import nl.hva.kieskeurig.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing user accounts.
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
     * Gets all accounts.
     * @return 200 OK + list of accounts
     */
    @GetMapping(path = "", produces = "application/json")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.status(HttpStatus.OK).body(accounts);
    }

    /**
     * Gets a single account by ID.
     * @param id Account ID
     * @return 200 OK or 404 Not Found
     */
    @GetMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<?> getOneAccount(@PathVariable long id) {
        Account account = accountService.getAccountById(id);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Account with ID " + id + " not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

    /**
     * Deletes account by ID if requester is admin.
     * @param id Account ID
     * @param jwtInfo JWT token info
     * @return 200 OK, 403 Forbidden, or 404 Not Found
     */
    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> deleteOneAccount(@PathVariable long id,
                                              @RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo) {
        try {
            Account deleted = accountService.deleteAccount(id, jwtInfo);
            if (deleted == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Account with ID " + id + " not found.");
            }
            return ResponseEntity.noContent().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not authorized to delete this account.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting account: " + e.getMessage());
        }
    }

    /**
     * Registers a new account.
     *  * The password is hashed and stored securely.
     * @param account JSON with email, password, etc.
     * @return 201 Created or 400 Bad Request
     */
    @PostMapping(path = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> register(@RequestBody Account account) {
        try {
            Account registered = accountService.registerAccount(account);
            return ResponseEntity.status(HttpStatus.CREATED).body(registered);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not register account.");
        }
    }
}