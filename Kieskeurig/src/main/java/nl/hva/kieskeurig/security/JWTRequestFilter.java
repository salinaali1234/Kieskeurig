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
 * A request filter that intercepts HTTP requests and checks for a valid JWT token
 * in secured paths. If the token is missing, invalid, or doesn't match the source IP,
 * the request will be blocked with a 401 Unauthorized response.
 */
@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    @Autowired
    APIConfig apiConfig;
    /**
     * Applies filtering logic on each HTTP request to check JWT validity.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param chain    the filter chain
     * @throws IOException      in case of I/O errors
     * @throws ServletException in case of servlet errors
     */
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                 FilterChain chain) throws IOException, ServletException {

        String servletPath = request.getServletPath();

        // OPTIONS requests and non-secured area should pass through without check
        if (HttpMethod.OPTIONS.matches(request.getMethod()) ||
                this.apiConfig.SECURED_PATHS.stream().noneMatch(servletPath::startsWith)) {

            chain.doFilter(request, response);
            return;
        }


        // get the encrypted token string from the authorization request header
        String encryptedToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        // block the request if no token was found
        if (encryptedToken == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No token provided. You need to logon first.");
            return;
        }

        // decode the encoded and signed token, after removing optional Bearer prefix
        JWToken jwToken;
        try {
            jwToken = JWToken.decode(encryptedToken.replace("Bearer ", ""),
                    this.apiConfig.getIssuer(), this.apiConfig.getPassphrase());
        } catch (RuntimeException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage() + " You need to logon first.");
            return;
        }

        // obtain the source ip address of the request
        String sourceIpAddress = JWToken.getIpAddress(request);

        // block the request if the source ip cannot be identified
        if (sourceIpAddress == null
                || !sourceIpAddress.equals(jwToken.getIpAddress())
        ) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Cannot identify or validate your source IP-Address.");
            return;
        }

        // pass-on the token info as an attribute for the request
        request.setAttribute(JWToken.JWT_ATTRIBUTE_NAME, jwToken);

        chain.doFilter(request, response);
    }
    /**
     * Adds CORS headers to the response (not called in current flow).
     */
    private void addAccessControlHeaders(HttpServletRequest request,
                                         HttpServletResponse response) {
        // add the access-control headers
        String originHeader = request.getHeader(HttpHeaders.ORIGIN);
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, Objects.requireNonNullElse(originHeader, "*"));

        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");

        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,POST,PUT,DELETE,OPTIONS");
        //response.addHeader("Access-Control-Allow-Headers", "Authorization, Origin, X-Requested-With, X-Forwarded-For, Content-Type, Accept");
        //response.addHeader("Access-Control-Expose-Headers", "Authorization, Content-Type");
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                HttpHeaders.AUTHORIZATION + ", " + HttpHeaders.CONTENT_TYPE + ", " + APIConfig.IP_FORWARDED_FOR);
        response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
                HttpHeaders.AUTHORIZATION + ", " + HttpHeaders.CONTENT_TYPE + ", " + APIConfig.IP_FORWARDED_FOR);
    }
    /**
     * Logs request metadata and cookies for debugging purposes.
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

        // show headers
        Enumeration<String> headerNames = request.getHeaderNames();
        System.out.println("Requestheaders:");
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String nextHeaderName = headerNames.nextElement();
                System.out.println("Header['" + nextHeaderName + "'] = " + request.getHeader(nextHeaderName));
            }
        }

        // show cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                String cookieValue = cookie.getValue();
                System.out.println("Cookie['" + cookieName + "']: " + cookieValue);
            }
        }
    }
}
