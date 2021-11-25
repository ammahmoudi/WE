package model;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
@Entity
@Table(name = "messages")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Message.class)

public
class Message {
    @Id

    private Integer id;
    @ManyToOne(cascade = CascadeType.REFRESH)
    //@LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "owner_id")

    private User owner;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "environment_id")

    private MessagingEnvironment messagingEnvironment;
    private String text;
    private LocalDateTime creation_date;
    @ManyToMany(cascade = CascadeType.REFRESH)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "Seens",
            joinColumns = {@JoinColumn(name = "message_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private final List <User> seenList = new LinkedList <>();

    private Boolean serverSeen=false;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "originalMessage_id")
    private Message originalMessage;

    @OneToMany(mappedBy = "originalMessage", orphanRemoval = true, cascade = {CascadeType.ALL})
    //@LazyCollection(LazyCollectionOption.FALSE)
    private final List <Message> forwardedChilds = new LinkedList <>();
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "post_id")
    private Post post=null;


    private byte[] image;


    //client only
    @Transient
    private boolean finalSeen;
    @Transient
    private boolean userSeen;

    public
    Message() {

    }


    public
    Integer getId() {
        return id;
    }

    public
    Message setId(Integer id) {
        this.id = id;
        return this;
    }

    public
    User getOwner() {
        return owner;
    }

    public
    Message setOwner(User owner) {
        this.owner = owner;
        return this;
    }


    public
    String getText() {
        return text;
    }

    public
    Message setText(String text) {
        this.text = text;
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
    Message setCreation_date(LocalDateTime creation_date) {
        this.creation_date = creation_date;
        return this;
    }
@JsonIgnore
    public
    boolean isSeen(User user) {
        return seenList.contains(user);
    }

    public
    void seenBy(User user) {
       seenList.add(user);
       user.getSeenMessages().add(this);

    }

    public
    MessagingEnvironment getMessagingEnvironment() {
        return messagingEnvironment;
    }

    public
    Message setMessagingEnvironment(MessagingEnvironment messagingEnvironment) {
        this.messagingEnvironment = messagingEnvironment;
        return this;
    }



    public
    Message getOriginalMessage() {
        return originalMessage;
    }

    public
    Message setOriginalMessage(Message originalMessage) {
        this.originalMessage = originalMessage;
        return this;
    }

    public
    List <Message> getForwardedChilds() {
        return forwardedChilds;
    }

    public
    Message(User owner, MessagingEnvironment messagingEnvironment, String text,Message originalMessage,byte[] image,Post post) {
        this.owner = owner;
        this.messagingEnvironment = messagingEnvironment;
        this.text = text;
        this.creation_date = LocalDateTime.now();
        this.originalMessage=originalMessage;
        messagingEnvironment.getMessages().add(this);
        this.image=image;
        this.post=post;
    }
    @JsonIgnore
    public boolean isForwarded(){
        return originalMessage != null;
    }
    public void forward(Message message){
        this.getForwardedChilds().add(message);
        message.getOwner().getSentMessages().add(message);

    }

    public
    List <User> getSeenList() {
        return seenList;
    }

    public
    byte[] getImage() {
        return image;
    }

    public
    Message setImage(byte[] image) {
        this.image = image;
        return this;
    }

    public
    Post getPost() {
   //     if(this.isForwarded())return originalMessage.getPost();
        return post;
    }

    public
    Message setPost(Post post) {
        this.post = post;
        return this;
    }

    public boolean checkPost(){
        return post != null;

    }

    public
    boolean isFinalSeen() {
        return finalSeen;
    }

    public
    Message setFinalSeen(boolean finalSeen) {
        this.finalSeen = finalSeen;
        return this;
    }
    public  void applyFinalSeen(){
        if (getSeenList().isEmpty()){
            setFinalSeen(false);

        }else setFinalSeen(true);
    }

    public
    boolean isUserSeen() {
        return userSeen;
    }

    public
    Message setUserSeen(boolean userSeen) {
        this.userSeen = userSeen;
        return this;
    }

    public
    Boolean getServerSeen() {
        return serverSeen;
    }

    public
    Message setServerSeen(Boolean serverSeen) {
        this.serverSeen = serverSeen;
        return this;
    }

    @Override
    public
    int hashCode() {
        return (this.getId());
    }

    @Override
    public
    boolean equals(Object obj) {

        if (!(obj instanceof User)) {
            return false;
        }

        return (this.getId().equals(((Message) (obj)).getId()));
    }

}


