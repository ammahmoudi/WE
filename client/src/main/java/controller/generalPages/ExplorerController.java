package controller.generalPages;

import JSon.JSonController;
import com.fasterxml.jackson.core.type.TypeReference;
import controller.Controller;
import controller.ServerHandler;
import model.LoggedInUser;
import model.Message;
import model.Post;
import model.User;
import request.Request;
import request.RequestType;
import response.Response;
import response.ResponseType;

import java.util.LinkedList;
import java.util.List;

public
class ExplorerController extends Controller {
    public
    LinkedList<User> search(String text){
        LinkedList<User> users = null;

         Response response= ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.SEARCH_USER, text, LoggedInUser.getUser().getToken()));


        if (response.getType() == ResponseType.SEARCH_USER) {


users=JSonController.stringToObjectMapper(response.getData(), new TypeReference <LinkedList <User>>() {});

        }
        return users;
    }
    public LinkedList<Post> getExplorerPosts(){
        LinkedList<Post> posts=context.Posts.popularPosts();
        posts.removeIf(post -> LoggedInUser.getUser().getSilenteds().contains(post.getOwner()));
        return posts;
    }
}
