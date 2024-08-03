package com.github.alexthe666.citadel.repack.jcodec.common.tools;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

    public static String md5sumBytes(byte[] bytes) {
        MessageDigest md5 = getDigest();
        md5.update(bytes);
        return digestToString(md5.digest());
    }

    private static String digestToString(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            byte item = digest[i];
            int b = item & 255;
            if (b < 16) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    public static String md5sum(ByteBuffer bytes) {
        MessageDigest md5 = getDigest();
        md5.update(bytes);
        byte[] digest = md5.digest();
        return digestToString(digest);
    }

    public static MessageDigest getDigest() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var2) {
            throw new RuntimeException(var2);
        }
    }
}