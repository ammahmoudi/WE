package view.generalPages;

import com.jfoenix.controls.JFXButton;
import controller.generalPages.MainPageController;
import controller.post.PostItemController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import model.LoggedInUser;
import model.Post;
import view.dialog.Dialogs;
import view.View;
import view.post.PostItemView;
import view.post.PostsListView;
import view.user.UserProfileView;

import java.io.IOException;
import java.util.LinkedList;

public class SocialMenuView extends View {
     @FXML
    private JFXButton Feed_button;

    @FXML
    private JFXButton explorer_button;

    @FXML
    private JFXButton new_post_button;

    @FXML
    private JFXButton activity_button;

    @FXML
    private JFXButton my_profile_button;
    MainPageController mainPageController =new MainPageController(LoggedInUser.getUser());

    @FXML
    void OnActivityButtonAction(ActionEvent event) {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/fxmls/generalPages/Activity.fxml"));
        try {
            Node content= loader.load();
            ActivityView activityView=loader.getController();
           // userProfileView.initializeValues(LoggedInUser.getUser());
            MainPageView.newTab(content,"Activity" );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void OnExplorerButtonAction(ActionEvent event) {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/fxmls/generalPages/Explorer.fxml"));
        try {
            Node content= loader.load();
             ExplorerView explorerView=loader.getController();
            MainPageView.newTab(content,"Explorer" );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnFeedButtonAction(ActionEvent event) {
        MainPageView.newTab(getPostListContent(mainPageController.getFeedPosts()),"my timeline");
    }

    @FXML
    void OnMyProfileButtonAction(ActionEvent event) {
        FXMLLoader userProfileLoader= new FXMLLoader(getClass().getResource("/fxmls/user/UserProfile.fxml"));
        try {
            Node userProfile= userProfileLoader.load();
            UserProfileView userProfileView=userProfileLoader.getController();
            userProfileView.initializeValues(LoggedInUser.getUser());
            MainPageView.newTab(userProfile,LoggedInUser.getUser().getFullName());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnNewPostButtonAction(ActionEvent event) {
        Dialogs.newPostDialog(MainPageView.universalCenterStackPane,new PostItemController(null),null);

    }







    public Node getPostListContent(LinkedList<Post> feedPosts){
        LinkedList<PostItemView> postItemsViews=new LinkedList <>();
        LinkedList<Node> postItems=new LinkedList <>();
        for(Post post :feedPosts){
            try {
                FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/fxmls/post/postItem.fxml"));
                Node node=fxmlLoader.load();
                PostItemView postItemView =fxmlLoader.getController();
                postItemView.initializeValues(post);

                postItems.add(node);
                postItemsViews.add(postItemView);
                MainPageView.universalViews.add(postItemView);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FXMLLoader loader=new FXMLLoader(this.getClass().getResource("/fxmls/post/PostsList.fxml"));
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PostsListView postsListView=loader.getController();
       // postsListView.initializeValues(postItems,postItemsViews);
        MainPageView.universalViews.add(postsListView);
        return  node;
    }
    public
    JFXButton getNew_post_button() {
        return new_post_button;
    }


}
