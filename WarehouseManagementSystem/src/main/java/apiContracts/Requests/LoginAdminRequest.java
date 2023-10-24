package apiContracts.Requests;

public class LoginAdminRequest extends Request {
    private String username;
    private String password;

    // GETTERS AND SETTERS ARE USED BY GENSON OBJECT MAPPER LIB, IF THEY ARE NOT DECLARED,
    // OBJECT MAPPER THROWS EXCEPTION

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
