package controller.generalPages;

import JSon.JSonController;
import com.fasterxml.jackson.core.type.TypeReference;
import controller.Actions;
import controller.Controller;
import controller.ServerHandler;
import javafx.concurrent.Service;
import model.LoggedInUser;
import model.Notification;
import model.User;
import request.Request;
import request.RequestType;
import response.Response;
import response.ResponseType;

import java.util.LinkedList;
import java.util.List;

public
class ActivityController extends Controller {
    public
    List <Notification> getNotifications() {
        return LoggedInUser.getUser().getNotifications();
    }


}
