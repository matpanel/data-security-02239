package gr.matamis.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class HashedFileBasedAuthenticator implements AuthenticationService {

    private final Map<String, Pair<String, String>> passwords;

    public HashedFileBasedAuthenticator(Path passwordFile) throws IOException {
        List<String> userEntries = Files.readAllLines(passwordFile);
        passwords = new HashMap<>();
        for (String userEntry : userEntries) {
            String[] entryParts = userEntry.split(":");
            passwords.put(entryParts[0], new ImmutablePair<>(entryParts[1], entryParts[2]));
        }
    }

    @Override
    public boolean isAuthenticated(Credentials credentials) {

        String usenameToAuthenticate = credentials.getUsername();
        String passwordToVerify = credentials.getPassword();

        boolean isAuthenticated = false;
        if (passwords.containsKey(usenameToAuthenticate)) {
            Pair<String, String> storedSaltAndBase64HashedPassword = passwords.get(usenameToAuthenticate);


            String salt = storedSaltAndBase64HashedPassword.getLeft();
            String storedBase64HashedPassword = storedSaltAndBase64HashedPassword.getRight();

            Base64.Decoder base64Decoder = Base64.getDecoder();
            byte[] saltBytes = base64Decoder.decode(salt);


            try {

                MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");

                messageDigest.update(saltBytes);
                byte[] hashedPasswordBytes = messageDigest.digest(passwordToVerify.getBytes());
                Base64.Encoder base64Encoder = Base64.getEncoder();
                String base64HashedPassword = base64Encoder.encodeToString(hashedPasswordBytes);

                isAuthenticated = base64HashedPassword.equals(storedBase64HashedPassword);


            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }


        }

        return isAuthenticated;

    }
}
