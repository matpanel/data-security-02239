package gr.matamis.server;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;

import gr.matamis.hasher.PasswordStorage;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class HashedFileBasedAuthenticator implements AuthenticationService {

    private final Map<String, String> passwords;

    public HashedFileBasedAuthenticator(Path passwordFile) throws IOException {
        List<String> userEntries = Files.readAllLines(passwordFile);
        passwords = new HashMap<>();
        for (String userEntry : userEntries) {
            int firstColonIndex = userEntry.indexOf(":");

            String key = userEntry.substring(0, firstColonIndex);

            String storedPassData = userEntry.substring(firstColonIndex + 1);


            passwords.put(key, storedPassData);


        }
    }

    @Override
    public boolean isAuthenticated(Credentials credentials) {

        String usernameToAuthenticate = credentials.getUsername();
        String passwordToVerify = credentials.getPassword();

        boolean isAuthenticated = false;
        if (passwords.containsKey(usernameToAuthenticate)) {

            String correctHash = passwords.get(usernameToAuthenticate);
            try {
                isAuthenticated = PasswordStorage.verifyPassword(passwordToVerify, correctHash);
            } catch (PasswordStorage.CannotPerformOperationException | PasswordStorage.InvalidHashException e) {
                System.out.println("Authentication failure due to exception: " + e.getMessage());
            }

        }

        return isAuthenticated;

    }
}
