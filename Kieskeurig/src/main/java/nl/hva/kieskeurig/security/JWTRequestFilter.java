package nl.hva.kieskeurig.security;

import nl.hva.kieskeurig.config.APIConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Objects;

/**
 * A request filter that intercepts incoming HTTP requests to validate JWT tokens
 * for secured paths. If a token is missing, invalid, or its IP address does not match,
 * the request is blocked with an HTTP 401 Unauthorized response.
 */
@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    @Autowired
    private APIConfig apiConfig;

    /**
     * Intercepts each HTTP request and performs JWT authentication for secured paths.
     * Allows all OPTIONS requests and non-secured paths to pass through.
     *
     * @param request  the incoming HTTP request
     * @param response the HTTP response
     * @param chain    the filter chain
     * @throws IOException if an I/O error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String servletPath = request.getServletPath();

        // Allow preflight (OPTIONS) requests and all non-secured paths
        if (HttpMethod.OPTIONS.matches(request.getMethod()) ||
                APIConfig.SECURED_PATHS.stream().noneMatch(servletPath::startsWith)) {
            chain.doFilter(request, response);
            return;
        }
        if (request.getMethod().equals("POST") && request.getServletPath().equals("/accounts")) {
            chain.doFilter(request, response);
            return;
        }

        // Extract the JWT from the Authorization header
        String encryptedToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (encryptedToken == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No token provided. You need to log in first.");
            return;
        }

        // Decode and verify the JWT
        JWToken jwToken;
        try {
            jwToken = JWToken.decode(encryptedToken.replace("Bearer ", ""),
                    apiConfig.getIssuer(), apiConfig.getPassphrase());
        } catch (RuntimeException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage() + " You need to log in first.");
            return;
        }

        // Get and verify the request's source IP address
        String sourceIpAddress = JWToken.getIpAddress(request);
        if (sourceIpAddress == null || !sourceIpAddress.equals(jwToken.getIpAddress())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Cannot identify or validate your source IP address.");
            return;
        }

        // Pass the token to the next filter/controller via request attribute
        request.setAttribute(JWToken.JWT_ATTRIBUTE_NAME, jwToken);
        chain.doFilter(request, response);
    }

    /**
     * Adds CORS headers to the response. This method is not used by default,
     * but can be helpful for debugging or custom CORS handling.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     */
    private void addAccessControlHeaders(HttpServletRequest request, HttpServletResponse response) {
        String originHeader = request.getHeader(HttpHeaders.ORIGIN);
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, Objects.requireNonNullElse(originHeader, "*"));
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,POST,PUT,DELETE,OPTIONS");

        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                HttpHeaders.AUTHORIZATION + ", " + HttpHeaders.CONTENT_TYPE + ", " + APIConfig.IP_FORWARDED_FOR);
        response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
                HttpHeaders.AUTHORIZATION + ", " + HttpHeaders.CONTENT_TYPE + ", " + APIConfig.IP_FORWARDED_FOR);
    }

    /**
     * Logs request headers, path info, and cookies for debugging purposes.
     *
     * @param request the HTTP request to log
     */
    private void logRequestContextAndCookies(HttpServletRequest request) {
        System.out.println("Request Method=" + request.getMethod());
        System.out.println("Request ServerName=" + request.getServerName());
        System.out.println("Request ServletPath=" + request.getServletPath());
        System.out.println("Request ContextPath=" + request.getContextPath());
        System.out.println("Request PathTranslated=" + request.getPathTranslated());
        System.out.println("Request PathInfo=" + request.getPathInfo());
        System.out.println("Request RequestURI=" + request.getRequestURI());
        System.out.println("Request QueryString=" + request.getQueryString());
        System.out.println("Request RemoteUser=" + request.getRemoteUser());

        // Print request headers
        Enumeration<String> headerNames = request.getHeaderNames();
        System.out.println("Request headers:");
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                System.out.println("Header['" + name + "'] = " + request.getHeader(name));
            }
        }

        // Print cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println("Cookie['" + cookie.getName() + "']: " + cookie.getValue());
            }
        }
    }
}