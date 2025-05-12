package nl.hva.kieskeurig.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class SecureHasher {

    // Configure a password hasher
    // See https://www.baeldung.com/java-password-hashing
    private static MessageDigest saltedMessageDigest = getMessageDigest("SHA-512");
    private static String secretSalt = "HvA-HBO-ICT-2022";
    private static MessageDigest getMessageDigest(String algorithm) {
        //System.out.println("GetMD-" + algorithm);
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(algorithm);
            // apply a secret salt to the hasher
            md.update(String.format("%-16s",secretSalt).getBytes(StandardCharsets.UTF_8));
            // for more security, add more salt, maybe different salt...

            // reset the md before its first use
            md.reset();
            return md;
        } catch (Exception ex) {
            // try to fallback on SHA-256
            if (!algorithm.equals("SHA-256"))
                return getMessageDigest("SHA-256");
        }
        // nothing found
        return null;
    }

    /**
     * Calculate a secure hash from a soource for the purpose of password hashing.
     * @param source
     * @return
     */
    public static String secureHash(String source) {
        if (saltedMessageDigest == null || source == null) return null;
        // hash the source using the salted SHA hasher
        byte[] hashedResult = saltedMessageDigest.digest(source.getBytes(StandardCharsets.UTF_8));
        // for more security, waste some time, maybe sleep a bit...
        // convert the hashed result to a string
        String result = Base64.getEncoder().encodeToString(hashedResult);
        //System.out.println(source + " was hashed into " + result);
        return result;
    }
}
