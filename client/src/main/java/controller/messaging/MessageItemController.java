package controller.messaging;

import controller.Actions;
import controller.Controller;
import controller.ServerHandler;
import javafx.concurrent.ScheduledService;
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

import java.util.List;

public
class MessageItemController extends Controller {

    static Logger LOGGER = LogManager.getLogger(MessageItemController.class.getName());

    Message message = null;

    public
    MessageItemController(Message message) {
        this.message = message;
    }

    public
    Message getMessage() {
        return message;
    }

    public
    MessageItemController setMessage(Message message) {
        this.message = message;
        return this;
    }

    //onlyprivatechat
    public
    User getTakerUser() {
        if (!message.getMessagingEnvironment().isGroup()) {
            for (User user : message.getMessagingEnvironment().getUsers()) {
                if (!user.equals(LoggedInUser.getUser())) return user;
            }

        }
        return null;
    }

    public
    boolean isSeen() {
        return getMessage().isFinalSeen();

    }

    public
    void Seen() {
        if (!message.isUserSeen()) {
            Request request = new Request(LoggedInUser.getUser().getId(), RequestType.SEEN, message.getId().toString(), LoggedInUser.getUser().getToken());
            Service service = Actions.executeOnce(new Runnable() {
                @Override
                public
                void run() {
                    try {
                         Response response= ServerHandler.transmitter(request);

                        if (response.getType() == ResponseType.ACCEPT_SEEN) {
                         //   System.out.println("message seen");
                            LOGGER.info("User " + LoggedInUser.getUser().getId() + "seen message " + message.getId());
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }


            }, null);
service.start();
        }

    }

    public
    void remove() {

        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.DELETE_MESSAGE, getMessage().getId().toString(), LoggedInUser.getUser().getToken());
        Service service = Actions.executeOnce(new Runnable() {
            @Override
            public
            void run() {
                 Response response= ServerHandler.transmitter(request);
              //  System.out.println("delete meeage :"+response.getType());
                if (response.getType() == ResponseType.ACCEPT_DELETE_MESSAGE) {
                    System.out.println("message deleted");
                }
            }
        }, new Runnable() {
            @Override
            public
            void run() {
      //          LoggedInUser.getMessagingEnvironmentsUpdater().restart();
//
            }
        });
        service.start();
        LOGGER.info("User " + message.getOwner().getId() + "removed message " + message.getId());


    }

    public
    List <MessagingEnvironment> getMessagingEnvironments() {
        return LoggedInUser.getUser().getMessagingEnvironments();
    }
}

