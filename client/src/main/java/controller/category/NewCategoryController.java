package controller.category;

import JSon.JSonController;
import controller.Actions;
import controller.Controller;
import controller.ServerHandler;
import event.category.NewCategoryEvent;
import event.messaging.NewMessageEvent;
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
class NewCategoryController extends Controller {
    static Logger LOGGER = LogManager.getLogger(NewCategoryController.class.getName());


    public
    NewCategoryController() {

    }




    public
    List <MessagingEnvironment> getMessagingEnvironments(){

        return LoggedInUser.getUser().getMessagingEnvironments();
    }
    LinkedList <User> usersList =new LinkedList <>();

    public
    LinkedList <User> getUsersList() {
        return usersList;
    }
    public Category newCategory(NewCategoryEvent newCategoryEvent,Runnable runnable) {


        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.NEW_CATEGORY, JSonController.objectToStringMapper(newCategoryEvent), LoggedInUser.getUser().getToken());
        Service service = Actions.executeOnce(new Runnable() {
            @Override
            public
            void run() {
                 Response response= ServerHandler.transmitter(request);

                if (response.getType() == ResponseType.NEW_CATEGORY) {
                    System.out.println("new Category");
                    LOGGER.info(LoggedInUser.getUser().getId()+" created a new category");
                }
            }
        }, runnable);
        service.start();


        // Category category=new Category(newCategoryEvent.getName(), newCategoryEvent.getOwner());
//        LOGGER.info("User "+category.getOwner().getId()+"created category "+category.getId());
//
//        for (User user :newCategoryEvent.getUsersList()){
//            category.addUser(user);
//            LOGGER.info("User "+category.getOwner().getId()+"added "+user.getId()+"to category "+category.getId());
//
//        }
//        context.Categories.add(category);
//        return category;


        //}

return null;    }
}
