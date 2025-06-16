package nl.hva.kieskeurig.repository;

import nl.hva.kieskeurig.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Loads initial sample accounts into the repository on application startup.
 */
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private AccountRepo accountRepo;

    @Override
    public void run(String... args) {
        System.out.println("=== Initializing In-Memory Account Repo ===");
        createInitialAccounts();
// Verify accounts were created
        List<Account> allAccounts = accountRepo.findAll();
        System.out.println("Total accounts: " + allAccounts.size());
        allAccounts.forEach(acc ->
                System.out.printf("Account: ID=%d, Email=%s, Hash=%s%n",
                        acc.getId(), acc.getEmail(), acc.getHashedPassword()));
    }

    /**
     * Creates a list of initial accounts if they don't already exist.
     */
    private void createInitialAccounts() {
        addIfNotExists("admin@hva.nl", "Admin", "welcome", "Administrator");
        addIfNotExists("user1@hva.nl", "User1", "welcome", "User");
        addIfNotExists("user2@hva.nl", "User2", "welcome", "User");
        addIfNotExists("user3@hva.nl", "User3", "welcome", "User");
    }

    /**
     * Adds a new account only if no account with the same email exists.
     */
    private void addIfNotExists(String email, String name, String password, String role) {
        List<Account> existing = accountRepo.findByEmail(email);
        if (!existing.isEmpty()) return;

        // Create account with basic info
        Account account = new Account();
        account.setEmail(email);
        account.setName(name);
        account.setRole(role);
        account = accountRepo.save(account);


        account.setPassword(password);


        accountRepo.save(account);

        System.out.println("Created account: " + email +
                " ID: " + account.getId() +
                " Hash: " + account.getHashedPassword());
    }
}