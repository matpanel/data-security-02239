package gr.matamis.hasher;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHasher {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String password = args[0];

        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);

        Base64.Encoder base64Encoder = Base64.getEncoder();
        String base64Salt = base64Encoder.encodeToString(saltBytes);

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");

        messageDigest.update(saltBytes);
        byte[] hashedPasswordBytes = messageDigest.digest(password.getBytes());
        String base64HashedPassword = base64Encoder.encodeToString(hashedPasswordBytes);

        System.out.println(base64Salt+"$"+base64HashedPassword);
    }
}
