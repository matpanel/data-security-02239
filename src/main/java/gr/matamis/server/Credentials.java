package gr.matamis.server;

import javax.print.DocFlavor;
import java.io.Serializable;
import java.util.Objects;

public class Credentials implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String username;
    private final String password;

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Credentials that = (Credentials) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    public String getPassword() {
        return password;
    }

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
