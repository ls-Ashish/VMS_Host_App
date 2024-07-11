package com.leegosolutions.vms_host_app.utility;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CS_ED {

    private static String _secretKey = "6j5nkwRNI";

    public static String Encrypt(String text) throws Exception {
        if (text != null){
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] keyBytes = new byte[16];
            byte[] b = _secretKey.getBytes(StandardCharsets.UTF_8);
            int len = b.length;
            if (len > keyBytes.length)
                len = keyBytes.length;
            System.arraycopy(b, 0, keyBytes, 0, len);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            byte[] results = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            return CS_Base64.encodeBytes(results, CS_Base64.ENCODE);
        }else {
            return "";
        }
    }

    public static String Decrypt(String text) throws Exception {
        if (text != null){
            Cipher cipher = Cipher.getInstance
                    ("AES/CBC/PKCS5Padding"); //this parameters should not be changed
            byte[] keyBytes = new byte[16];
            byte[] b = _secretKey.getBytes(StandardCharsets.UTF_8);
            int len = b.length;
            if (len > keyBytes.length)
                len = keyBytes.length;
            System.arraycopy(b, 0, keyBytes, 0, len);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] results = new byte[text.length()];

            try {
                results = cipher.doFinal(CS_Base64.decode(text));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new String(results, StandardCharsets.UTF_8); // it returns the result as a String
        }else {
            return "";
        }
    }
}
