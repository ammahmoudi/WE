package controller.post;

import JSon.JSonController;
import com.fasterxml.jackson.core.type.TypeReference;
import controller.Actions;
import controller.Controller;
import controller.ServerHandler;
import javafx.concurrent.Service;
import model.LoggedInUser;
import model.Post;
import request.Request;
import request.RequestType;
import response.Response;
import response.ResponseType;

import java.util.LinkedList;
import java.util.List;

public
class PostListController extends Controller {
    PostListMode mode;
    Integer id;
    List <Post> posts = new LinkedList <>();

    public
    PostListController(PostListMode mode, Integer id) {
        this.mode = mode;
        this.id = id;
    }

    public
    PostListMode getMode() {
        return mode;
    }

    public
    PostListController setMode(PostListMode mode) {
        this.mode = mode;
        return this;
    }

    public
    Integer getId() {
        return id;
    }

    public
    PostListController setId(Integer id) {
        this.id = id;
        return this;
    }

    public
    List <Post> getPosts() {
        return posts;
    }

    public
    PostListController setPosts(List <Post> posts) {
        this.posts = posts;
        return this;
    }

    public
    Service updateList(Runnable runnable) {
        final List <Post>[] updatedPosts = new List[]{new LinkedList <>()};
        Request request = null;
        switch (mode) {
            case EXPLORER:
                request = new Request(LoggedInUser.getUser().getId(), RequestType.UPDATE_EXPLORER, null, LoggedInUser.getUser().getToken());
                break;
            case FEED:
                request = new Request(LoggedInUser.getUser().getId(), RequestType.UPDATE_FEED, null, LoggedInUser.getUser().getToken());
                break;
            case COMMENTS:
                request = new Request(LoggedInUser.getUser().getId(), RequestType.UPDATE_COMMENTS, id.toString(), LoggedInUser.getUser().getToken());
                break;
            case USER_POST:
                request = new Request(LoggedInUser.getUser().getId(), RequestType.UPDATE_USER_POSTS, id.toString(), LoggedInUser.getUser().getToken());
                break;
        }

        Request finalRequest = request;
        Runnable task = new Runnable() {
            @Override
            public
            void run() {
                 Response response= ServerHandler.transmitter(finalRequest);



                if (response.getType() == ResponseType.UPDATED_POSTS) {
                    updatedPosts[0] = JSonController.stringToObjectMapper(response.getData(), new TypeReference <List <Post>>() {
                    });
                    setPosts(updatedPosts[0]);
                    if(updatedPosts[0]==null)setPosts(new LinkedList <Post>());

                }
            }
        };
        Service service = Actions.executeOnce(task, runnable);
        service.start();
        return service;
    }
}
