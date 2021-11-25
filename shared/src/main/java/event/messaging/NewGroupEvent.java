package event.messaging;

import java.util.LinkedList;

public
class NewGroupEvent {
    String name;
    Integer owner;
    LinkedList <Integer> usersList;

    public
    NewGroupEvent(String name, Integer owner, LinkedList <Integer> usersList) {

        this.name = name;
        this.owner = owner;
        this.usersList = usersList;
    }
    public
    NewGroupEvent() {
    }

    public
    String getName() {
        return name;
    }

    public
    NewGroupEvent setName(String name) {
        this.name = name;
        return this;
    }

    public
    Integer getOwner() {
        return owner;
    }

    public
    NewGroupEvent setOwner(Integer owner) {
        this.owner = owner;
        return this;
    }

    public
    LinkedList <Integer> getUsersList() {
        return usersList;
    }

    public
    NewGroupEvent setUsersList(LinkedList <Integer> usersList) {
        this.usersList = usersList;
        return this;
    }
}
