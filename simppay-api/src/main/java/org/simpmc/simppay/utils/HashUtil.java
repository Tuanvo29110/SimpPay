package org.simpmc.simppay.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class HashUtil {
    public static String randomMD5() {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest((System.currentTimeMillis() + (long) randomInt(0, 999999) + "").getBytes("UTF-8"));
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashtext = new StringBuilder(no.toString(16));

            while (hashtext.length() < 32) {
                hashtext.insert(0, "0");
            }

            return hashtext.toString();
        } catch (Exception var4) {
            return "";
        }
    }

    public static String md5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available.", e);
        }
    }

    public static int randomInt(int min, int max) {
        return (new Random()).nextInt(max - min + 1) + min;
    }

    public static int randomInt(String min, String max) {
        return randomInt(ri(min), ri(max));
    }

    public static double randomDouble(double min, double max) {
        Random r = new Random();
        return min + (max - min) * r.nextDouble();
    }

    public static double randomDouble(String min, String max) {
        return randomDouble(rd(max), rd(min));
    }

    public static double randomDoubleNnega(double d) {
        double nega = d * -1.0D;
        return Math.random() * (d - nega) + nega;
    }

    public static double rd(String s) {
        return Double.valueOf(s);
    }

    public static int ri(String s) {
        return Integer.valueOf(s);
    }
}
