package nl.hva.kieskeurig.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecureHasher {

    // Remove static MessageDigest instance and secret salt
    private static final String HASH_ALGORITHM = "SHA-512";

    /**
     * Calculates a secure hash of the input string
     * @param input The string to hash
     * @return Base64-encoded hash value
     */
    public static String secureHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to create hash", e);
        }
    }
}