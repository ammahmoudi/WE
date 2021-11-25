package event.user;

import java.util.EventObject;

public class  SignInFormEvent  {
    private String username;
    private String password;

    public
    SignInFormEvent() {

    }

    public
    SignInFormEvent( String username, String password) {

        this.username = username;

        this.password = password;

    }

    public
    String getUsername() {
        return username;
    }

    public
    SignInFormEvent setUsername(String username) {
        this.username = username;
        return this;
    }

    public
    String getPassword() {
        return password;
    }

    public
    SignInFormEvent setPassword(String password) {
        this.password = password;
        return this;
    }
}
