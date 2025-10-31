package com.hcl.springecomapp.jwt;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtCookieName:jwtToken}")
    private String jwtCookie;

    // ✅ Generate Signing Key
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // ✅ Generate Token from UserDetails
    public String generateTokenFromUsername(UserDetails userDetails) {
        String username = userDetails.getUsername();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }

    // ✅ Overloaded Method - Generate Token from username string
    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }

    // ✅ Extract Username from JWT Token
    public String getUserNameFromJwtToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            return null;
        }
    }

    // ✅ Validate JWT Token
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    // ✅ Extract JWT from HTTP Header
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        logger.debug("Authorization Header: {}", bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // remove "Bearer " prefix
        }
        return null;
    }

    // ✅ Generate JWT Cookie (used in login)
    public ResponseCookie generateJwtCookie(String username) {
        String jwt = generateTokenFromUsername(username);
        return ResponseCookie.from(jwtCookie, jwt)
                .path("/")
                .maxAge(jwtExpirationMs / 1000L) // in seconds
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                //.secure(false) // change to true when using HTTPS
                .build();
    }

    // ✅ Clear JWT Cookie (used in logout)
    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookie, "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                //.secure(false)
                .secure(true)
                .sameSite("None")
                .build();
    }
    
    public String getJwtFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(jwtCookie)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    
 // ✅ Extract email (subject) from JWT Token
 	public String getEmailFromJwtToken(String token) {
 	    try {
 	        return Jwts.parser()
 	                .verifyWith((SecretKey) key())
 	                .build()
 	                .parseSignedClaims(token)
 	                .getPayload()
 	                .getSubject();  // <-- The 'subject' is the email we stored while generating the token
 	    } catch (Exception e) {
 	        logger.error("Error extracting email from JWT token: {}", e.getMessage());
 	        return null;
 	    }
 	}
 // ✅ Generate Token using email (from UserDetails)
    public String generateTokenFromEmail(UserDetails userDetails) {
        String email = userDetails.getUsername(); // username holds email
        return generateTokenFromEmail(email);
    }

    // ✅ Generate Token using plain email
    public String generateTokenFromEmail(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }
}
