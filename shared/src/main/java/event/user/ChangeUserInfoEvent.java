package event.user;

import model.LastSeenPrivacy;

import java.time.LocalDate;
import java.util.EventObject;

public class ChangeUserInfoEvent  {

    private  String firstName;
    private  String lastName;
    private  String oldPassword;
    private  String newPassword;
    private  String username;
    private  String phoneNumber;
    private  String email;
    private  String bio;
    private LocalDate birthDay;
    private  LastSeenPrivacy lastSeenPrivacy;
    private  boolean privateAccount;
    private byte[] image;
    public
    ChangeUserInfoEvent() {


    }
    public
    ChangeUserInfoEvent( String firstName, String lastName, String oldPassword, String newPassword, String username, String phoneNumber, String email, String bio, LocalDate birthDay, LastSeenPrivacy lastSeenPrivacy, boolean privateAccount,byte[] image) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.bio = bio;
        this.birthDay = birthDay;
        this.lastSeenPrivacy = lastSeenPrivacy;
        this.privateAccount = privateAccount;
        this.image=image;

    }

    public
    String getFirstName() {
        return firstName;
    }

    public
    String getLastName() {
        return lastName;
    }

    public
    String getOldPassword() {
        return oldPassword;
    }

    public
    String getNewPassword() {
        return newPassword;
    }

    public
    String getUsername() {
        return username;
    }

    public
    String getPhoneNumber() {
        return phoneNumber;
    }

    public
    String getEmail() {
        return email;
    }

    public
    String getBio() {
        return bio;
    }

    public
    LocalDate getBirthDay() {
        return birthDay;
    }

    public
    LastSeenPrivacy getLastSeenPrivacy() {
        return lastSeenPrivacy;
    }

    public
    boolean isPrivateAccount() {
        return privateAccount;
    }

    public
    byte[] getImage() {
        return image;
    }

    public
    ChangeUserInfoEvent setImage(byte[] image) {
        this.image = image;
        return this;
    }
}

