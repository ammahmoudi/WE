package controller.generalPages;

import JSon.JSonController;
import controller.Actions;
import controller.Controller;
import controller.ServerHandler;
import event.user.ChangeUserInfoEvent;
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
class SettingsController extends Controller {
    static Logger LOGGER = LogManager.getLogger(SettingsController.class.getName());

    public void applyChanges(ChangeUserInfoEvent changeUserInfoEvent,Runnable runnable){
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
        }, runnable);
        if(LoggedInUser.getOffline())Actions.changeUserInfoEvents.add(changeUserInfoEvent);
        else{
        service.start();}


        LOGGER.info("User "+LoggedInUser.getUser().getId()+"changed the settings. ");


      //  context.Users.update(LoggedInUser.getUser());

    }
    public boolean isUsedUsername(String text){
        final Boolean[] result = {false};
        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.IS_USED_USERNAME, text, LoggedInUser.getUser().getToken());

                 Response response= ServerHandler.transmitter(request);

                if (response.getType() == ResponseType.IS_USED_USERNAME) {
                    result[0] =JSonController.stringToObjectMapper(response.getData(),Boolean.class);
                }

return result[0];
    }
    public boolean isUsedEmail(String text){
        final Boolean[] result = {false};
        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.IS_USED_EMAIL, text, LoggedInUser.getUser().getToken());

                 Response response= ServerHandler.transmitter(request);

                if (response.getType() == ResponseType.IS_USED_EMAIL) {
                    result[0] =JSonController.stringToObjectMapper(response.getData(),Boolean.class);
                }



        return result[0];
    }
    public boolean isCurrentPassword(String text){
        final Boolean[] result = {false};
        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.IS_CURRENT_PASSWORD, text, LoggedInUser.getUser().getToken());

                 Response response= ServerHandler.transmitter(request);

                if (response.getType() == ResponseType.IS_CURRENT_PASSWORD) {

                    result[0] = JSonController.stringToObjectMapper(response.getData(), Boolean.class);
                }
        return result[0];
    }
    public void deActive(){

        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.DEACTIVATE_ACCOUNT, "", LoggedInUser.getUser().getToken());

         Response response= ServerHandler.transmitter(request);

        if (response.getType() == ResponseType.DEACTIVATE_ACCOUNT) {

            LOGGER.info("User "+LoggedInUser.getUser().getId()+"signed out. ");
            LOGGER.info("User "+LoggedInUser.getUser().getId()+"deactivated the account .");
            ServerHandler.disconnectFromServer();
            Actions.updaters.clear();
            Actions.services.clear();

            LoggedInUser.setUser(null);
        }

      //  LoggedInUser.getUser().setActivate(false);


       // context.Users.update(LoggedInUser.getUser());
    }
    public void deleteAccount(){
        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.DELETE_ACCOUNT, "", LoggedInUser.getUser().getToken());

         Response response= ServerHandler.transmitter(request);

        if (response.getType() == ResponseType.DELETE_ACCOUNT) {
            LOGGER.info("User "+LoggedInUser.getUser().getId()+"deleted the account.");
            LOGGER.info("User "+LoggedInUser.getUser().getId()+"signed out. ");
            ServerHandler.disconnectFromServer();
            Actions.updaters.clear();
            Actions.services.clear();
            LoggedInUser.setUser(null);
        }


       // context.Users.update(LoggedInUser.getUser());
    }
}
