package controller.category;

import controller.Controller;
import model.Category;
import model.LoggedInUser;
import model.MessagingEnvironment;

import java.util.List;

public
class CategoryItemController extends Controller {
    Category category;

    public
    CategoryItemController(Category category) {
        this.category = category;
    }

    public
    Category getCategory() {
        return category;
    }

    public
    CategoryItemController setCategory(Category category) {
        this.category = category;
        return this;
    }
    public
    List <MessagingEnvironment> getMessagingEnvironments(){
        return LoggedInUser.getUser().getMessagingEnvironments();

    }
}
