package nl.hva.kieskeurig.repository;

import nl.hva.kieskeurig.model.Account;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


import java.util.List;

import static org.springframework.util.ClassUtils.getUserClass;

@Component
public class DataLoader implements CommandLineRunner {

    @Override
    @Transactional
    public void run(String... args)  {
        System.out.println("Running CommandLine Startup");
        this.createInitialAccounts();

        System.out.println("Injected accounts from " +
                (this.accountsRepo != null ? getUserClass(this.accountsRepo.getClass()).getName() : "none"));
    }

    @Autowired
    private EntityRepository<Account> accountsRepo;

    private void createInitialAccounts() {
        // check whether the repo is empty
        List<Account> accounts = this.accountsRepo.findAll();
        if (!accounts.isEmpty()) return;
        System.out.println("Configuring some initial accounts in the repository");
        accounts.add(this.accountsRepo.save(new Account(0, "admin@hva.nl", "Admin")));
        accounts.add(this.accountsRepo.save(new Account(0, "user1@hva.nl", "User1")));
        accounts.add(this.accountsRepo.save(new Account(0, "user2@hva.nl", "User2")));
        accounts.add(this.accountsRepo.save(new Account(0, "user3@hva.nl", "User3")));
        // updates will be persisted by the transaction
        for (Account a : accounts) {
            a.setPassword("welcome");
            System.out.println("Added account: " + a + " (initial password = 'welcome')");
        }
        accounts.getFirst().setRole("Administrator");
    }
}
