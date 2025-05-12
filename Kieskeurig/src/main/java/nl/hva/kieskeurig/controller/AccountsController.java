package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.exception.*;
import nl.hva.kieskeurig.exception.ResourceNotFoundException;
import nl.hva.kieskeurig.model.Account;
import nl.hva.kieskeurig.repository.EntityRepository;
import nl.hva.kieskeurig.security.JWToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountsController {

    @Autowired
    EntityRepository<Account> accountsRepo;

    @GetMapping(path = "", produces = "application/json")
    public List<Account> getAllAccounts() {
        return this.accountsRepo.findAll();
    }

    @GetMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Account> getOneAccount(@PathVariable() long id) {
        Account account = this.accountsRepo.findById(id);

        if (account == null) {
            throw new ResourceNotFoundException("Cannot provide a account with id="+id);
        }

        return ResponseEntity.ok().body(account);
    }

    @DeleteMapping(path = "{id}")
    public Account deleteOneAccount(@PathVariable() long id,
       @RequestAttribute(name = JWToken.JWT_ATTRIBUTE_NAME) JWToken jwtInfo) {

        if (jwtInfo == null || !jwtInfo.isAdmin()) {
            throw new UnAuthorizedException(
                    "Admin role is required to remove an account");
        }
        Account account = this.accountsRepo.deleteById(id);

        if (account == null) {
            throw new ResourceNotFoundException("Cannot delete an account with id="+id);
        }

        return account;
    }
}
