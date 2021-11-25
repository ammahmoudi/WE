package controller.post; /**
 * Sample Skeleton for 'postItem.fxml' view Class
 */

import JSon.JSonController;
import com.fasterxml.jackson.core.type.TypeReference;
import controller.Actions;
import controller.Controller;
import controller.ServerHandler;
import event.messaging.NewMessageEvent;
import event.post.LikePostEvent;
import event.post.NewPostEvent;
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

public class PostItemController extends Controller {
    static Logger LOGGER = LogManager.getLogger(PostItemController.class.getName());

    Post post=null;

    public
    PostItemController(Post post) {
        this.post = post;
    }

    public void like(LikePostEvent e ,Runnable runnable){
        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.LIKE_POST, JSonController.objectToStringMapper(e), LoggedInUser.getUser().getToken());
        Service service = Actions.executeOnce(new Runnable() {
            @Override
            public
            void run() {
                 Response response= ServerHandler.transmitter(request);

                if (response.getType() == ResponseType.LIKE_POST) {
                    setPost(JSonController.stringToObjectMapper(response.getData(),Post.class));
                    System.out.println("liked");
                }
            }
        }, runnable);
        service.start();





//        e.getPost().like(e.getLiker());
//        LOGGER.info("User "+LoggedInUser.getUser().getId()+"changed like status of post "+post.getId());
//
//
//        context.Posts.update(e.getPost());
//        return e.getPost().likedChecker(e.getLiker());
    }

    public void NewPost(NewPostEvent e,Runnable runnable){
        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.NEW_POST, JSonController.objectToStringMapper(e), LoggedInUser.getUser().getToken());
        final Post[] parentPost = new Post[1];
        Service service = Actions.executeOnce(new Runnable() {
            @Override
            public
            void run() {
                 Response response= ServerHandler.transmitter(request);

                if (response.getType() == ResponseType.NEW_POST) {
                    if (e.getPostType()== PostType.COMMENT) {parentPost[0] =JSonController.stringToObjectMapper(response.getData(),Post.class);
                    setPost(parentPost[0]);
                    }
                    System.out.println("posted");
                }
            }
        }, runnable);
        service.start();

    }

    public
    Post getPost() {

        return post;
    }

    public
    PostItemController setPost(Post post) {
        this.post = post;
        return this;
    }
    public void report(){
        post.report();
        LOGGER.info("User "+LoggedInUser.getUser().getId()+"reported post "+post.getId());

        context.Posts.update(post);
    }
    public
    void newMessage(NewMessageEvent newMessageEvent) {

        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.NEW_MESSAGE, JSonController.objectToStringMapper(newMessageEvent), LoggedInUser.getUser().getToken());
        Service service = Actions.executeOnce(new Runnable() {
            @Override
            public
            void run() {
                 Response response= ServerHandler.transmitter(request);

                if (response.getType() == ResponseType.ACCEPT_NEW_MESSAGE) {
                    System.out.println("new Messages sent");
                }
            }
        }, null);
        service.start();

    }
    LinkedList <MessagingEnvironment> forwardingList=new LinkedList <>();

    public
    LinkedList <MessagingEnvironment> getForwardingList() {
        return forwardingList;
    }
    public
    List <MessagingEnvironment> getMessagingEnvironments(){
        return LoggedInUser.getUser().getMessagingEnvironments();
    }

    public
    Service updateLikers(Runnable runnable) {
        final List <User>[] updatedUsers = new List[]{new LinkedList <>()};

        Runnable task = new Runnable() {
            @Override
            public
            void run() {


                Response response= ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.UPDATE_POST_LIKERS, post.getId().toString(), LoggedInUser.getUser().getToken()));

                if (response.getType() == ResponseType.UPDATED_POST_LIKERS) {
                    updatedUsers[0] = JSonController.stringToObjectMapper(response.getData(), new TypeReference <List <User>>() {
                    });
                }
             getPost().setLikes(updatedUsers[0]);


            }
        };
        Service service = Actions.executeOnce(task, runnable);
        service.start();
        return service;
    }

}
