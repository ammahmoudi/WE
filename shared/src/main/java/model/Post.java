package model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;


@Entity
@Table(name = "posts")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Post.class)
public
class Post {

    List <Message> forwardedMessages = new LinkedList <>();
    private List <User> likes = new LinkedList <>();
    private List <Post> comments = new LinkedList <>();
    private List <Post> repostedChilds = new LinkedList <>();
    private Integer id;
    private User owner;
    private String text;
    private LocalDateTime creation_date;
    private Post parrent = null;
    private Post originalPost;
    private PostType postType;
    private Integer reported = 0;
    private byte[] image;



    //client only
    @Transient
    Integer numberOfComments;
    @Transient
    Integer numberOfLikes;
    @Transient
    Boolean likedByMe;

    public
    Post() {

    }

    public
    Post(User owner, String text, Post parrent, Post originalPost, byte[] image) {
        this.owner = owner;
        this.text = text;
        this.creation_date = LocalDateTime.now();
        this.parrent = parrent;
        this.originalPost = originalPost;
        this.image = image;

        if (parrent != null) postType = PostType.COMMENT;
        else if (originalPost != null) postType = PostType.REPOSTED_POST;
        else postType = PostType.POST;

    }


    public
    Integer getReported() {
        return reported;
    }

    public
    Post setReported(Integer reported) {
        this.reported = reported;
        return this;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public
    Integer getId() {
        return id;
    }

    public
    Post setId(Integer id) {
        this.id = id;
        return this;
    }

    @ManyToOne(cascade = CascadeType.REFRESH)
    //@LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "owner_id")
    public
    User getOwner() {
        return owner;
    }

    public
    Post setOwner(User owner) {
        this.owner = owner;
        return this;
    }

    public
    String getText() {

        if (notOriginalPost()) return originalPost.getText();

        return text;
    }

    public
    Post setText(String text) {
        this.text = text;
        return this;
    }

    public
    byte[] getImage() {
        return image;
    }

    public
    Post setImage(byte[] image) {
        this.image = image;
        return this;
    }

    @ManyToMany(cascade = CascadeType.REFRESH)
    //@LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "likes",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    public
    List <User> getLikes() {
        return likes;
    }

    public
    Post setLikes(List <User> likes) {
        this.likes = likes;
        return this;
    }

    @OneToMany(mappedBy = "post", cascade = {CascadeType.REFRESH})
    //@LazyCollection(LazyCollectionOption.FALSE)
    public
    List <Message> getForwardedMessages() {
        return forwardedMessages;
    }

    public
    Post setForwardedMessages(List <Message> forwardedMessages) {
        this.forwardedMessages = forwardedMessages;
        return this;
    }


    public
    LocalDateTime getCreation_date() {

        return creation_date;
    }

    public
    Post setCreation_date(LocalDateTime creation_date) {
        this.creation_date = creation_date;
        return this;
    }


    public
    List <User> observed_getLikes() {
        List <User> temp = new LinkedList <>();
        for (User user : getLikes()) {
            temp.add(user);
        }
        temp.removeIf(User::isDeactivate);


        if (notOriginalPost()) return originalPost.observed_getLikes();
        return temp;
    }

    public
    List <Post> observed_getComments() {
        List <Post> temp = new LinkedList <>();
        for (Post post : getComments()) {
            if (post.getOwner().isActivate()) temp.add(post);
        }

        if (notOriginalPost()) return originalPost.observed_getComments();
        return temp;
    }

    @OneToMany(mappedBy = "parrent", orphanRemoval = true, cascade = {CascadeType.ALL})
    //@LazyCollection(LazyCollectionOption.FALSE)
    public
    List <Post> getComments() {
        return comments;
    }

    public
    Post setComments(List <Post> comments) {
        this.comments = comments;
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

        if (obj == null || !(obj instanceof Post)) {
            return false;
        }

        return (this.getId() == ((Post) (obj)).getId());
    }



    public
    boolean notPost() {
        return this.getParrent() != null;
    }

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "parrent_id")
    public
    Post getParrent() {
        return parrent;
    }

    public
    Post setParrent(Post parrent) {
        this.parrent = parrent;
        return this;
    }

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "originalPost_id")
    public
    Post getOriginalPost() {
        return originalPost;
    }

    public
    Post setOriginalPost(Post originalPost) {
        this.originalPost = originalPost;
        return this;
    }

    public
    PostType getPostType() {
        return postType;
    }

    public
    Post setPostType(PostType postType) {
        this.postType = postType;
        return this;
    }

    @OneToMany(mappedBy = "originalPost", orphanRemoval = true, cascade = {CascadeType.ALL})
    //@LazyCollection(LazyCollectionOption.FALSE)
    public
    List <Post> getRepostedChilds() {
        return repostedChilds;
    }

    public
    Post setRepostedChilds(List <Post> repostedChilds) {
        this.repostedChilds = repostedChilds;
        return this;
    }

    public
    Integer getNumberOfComments() {
        return numberOfComments;
    }

    public
    Post setNumberOfComments(Integer numberOfComments) {
        this.numberOfComments = numberOfComments;
        return this;
    }

    public
    Integer getNumberOfLikes() {
        return numberOfLikes;
    }

    public
    Post setNumberOfLikes(Integer numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
        return this;
    }

    public
    boolean notOriginalPost() {
        return originalPost != null;
    }


    public
    boolean fullyReported() {
        return reported > 10;
    }


    public
    void report() {
        this.setReported(this.getReported() + 1);
    }

    public
    String stringed_getCreation_date() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");
        return creation_date.format(formatter);
    }

    public
    boolean likedChecker(User user) {
        if (notOriginalPost()) return originalPost.likedChecker(user);
        return this.getLikes().contains(user);


    }

    public
    Post like(User user) {
        if (this.getLikes().contains(user)) {
            this.getLikes().remove(user);
          //  user.getLikedPost().remove(this);
        } else {
            this.getLikes().add(user);
           // user.getLikedPost().add(this);
        }
        return this;
    }

    public
    Post comment(Post comment) {
        comment.getOwner().getPosts().add(comment);
        this.getComments().add(comment);
        return comment;
    }

    public
    Post repost(Post reposted) {
        reposted.getOwner().getPosts().add(reposted);
        this.getRepostedChilds().add(reposted);
        return reposted;
    }

    public
    Boolean isLikedByMe() {
        return likedByMe;
    }

    public
    Post setLikedByMe(Boolean likedByMe) {
        this.likedByMe = likedByMe;
        return this;
    }
}



