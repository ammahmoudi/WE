package controller.messaging;

import JSon.JSonController;
import com.fasterxml.jackson.core.type.TypeReference;
import controller.Actions;
import controller.Controller;
import controller.ServerHandler;
import event.messaging.AddMemberEvent;
import event.messaging.NewGroupEvent;
import event.messaging.NewMessageEvent;
import javafx.concurrent.Service;
import model.LoggedInUser;
import model.Message;
import model.MessagingEnvironment;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import request.Request;
import request.RequestType;
import response.Response;
import response.ResponseType;

import java.util.LinkedList;
import java.util.List;

public
class AddMemberController extends Controller {
MessagingEnvironment messagingEnvironment;
    static Logger LOGGER = LogManager.getLogger(AddMemberController.class.getName());

    public
    AddMemberController(MessagingEnvironment messagingEnvironment)

    {
this.messagingEnvironment=messagingEnvironment;
    }


    public
    void newMessage(NewMessageEvent newMessageEvent){
//        Message message=new Message(newMessageEvent.getSender(), newMessageEvent.getTaker(), newMessageEvent.getText(),newMessageEvent.getOriginalMessage(),newMessageEvent.getImage(),null);
//        LOGGER.info("User "+message.getOwner().getId()+"sent message "+message.getId()+"to messaging Environment "+message.getMessagingEnvironment().getId());
//
//        if(message.isForwarded()){
//            message.getOriginalMessage().forward(message);
//        }
//
//        context.Messages.add(message);
//        return message;
    }

    public
    List <MessagingEnvironment> getMessagingEnvironments(){

        return LoggedInUser.getUser().getMessagingEnvironments();
    }
    LinkedList <User> selectedUsersList =new LinkedList <>();

    public
    LinkedList <User> getSelectedUsersList() {
        return selectedUsersList;
    }
    public MessagingEnvironment newGroup(NewGroupEvent newGroupEvent){
//        MessagingEnvironment messagingEnvironment=new MessagingEnvironment(newGroupEvent.getName(),newGroupEvent.getOwner());
//        messagingEnvironment.addMember(newGroupEvent.getOwner());
//        for (User user :newGroupEvent.getUsersList()){
//            messagingEnvironment.addMember(user);
//        }
//        context.MessagingEnvironmentDB.add(messagingEnvironment);
//        return messagingEnvironment;
        return null;



    }

    public
    MessagingEnvironment getMessagingEnvironment() {
        return messagingEnvironment;
    }

    public
    AddMemberController setMessagingEnvironment(MessagingEnvironment messagingEnvironment) {
        this.messagingEnvironment = messagingEnvironment;
        return this;
    }

    public
    AddMemberController setSelectedUsersList(LinkedList <User> selectedUsersList) {
        this.selectedUsersList = selectedUsersList;
        return this;
    }

    public List<User> getUsersList(){
        return LoggedInUser.getUser().getFollowings();
    }
    public boolean isMember(User user){
        return messagingEnvironment.getUsers().contains(user);
    }
    public void applyAddingMember(){
        List<Integer> selectedUsersId=new LinkedList <>();
        for (User user :selectedUsersList){
            selectedUsersId.add(user.getId());
            LOGGER.info("User "+LoggedInUser.getUser().getId()+" added user "+user.getId()+"to messaging Environment "+messagingEnvironment.getId());

        }
        AddMemberEvent addMemberEvent=new AddMemberEvent(messagingEnvironment.getId(), selectedUsersId);

        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.ADD_MEMBER, JSonController.objectToStringMapper(addMemberEvent), LoggedInUser.getUser().getToken());
        Service service = Actions.executeOnce(new Runnable() {
            @Override
            public
            void run() {
                 Response response= ServerHandler.transmitter(request);

                if (response.getType() == ResponseType.ADD_MEMBER) {
                 messagingEnvironment.setUsers(  JSonController.stringToObjectMapper(response.getData(), new TypeReference <List<User>>() {}));

                    System.out.println("Members added");
                }
            }
        }, null);
        service.start();
      //  context.MessagingEnvironmentDB.update(messagingEnvironment);
    }
}
