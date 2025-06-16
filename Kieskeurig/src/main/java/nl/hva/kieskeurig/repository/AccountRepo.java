package nl.hva.kieskeurig.repository;

import nl.hva.kieskeurig.model.Account;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountRepo {
    private final List<Account> accounts = new ArrayList<>();
    private long nextId = 1;

    public List<Account> findAll() {
        return accounts;
    }

    public Account findById(long id) {
        return accounts.stream().filter(a -> a.getId() == id).findFirst().orElse(null);
    }

    public Account save(Account account) {
        if (account.getId() == 0) {
            account.setId(nextId++); // ✅ Genereer automatisch een ID
        }

        deleteById(account.getId()); // verwijder oude met hetzelfde ID (indien aanwezig)
        accounts.add(account);
        return account;
    }

    public Account deleteById(long id) {
        Account acc = findById(id);
        if (acc != null) accounts.remove(acc);
        return acc;
    }

    public List<Account> findByEmail(String email) {
        return accounts.stream()
                .filter(a -> a.getEmail().equalsIgnoreCase(email))
                .toList();
    }
}