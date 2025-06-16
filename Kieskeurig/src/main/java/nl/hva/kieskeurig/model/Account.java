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
@Table(name = "accounts") // optioneel, maar goed voor duidelijkheid
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // laat de database zelf het ID genereren
    @Column(name = "id")
    private long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email = "";

    @Column(name = "role", nullable = false, length = 50)
    private String role = "Regular User";

    @JsonIgnore
    @Column(name = "hashed_password", nullable = false, length = 255)
    private String hashedPassword = null;

    @Transient // dit wordt niet opgeslagen in de database
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

    public String hashPassword(String password) {
        if (this.id == 0) {
            throw new IllegalStateException("Account must have an ID before hashing password");
        }
        String salt = "Id-" + this.id + ":";
        return SecureHasher.secureHash(salt + password);
    }

    public void setPassword(String password) {
        this.password = password;
        if (password != null && this.id > 0) {
            this.hashedPassword = hashPassword(password);
        } else {
            this.hashedPassword = null;
        }
    }

    public boolean verifyPassword(String password) {
        if (this.hashedPassword == null || password == null) return false;
        return this.hashedPassword.equals(hashPassword(password));
    }

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
            "Boekenwurm", "Pageturner", "Papiervreter", "Hobbyist",
            "Philosopher", "Journalist", "Scientist", "Teacher"
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
        return String.format("{ login=%s, name=%s, id=%d }", this.email, this.name, this.id);
    }
}