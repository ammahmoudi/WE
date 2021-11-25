package controller.category;

import JSon.JSonController;
import controller.Actions;
import controller.Controller;
import controller.ServerHandler;
import event.category.ModifyCategoryUsersEvent;
import event.messaging.ModifyGroupInfoEvent;
import event.messaging.NewGroupEvent;
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
class CategoryMemberChoserController extends Controller {
Category category;
    static Logger LOGGER = LogManager.getLogger(CategoryMemberChoserController.class.getName());

    public
    CategoryMemberChoserController(Category category) {
        this.category = category;
        selectedUsersList.addAll(category.getUserList());
    }




    LinkedList <User> selectedUsersList =new LinkedList <>();

    public
    LinkedList <User> getSelectedUsersList() {
        return selectedUsersList;
    }




    public
    Category getCategory() {
        return category;
    }


    public
    CategoryMemberChoserController setCategory(Category category) {
        this.category = category;
        return this;
    }

    public
    CategoryMemberChoserController setSelectedUsersList(LinkedList <User> selectedUsersList) {
        this.selectedUsersList = selectedUsersList;
        return this;
    }

    public List<User> getUsersList(){

        return LoggedInUser.getUser().getFollowings();
    }
    public boolean isMember(User user){
        return category.getUserList().contains(user);
    }
    public void applyModifyingMember(Runnable runnable){
        List<Integer>userIds=new LinkedList <>();
        for (User user :selectedUsersList){
          userIds.add(user.getId());

        }
        ModifyCategoryUsersEvent modifyCategoryUsersEvent=new ModifyCategoryUsersEvent(category.getId(),userIds);
        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.MODIFY_CATEGORY_USERS, JSonController.objectToStringMapper(modifyCategoryUsersEvent), LoggedInUser.getUser().getToken());
        Service service = Actions.executeOnce(new Runnable() {
            @Override
            public
            void run() {
                 Response response= ServerHandler.transmitter(request);

                if (response.getType() == ResponseType.MODIFY_CATEGORY_USERS) {
                    System.out.println("Category users modified");
                    LOGGER.info(LoggedInUser.getUser().getId()+" changed the users of category "+category.getId());
                }
            }
        }, runnable);
        service.start();
//        for (User user :selectedUsersList){
//            if(!category.getUserList().contains(user)){
//                category.addUser(user);
//                LOGGER.info("User "+category.getOwner().getId()+" add user "+user.getId()+" to category  "+category.getId());
//
//            }
//
//        }
//        List<User>temp =new LinkedList<>(category.getUserList());
//        for(User user : temp){
//            if(!selectedUsersList.contains(user)){
//                category.removeUser(user);
//                LOGGER.info("User "+category.getOwner().getId()+" removed user "+user.getId()+" from category  "+category.getId());
//
//
//            }
//        }

    //    context.Categories.update(category);
    }
}
