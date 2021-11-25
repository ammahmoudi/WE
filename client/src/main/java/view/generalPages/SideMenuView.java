package view.generalPages;

import com.jfoenix.controls.JFXButton;
import controller.ImageController;
import controller.generalPages.MainPageController;
import controller.generalPages.SideMenuController;
import controller.post.PostItemController;
import controller.post.PostListMode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import model.LoggedInUser;
import model.MessagingEnvironment;
import view.View;
import view.category.CategoriesPageView;
import view.dialog.Dialogs;
import view.messaging.MessagingEnvironmentItemView;
import view.messaging.MessagingEnvironmentsListView;
import view.messaging.MessagingPageView;
import view.post.PostsListView;
import view.user.UserProfileView;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public
class SideMenuView extends View {

    SideMenuController sideMenuController;
    MainPageController mainPageController = new MainPageController(LoggedInUser.getUser());
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Circle avatar_circle;
    @FXML
    private Label user_real_name_label;
    @FXML
    private Label username_label;
    @FXML
    private JFXButton messaging_button;
    @FXML
    private JFXButton posts_button;
    @FXML
    private JFXButton saved_messages_button;
    @FXML
    private JFXButton setting_button;
    @FXML
    private JFXButton new_group_button;
    @FXML
    private JFXButton sign_out_button;
    @FXML
    private JFXButton categories_button;
    @FXML
    private JFXButton feed_button;
    @FXML
    private JFXButton explorer_button;
    @FXML
    private JFXButton new_post_button;
    @FXML
    private JFXButton activity_button;
    @FXML
    private JFXButton my_profile_button;

    @FXML
    void OnCategoriesButtonAction(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/category/CategoriesPage.fxml"));
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        CategoriesPageView categoriesPageView = loader.getController();
        categoriesPageView.initializeValues();
        MainPageView.universalSideDrawer.close();
        MainPageView.newTab(node, "Categories");


    }

    @FXML
    void OnAvatarCircleClicked(MouseEvent event) {

    }

    @FXML
    void OnMessagingButtonAction(ActionEvent event) {
        MainPageView.setLeftStackPaneContent(getMessagingEnvironmentsListContent(sideMenuController.getMessagingEnvironments()));
        MainPageView.universalSideDrawer.close();

    }

    @FXML
    void OnPostsButtonAction(ActionEvent event) {

        FXMLLoader socialMenuLoader = new FXMLLoader(getClass().getResource("/fxmls/generalPages/Social_menu.fxml"));
        VBox socialMenuVBox = null;
        try {
            socialMenuVBox = socialMenuLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SocialMenuView socialMenuView = socialMenuLoader.getController();
        //  MainPageView.setLeftStackPaneContent(socialMenuVBox);

        MainPageView.universalSideDrawer.close();

    }

    @FXML
    void OnSavedMessagesButtonAction(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/messaging/MessagingPage.fxml"));
        try {
            Node privateChat = loader.load();
            MessagingPageView messagingPageView = loader.getController();


            sideMenuController.getSavedMessageMessagingEnvironment(new Runnable() {
                @Override
                public
                void run() {

                    MainPageView.newTab(privateChat, "Saved Messages");
                    messagingPageView.initializeValues(sideMenuController.getSavedMessages());
                    MainPageView.universalSideDrawer.close();
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void OnSettingButtonAction(ActionEvent event) {
        Dialogs.SettingsDialog(MainPageView.universalCenterStackPane);
        MainPageView.universalSideDrawer.close();
    }

    @FXML
    void OnNewGroupButtonAction(ActionEvent event) {
        LoggedInUser.updateFollowings(new Runnable() {
            @Override
            public
            void run() {
                Dialogs.NewGroupDialog(MainPageView.universalCenterStackPane, LoggedInUser.getUser().getFollowings());
                MainPageView.universalSideDrawer.close();

            }
        });


    }

    @FXML
    void OnSignOutButtonAction(ActionEvent event) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/generalPages/SignIn.fxml"));
        try {
            Parent root = loader.load();
            View.setScene(new_post_button.getScene());
            View.getScene().setRoot(root);


        } catch (IOException e) {
            e.printStackTrace();
        }
        sideMenuController.singOut();
    }

    public
    Node getMessagingEnvironmentsListContent(List <MessagingEnvironment> messages) {

        LinkedList <MessagingEnvironmentItemView> messageItemViews = new LinkedList <>();
        LinkedList <Node> messageItems = new LinkedList <>();
        for (MessagingEnvironment message : messages) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/messaging/MessagingEnvironmentItem.fxml"));
                Node node = fxmlLoader.load();

                MessagingEnvironmentItemView messagingEnvironmentItemView = fxmlLoader.getController();
                messagingEnvironmentItemView.initializeValues(message);
                messageItems.add(node);
                messageItemViews.add(messagingEnvironmentItemView);
                MainPageView.universalViews.add(messagingEnvironmentItemView);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmls/messaging/MessagingEnvironmentsList.fxml"));
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MessagingEnvironmentsListView messagesListView = loader.getController();
        messagesListView.initializeValues(messageItems, messageItemViews);
        MainPageView.universalViews.add(messagesListView);
        return node;
    }

    public
    LinkedList <Node> getMessagingEnvironmentsItems(List <MessagingEnvironment> messages) {
        LinkedList <MessagingEnvironmentItemView> messageItemViews = new LinkedList <>();
        LinkedList <Node> messageItems = new LinkedList <>();
        for (MessagingEnvironment message : messages) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/messaging/MessagingEnvironmentItem.fxml"));
                Node node = fxmlLoader.load();

                MessagingEnvironmentItemView messagingEnvironmentItemView = fxmlLoader.getController();
                messagingEnvironmentItemView.initializeValues(message);
                messageItems.add(node);
                messageItemViews.add(messagingEnvironmentItemView);
                MainPageView.universalViews.add(messagingEnvironmentItemView);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return messageItems;
    }

    @FXML
    public
    void initialize() {
        if (LoggedInUser.getUser().getImage() != null) {
            avatar_circle.setFill(new ImagePattern(ImageController.getImage(LoggedInUser.getUser().getImage())));
        } else {
            avatar_circle.setFill(new ImagePattern(new Image(getClass().getResource("/user_avatar.jpg").toExternalForm())));

        }
        sideMenuController = new SideMenuController();
        user_real_name_label.setText(LoggedInUser.getUser().getFullName());
        username_label.setText(LoggedInUser.getUser().getUserName());
        updateOfflineMode();
    }
public  void updateOfflineMode(){
        if(LoggedInUser.getOffline()){

            new_post_button.setDisable(true);
            new_group_button.setDisable(true);
            activity_button.setDisable(true);
            categories_button.setDisable(true);
            explorer_button.setDisable(true);
            feed_button.setDisable(true);
            my_profile_button.setDisable(true);
            saved_messages_button.setDisable(true);



        }else{
            new_post_button.setDisable(false);
            new_group_button.setDisable(false);
            activity_button.setDisable(false);
            categories_button.setDisable(false);
            explorer_button.setDisable(false);
            feed_button.setDisable(false);
            my_profile_button.setDisable(false);
            saved_messages_button.setDisable(false);

        }
}
    @FXML
    void OnActivityButtonAction(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/generalPages/Activity.fxml"));
        try {
            Node content = loader.load();
            ActivityView activityView = loader.getController();
            // userProfileView.initializeValues(LoggedInUser.getUser());
            MainPageView.newTab(content, "Activity");
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainPageView.universalSideDrawer.close();
    }

    @FXML
    void OnExplorerButtonAction(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/generalPages/Explorer.fxml"));
        try {
            Node content = loader.load();
            ExplorerView explorerView = loader.getController();
            MainPageView.newTab(content, "Explorer");
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainPageView.universalSideDrawer.close();
    }

    @FXML
    void OnFeedButtonAction(ActionEvent event) {
        MainPageView.newTab(getFeedPostListContent(), "My feed");
        MainPageView.universalSideDrawer.close();
    }

    @FXML
    void OnMyProfileButtonAction(ActionEvent event) {
        FXMLLoader userProfileLoader = new FXMLLoader(getClass().getResource("/fxmls/user/UserProfile.fxml"));
        try {
            Node userProfile = userProfileLoader.load();
            UserProfileView userProfileView = userProfileLoader.getController();
            userProfileView.initializeValues(LoggedInUser.getUser());
            MainPageView.newTab(userProfile, LoggedInUser.getUser().getFullName());

        } catch (IOException e) {
            e.printStackTrace();
        }
        MainPageView.universalSideDrawer.close();
    }

    @FXML
    void OnNewPostButtonAction(ActionEvent event) {
        Dialogs.newPostDialog(MainPageView.universalCenterStackPane, new PostItemController(null), null);
        MainPageView.universalSideDrawer.close();

    }


    public
    Node getFeedPostListContent() {

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmls/post/PostsList.fxml"));
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PostsListView postsListView = loader.getController();
        postsListView.initializeValues(PostListMode.FEED, LoggedInUser.getUser().getId());
        MainPageView.universalViews.add(postsListView);
        return node;
    }

    public
    JFXButton getNew_post_button() {
        return new_post_button;
    }

}
