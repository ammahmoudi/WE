package controller.user;

import JSon.JSonController;
import com.fasterxml.jackson.core.type.TypeReference;
import controller.Actions;
import controller.Controller;
import controller.ServerHandler;
import event.user.BlockEvent;
import event.user.MuteEvent;
import event.user.RelationshipEvent;
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

public
class UserProfileController extends Controller {
    static Logger LOGGER = LogManager.getLogger(UserProfileController.class.getName());
    User user = null;
    RelationshipType loggedInUserRelationship;
    RelationshipType userRelationship;
    MessagingEnvironment messagingEnvironment;

    public
    UserProfileController(User user) {
        this.user = user;
    }

    public
    User getUser() {
        return user;
    }

    public
    UserProfileController setUser(User user) {
        this.user = user;
        return this;
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
                    loggedInUserRelationship = JSonController.stringToObjectMapper(response.getData(), RelationshipType.class);
                }
                 Response response2= ServerHandler.transmitter(request2);

                if (response2.getType() == ResponseType.USER_RELATIONSHIP) {
                    userRelationship = JSonController.stringToObjectMapper(response2.getData(), RelationshipType.class);
                }
            }
        }, runnable);
        service.start();

    }

    public
    LinkedList <Post> getUserPosts() {
        LinkedList <Post> userPosts = new LinkedList <>();
        userPosts = context.Posts.postsList(user);
        return userPosts;
    }

    public
    void applyRelationship(RelationshipEvent relationshipEvent, Runnable runnable) {
        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.APPLY_RELATIONSHIP, JSonController.objectToStringMapper(relationshipEvent), LoggedInUser.getUser().getToken());

        Service service = Actions.executeOnce(new Runnable() {
            @Override
            public
            void run() {
              //  System.out.println(getLoggedInUserRelationship());
                 Response response= ServerHandler.transmitter(request);


                if (response.getType() == ResponseType.APPLY_RELATIONSHIP) {
                    loggedInUserRelationship = JSonController.stringToObjectMapper(response.getData(), RelationshipType.class);
                   // System.out.println(getLoggedInUserRelationship());
                }

            }
        }, runnable);
        service.start();

    }

    public
    boolean isBlockedPage() {
        return LoggedInUser.getUser().isBlockedBy(user);
    }

    public
    boolean isPrivatePage() {
        return !(getLoggedInUserRelationship() == RelationshipType.FOLLOWING || getLoggedInUserRelationship() == RelationshipType.FOLLOWING_MUTED) && user.isPrivated() && !user.equals(LoggedInUser.getUser());
    }

    public
    Service getPrivateChat(Runnable runnable) {
        final MessagingEnvironment[] temp = new MessagingEnvironment[1];
        Runnable task = new Runnable() {
            @Override
            public
            void run() {


                 Response response= ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.PRIVATE_CHAT, getUser().getId().toString(), LoggedInUser.getUser().getToken()));

                if (response.getType() == ResponseType.PRIVATE_CHAT) {

                  temp[0] =JSonController.stringToObjectMapper(response.getData(),MessagingEnvironment.class);
                //    System.out.println(temp[0].getId());

                }
               setMessagingEnvironment(temp[0]);


            }
        };
        Service service = Actions.executeOnce(task, runnable);
        service.start();
        return service;
    }

    public
    RelationshipType getLoggedInUserRelationship() {
        return loggedInUserRelationship;
    }

    public
    UserProfileController setLoggedInUserRelationship(RelationshipType loggedInUserRelationship) {
        this.loggedInUserRelationship = loggedInUserRelationship;
        return this;
    }

    public
    RelationshipType getUserRelationship() {
        return userRelationship;
    }

    public
    UserProfileController setUserRelationship(RelationshipType userRelationship) {
        this.userRelationship = userRelationship;
        return this;
    }

    public
    Service updateFollowings(Runnable runnable) {
        final List <User>[] updatedUsers = new List[]{new LinkedList <>()};

        Runnable task = new Runnable() {
            @Override
            public
            void run() {


                 Response response= ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.USER_FOLLOWINGS, getUser().getId().toString(), LoggedInUser.getUser().getToken()));

                if (response.getType() == ResponseType.USER_FOLLOWINGS) {
                    updatedUsers[0] = JSonController.stringToObjectMapper(response.getData(), new TypeReference <List <User>>() {
                    });
                }
                getUser().setFollowings(updatedUsers[0]);
                if(updatedUsers[0]==null)getUser().setFollowings(new LinkedList <>());


            }
        };
        Service service = Actions.executeOnce(task, runnable);
        service.start();
        return service;
    }
    public
    Service updateFollowers(Runnable runnable) {
        final List <User>[] updatedUsers = new List[]{new LinkedList <>()};

        Runnable task = new Runnable() {
            @Override
            public
            void run() {


                 Response response= ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.USER_FOLLOWERS, getUser().getId().toString(), LoggedInUser.getUser().getToken()));

         //       System.out.println(response.getType());
             //   System.out.println(response.getData());
                if (response.getType() == ResponseType.USER_FOLLOWERS) {
                    updatedUsers[0] = JSonController.stringToObjectMapper(response.getData(), new TypeReference <List <User>>() {
                    });
                 //   System.out.println(updatedUsers[0].size());
                }
                getUser().setFollowers(updatedUsers[0]);
                if(updatedUsers[0]==null)getUser().setFollowers(new LinkedList <>());



            }
        };
        Service service = Actions.executeOnce(task, runnable);
        service.start();
        return service;
    }
    public
    Service updateUser(Runnable runnable) {
        final User[] tempUser = new User[1];

        Runnable task = new Runnable() {
            @Override
            public
            void run() {


                 Response response= ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.UPDATE_USER, getUser().getId().toString(), LoggedInUser.getUser().getToken()));

                if (response.getType() == ResponseType.UPDATE_USER) {
                   tempUser[0] = JSonController.stringToObjectMapper(response.getData(), User.class);
                }
                getUser().setUserName(tempUser[0].getUserName());
                getUser().setBio(tempUser[0].getBio());
                getUser().setEmail(tempUser[0].getEmail());
                getUser().setFirstName(tempUser[0].getFirstName());
                getUser().setLastName(tempUser[0].getLastName());
                getUser().setBirthDay(tempUser[0].getBirthDay());
                getUser().setImage(tempUser[0].getImage());
                getUser().setPrivated(tempUser[0].isPrivated());

            }
        };
        Service service = Actions.executeOnce(task, runnable);
        service.start();
        return service;
    }

    public
    MessagingEnvironment getMessagingEnvironment() {
        return messagingEnvironment;
    }

    public
    UserProfileController setMessagingEnvironment(MessagingEnvironment messagingEnvironment) {
        this.messagingEnvironment = messagingEnvironment;
        return this;
    }
}
