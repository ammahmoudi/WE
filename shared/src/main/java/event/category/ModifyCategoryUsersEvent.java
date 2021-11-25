package event.category;

import java.util.List;

public
class ModifyCategoryUsersEvent {

    Integer category;
    List <Integer> usersList;
    public
    ModifyCategoryUsersEvent(){

    }
    public
    ModifyCategoryUsersEvent(Integer category, List <Integer> usersList) {

        this.category = category;
        this.usersList = usersList;
    }


    public
    Integer getCategory() {
        return category;
    }

    public
    ModifyCategoryUsersEvent setCategory(Integer category) {
        this.category = category;
        return this;
    }

    public
    List <Integer> getUsersList() {
        return usersList;
    }

    public
    ModifyCategoryUsersEvent setUsersList(List <Integer> usersList) {
        this.usersList = usersList;
        return this;
    }
}
