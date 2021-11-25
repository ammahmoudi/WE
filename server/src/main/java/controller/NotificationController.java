package controller;

import JSon.JSonController;
import controller.Controller;
import controller.server.ClientHandler;
import controller.server.Transmitter;
import model.Notification;
import response.Response;
import response.ResponseType;

import java.util.List;

public
class NotificationController extends Controller {
    public
    NotificationController(ClientHandler clientHandler) {
        super(clientHandler);
    }
    public void sendNotifications(){
        List <Notification> notifications=context.Users.getList(clientHandler.getLoggedInUser().getToken(),Notification.class,"Notiications",getClientHandler().getLoggedInUser().getId());
        String data;
        data = JSonController.objectToStringMapper(notifications);
        Response response = new Response(ResponseType.UPDATED_NOTIFICATIONS, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);
    }
    public void seenNotification(Integer id){
        Notification notification=context.Notifications.get(id);
        notification.setSeen(true);
        context.Notifications.update(notification);
        String data;
        data = JSonController.objectToStringMapper(id.toString());
        Response response = new Response(ResponseType.SEEN_NOTIFICATION, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }
}
