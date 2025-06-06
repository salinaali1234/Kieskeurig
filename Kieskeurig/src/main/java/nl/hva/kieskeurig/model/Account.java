package nl.hva.kieskeurig.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import nl.hva.kieskeurig.security.SecureHasher;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.*;

@Setter
@Getter
@Entity
@NamedQueries({
        @NamedQuery(name = "Accounts_find_by_email",
                query = "select a from Account a where a.email = ?1")
})
public class Account {

    @SequenceGenerator(name = "Account_ids", initialValue = 100001)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Account_ids")
    @Id
    private long id = 0L;

    private String name;
    private String email = "";
    private String role = "Regular User";

    @JsonIgnore
    private String hashedPassword = null;

    @Transient
    @JsonIgnore
    private String password;

    public Account() {}

    public Account(long id) {
        this.id = id;
    }

    public Account(long id, String email, String name) {
        this(id);
        this.email = email;
        this.name = name;
    }

    /**
     * Hashes a password using a consistent salt based on account ID.
     *
     * @param password the plain text password
     * @return the hashed version
     */
    public String hashPassword(String password) {
        if (this.id == 0) {
            throw new IllegalStateException("Account must have an ID before hashing password");
        }
        String salt = "Id-" + this.id + ":";
        return SecureHasher.secureHash(salt + password);
    }

    /**
     * Sets and hashes the password based on the current ID.
     *
     * @param password the plain text password
     */
    public void setPassword(String password) {
        this.password = password;
        if (password != null && this.id > 0) {
            this.hashedPassword = hashPassword(password);
        } else {
            this.hashedPassword = null;
        }
    }

    /**
     * Verifies a password by comparing it to the stored hash.
     *
     * @param password the input password
     * @return true if the hash matches, false otherwise
     */
    public boolean verifyPassword(String password) {
        if (this.hashedPassword == null || password == null) return false;
        return this.hashedPassword.equals(hashPassword(password));
    }

    /**
     * Creates a random sample account with the given ID.
     */
    public static Account createSample(long id) {
        return createSample(id, callNames[randomizer.nextInt(Account.callNames.length)]);
    }

    public static Account createSample(long id, String callName) {
        Account newAuthor = new Account(id, callName.toLowerCase() + "@hva.nl", callName);
        newAuthor.setPassword("welcome");
        return newAuthor;
    }

    private static final Random randomizer = new Random();
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
