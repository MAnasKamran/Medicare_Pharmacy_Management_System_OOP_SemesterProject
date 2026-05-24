package com.pharmacy.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public final class PasswordUtil {

    private static final String SALT = "PharmacyPMS_OOP_2024";

    private PasswordUtil() {}

    public static String hash(String plain) {
        if (plain == null) throw new IllegalArgumentException("Password cannot be null");
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update((SALT + plain).getBytes());
            return Base64.getEncoder().encodeToString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    public static boolean verify(String plain, String storedHash) {
        if (plain == null || storedHash == null) return false;
        return hash(plain).equals(storedHash);
    }
}
