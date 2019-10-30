package gr.matamis.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBasedAuthenticator implements AuthenticationService {


    protected final Map<String, String> passwords;

    public FileBasedAuthenticator(Path passwordFile) throws IOException {
        List<String> userEntries = Files.readAllLines(passwordFile);
        passwords = new HashMap<>();
        for (String userEntry : userEntries) {
            String[] entryParts = userEntry.split(":");
            passwords.put(entryParts[0], entryParts[1]);
        }
    }

    @Override
    public boolean isAuthenticated(Credentials credentials) {
        String username = credentials.getUsername();
        String password = credentials.getPassword();

        return passwords.containsKey(username) && passwords.get(username).equals(password);
    }


}
