package controller.notification;

import JSon.JSonController;
import controller.Actions;
import controller.Controller;
import controller.ServerHandler;
import javafx.concurrent.Service;
import model.LoggedInUser;
import model.Notification;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import request.Request;
import request.RequestType;
import response.Response;
import response.ResponseType;

public
class NotificationItemController extends Controller {
    Notification notification;

    static Logger LOGGER = LogManager.getLogger(NotificationItemController.class.getName());


    public
    NotificationItemController(Notification notification) {
        this.notification = notification;

    }

    public
    Notification getNotification() {
        return notification;
    }

    public
    NotificationItemController setNotification(Notification notification) {
        this.notification = notification;
        return this;
    }
    public void seen(){
if(!notification.isSeen()) {

    Request request = new Request(LoggedInUser.getUser().getId(), RequestType.SEEN_NOTIFICATION, notification.getId().toString(), LoggedInUser.getUser().getToken());
    Service service = Actions.executeOnce(new Runnable() {
        @Override
        public
        void run() {
             Response response= ServerHandler.transmitter(request);

            if (response.getType() == ResponseType.SEEN_NOTIFICATION) {
                System.out.println("Seen notification");
            }
        }
    }, null);
    service.start();
    //  notification.setSeen(true);
    //  LOGGER.info("User "+ LoggedInUser.getUser().getId()+"seen the notification "+notification.getId());

    //   context.Notifications.update(notification);
}
    }
    public User getSender(){
        return notification.getSenderUser();
    }
}
