package controller.generalPages;

import JSon.JSonController;
import com.fasterxml.jackson.core.type.TypeReference;
import controller.Actions;
import controller.Controller;
import controller.Loop;
import controller.ServerHandler;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import model.LoggedInUser;
import model.MessagingEnvironment;
import model.Post;
import model.User;
import request.Request;
import request.RequestType;
import response.Response;
import response.ResponseType;
import view.generalPages.MainPageView;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.lang.Thread.sleep;

public
class MainPageController extends Controller {
    User user;
 boolean running=true;

    public
    MainPageController(User user) {
        running=true;
        this.user = user;
       if(LoggedInUser.getUser().getMessagingEnvironments()==null) LoggedInUser.getUser().setMessagingEnvironments(new LinkedList <>());




    }

    public  LinkedList<Post> getFeedPosts(){

     LinkedList <Post> feedPosts=new LinkedList<>();
     feedPosts=context.Posts.feedPosts(LoggedInUser.getUser());
        feedPosts.removeIf(post -> LoggedInUser.getUser().getSilenteds().contains(post.getOwner()));

     return feedPosts;

 }
    public  LinkedList<Post> getAllPosts(){
        LinkedList <Post> posts=new LinkedList<>();
        posts=context.Posts.all();
        return posts;

    }
    public
    List <MessagingEnvironment> getMessagingEnvironments(){

        return LoggedInUser.getUser().getMessagingEnvironments();

    }


}
