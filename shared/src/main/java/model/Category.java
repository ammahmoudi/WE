package model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public
class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ManyToOne
    //@LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "owner_id")
    private User owner;
    @ManyToMany(cascade = CascadeType.REFRESH)
    //@LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "Categories",
            joinColumns = {@JoinColumn(name = "category_id")},
            inverseJoinColumns = {@JoinColumn(name = "member_id")})
    private final List <User> userList = new LinkedList <>();
    private byte[] image;


    @Transient
    private  Integer numberOfUsers;
    public
    byte[] getImage() {
        return image;
    }

    public
    Category setImage(byte[] image) {
        this.image = image;
        return this;
    }

    public
    Category() {

    }

    public
    Integer getId() {
        return id;
    }

    public
    Category setId(Integer id) {
        this.id = id;
        return this;
    }

    public
    String getName() {
        return name;
    }

    public
    Category setName(String name) {
        this.name = name;
        return this;
    }

    public
    User getOwner() {
        return owner;
    }

    public
    Category setOwner(User owner) {
        this.owner = owner;
        return this;
    }

    public
    Category(String name, User owner) {
        this.name = name;
        this.owner = owner;
    }

    public
    List <User> getUserList() {
        return userList;
    }
    public
    User addUser(User user) {
        if(this.getUserList().contains(user)){return null;}
        this.getUserList().add(user);
      //  user.getJoinedCategories().add(this);
        return user;
    }
    public
    User removeUser(User user) {
        if(this.getUserList().contains(user)) {
            this.getUserList().remove(user);
      //      user.getJoinedCategories().remove(this);
        }
        return user;
    }

    public
    Integer getNumberOfUsers() {
        return numberOfUsers;
    }

    public
    Category setNumberOfUsers(Integer numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
        return this;
    }
}
