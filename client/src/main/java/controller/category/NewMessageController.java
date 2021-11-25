package controller.category;

import JSon.JSonController;
import controller.Actions;
import controller.Controller;
import controller.ServerHandler;
import event.messaging.NewMessageEvent;
import javafx.concurrent.Service;
import model.Category;
import model.LoggedInUser;
import model.MessagingEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import request.Request;
import request.RequestType;
import response.Response;
import response.ResponseType;

public
class NewMessageController extends Controller {
    Category category;
    static Logger LOGGER = LogManager.getLogger(NewMessageController.class.getName());
    public
    NewMessageController(Category category) {
       // System.out.println(category.getUserList().size());
        this.category = category;
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
                    LOGGER.info(LoggedInUser.getUser().getId()+" sent a new Message ");
                }
            }
        }, null);
        service.start();

    }

    public
    Category getCategory() {
        return category;
    }

    public
    NewMessageController setCategory(Category category) {
        this.category = category;
        return this;
    }


    public
    MessagingEnvironment getPrivateChat(Integer userId) {
        final MessagingEnvironment[] temp = new MessagingEnvironment[1];


         Response response= ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.PRIVATE_CHAT, userId.toString(), LoggedInUser.getUser().getToken()));

        if (response.getType() == ResponseType.PRIVATE_CHAT) {

            temp[0] = JSonController.stringToObjectMapper(response.getData(), MessagingEnvironment.class);
      //      System.out.println(temp[0].getId());

        }


        return temp[0];
    }
    

}



