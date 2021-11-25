package event.messaging;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "scheduled_messages")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = NewScheduledMessageEvent.class)
public
class NewScheduledMessageEvent {
    Integer Sender;
    @ManyToOne(cascade = CascadeType.REFRESH)
    User senderUser;
    Integer taker;
    String Text;
    Integer originalMessage;
    byte[] image;
    Integer post;
    LocalDateTime sendingDate;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public
    NewScheduledMessageEvent(Integer sender, Integer taker, String text, Integer originalMessage, byte[] image, Integer post, LocalDateTime sendingDate) {

        Sender = sender;
        this.taker = taker;
        Text = text;
        this.originalMessage = originalMessage;
        this.image = image;
        this.post = post;
        this.sendingDate = sendingDate;
    }

    public
    NewScheduledMessageEvent() {

    }

    public
    int getSender() {
        return Sender;
    }

    public
    NewScheduledMessageEvent setSender(int sender) {
        Sender = sender;
        return this;
    }

    public
    NewScheduledMessageEvent setSender(Integer sender) {
        Sender = sender;
        return this;
    }

    public
    Integer getTaker() {
        return taker;
    }

    public
    NewScheduledMessageEvent setTaker(Integer taker) {
        this.taker = taker;
        return this;
    }

    public
    String getText() {
        return Text;
    }

    public
    NewScheduledMessageEvent setText(String text) {
        Text = text;
        return this;
    }

    public
    Integer getOriginalMessage() {
        return originalMessage;
    }

    public
    NewScheduledMessageEvent setOriginalMessage(Integer originalMessage) {
        this.originalMessage = originalMessage;
        return this;
    }

    public
    byte[] getImage() {
        return image;
    }

    public
    NewScheduledMessageEvent setImage(byte[] image) {
        this.image = image;
        return this;
    }

    public
    Integer getPost() {
        return post;
    }

    public
    NewScheduledMessageEvent setPost(Integer post) {
        this.post = post;
        return this;
    }

    public
    LocalDateTime getSendingDate() {
        return sendingDate;
    }

    public
    NewScheduledMessageEvent setSendingDate(LocalDateTime sendingDate) {
        this.sendingDate = sendingDate;
        return this;
    }

    public
    User getSenderUser() {
        return senderUser;
    }

    public
    NewScheduledMessageEvent setSenderUser(User senderUser) {
        this.senderUser = senderUser;
        return this;
    }

    public
    Integer getId() {
        return id;
    }

    public
    NewScheduledMessageEvent setId(Integer id) {
        this.id = id;
        return this;
    }
}
