package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.model.Account;
import nl.hva.kieskeurig.security.JWToken;
import nl.hva.kieskeurig.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/accounts")
public class AccountsController {

    @Autowired
    private AccountService accountService;

    @GetMapping(path = "", produces = "application/json")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Account> getOneAccount(@PathVariable long id) {
        Account account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @DeleteMapping(path = "{id}")
    public Account deleteOneAccount(@PathVariable long id,
                                    @RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo) {
        return accountService.deleteAccount(id, jwtInfo);
    }

    @PostMapping(path = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        Account registered = accountService.registerAccount(account);
        return ResponseEntity.ok(registered);
    }
}