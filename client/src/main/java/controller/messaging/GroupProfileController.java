package controller.messaging;

import JSon.JSonController;
import controller.Actions;
import controller.Controller;
import controller.ServerHandler;
import event.messaging.ModifyGroupInfoEvent;
import javafx.concurrent.Service;
import model.LoggedInUser;
import model.MessagingEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import request.Request;
import request.RequestType;
import response.Response;
import response.ResponseType;

public
class GroupProfileController extends Controller {
    static Logger LOGGER = LogManager.getLogger(GroupProfileController.class.getName());
    MessagingEnvironment messagingEnvironment;


    public
    GroupProfileController(MessagingEnvironment messagingEnvironment) {
        this.messagingEnvironment = messagingEnvironment;
    }

    public
    MessagingEnvironment getMessagingEnvironment() {
        return messagingEnvironment;
    }

    public
    GroupProfileController setMessagingEnvironment(MessagingEnvironment messagingEnvironment) {
        this.messagingEnvironment = messagingEnvironment;
        return this;
    }

    public
    void leave(Runnable runnable) {
        //  messagingEnvironment.removeMember(LoggedInUser.getUser());
        LOGGER.info("User " + LoggedInUser.getUser().getId() + " left the messaging environment " + messagingEnvironment.getId());
        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.LEAVE_GROUP, messagingEnvironment.getId().toString(), LoggedInUser.getUser().getToken());
        Service service = Actions.executeOnce(new Runnable() {
            @Override
            public
            void run() {
                 Response response= ServerHandler.transmitter(request);

                if (response.getType() == ResponseType.LEAVE_GROUP) {

                    System.out.println("left the Group");
                    setMessagingEnvironment(null);
                }
            }
        }, runnable);
        service.start();
        //  context.MessagingEnvironmentDB.update(messagingEnvironment);
    }

    public
    void applyChanges(ModifyGroupInfoEvent modifyGroupInfoEvent) {


        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.MODIFY_GROUP_INFO, JSonController.objectToStringMapper(modifyGroupInfoEvent), LoggedInUser.getUser().getToken());
        Service service = Actions.executeOnce(new Runnable() {
            @Override
            public
            void run() {
                 Response response= ServerHandler.transmitter(request);

                if (response.getType() == ResponseType.MODIFY_GROUP_INFO) {
                    messagingEnvironment.setImage(modifyGroupInfoEvent.getImage());
                    messagingEnvironment.setName(modifyGroupInfoEvent.getName());
                    System.out.println("Group info changed");
                }
            }
        }, null);
        service.start();
        LOGGER.info("User " + messagingEnvironment.getOwner().getId() + " changed the properties of messaging environment" + messagingEnvironment.getId());

        //context.MessagingEnvironmentDB.update(messagingEnvironment);
    }
}
