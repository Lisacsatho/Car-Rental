package se.hkr;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class HashUtils {
    public static byte[] hash(String password) {
        String salt = "1234";
        final int ITERATIONS = 1000;
        final int KEY_LENGTH = 128;
        char[] passwordChars = password.toCharArray();

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
            PBEKeySpec spec = new PBEKeySpec( passwordChars, salt.getBytes(), ITERATIONS, KEY_LENGTH);
            SecretKey key = skf.generateSecret(spec);
            byte[] result = key.getEncoded();
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }
}
