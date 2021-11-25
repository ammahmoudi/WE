package controller.category;

import JSon.JSonController;
import com.fasterxml.jackson.core.type.TypeReference;
import controller.Actions;
import controller.Controller;
import controller.ServerHandler;
import event.messaging.NewMessageEvent;

import javafx.concurrent.Service;
import model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import request.Request;
import request.RequestType;
import response.Response;
import response.ResponseType;


import java.util.LinkedList;
import java.util.List;

import static model.LoggedInUser.getUser;

public
class CategoriesPageController extends Controller {
 User user;
    static Logger LOGGER = LogManager.getLogger(CategoriesPageController.class.getName());

    public
    CategoriesPageController(User user) {
        this.user=user;

    }


    public
    void newMessage(NewMessageEvent newMessageEvent) {

        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.NEW_MESSAGE, JSonController.objectToStringMapper(newMessageEvent), LoggedInUser.getUser().getToken());
        Service service = Actions.executeOnce(new Runnable() {
            @Override
            public
            void run() {
                 Response response= ServerHandler.transmitter(request);

                if (response.getType() == ResponseType.ACCEPT_NEW_MESSAGE) {
                    System.out.println("new Messages sent");
                    LOGGER.info(LoggedInUser.getUser().getId()+" sent a new message");
                }
            }
        }, null);
        service.start();

    }



  public List<Category> getCategories(){
        return user.getCategories();
  }
    public
    List <MessagingEnvironment> getMessagingEnvironments(){
        return getUser().getMessagingEnvironments();

    }
}



