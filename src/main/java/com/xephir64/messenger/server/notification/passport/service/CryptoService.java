package com.xephir64.messenger.server.notification.passport.service;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CryptoService {
    private static final String SECRET = "azertyuiopqsdfghjklmwxcvbn1234567890&é'(-è_çà)"; // Change this for a more robust secret. Should be the same as the .NET Passport Server

    public static boolean validateTicket(String data, String key) {
        try {
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256HMAC.init(secretKey);
            String signed = Base64.encodeBase64URLSafeString(sha256HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8)));
            return signed.equals(key);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decodeTicket(String data) {
        return new String(Base64.decodeBase64UrlSafe(data), StandardCharsets.UTF_8);
    }
}
