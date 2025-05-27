package nl.hva.kieskeurig.repository;

import nl.hva.kieskeurig.model.Account;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


import java.util.List;


@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private EntityRepository<Account> accountRepository;

    @Override
    @Transactional
    public void run(String... args) {
        System.out.println("=== Initializing Database ===");
        this.createInitialAccounts();

        // Verify accounts were created
        List<Account> allAccounts = accountRepository.findAll();
        System.out.println("Total accounts in database: " + allAccounts.size());
        allAccounts.forEach(acc -> System.out.printf("Account: ID=%d, Email=%s, Hash=%s%n",
                acc.getId(), acc.getEmail(), acc.getHashedPassword()));
    }

    private void createInitialAccounts() {
        addIfNotExists("admin@hva.nl", "Admin", "welcome", "Administrator");
        addIfNotExists("user1@hva.nl", "User1", "welcome", "User");
        addIfNotExists("user2@hva.nl", "User2", "welcome", "User");
        addIfNotExists("user3@hva.nl", "User3", "welcome", "User");

        System.out.println("Accounts in database:");
        accountRepository.findAll().forEach(System.out::println);
    }

    private void addIfNotExists(String email, String name, String password, String role) {
        List<Account> existing = accountRepository.findByQuery("Accounts_find_by_email", email);
        if (!existing.isEmpty()) return;

        // Create account with basic info
        Account account = new Account(0, email, name);
        account.setRole(role);

        // Save to generate ID
        account = accountRepository.save(account);

        // Set password with proper ID-based hashing
        account.setPassword(password);

        // Final save
        accountRepository.save(account);

        System.out.println("Created account: " + email +
                " ID: " + account.getId() +
                " Hash: " + account.getHashedPassword());
    }
}