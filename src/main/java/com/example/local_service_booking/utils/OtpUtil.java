package com.example.local_service_booking.utils;

import org.apache.commons.codec.binary.Hex;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class OtpUtil {

    private static final String SECRET_KEY = "super-secret-key-change-me";

    public static String generateOtpToken(String email, String otp, long expiryTime) {
        try {
            String hash = hmacSha256(email + "|" + otp, SECRET_KEY);
            String payload = hash + "|" + expiryTime;
            return Base64.getEncoder().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Error generating OTP token", e);
        }
    }

    public static boolean verifyOtp(String email, String otpEntered, String token) {
        try {
            String decoded = new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
            String[] parts = decoded.split("\\|");
            if (parts.length != 2) return false;

            String hashFromToken = parts[0];
            long expiryTime = Long.parseLong(parts[1]);

            if (System.currentTimeMillis() > expiryTime) return false;
            String newHash = hmacSha256(email + "|" + otpEntered, SECRET_KEY);
            return newHash.equals(hashFromToken);
        } catch (Exception e) {
            return false;
        }
    }

    private static String hmacSha256(String data, String secret) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secretKey);
        return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }
}
