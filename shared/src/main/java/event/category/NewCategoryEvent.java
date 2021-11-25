package event.category;

import java.util.LinkedList;
import java.util.List;

public
class NewCategoryEvent  {
    String name;
    Integer owner;
    List <Integer> usersList;
    public NewCategoryEvent(){

    }
    public
    NewCategoryEvent( String name, Integer owner, List <Integer> usersList) {

        this.name = name;
        this.owner = owner;
        this.usersList = usersList;
    }

    public
    String getName() {
        return name;
    }

    public
    NewCategoryEvent setName(String name) {
        this.name = name;
        return this;
    }

    public
    Integer getOwner() {
        return owner;
    }

    public
    NewCategoryEvent setOwner(Integer owner) {
        this.owner = owner;
        return this;
    }

    public
    List <Integer> getUsersList() {
        return usersList;
    }

    public
    NewCategoryEvent setUsersList(List <Integer> usersList) {
        this.usersList = usersList;
        return this;
    }
}
