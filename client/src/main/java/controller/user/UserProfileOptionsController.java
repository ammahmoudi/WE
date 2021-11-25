package controller.user;

import JSon.JSonController;
import controller.Actions;
import controller.Controller;
import controller.ServerHandler;
import event.user.BlockEvent;
import event.user.MuteEvent;
import javafx.concurrent.Service;
import model.LoggedInUser;
import model.RelationshipType;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import request.Request;
import request.RequestType;
import response.Response;
import response.ResponseType;

public
class UserProfileOptionsController extends Controller {
    UserProfileController userProfileController;
    static Logger LOGGER = LogManager.getLogger(UserProfileOptionsController.class.getName());


    public
    UserProfileOptionsController(UserProfileController userProfileController) {
        this.userProfileController=userProfileController;
    }

    public
    UserProfileController getUserProfileController() {
        return userProfileController;
    }

    public
    UserProfileOptionsController setUserProfileController(UserProfileController userProfileController) {
        this.userProfileController = userProfileController;
        return this;
    }
    public boolean isBLocked(){
        if(userProfileController.getLoggedInUserRelationship()== RelationshipType.BLOCKED) return true;
        return false;
    }
    public boolean isMuted(){
        if(userProfileController.getLoggedInUserRelationship()==RelationshipType.MUTED||userProfileController.getLoggedInUserRelationship()==RelationshipType.FOLLOWING_MUTED)return true;
        return false;
    }

    public Service applyBlock(BlockEvent blockEvent,Runnable runnable){

        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.BLOCK_USER, JSonController.objectToStringMapper(blockEvent), LoggedInUser.getUser().getToken());
        Service service = Actions.executeOnce(new Runnable() {
            @Override
            public
            void run() {
                 Response response= ServerHandler.transmitter(request);

                if (response.getType() == ResponseType.BLOCK_USER) {
                    //setPost(JSonController.stringToObjectMapper(response.getData(),Post.class));
                    System.out.println("blocked");
                }
            }
        }, runnable);
        service.start();
        LOGGER.info("User "+ blockEvent.getFrom()+"applied blocking on "+blockEvent.getTo());

     //   context.Users.update(blockEvent.getFrom());
  return  service;
    }
    public Service applyMute(MuteEvent muteEvent,Runnable runnable){

        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.MUTE_USER, JSonController.objectToStringMapper(muteEvent), LoggedInUser.getUser().getToken());
        Service service = Actions.executeOnce(new Runnable() {
            @Override
            public
            void run() {
                 Response response= ServerHandler.transmitter(request);

                if (response.getType() == ResponseType.MUTE_USER) {

                    System.out.println("muted");
                }
            }
        }, runnable);
        service.start();
        LOGGER.info("User "+ muteEvent.getFrom()+"applied muting on "+muteEvent.getTo());

     //   context.Users.update(muteEvent.getFrom());
        return service;
    }
}
