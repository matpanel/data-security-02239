package gr.matamis.server;

import gr.matamis.hasher.PasswordStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class HashedFileBasedRBACAuthenticator implements AuthenticationService {

    private final Map<String, String> passwords;
    private final Map<String, Set<String>> permissions;

    public HashedFileBasedRBACAuthenticator(Path passwordFile, Path rolesFile, Path userRolesFile) throws IOException {

        List<String> userEntries = Files.readAllLines(passwordFile);
        passwords = new HashMap<>();
        permissions = new HashMap<>();

        for (String userEntry : userEntries) {
            String[] entries = userEntry.split(":");
            String key = entries[0].trim();
            String storedPassData = Arrays
                    .stream(entries, 1, 6)
                    .reduce((left, right) -> left + ":" + right)
                    .get();

            passwords.put(key, storedPassData);
        }

        List<String> rolesEntries = Files.readAllLines(rolesFile);
        HashMap<String, Set<String>> rolePermissions = new HashMap<>();

        for (String roleEntry: rolesEntries) {
            String[] entries = roleEntry.split(":");
            String key = entries[0].trim();
            Set<String> functionNames = new HashSet<String>(Arrays.asList(entries[1].split(",")));
            rolePermissions.put(key, functionNames);
        }

        List<String> userRolesEntries = Files.readAllLines(userRolesFile);
        for (String userRoleEntry: userRolesEntries) {
            String[] entries = userRoleEntry.split(":");
            String key = entries[0].trim();
            Set<String> roleNames = new HashSet<String>(Arrays.asList(entries[1].split(",")));

            Set<String> permissions = roleNames.stream()
                    .flatMap(role -> rolePermissions.get(role).stream())
                    .collect(Collectors.toSet());

            this.permissions.put(key, permissions);
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
        return permissions.containsKey(username) && permissions.get(username).contains(functionName);
    }
}
