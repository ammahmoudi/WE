package controller.user;

import JSon.JSonController;
import controller.Actions;
import controller.Controller;
import controller.ServerHandler;
import event.messaging.NewMessageEvent;
import event.user.RelationshipEvent;
import event.user.RequestEvent;
import javafx.concurrent.Service;
import model.LoggedInUser;
import model.Notification;
import model.RelationshipType;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import request.Request;
import request.RequestType;
import response.Response;
import response.ResponseType;

public
class UserItemController  extends Controller {
    static Logger LOGGER = LogManager.getLogger(UserItemController.class.getName());

    User user=null;
    RelationshipType loggedInUserRelationship;
    RelationshipType userRelationship;

    public
    UserItemController(User user) {
        this.user = user;
    }

    public
    User getUser() {
        return user;
    }

    public
    UserItemController setUser(User user) {
        this.user = user;
        return this;
    }
    public void applyRelationship(RelationshipEvent relationshipEvent,Runnable runnable){
        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.APPLY_RELATIONSHIP, JSonController.objectToStringMapper(relationshipEvent), LoggedInUser.getUser().getToken());

        Service service = Actions.executeOnce(new Runnable() {
            @Override
            public
            void run() {
              //  System.out.println(getLoggedInUserRelationship());
                 Response response= ServerHandler.transmitter(request);


                if (response.getType() == ResponseType.APPLY_RELATIONSHIP) {
                    loggedInUserRelationship=JSonController.stringToObjectMapper(response.getData(),RelationshipType.class);
                }

            }
        }, runnable);
        service.start();

//        if (newState.equals("Following")) {
//            LOGGER.info("User "+ LoggedInUser.getUser().getId()+"started following user "+relationshipEvent.getTo());
//            Notification notification = new Notification(relationshipEvent.getFrom().getFullName() + " Started following you.", relationshipEvent.getTo(), relationshipEvent.getFrom().getId());
//            context.Notifications.add(notification);
//        }
//        if (newState.equals("NotFollowing")&&oldState.equals("Following")) {
//            LOGGER.info("User "+ LoggedInUser.getUser().getId()+"unfollowed user "+relationshipEvent.getTo());
//
//            Notification notification = new Notification(relationshipEvent.getFrom().getFullName() + "  unfollowed you.", relationshipEvent.getTo(), relationshipEvent.getFrom().getId());
//            context.Notifications.add(notification);
//        }
//        return newState;
    }
    public void applyRequest(RequestEvent requestEvent,Runnable runnable){
        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.APPLY_REQUEST, JSonController.objectToStringMapper(requestEvent), LoggedInUser.getUser().getToken());

        Service service = Actions.executeOnce(new Runnable() {
            @Override
            public
            void run() {
            //    System.out.println(getLoggedInUserRelationship());
                 Response response= ServerHandler.transmitter(request);


                if (response.getType() == ResponseType.APPLY_REQUEST) {
                   userRelationship=JSonController.stringToObjectMapper(response.getData(),RelationshipType.class);
                }

            }
        }, runnable);
        service.start();
//        if(requestEvent.isAccept()){
//            requestEvent.getFrom().accept(requestEvent.getTo());
//            LOGGER.info("User "+ requestEvent.getFrom().getId()+"accepted following request of user "+requestEvent.getTo());
//
//            Notification notification=new Notification(requestEvent.getFrom().getFullName()+" accepted your follow request.",requestEvent.getTo(),requestEvent.getFrom().getId());
//            context.Notifications.add(notification);
//        }
//        else{ requestEvent.getFrom().deny(requestEvent.getTo());
//            LOGGER.info("User "+ requestEvent.getFrom().getId()+"rejected following request of user "+requestEvent.getTo());
//
//            Notification notification=new Notification(requestEvent.getFrom().getFullName()+" denied your follow request.",requestEvent.getTo(),requestEvent.getFrom().getId());
//            context.Notifications.add(notification);
//        }
//        context.Users.update(requestEvent.getFrom());
//        context.Users.update(requestEvent.getTo());
//        return context.Users.get(requestEvent.getFrom().getId()).getRelationship(requestEvent.getTo());
    }
    public
    void updateRelationship(Runnable runnable) {

        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.LOGGED_IN_USER_RELATIONSHIP, getUser().getId().toString(), LoggedInUser.getUser().getToken());
        Request request2 = new Request(LoggedInUser.getUser().getId(), RequestType.USER_RELATIONSHIP, getUser().getId().toString(), LoggedInUser.getUser().getToken());

        Service service = Actions.executeOnce(new Runnable() {
            @Override
            public
            void run() {
                 Response response= ServerHandler.transmitter(request);

                if (response.getType() == ResponseType.LOGGED_IN_USER_RELATIONSHIP) {
                 loggedInUserRelationship=JSonController.stringToObjectMapper(response.getData(),RelationshipType.class);
                }
                 Response response2= ServerHandler.transmitter(request2);

                if (response2.getType() == ResponseType.USER_RELATIONSHIP) {
                    userRelationship=JSonController.stringToObjectMapper(response2.getData(),RelationshipType.class);
                }
            }
        }, runnable);
        service.start();

    }

    public
    RelationshipType getLoggedInUserRelationship() {
        return loggedInUserRelationship;
    }

    public
    UserItemController setLoggedInUserRelationship(RelationshipType loggedInUserRelationship) {
        this.loggedInUserRelationship = loggedInUserRelationship;
        return this;
    }

    public
    RelationshipType getUserRelationship() {
        return userRelationship;
    }

    public
    UserItemController setUserRelationship(RelationshipType userRelationship) {
        this.userRelationship = userRelationship;
        return this;
    }
}
