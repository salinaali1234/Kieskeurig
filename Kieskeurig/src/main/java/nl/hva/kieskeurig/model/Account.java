package nl.hva.kieskeurig.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import nl.hva.kieskeurig.security.SecureHasher;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.*;

@Setter
@Getter
@NamedQueries({
        @NamedQuery(name="Accounts_find_by_email",
                query = "select a from Account a where a.email = ?1")
})
@Entity
public class Account {

    @SequenceGenerator(name="Account_ids", initialValue=100001)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="Account_ids")
    @Id
    private long id = 0L;

    private String name;
    private String email = "";
    private String role = "Regular User";

    @JsonIgnore
    private String hashedPassword = null;

    public Account() {
    }

    public Account(long id) {
        this.id = id;
    }

    public Account(long id, String email, String name) {
        this(id);
        this.setEmail(email);
        this.setName(name);
    }
    public String hashPassword(String password) {
        if (this.id == 0) {
            throw new IllegalStateException("Account must have an ID before hashing password");
        }
        // Consistent salt application - don't change this format
        String salt = "Id-" + this.id + ":";
        return SecureHasher.secureHash(salt + password);
    }

    public void setPassword(String password) {
        if (password == null) {
            this.hashedPassword = null;
        } else {
            // Always hash with current ID
            this.hashedPassword = hashPassword(password);
        }
    }

    public boolean verifyPassword(String password) {
        if (this.hashedPassword == null || password == null) {
            return false;
        }
        return this.hashedPassword.equals(hashPassword(password));
    }
    private static Random randomizer = new Random();

    public static Account createSample(long id) {
        return createSample(id, callNames[randomizer.nextInt(Account.callNames.length)]);
    }
    public static Account createSample(long id, String callName) {
        Account newAuthor = new Account(id, callName.toLowerCase()+"@hva.nl", callName);
        // password needs to be reset later, the password hash changes if the account id changes.
        newAuthor.setPassword("welcome");
        return newAuthor;
    }

    private static final String[] callNames = {
            "Boekenwurm", "Pageturner", "Papiervreter", "Hobbyist", "Philosopher", "Journalist", "Scientist", "Teacher"
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        return id == ((Account) o).id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("{ login=%s, callName=%s, id=%d }", this.email, this.name, this.id);
    }
}
