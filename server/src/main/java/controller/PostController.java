package controller;

import JSon.JSonController;
import controller.server.ClientHandler;
import controller.server.Transmitter;
import event.post.LikePostEvent;
import event.post.NewPostEvent;
import model.Post;
import model.PostType;
import model.RelationshipType;
import model.User;
import response.Response;
import response.ResponseType;

import java.util.List;

public
class PostController extends Controller {
    public
    PostController(ClientHandler clientHandler) {
        super(clientHandler);
    }

    public
    void newPost(NewPostEvent e) {
        System.out.println(e.getImage().length);
        Post post = new Post(clientHandler.getLoggedInUser(), e.getText(), e.getParrent(), e.getOriginalPost(), e.getImage());
        String data="";
        switch (e.getPostType()) {
            case POST:
                break;
            case COMMENT:

                // e.getParrent().comment(post);
                break;
            case REPOSTED_POST:

                //  e.getOriginalPost().repost(post);
        }
        context.Posts.add(post);
        if (e.getPostType()== PostType.COMMENT)data=JSonController.objectToStringMapper(context.Posts.get(e.getParrent().getId(),clientHandler.getLoggedInUser()));
        Response response = new Response(ResponseType.NEW_POST, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);
    }

    public
    void sendFeedPosts() {
        List <Post> posts = context.Posts.feedPosts(clientHandler.getLoggedInUser());
        System.out.println(posts.size());
        String data;
        data = JSonController.objectToStringMapper(posts);
        Response response = new Response(ResponseType.UPDATED_POSTS, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);
    }


    public
    void sendComments(Integer id) {
        List <Post> posts = context.Posts.commentList(id, clientHandler.getLoggedInUser());
        String data;
        data = JSonController.objectToStringMapper(posts);
        Response response = new Response(ResponseType.UPDATED_POSTS, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);
    }

    public
    void sendLikes(Integer id) {
        List <User> posts = context.Posts.likesList(id, clientHandler.getLoggedInUser());
        String data;
        data = JSonController.objectToStringMapper(posts);
        Response response = new Response(ResponseType.UPDATED_POST_LIKERS, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);
    }
    public
    void sendExplorerPosts() {
        List <Post> posts = context.Posts.popularPosts(clientHandler.getLoggedInUser());
        String data;
        data = JSonController.objectToStringMapper(posts);
        Response response = new Response(ResponseType.UPDATED_POSTS, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);
    }
    public
    void sendUserPosts(Integer id) {
        User user=context.Users.get(id);
        List <Post> posts;
        RelationshipType relationshipType=context.Users.getRelationship(clientHandler.getLoggedInUser().getId(),user.getId());
        if((user.isPrivated()&&(relationshipType== RelationshipType.FOLLOWING||relationshipType==RelationshipType.FOLLOWING_MUTED)||!user.isPrivated())||user.equals(clientHandler.getLoggedInUser())){

            posts = context.Posts.postsList(user,clientHandler.getLoggedInUser());
        }else{posts=null;}
        String data;
        data = JSonController.objectToStringMapper(posts);
        Response response = new Response(ResponseType.UPDATED_POSTS, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);
    }
    public
    void like(LikePostEvent likePostEvent) {
        User user = context.Users.get(likePostEvent.getLiker());
        Post post = context.Posts.like(likePostEvent.getPost(), user);
        post.setLikes(null);
        String data;
        data = JSonController.objectToStringMapper(post);

        Response response = new Response(ResponseType.LIKE_POST, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);
    }

}
