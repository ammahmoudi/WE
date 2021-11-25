package controller.generalPages;

import JSon.JSonController;
import controller.Actions;
import controller.Controller;
import controller.ServerHandler;
import javafx.concurrent.Service;
import model.LoggedInUser;
import model.MessagingEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.jmx.Server;
import request.Request;
import request.RequestType;
import response.Response;
import response.ResponseType;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public
class SideMenuController extends Controller {
    static Logger LOGGER = LogManager.getLogger(SideMenuController.class.getName());
    private MessagingEnvironment savedMessages;
    public
    List <MessagingEnvironment> getMessagingEnvironments(){
return LoggedInUser.getUser().getMessagingEnvironments();

    }

    public
    Service getSavedMessageMessagingEnvironment(Runnable runnable) {
        final MessagingEnvironment[] temp = new MessagingEnvironment[1];
        Runnable task = new Runnable() {
            @Override
            public
            void run() {


                 Response response= ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.PRIVATE_CHAT, LoggedInUser.getUser().getId().toString(), LoggedInUser.getUser().getToken()));

                if (response.getType() == ResponseType.PRIVATE_CHAT) {

                    temp[0] = JSonController.stringToObjectMapper(response.getData(),MessagingEnvironment.class);
                    setSavedMessages(temp[0]);

                }


            }
        };
        Service service = Actions.executeOnce(task, runnable);
        service.start();
        return service;
    }
    public void singOut(){
        LOGGER.info("User "+LoggedInUser.getUser().getId()+"signed out. ");
      ServerHandler.disconnectFromServer();
      Actions.cancelUpdaters();
      Actions.updaters.clear();
        Actions.services.clear();
        LoggedInUser.setUser(null);
        weConfig.loadProperties().remove("LoggedInUser");
        weConfig.saveProperties();

        System.out.println("signed out");

    }

    public
    MessagingEnvironment getSavedMessages() {
        return savedMessages;
    }

    public
    SideMenuController setSavedMessages(MessagingEnvironment savedMessages) {
        this.savedMessages = savedMessages;
        return this;
    }
}
