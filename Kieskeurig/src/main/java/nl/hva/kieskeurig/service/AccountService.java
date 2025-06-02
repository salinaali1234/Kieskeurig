package nl.hva.kieskeurig.service;

import nl.hva.kieskeurig.config.APIConfig;
import nl.hva.kieskeurig.exception.ResourceNotFoundException;
import nl.hva.kieskeurig.exception.UnAuthorizedException;
import nl.hva.kieskeurig.model.Account;
import nl.hva.kieskeurig.repository.EntityRepository;
import nl.hva.kieskeurig.security.JWToken;
import org.springframework.beans.factory.annotation.Autowired;
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
}