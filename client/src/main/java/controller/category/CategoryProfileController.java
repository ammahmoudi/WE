package controller.category;

import JSon.JSonController;
import com.fasterxml.jackson.core.type.TypeReference;
import controller.Actions;
import controller.Controller;
import controller.ServerHandler;
import javafx.concurrent.Service;
import model.Category;
import model.LoggedInUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import request.Request;
import request.RequestType;
import response.Response;
import response.ResponseType;

import java.util.LinkedList;
import java.util.List;

import static model.LoggedInUser.getUser;

public
class CategoryProfileController extends Controller {
    Category category;
    static Logger LOGGER = LogManager.getLogger(CategoryProfileController.class.getName());

    public
    CategoryProfileController(Category category) {
        this.category = category;
    }

    public
    Category getCategory() {
        return category;
    }

    public
    CategoryProfileController setCategory(Category category) {
        this.category = category;
        return this;
    }

    public
    Service updateCategory(Runnable runnable) {
        final Category[] temp = new Category[1];

        Runnable task = new Runnable() {
            @Override
            public
            void run() {


                 Response response= ServerHandler.transmitter(new Request(getUser().getId(), RequestType.CATEGORY, category.getId().toString(), getUser().getToken()));

                if (response.getType() == ResponseType.CATEGORY) {
                    temp[0] = JSonController.stringToObjectMapper(response.getData(),Category.class);
                }
                setCategory(temp[0]);


            }
        };
        Service service = Actions.executeOnce(task, runnable);
        service.start();
        return service;
    }
    public void delete(Runnable runnable){
        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.DELETE_CATEGORY, category.getId().toString(), LoggedInUser.getUser().getToken());
        Service service = Actions.executeOnce(new Runnable() {
            @Override
            public
            void run() {
                 Response response= ServerHandler.transmitter(request);

                if (response.getType() == ResponseType.DELETE_CATEGORY) {
                    System.out.println("Category Deleted");
                    LOGGER.info(LoggedInUser.getUser().getId()+" deleted category "+category.getId());
                }
            }
        }, runnable);
        service.start();
// context.Categories.remove(category);
// context.Users.update(LoggedInUser.getUser());
 category=null;
    }
    public void applyChanges(String groupName,byte[] image,Runnable runnable){
        category.setImage(image);
        category.setName(groupName);
        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.MODIFY_CATEGORY_INFO, JSonController.objectToStringMapper(category), LoggedInUser.getUser().getToken());
        Service service = Actions.executeOnce(new Runnable() {
            @Override
            public
            void run() {
                 Response response= ServerHandler.transmitter(request);

                if (response.getType() == ResponseType.MODIFY_CATEGORY_INFO){
                    System.out.println("Category Modified");
                }
            }
        }, runnable);
        service.start();

        LOGGER.info("User "+category.getOwner().getId()+" changed the properties of  "+category.getId());

       // context.Categories.update(category);
    }
}
