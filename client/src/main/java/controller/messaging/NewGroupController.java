package controller.messaging;

import JSon.JSonController;
import com.fasterxml.jackson.core.type.TypeReference;
import controller.Actions;
import controller.Controller;
import controller.ServerHandler;
import event.messaging.NewGroupEvent;
import event.messaging.NewMessageEvent;
import javafx.concurrent.Service;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import model.LoggedInUser;
import model.MessagingEnvironment;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import request.Request;
import request.RequestType;
import response.Response;
import response.ResponseType;
import view.generalPages.MainPageView;
import view.messaging.MessagingPageView;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public
class NewGroupController extends Controller {

    static Logger LOGGER = LogManager.getLogger(NewGroupController.class.getName());
    LinkedList <User> usersList = new LinkedList <>();


    public
    NewGroupController() {

    }

    public
    void newMessage(NewMessageEvent newMessageEvent) {
//        Message message=new Message(newMessageEvent.getSender(), newMessageEvent.getTaker(), newMessageEvent.getText(),newMessageEvent.getOriginalMessage(),newMessageEvent.getImage(),null);
//        LOGGER.info("User "+message.getOwner().getId()+"sent message "+message.getId()+"to messaging Environment "+message.getMessagingEnvironment().getId());
//
//        if(message.isForwarded()){
//            message.getOriginalMessage().forward(message);
//        }
//        context.Messages.add(message);
//        return message;
    }

    public
    List <MessagingEnvironment> getMessagingEnvironments() {

        return LoggedInUser.getUser().getMessagingEnvironments();
    }

    public
    LinkedList <User> getUsersList() {
        return usersList;
    }

    public
    MessagingEnvironment newGroup(NewGroupEvent newGroupEvent) {
        final MessagingEnvironment[] messagingEnvironment = new MessagingEnvironment[1];
        final List <User>[] updatedUsers = new List[]{new LinkedList <>()};
        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.NEW_GROUP, JSonController.objectToStringMapper(newGroupEvent), LoggedInUser.getUser().getToken());

        Service service = Actions.executeOnce(new Runnable() {
                                                  @Override
                                                  public
                                                  void run() {
                                                       Response response= ServerHandler.transmitter(request);

                                                      if (response.getType() == ResponseType.NEW_GROUP) {

                                                          messagingEnvironment[0] = JSonController.stringToObjectMapper(response.getData(), MessagingEnvironment.class);
                                                          System.out.println("group created");

                                                      }

                                                       Response response2= ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.UPDATE_USERS, messagingEnvironment[0].getId().toString(), LoggedInUser.getUser().getToken()));


                                                      if (response2.getType() == ResponseType.UPDATED_USERS) {

                                                          updatedUsers[0] = JSonController.stringToObjectMapper(response2.getData(), new TypeReference <List <User>>() {
                                                          });
                                                         messagingEnvironment[0].setUsers(updatedUsers[0]);
                                                          messagingEnvironment[0].applyTaker(LoggedInUser.getUser());

                                                      }
                                                  }


                                              }
                , new Runnable() {
                    @Override
                    public
                    void run() {
                        MainPageView.newTab(getMessagingPageContent(messagingEnvironment[0]), newGroupEvent.getName());
                    }
                });
        service.start();

//      //  context.MessagingEnvironmentDB.add(messagingEnvironment);
        return messagingEnvironment[0];


    }

    public
    Node getMessagingPageContent(MessagingEnvironment messagingEnvironment) {
        Node chat = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/messaging/MessagingPage.fxml"));
        try {

            chat = loader.load();
            MessagingPageView messagingPageView = loader.getController();


            messagingPageView.initializeValues(messagingEnvironment);
            MainPageView.newTab(chat, messagingEnvironment.getName());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return chat;
    }

}
