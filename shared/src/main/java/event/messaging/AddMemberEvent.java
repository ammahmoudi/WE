package event.messaging;

import model.MessagingEnvironment;

import java.util.List;

public
class AddMemberEvent {
    private Integer messagingEnvironmentId;
    private List <Integer> selectedUsersId;

    public
    AddMemberEvent(Integer messagingEnvironmentId, List <Integer> selectedUsersId) {
        this.messagingEnvironmentId = messagingEnvironmentId;
        this.selectedUsersId = selectedUsersId;
    }
    public
    AddMemberEvent(){

    }
    public
    Integer getMessagingEnvironmentId() {
        return messagingEnvironmentId;
    }

    public
    AddMemberEvent setMessagingEnvironmentId(Integer messagingEnvironmentId) {
        this.messagingEnvironmentId = messagingEnvironmentId;
        return this;
    }

    public
    List <Integer> getSelectedUsersId() {
        return selectedUsersId;
    }

    public
    AddMemberEvent setSelectedUsersId(List <Integer> selectedUsersId) {
        this.selectedUsersId = selectedUsersId;
        return this;
    }
}
