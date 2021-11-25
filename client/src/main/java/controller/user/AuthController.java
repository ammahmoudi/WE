package controller.user;

import JSon.JSonController;
import controller.Actions;
import controller.Controller;
import controller.ServerHandler;
import event.user.ChangeUserInfoEvent;
import event.user.SignInFormEvent;
import event.user.SignUpFormEvent;
import javafx.concurrent.Service;
import model.LoggedInUser;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import request.Request;
import request.RequestType;
import response.Response;
import response.ResponseType;

public
class AuthController extends Controller {
    private boolean loggedIn=false;
    static Logger LOGGER = LogManager.getLogger(AuthController.class.getName());

    public
    AuthController() {


    }
    public void checkForLoggedInUser(){

        if(weConfig.loadProperties().containsKey("LoggedInUser")){
            loggedIn=true;
            String userId=weConfig.loadProperties().getProperty("LoggedInUser");
         //   System.out.println(userId);
            User user=context.Users.get(Integer.parseInt(userId));
          //  System.out.println(user.getToken());
            LoggedInUser.setUser(user);
          //  System.out.println(LoggedInUser.getUser().getToken());
            Response result;
            ServerHandler.connectToServer();
            Boolean status=ServerHandler.isConnectionAlive();

            if(status==null|| !status){
                LoggedInUser.setOffline(true);
            }else{
                result=reSignIn();
             //   System.out.println(result.getType());
                if(result.getType()==ResponseType.ACCEPT_SIGN_IN){
                    LoggedInUser.setOffline(false);
                   // System.out.println(result.getType());
                }else {
                    LoggedInUser.setOffline(true);
                }

            }

        }
    }
    public Response reSignIn(){
        SignInFormEvent signInFormEvent = new SignInFormEvent(LoggedInUser.getUser().getUserName(), null);
        Response response= ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.SIGN_IN, JSonController.objectToStringMapper(signInFormEvent), LoggedInUser.getUser().getToken()));
        System.out.println("send login request");
        //System.out.println(LoggedInUser.getUser().getId()+"..."+LoggedInUser.getUser().getToken());
        //    System.out.println(response.getType());
        if (response.getType() == ResponseType.ACCEPT_SIGN_IN) {
            User user = JSonController.stringToObjectMapper(response.getData(), User.class);
            LOGGER.info("User " + user.getId() + " signed in");
            LoggedInUser.setUser(user);
            LoggedInUser.setOffline(false);
            LoggedInUser.sendOfflineMessages();
            System.out.println("start apply offline settings:"+Actions.changeUserInfoEvents.size());
            for(ChangeUserInfoEvent changeUserInfoEvent:Actions.changeUserInfoEvents){
                Request request=new Request(LoggedInUser.getUser().getId(), RequestType.CHANGE_USER_SETTING,JSonController.objectToStringMapper(changeUserInfoEvent),LoggedInUser.getUser().getToken());
                Service service = Actions.executeOnce(new Runnable() {
                    @Override
                    public
                    void run() {
                        Response response= ServerHandler.transmitter(request);

                        if (response.getType() == ResponseType.CHANGE_USER_SETTING) {
                            System.out.println("setting changed");
                            User user=JSonController.stringToObjectMapper(response.getData(),User.class);
                            LoggedInUser.getUser().changeInfo(user.getFirstName(), user.getLastName(), null,user.getUserName(), user.getPhoneNumber(), user.getEmail(),user.getBio(), user.getBirthDay(),user.getLastSeenPrivacy(),user.isPrivated(), user.getImage());
                            LoggedInUser.getUser().setMyOwnLastSeenPrivacy(user.getMyOwnLastSeenPrivacy());

                        }
                    }
                }, null);
                service.start();
            }
            System.out.println("done settings");

            Actions.offlineQueueServices.clear();


            context.Users.clearDB();

            context.Users.update(user);

            weConfig.loadProperties().remove("LoggedInUser");
            weConfig.appProps.setProperty("LoggedInUser",user.getId().toString());
            weConfig.saveProperties();
           // Actions.restartUpdaters();
            return response;
        } else if (response.getType() == ResponseType.ERROR_SIGN_IN) {
            return response;
        } else if (response.getType() == ResponseType.ERROR_NO_SERVER) {
            return response;
        }
        return null;
    }
    public
    String signIn(SignInFormEvent e) {

        ServerHandler.connectToServer();
        Response response = ServerHandler.transmitter(new Request(RequestType.SIGN_IN, JSonController.objectToStringMapper(e)));
       // System.out.println(response.getType());
        if (response.getType() == ResponseType.ACCEPT_SIGN_IN) {
            User user = JSonController.stringToObjectMapper(response.getData(), User.class);
            if (user != null) {
                LoggedInUser.setOffline(false);
                context.Users.clearDB();
                LOGGER.info("User " + user.getId() + " signed in");
                LoggedInUser.setUser(user);
                System.out.println("loging in");
                context.Users.update(user);
                weConfig.loadProperties().remove("LoggedInUser");
                weConfig.appProps.setProperty("LoggedInUser",user.getId().toString());
                weConfig.saveProperties();


            }


            return null;
        } else if (response.getType() == ResponseType.ERROR_SIGN_IN) {
            return response.getData();
        }
        return "unknown error!";

    }


    public
    boolean isUsedUsername(String text) {
        final Boolean[] result = {false};
        Request request = new Request(RequestType.IS_USED_USERNAME, text);

        Response response = ServerHandler.transmitter(request);

        if (response.getType() == ResponseType.IS_USED_USERNAME) {
            result[0] = JSonController.stringToObjectMapper(response.getData(), Boolean.class);
        }


        if (result[0] != null) {
            return result[0];
        }
        return false;
    }

    public
    boolean isUsedEmail(String text) {
        final Boolean[] result = {false};
        Request request = new Request(RequestType.IS_USED_EMAIL, text);

        Response response = ServerHandler.transmitter(request);

        if (response.getType() == ResponseType.IS_USED_EMAIL) {
            result[0] = JSonController.stringToObjectMapper(response.getData(), Boolean.class);
        }


        if (result[0] != null) {
            return result[0];
        }
        return false;
    }

    public
    String signUp(SignUpFormEvent signUpFormEvent) {

        Response response = ServerHandler.transmitter(new Request(RequestType.SIGN_UP, JSonController.objectToStringMapper(signUpFormEvent)));

        if (response.getType() == ResponseType.ACCEPT_SIGN_UP) {

            return null;
        } else if (response.getType() == ResponseType.ERROR_SIGN_UP) {
            return response.getData();
        }
        return "unknown error!";


    }

    public
    boolean isLoggedIn() {
        return loggedIn;
    }

    public
    AuthController setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
        return this;
    }
}
