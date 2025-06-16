package nl.hva.kieskeurig.security;

import lombok.Getter;
import lombok.Setter;
import nl.hva.kieskeurig.config.APIConfig;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * Represents a JWT token used for authentication and authorization.
 * Includes methods to encode, decode, and verify the token's IP and role.
 */
@Getter
public class JWToken {

    public static final String JWT_ATTRIBUTE_NAME = "JWTokenInfo";

    private static final String JWT_ROLE_CLAIM = "role";
    private static final String JWT_IPADDRESS_CLAIM = "ipa";

    private final String callName;
    private final Long accountId;
    private final String role;
    /**
     * -- SETTER --
     *  Sets the IP address associated with this token.
     *
     * @param ipAddress the client's IP address
     */
    @Setter
    private String ipAddress;

    /**
     * Constructs a token with identity information.
     *
     * @param callName  the display name of the user
     * @param accountId the account's ID
     * @param role      the user's role
     */
    public JWToken(String callName, Long accountId, String role) {
        this.callName = callName;
        this.accountId = accountId;
        this.role = role;
    }

    /**
     * Constructs a token with identity information and IP address.
     *
     * @param callName        the display name of the user
     * @param accountId       the account's ID
     * @param role            the user's role
     * @param sourceIpAddress the originating IP address of the request
     */
    public JWToken(String callName, Long accountId, String role, String sourceIpAddress) {
        this(callName, accountId, role);
        this.setIpAddress(sourceIpAddress);
    }

    /**
     * Encodes this object into a signed JWT string.
     *
     * @param issuer     the issuer name
     * @param passphrase the secret key
     * @param expiration token validity in seconds
     * @return the encoded JWT string
     */
    public String encode(String issuer, String passphrase, int expiration) {
        Key key = getKey(passphrase);

        return Jwts.builder()
                .setSubject(this.callName)
                .setId(this.accountId.toString())
                .claim(JWT_ROLE_CLAIM, this.role)
                .claim(JWT_IPADDRESS_CLAIM, this.ipAddress != null ? this.ipAddress : "1.1.1.1")
                .setIssuer(issuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Decodes a JWT string into a JWToken object.
     *
     * @param token      the JWT token string
     * @param issuer     the expected issuer
     * @param passphrase the secret used for signing
     * @return a validated JWToken instance
     * @throws ExpiredJwtException if the token has expired
     * @throws MalformedJwtException if the issuer is invalid or token is corrupted
     */
    public static JWToken decode(String token, String issuer, String passphrase)
            throws ExpiredJwtException, MalformedJwtException {
        Key key = getKey(passphrase);
        Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        Claims claims = jws.getBody();

        if (!claims.getIssuer().equals(issuer)) {
            throw new MalformedJwtException("Invalid issuer");
        }

        JWToken jwToken = new JWToken(
                claims.getSubject(),
                Long.valueOf(claims.getId()),
                claims.get(JWT_ROLE_CLAIM).toString()
        );
        jwToken.setIpAddress((String) claims.get(JWT_IPADDRESS_CLAIM));
        return jwToken;
    }

    /**
     * Validates whether the current token can impersonate a target account.
     *
     * @param targetAccountId the ID of the target account
     * @return the ID to use for the action, or -1 if impersonation is not allowed
     */
    public long validateImpersonation(long targetAccountId) {
        if (targetAccountId == 0) return this.accountId;
        if (targetAccountId == this.accountId || this.isAdmin()) return targetAccountId;
        return -1L;
    }

    /**
     * Returns true if the user has an admin role.
     */
    public boolean isAdmin() {
        return this.role != null && this.role.toLowerCase().contains("admin");
    }

    /**
     * Gets the IP address of the incoming request.
     *
     * @param request the HTTP request
     * @return the client's IP address
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader(APIConfig.IP_FORWARDED_FOR);
        return (ipAddress != null) ? ipAddress : request.getRemoteAddr();
    }

    /**
     * Creates a cryptographic key based on a passphrase.
     *
     * @param passphrase the secret string
     * @return a cryptographic key
     */
    private static Key getKey(String passphrase) {
        byte[] hmacKey = passphrase.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(hmacKey, SignatureAlgorithm.HS512.getJcaName());
    }
}