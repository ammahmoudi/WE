package model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public
class Notification {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String text;
    @ManyToOne(cascade={CascadeType.ALL},fetch = FetchType.LAZY)
    @JoinColumn(name="taker_id")
    private User taker=null;
    private int senderId;
    private LocalDateTime creation_date;
    private boolean seen=false;

    @Transient
    User senderUser;
    public
    Notification() {
    }

    public
    Notification(String text, User taker, int senderId) {
        this.text = text;
        this.taker = taker;
        this.senderId = senderId;
        this.creation_date = LocalDateTime.now();
        taker.getNotifications().add(this);
    }

    public
    Integer getId() {
        return id;
    }

    public
    Notification setId(Integer id) {
        this.id = id;
        return this;
    }

    public
    String getText() {
        return text;
    }

    public
    Notification setText(String text) {
        this.text = text;
        return this;
    }

    public
    User getTaker() {
        return taker;
    }

    public
    Notification setTaker(User taker) {
        this.taker = taker;
        return this;
    }
@JsonIgnore
    public
    String getCreation_dateString() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");
        return creation_date.format(formatter);
    }
    public
    LocalDateTime getCreation_date() {


        return creation_date;
    }


    public
    Notification setCreation_date(LocalDateTime creation_date) {
        this.creation_date = creation_date;
        return this;
    }

    public
    boolean isSeen() {
        return seen;
    }

    public
    Notification setSeen(boolean seen) {
        this.seen = seen;
        return this;
    }

    public
    int getSenderId() {

        return senderId;
    }

    public
    Notification setSenderId(int senderId) {
        this.senderId = senderId;
        return this;
    }

    public
    User getSenderUser() {
        return senderUser;
    }

    public
    Notification setSenderUser(User senderUser) {
        this.senderUser = senderUser;
        return this;
    }
}
