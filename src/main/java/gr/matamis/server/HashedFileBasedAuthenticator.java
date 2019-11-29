package gr.matamis.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import gr.matamis.hasher.PasswordStorage;

public class HashedFileBasedAuthenticator implements AuthenticationService {

    private final Map<String, String> passwords;
    private final Map<String, Set<String>> acls;

    public HashedFileBasedAuthenticator(Path passwordFile) throws IOException {

        List<String> userEntries = Files.readAllLines(passwordFile);
        passwords = new HashMap<>();
        acls = new HashMap<>();
        for (String userEntry : userEntries) {
            String[] entries = userEntry.split(":");
            String key = entries[0].trim();
            String storedPassData = Arrays
                    .stream(entries, 1, 6)
                    .reduce((left, right) -> left + ":" + right)
                    .get();

            passwords.put(key, storedPassData);

            if (entries.length == 7) {
                Set<String> functionNames = new HashSet<String>(Arrays.asList(entries[6].split(",")));
                acls.put(key, functionNames);
            }
            else {
                acls.put(key, Collections.<String>emptySet());
            }
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

    @Override
    public boolean hasPermission(Credentials credentials, String functionName) {
        String username = credentials.getUsername();
        return acls.containsKey(username) && acls.get(username).contains(functionName);
    }
}
