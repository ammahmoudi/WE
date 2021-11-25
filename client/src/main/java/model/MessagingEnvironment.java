package model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "messaging_environments")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = MessagingEnvironment.class)
public
class MessagingEnvironment {
    @ManyToMany(cascade = CascadeType.REFRESH)

    @JoinTable(name = "messaging_relationships",
            joinColumns = {@JoinColumn(name = "environment_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    @LazyCollection(LazyCollectionOption.FALSE)
    private  List <User> users = new LinkedList <>();
    @OneToMany(mappedBy = "messagingEnvironment", orphanRemoval = true, cascade = {CascadeType.ALL})
     @LazyCollection(LazyCollectionOption.FALSE)

    private  List <Message> messages = new LinkedList <>();
    @Id

    private Integer id;
    private String name;
    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "owner_id")
    private User owner;
    private LocalDateTime creation_date;
    private byte[] image;


    //only client
    @Transient
    private Message lastMessage;
    @Transient
    private String lastMessageCreationDateString;
    @Transient
    private User taker;
    @Transient
    private Integer unseenCounter;
    @Transient
    private int savedMessage;
    @Transient
    private int userCounter;
    @Transient
    private boolean lastMessageSeen;


    public
    MessagingEnvironment() {
    }

    public
    MessagingEnvironment(String name, User owner) {
        this.name = name;
        this.owner = owner;
        this.creation_date = LocalDateTime.now();
    //    if (owner != null) owner.getOwnedMessagingEnvironments().add(this);
    }

    public
    byte[] getImage() {
        return image;
    }

    public
    MessagingEnvironment setImage(byte[] image) {
        this.image = image;
        return this;
    }

    public
    Integer getId() {
        return id;
    }

    public
    MessagingEnvironment setId(Integer id) {
        this.id = id;
        return this;
    }

    public
    String getName() {
        return name;
    }

    public
    MessagingEnvironment setName(String name) {
        this.name = name;
        return this;
    }

    public
    User getOwner() {
        return owner;
    }

    public
    MessagingEnvironment setOwner(User owner) {
        this.owner = owner;
        return this;
    }

    public
    List <User> observed_getUsers() {
        List <User> temp = new LinkedList <>();
        for (User user : users) {
            temp.add(user);
        }
        temp.removeIf(User::isDeactivate);
        return temp;
    }

    public
    List <User> getUsers() {
        return users;
    }

    public
    List <Message> getMessages() {
        return messages;
    }

    public
    List <Message> observed_getMessages() {
        messages.sort(Comparator.comparing(Message::getCreation_date));

        List <Message> temp = new LinkedList <>();
        for (Message message : messages) {
            if (message.getOwner().isActivate()) {
                temp.add(message);
            }
        }
        return temp;
    }

    public
    String string_getCreation_date() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");
        return creation_date.format(formatter);

    }

    public
    LocalDateTime getCreation_date() {

        return creation_date;

    }

    public
    MessagingEnvironment setCreation_date(LocalDateTime creation_date) {
        this.creation_date = creation_date;
        return this;
    }

    public
    String getLastMessageCreationDateString() {
        return lastMessageCreationDateString;
    }

    public
    MessagingEnvironment setLastMessageCreationDateString(String lastMessageCreationDateString) {
        this.lastMessageCreationDateString = lastMessageCreationDateString;
        return this;
    }

    public
    User getTaker() {
        return taker;
    }

    public
    MessagingEnvironment setUsers(List <User> users) {
        this.users = users;
        return this;
    }

    public
    MessagingEnvironment setMessages(List <Message> messages) {
        this.messages = messages;
        return this;
    }

    public
    MessagingEnvironment setTaker(User taker) {
        this.taker = taker;
        return this;
    }

    public
    MessagingEnvironment applyTaker(User loggedInUser) {
        if (!isGroup()) {
            for (User user : getUsers()) {
                if (!user.equals(loggedInUser)) this.taker = user;
            }

        }
        return this;
    }

    public
    Integer getUnseenCounter() {
        return unseenCounter;
    }

    public
    MessagingEnvironment setUnseenCounter(Integer unseenCounter) {
        this.unseenCounter = unseenCounter;
        return this;
    }


    public
    Message getLastMessage() {
        return lastMessage;
    }


    public
    MessagingEnvironment setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
        return this;
    }

    public
    void addMember(User user) {
        this.getUsers().add(user);
        user.getMessagingEnvironments().add(this);
    }

    public
    void removeMember(User user) {
        this.getUsers().remove(user);
        user.getMessagingEnvironments().remove(this);
    }

    @JsonIgnore
    public
    boolean isGroup() {
        return owner != null;
    }

    @JsonIgnore
    public
    Message applyLastMessage() {
        messages.sort(Comparator.comparing(Message::getCreation_date));
        if (!getMessages().isEmpty()) {
            setLastMessage( getMessages().get(getMessages().size() - 1));
            return getLastMessage();
        } else {
            return null;
        }

    }

    @JsonIgnore
    public
    String getLastMessageCreatedDateString() {
        messages.sort(Comparator.comparing(Message::getCreation_date));

        if (!getMessages().isEmpty()) {
            return getMessages().get(getMessages().size() - 1).getCreation_dateString();
        } else {
            return string_getCreation_date();
        }
    }

    public
    boolean isLastMessageSeen() {
        return lastMessageSeen;
    }

    public
    MessagingEnvironment setLastMessageSeen(boolean lastMessageSeen) {
        this.lastMessageSeen = lastMessageSeen;
        return this;
    }

    public
    void applyLastMessageSeen() {
        if (getLastMessage() != null) {
            if (getLastMessage().getSeenList().isEmpty()) {
                setLastMessageSeen(false);

            } else {
                setLastMessageSeen(true);
            }
        } else {
            setLastMessageSeen(false);
        }
    }

    @JsonIgnore
    public
    LocalDateTime getLastMessageCreatedDate() {
        if(getLastMessage()!=null)return (getLastMessage().getCreation_date());
        else{
            return getCreation_date();
        }
      //  messages.sort(Comparator.comparing(Message::getCreation_date));

//        if (!getMessages().isEmpty()) {
//            return getMessages().get(getMessages().size() - 1).getCreation_date();
//        } else {
//            return getCreation_date();
//        }
    }

    @JsonIgnore
    public
    Integer getUnseenMessagesNumberBy(User user) {
        Integer counter = 0;
        for (Message message : messages) {
            if (message.getOwner().equals(user)) continue;
            if (!message.getSeenList().contains(user)) counter++;
        }
        return counter;
    }

    @JsonIgnore
    public
    boolean SavedMessage_checker() {

        return !this.isGroup() && getUsers().size() == 1;

    }

    public
    int getSavedMessage() {
        return savedMessage;
    }

    public
    MessagingEnvironment setSavedMessage(int savedMessage) {
        this.savedMessage = savedMessage;
        return this;
    }

    public
    void applySavedMessage() {


        if (!this.isGroup() && this.getUsers().size() == 1) savedMessage = 1;
        else savedMessage = 0;
    }

    public
    int getUserCounter() {
        return userCounter;
    }

    public
    MessagingEnvironment setUserCounter(int userCounter) {
        this.userCounter = userCounter;
        return this;
    }


}

