package model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import event.messaging.NewScheduledMessageEvent;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "users")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = User.class)
public
class User {
    //Clientonly
    @Transient
    RelationshipType loggedInUserRelationship;
    @Transient
    RelationshipType userRelationship;
    //clientonly
    @Transient
    LastSeenPrivacy myOwnLastSeenPrivacy;
    @Transient
    String firstTimeToken;
    @Id

    private Integer id;
    private String firstName;
    private String lastName;
    @JsonIgnore
    private String password;
    private String userName;
    private String phoneNumber;
    private String email;
    private String bio;
    private LocalDateTime lastSeen;
    private LocalDate birthDay;
    @JsonIgnore
    private LastSeenPrivacy lastSeenPrivacy = LastSeenPrivacy.EVERYONE; // 0->everyone 1->no one 2->followings
    @JsonIgnore
    private boolean activate = true;
    private boolean privated = false;
    private byte[] image;
    @JsonIgnore
    private String token;
    @JsonIgnore
    private boolean deleted = false;
    @OneToMany(mappedBy = "owner", orphanRemoval = true, cascade = {CascadeType.ALL})
    @LazyCollection(LazyCollectionOption.FALSE)
    private List <MessagingEnvironment> ownedMessagingEnvironments = new LinkedList <>();
    @OneToMany(mappedBy = "owner", orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List <Post> posts = new LinkedList <>();
    @Transient
    @LazyCollection(LazyCollectionOption.FALSE)
    private List <Message> sentMessages = new LinkedList <>();
    @OneToMany(mappedBy = "taker", orphanRemoval = true, cascade = {CascadeType.ALL})
    @LazyCollection(LazyCollectionOption.FALSE)
    private List <Notification> notifications = new LinkedList <>();
    @Transient
    //@LazyCollection(LazyCollectionOption.FALSE)
    private List <Post> likedPost = new LinkedList <>();
    @Transient
    private List <User> followings = new LinkedList <>();
    @Transient
    //@LazyCollection(LazyCollectionOption.FALSE)
    private List <User> followers = new LinkedList <>();
    @Transient
    //@LazyCollection(LazyCollectionOption.FALSE)
    private List <Message> SeenMessages = new LinkedList <>();
    @Transient
    //@LazyCollection(LazyCollectionOption.FALSE)

    private List <Category> joinedCategories = new LinkedList <>();
    @ManyToMany(mappedBy = "users")
    @LazyCollection(LazyCollectionOption.FALSE)


    private List <MessagingEnvironment> messagingEnvironments = new LinkedList <>();
    @Transient
    private List <User> requesteds = new LinkedList <>();
    @Transient
    //@LazyCollection(LazyCollectionOption.FALSE)
    private List <User> requests = new LinkedList <>();
    //@LazyCollection(LazyCollectionOption.FALSE)
    @Transient
    private List <User> Blockeds = new LinkedList <>();
    @Transient
    //@LazyCollection(LazyCollectionOption.FALSE)
    private List <User> Blockers = new LinkedList <>();
    @Transient
    private List <User> silenteds = new LinkedList <>();
    @Transient
    //@LazyCollection(LazyCollectionOption.FALSE)
    private List <User> silenters = new LinkedList <>();
    @OneToMany(mappedBy = "owner", orphanRemoval = true, cascade = {CascadeType.ALL})
    //@LazyCollection(LazyCollectionOption.FALSE)
    private List <Category> categories = new LinkedList <>();
    @Transient
    @OneToMany(mappedBy = "senderUser", orphanRemoval = true, cascade = {CascadeType.ALL})
    //@LazyCollection(LazyCollectionOption.FALSE)
    private List <NewScheduledMessageEvent> scheduledMessageEvents = new LinkedList <>();


    public
    User(String firstName, String lastName, String password, String userName, String phoneNumber, String email, String bio, LocalDate birthDay) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.bio = bio;
        this.birthDay = birthDay;
        categories = null;
    }

    public
    User() {

    }

    static
    void add_request(User from, User to) {
        if (!from.isRequesting(to)) {
            from.getRequesteds().add(to);
            to.getRequests().add(from);
        }

    }

    static
    void add_relationship(User from, User to) {
        if (!from.isFollowing(to)) {
            from.getFollowings().add(to);
            to.getFollowers().add(from);
        }
    }

    static
    void add_block(User from, User to) {
        if (!from.isBlocking(to)) {
            from.getBlockeds().add(to);
            to.getBlockers().add(from);
        }
    }

    static
    void remove_block(User from, User to) {
        if (from.isBlocking(to)) {
            from.getBlockeds().remove(to);
            to.getBlockers().remove(from);
        }
    }

    static
    void add_silent(User from, User to) {
        if (!from.isSilenting(to)) {
            from.getSilenteds().add(to);
            to.getSilenters().add(from);
        }

    }

    static
    void remove_silent(User from, User to) {
        if (from.isSilenting(to)) {
            from.getSilenteds().remove(to);
            to.getSilenters().remove(from);
        }
    }

    static
    void remove_request(User from, User to) {
        if (from.isRequesting(to)) {
            from.getRequesteds().remove(to);
            to.getRequests().remove(from);
        }
    }

    static
    void remove_relationship(User from, User to) {
        if (from.isFollowing(to)) {
            from.getFollowings().remove(to);
            to.getFollowers().remove(from);
        }
    }

    public
    boolean isDeleted() {
        return deleted;
    }

    public
    User setDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public
    Integer getId() {
        return id;
    }

    public
    User setId(Integer id) {
        this.id = id;
        return this;
    }

    public
    String getFirstName() {
        return firstName;
    }

    public
    User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public
    String getLastName() {
        return lastName;
    }

    public
    User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public
    String getPassword() {
        return password;
    }

    public
    User setPassword(String password) {
        this.password = password;
        return this;
    }

    public
    String getUserName() {
        return userName;
    }

    public
    User setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public

    String getPhoneNumber() {
        if (phoneNumber == null) return "";
        return phoneNumber;
    }

    public
    User setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public
    String getEmail() {
        return email;
    }

    public
    User setEmail(String email) {
        this.email = email;
        return this;
    }

    public
    String getBio() {
        return bio;
    }

    public
    User setBio(String bio) {
        this.bio = bio;
        return this;
    }

    @JsonIgnore
    public
    String getLastSeenString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");

        if (lastSeen == null) {
            return LocalDateTime.now().format(formatter);
        }
        return lastSeen.format(formatter);
    }

    public
    LocalDateTime getLastSeen() {

        return lastSeen;
    }

    public
    User setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
        return this;
    }

    public
    LocalDate getBirthDay() {
        return birthDay;
    }

    public
    User setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
        return this;
    }

    public
    List <Category> getCategories() {
        return categories;
    }

    public
    User setCategories(List <Category> categories) {
        this.categories = categories;
        return this;
    }

    public
    List <Post> getPosts() {
        return posts;
    }

    public
    User setPosts(List <Post> posts) {
        this.posts = posts;
        return this;
    }

    public
    List <Message> getSentMessages() {
        return sentMessages;
    }

    public
    User setSentMessages(List <Message> sentMessages) {
        this.sentMessages = sentMessages;
        return this;
    }

    public
    List <Notification> getNotifications() {
        //   notifications.sort(Comparator.comparing(Notification::getCreation_date).reversed());
        return notifications;
    }

    public
    User setNotifications(List <Notification> notifications) {
        this.notifications = notifications;
        return this;
    }

    public
    boolean isActivate() {
        return activate;
    }

    public
    User setActivate(boolean activate) {
        this.activate = activate;
        return this;
    }

    @JsonIgnore
    public
    boolean isDeactivate() {
        return !activate;
    }

    public
    boolean isPrivated() {
        return privated;
    }

    public
    User setPrivated(boolean privated) {
        this.privated = privated;
        return this;
    }

    public
    List <Post> getLikedPost() {
        return likedPost;
    }

    public
    User setLikedPost(List <Post> likedPost) {
        this.likedPost = likedPost;
        return this;
    }

    public
    List <Category> getJoinedCategories() {
        return joinedCategories;
    }

    public
    User setJoinedCategories(List <Category> joinedCategories) {
        this.joinedCategories = joinedCategories;
        return this;
    }

    public
    List <MessagingEnvironment> getOwnedMessagingEnvironments() {
        return ownedMessagingEnvironments;
    }

    public
    User setOwnedMessagingEnvironments(List <MessagingEnvironment> ownedMessagingEnvironments) {
        this.ownedMessagingEnvironments = ownedMessagingEnvironments;
        return this;
    }

    public
    List <Message> getSeenMessages() {
        return SeenMessages;
    }

    public
    User setSeenMessages(List <Message> seenMessages) {
        SeenMessages = seenMessages;
        return this;
    }

    public
    LastSeenPrivacy getLastSeenPrivacy() {
        return lastSeenPrivacy;
    }

    public
    User setLastSeenPrivacy(LastSeenPrivacy lastSeenPrivacy) {
        this.lastSeenPrivacy = lastSeenPrivacy;
        return this;
    }

    public
    byte[] getImage() {
        return image;
    }

    public
    User setImage(byte[] image) {
        this.image = image;
        return this;
    }

    public
    List <User> getFollowings() {
        return followings;
    }

    public
    User setFollowings(List <User> followings) {
        this.followings = followings;
        return this;
    }

    public
    List <User> getFollowers() {
        return followers;
    }

    public
    User setFollowers(List <User> followers) {
        this.followers = followers;
        return this;
    }

    public synchronized
    List <MessagingEnvironment> getMessagingEnvironments() {
        return messagingEnvironments;
    }

    public synchronized
    User setMessagingEnvironments(List <MessagingEnvironment> messagingEnvironments) {
        this.messagingEnvironments = messagingEnvironments;
        return this;
    }

    public
    List <User> getRequesteds() {
        return requesteds;
    }

    public
    User setRequesteds(List <User> requesteds) {
        this.requesteds = requesteds;
        return this;
    }

    public
    List <User> getRequests() {
        return requests;
    }

    public
    User setRequests(List <User> requests) {
        this.requests = requests;
        return this;
    }

    public
    List <User> getBlockeds() {
        return Blockeds;
    }

    public
    User setBlockeds(List <User> blockeds) {
        Blockeds = blockeds;
        return this;
    }

    public
    List <User> getBlockers() {
        return Blockers;
    }

    public
    User setBlockers(List <User> blockers) {
        Blockers = blockers;
        return this;
    }

    public
    List <User> getSilenteds() {
        return silenteds;
    }

    public
    User setSilenteds(List <User> silenteds) {
        this.silenteds = silenteds;
        return this;
    }

    public
    List <User> getSilenters() {
        return silenters;
    }

    public
    User setSilenters(List <User> silenters) {
        this.silenters = silenters;
        return this;
    }

    public
    String getToken() {
        return token;
    }

    public
    User setToken(String token) {
        this.token = token;
        return this;
    }


//observed getters


//    public
//    List <User> getFollowings_observed() {
//        List <User> temp = new LinkedList <>();
//        for (User user : getFollowings()) {
//            temp.add(user);
//        }
//        temp.removeIf(User::isDeactivate);
//        return temp;
//    }

//    public
//    List <User> getFollowers_observed() {
//
//
//        List <User> temp = new LinkedList <>();
//        for (User user : followers) {
//            temp.add(user);
//        }
//        temp.removeIf(User::isDeactivate);
//        return temp;
//    }


//    public
//    List <User> getRequesteds_observed() {
//
//        List <User> temp = new LinkedList <>();
//        for (User user : requesteds) {
//            temp.add(user);
//        }
//        temp.removeIf(User::isDeactivate);
//        return temp;
//    }

//    public
//    List <User> getRequests_observed() {
//        List <User> temp = new LinkedList <>();
//        for (User user : requests) {
//            temp.add(user);
//        }
//        temp.removeIf(User::isDeactivate);
//        return temp;
//    }

    // public
//    List <User> getBlockeds_observed() {
//        List <User> temp = new LinkedList <>();
//        for (User user : Blockeds) {
//            temp.add(user);
//        }
//        temp.removeIf(User::isDeactivate);
//        return temp;
//    }

//    public
//    List <User> getBlockers_observed() {
//        List <User> temp = new LinkedList <>();
//        for (User user : Blockers) {
//            temp.add(user);
//        }
//        temp.removeIf(User::isDeactivate);
//        return temp;
//    }

//    public
//    List <User> getSilenteds_observed() {
//        List <User> temp = new LinkedList <>();
//        for (User user : getSilenteds()) {
//            temp.add(user);
//        }
//        temp.removeIf(User::isDeactivate);
//        return temp;
//    }

//    public
//    List <User> getSilenters_observed() {
//        List <User> temp = new LinkedList <>();
//        for (User user : silenters) {
//            temp.add(user);
//        }
//        temp.removeIf(User::isDeactivate);
//        return temp;
//    }

//    public
//    List <MessagingEnvironment> getMessagingEnvironments_observed() {
//        messagingEnvironments.sort(Comparator.comparing(MessagingEnvironment::getLastMessageCreatedDate).reversed());
//
//        List <MessagingEnvironment> temp = new LinkedList <>();
//        for (MessagingEnvironment messagingEnvironment : messagingEnvironments) {
//            if (messagingEnvironment.isGroup()) {
//                if(messagingEnvironment.getOwner().isActivate()){
//                    temp.add(messagingEnvironment);}
//
//            } else {
//                if (messagingEnvironment.isSavedMessage()) {temp.add(messagingEnvironment);}
//                else {
//                    if (messagingEnvironment.getUsers_observed().size()!=1) {
//                        temp.add(messagingEnvironment);
//                    }
//                }
//            }
//        }
//        return temp;
//    }


    public
    void
    changeInfo(String firstName, String lastName, String password, String userName, String phoneNumber, String email, String bio, LocalDate birthDay, LastSeenPrivacy lastSeenPrivacy, boolean privated, byte[] image) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.bio = bio;
        this.birthDay = birthDay;
        this.lastSeenPrivacy = lastSeenPrivacy;
        this.privated = privated;
        this.image = image;
    }


    public
    User block(User user) {

        if (!this.isBlocking(user)) {
            if (this.isFollowing(user)) {
                User.remove_relationship(this, user);

            }
            if (this.isFollowedBy(user)) {
                User.remove_relationship(user, this);

            }
            if (this.isRequestedBy(user)) {
                User.remove_request(user, this);

            }
            if (this.isRequesting(user)) {
                User.remove_request(this, user);

            }

            User.add_block(this, user);

        } else {
            //    if(this.isRequesting(user))
            User.remove_block(this, user);

        }

        return this;
    }

    public
    User silent(User user) {
        if (!this.isSilenting(user)) {
            User.add_silent(this, user);


        } else {
            User.remove_silent(this, user);

        }
        return this;
    }

    public
    User follow(User user) {


        if (!this.isFollowing(user)) {
            if (this.isBlocking(user)) {
                this.block(user);
                return this;

            }
            if (user.isPrivated()) {
                if (this.isRequesting(user)) {
                    User.remove_request(this, user);

                } else {
                    User.add_request(this, user);

                }
            } else {
                User.add_relationship(this, user);

            }
        } else {
            User.remove_relationship(this, user);


        }

        return this;
    }

    public
    User accept(User user) {
        remove_request(user, this);
        add_relationship(user, this);
        return this;
    }

    public
    User deny(User user) {
        remove_request(user, this);
        return this;
    }

    public
    MessagingEnvironment getPrivateChatWith(User user) {
        for (MessagingEnvironment messagingEnvironment : messagingEnvironments) {
            if (messagingEnvironment.isGroup()) continue;
            if (user.equals(this)) {
                if (messagingEnvironment.SavedMessage_checker()) {

                    return messagingEnvironment;
                }
                continue;
            }
            for (User user1 : messagingEnvironment.getUsers()) {
                if (user1.equals(user)) {
                    // System.out.println(messagingEnvironment.getId());
                    return messagingEnvironment;
                }
            }
        }
        return null;
    }

    @JsonIgnore
    public
    String getFullName() {
        return firstName + " " + lastName;
    }


    public
    String getRelationship(User user) {
        if (this.getBlockeds().contains(user)) return "Blocked";
        if (this.getBlockers().contains(user)) return "Blocker";
        if (this.getFollowings().contains(user)) return "Following";
        else return "NotFollowing";
    }

    public
    RelationshipType getRelationshipTo(User user) {

        if (this.isRequesting(user)) return RelationshipType.REQUESTED;
        if (this.isFollowing(user) && !this.isSilenting(user)) return RelationshipType.FOLLOWING;
        if (this.isBlocking(user)) return RelationshipType.BLOCKED;
        if (this.isFollowing(user) && this.isSilenting(user)) return RelationshipType.FOLLOWING_MUTED;
        if (this.isSilenting(user) && !this.isFollowing(user)) return RelationshipType.MUTED;
        return RelationshipType.NONE;
    }

    public
    boolean isFollowing(User user) {
        return this.getFollowings().contains(user);
    }

    public
    boolean isFollowedBy(User user) {
        return this.getFollowers().contains(user);
    }

    public
    boolean isBlockedBy(User user) {
        return this.getBlockers().contains(user);
    }

    public
    boolean isBlocking(User user) {
        return this.getBlockeds().contains(user);
    }

    public
    boolean isRequestedBy(User user) {
        return this.getRequests().contains(user);
    }

    public
    boolean isRequesting(User user) {
        return this.requesteds.contains(user);
    }

    public
    boolean isSilenting(User user) {
        return this.getSilenteds().contains(user);
    }

    public
    boolean isSilentedBy(User user) {
        return this.getSilenters().contains(user);
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

        return (this.getId().equals(((User) (obj)).getId()));
    }

    public
    RelationshipType getLoggedInUserRelationship() {
        return loggedInUserRelationship;
    }

    public
    User setLoggedInUserRelationship(RelationshipType loggedInUserRelationship) {
        this.loggedInUserRelationship = loggedInUserRelationship;
        return this;
    }

    public
    RelationshipType getUserRelationship() {
        return userRelationship;
    }

    public
    User setUserRelationship(RelationshipType userRelationship) {
        this.userRelationship = userRelationship;
        return this;
    }

    public
    LastSeenPrivacy getMyOwnLastSeenPrivacy() {
        return myOwnLastSeenPrivacy;
    }

    public
    User setMyOwnLastSeenPrivacy(LastSeenPrivacy myOwnLastSeenPrivacy) {
        this.myOwnLastSeenPrivacy = myOwnLastSeenPrivacy;
        return this;
    }

    public
    String getFirstTimeToken() {
        return firstTimeToken;
    }

    public
    User setFirstTimeToken(String firstTimeToken) {
        this.firstTimeToken = firstTimeToken;
        return this;
    }

    public
    List <NewScheduledMessageEvent> getScheduledMessageEvents() {
        return scheduledMessageEvents;
    }

    public
    User setScheduledMessageEvents(List <NewScheduledMessageEvent> scheduledMessageEvents) {
        this.scheduledMessageEvents = scheduledMessageEvents;
        return this;
    }
}
