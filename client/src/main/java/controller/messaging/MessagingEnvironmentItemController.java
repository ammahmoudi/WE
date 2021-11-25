package controller.messaging;

import JSon.JSonController;
import com.fasterxml.jackson.core.type.TypeReference;
import controller.Actions;
import controller.Controller;
import controller.ServerHandler;
import javafx.concurrent.Service;
import model.LoggedInUser;
import model.MessagingEnvironment;
import model.User;
import request.Request;
import request.RequestType;
import response.Response;
import response.ResponseType;

import java.util.LinkedList;
import java.util.List;

public
class MessagingEnvironmentItemController extends Controller {
    MessagingEnvironment messagingEnvironment;

    public
    MessagingEnvironmentItemController(MessagingEnvironment messagingEnvironment) {
        this.messagingEnvironment = messagingEnvironment;
    }

    public
    MessagingEnvironment getMessagingEnvironment() {
        return messagingEnvironment;
    }
public Service getFullMessagingEnvironment(Runnable runnable){
    final List <User>[] updatedUsers = new List[]{new LinkedList <>()};

    Runnable task = new Runnable() {
        @Override
        public
        void run() {


             Response response= ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.UPDATE_USERS, messagingEnvironment.getId().toString(), LoggedInUser.getUser().getToken()));


            if (response.getType() == ResponseType.UPDATED_USERS) {

                updatedUsers[0] = JSonController.stringToObjectMapper(response.getData(), new TypeReference <List <User>>() {
                });
                getMessagingEnvironment().setUsers(updatedUsers[0]);
                messagingEnvironment.applyTaker(LoggedInUser.getUser());

            }






        }
    };
    Service service= Actions.executeOnce(task,runnable);
    return service;
}
    public
    MessagingEnvironmentItemController setMessagingEnvironment(MessagingEnvironment messagingEnvironment) {
        this.messagingEnvironment = messagingEnvironment;
        return this;
    }
    public
    User getTakerUser(){
        if(!messagingEnvironment.isGroup()){
            for(User user :messagingEnvironment.getUsers()){
                if (!user.equals(LoggedInUser.getUser()))return user;
            }
            return LoggedInUser.getUser();

        }
        return null;
    }


}
