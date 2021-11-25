package controller;

import JSon.JSonController;
import controller.server.ClientHandler;
import controller.server.Transmitter;
import event.category.ModifyCategoryUsersEvent;
import event.category.NewCategoryEvent;
import model.Category;
import model.RelationshipType;
import model.User;
import response.Response;
import response.ResponseType;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.LinkedList;
import java.util.List;

public
class CategoryController extends  Controller{
    public
    CategoryController(ClientHandler clientHandler) {
        super(clientHandler);
    }
    public void newCategory(NewCategoryEvent newCategoryEvent){
        User owner=context.Users.get(newCategoryEvent.getOwner());
        Category category=new Category(newCategoryEvent.getName(), owner);
        for(Integer id: newCategoryEvent.getUsersList()) {
            category.addUser(context.Users.get(id));
        }
        context.Categories.add(category);
        String data="n";
     //   data = JSonController.objectToStringMapper(po);
        Response response = new Response(ResponseType.NEW_CATEGORY, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }
    public void sendCategory(Integer id){
      Category category=context.Categories.get(id);

        String data;
          data = JSonController.objectToStringMapper(category);
        Response response = new Response(ResponseType.CATEGORY, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    } public void deleteCategory(Integer id){
        Category category=context.Categories.get(id);
        context.Categories.remove(category);
        String data="n";
     //   data = JSonController.objectToStringMapper(category);
        Response response = new Response(ResponseType.DELETE_CATEGORY, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }
    public void modifyCategoryInfo(Category newCategory){
        Category category=context.Categories.get(newCategory.getId());
        category.setImage(newCategory.getImage());
        category.setName(newCategory.getName());
        context.Categories.update(category);

        String data;
        data = JSonController.objectToStringMapper(category);
        Response response = new Response(ResponseType.MODIFY_CATEGORY_INFO, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }
    public void modifyCategoryUsers(ModifyCategoryUsersEvent modifyCategoryUsersEvent){
        List <User> selectedUsersList=new LinkedList <>();
        for(Integer id:modifyCategoryUsersEvent.getUsersList()){
            selectedUsersList.add(context.Users.get(id));
        }
        Category category=context.Categories.get(modifyCategoryUsersEvent.getCategory());
                for (User user :selectedUsersList){
            if(!category.getUserList().contains(user)){
                category.addUser(user);

            }

        }
        List<User>temp =new LinkedList<>(category.getUserList());
        for(User user : temp){
            if(!selectedUsersList.contains(user)){
                category.removeUser(user);


            }
        }
        context.Categories.update(category);
        for(User user :category.getUserList()){
            switch (user.getLastSeenPrivacy()){
                case EVERYONE:break;
                case NOBODY:user.setLastSeen(null);break;
                case ONLY_FOLLOWINGS:
                    RelationshipType relationshipType=user.getRelationshipTo(clientHandler.getLoggedInUser());
                    if(relationshipType!=RelationshipType.FOLLOWING&&relationshipType!=RelationshipType.FOLLOWING_MUTED){
                        user.setLastSeen(null);
                    }
                    break;
            }
        }
        String data;
        data = JSonController.objectToStringMapper(category);
        Response response = new Response(ResponseType.MODIFY_CATEGORY_USERS, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }
}
