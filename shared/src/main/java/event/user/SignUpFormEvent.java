package event.user;

import java.time.LocalDate;
import java.util.EventObject;

public class SignUpFormEvent  {

    private String firstName;
    private String lastName;
    private String password;
    private String username;
    private String phoneNumber;
    private String email;
    private String bio;
    private LocalDate birthDay;

    public
    SignUpFormEvent( String firstName, String lastName, String password, String username, String phoneNumber, String email, String bio, LocalDate birthDay) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.bio = bio;
        this.birthDay = birthDay;
    }
public SignUpFormEvent(){}
    public
    String getFirstName() {
        return firstName;
    }

    public
    SignUpFormEvent setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public
    String getLastName() {
        return lastName;
    }

    public
    SignUpFormEvent setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public
    String getPassword() {
        return password;
    }

    public
    SignUpFormEvent setPassword(String password) {
        this.password = password;
        return this;
    }

    public
    String getUsername() {
        return username;
    }

    public
    SignUpFormEvent setUsername(String username) {
        this.username = username;
        return this;
    }

    public
    String getPhoneNumber() {
        return phoneNumber;
    }

    public
    SignUpFormEvent setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public
    String getEmail() {
        return email;
    }

    public
    SignUpFormEvent setEmail(String email) {
        this.email = email;
        return this;
    }

    public
    String getBio() {
        return bio;
    }

    public
    SignUpFormEvent setBio(String bio) {
        this.bio = bio;
        return this;
    }

    public
    LocalDate getBirthDay() {
        return birthDay;
    }

    public
    SignUpFormEvent setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
        return this;
    }
}
