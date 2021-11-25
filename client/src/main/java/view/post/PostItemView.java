/**
 * Sample Skeleton for 'postItem.fxml' Controller Class
 */

package view.post;

import com.github.kevinsawicki.timeago.TimeAgo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.events.JFXDialogEvent;
import controller.ImageController;
import controller.post.PostItemController;
import controller.post.PostListMode;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import event.messaging.NewMessageEvent;
import event.post.LikePostEvent;
import event.post.NewPostEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import model.LoggedInUser;
import model.MessagingEnvironment;
import model.Post;
import model.User;
import view.View;
import view.dialog.Dialogs;
import view.generalPages.MainPageView;
import view.messaging.MessagingEnvironmentItemView;
import view.messaging.MessagingEnvironmentsListView;
import view.messaging.SimpleMessagingEnvironmentItemView;
import view.user.UserItemView;
import view.user.UserProfileView;
import view.user.UsersListView;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public
class PostItemView extends View implements Initializable {

    PostItemController postItemController;
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;
    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    @FXML // fx:id="user_info_bar"
    private HBox user_info_bar; // Value injected by FXMLLoader
    @FXML // fx:id="user_real_name_label"
    private Label user_real_name_label; // Value injected by FXMLLoader
    @FXML // fx:id="username_label"
    private Label username_label; // Value injected by FXMLLoader
    @FXML // fx:id="post_text_label"
    private Label post_text_label; // Value injected by FXMLLoader
    @FXML // fx:id="post_image_anchor"
    private AnchorPane post_image_anchor; // Value injected by FXMLLoader
    @FXML // fx:id="like_button"
    private JFXButton like_button; // Value injected by FXMLLoader
    @FXML // fx:id="like_button_icon"
    private FontAwesomeIcon like_button_icon; // Value injected by FXMLLoader
    @FXML // fx:id="comment_button"
    private JFXButton comment_button; // Value injected by FXMLLoader
    @FXML // fx:id="comment_button_icon"
    private FontAwesomeIcon comment_button_icon; // Value injected by FXMLLoader
    @FXML // fx:id="share_button"
    private JFXButton share_button; // Value injected by FXMLLoader
    @FXML // fx:id="share_button_icon"
    private FontAwesomeIcon share_button_icon; // Value injected by FXMLLoader
    @FXML // fx:id="repost_button"
    private JFXButton repost_button; // Value injected by FXMLLoader
    @FXML // fx:id="repost_button_icon"
    private FontAwesomeIcon repost_button_icon; // Value injected by FXMLLoader
    @FXML // fx:id="report_button"
    private JFXButton report_button; // Value injected by FXMLLoader
    @FXML // fx:id="report_button_icon"
    private FontAwesomeIcon report_button_icon; // Value injected by FXMLLoader
    @FXML // fx:id="all_likes_button"
    private JFXButton all_likes_button; // Value injected by FXMLLoader
    @FXML // fx:id="all_comment_button"
    private JFXButton all_comment_button; // Value injected by FXMLLoader
    @FXML // fx:id="post_creation_date_label"
    private Label post_creation_date_label; // Value injected by FXMLLoader
    private StackPane parrentStackPane;
    @FXML
    private JFXButton reposted_button;

    @FXML
    private Circle avatar_circle;

    @FXML

    private JFXButton edit_button;

    @FXML
    private VBox user_detail_vbox;
    @FXML
    private ImageView post_image;

    @FXML
    void OnAvatarCircleClicked(MouseEvent event) {

    }

    @FXML
    void OnAllCommentsButtonAction(ActionEvent event) {
        MainPageView.newTab(getCommentsListContent(), "Comments");

    }

    @FXML
    void OnAllLikesButtonAction(ActionEvent event) {
        postItemController.updateLikers(new Runnable() {
            @Override
            public
            void run() {
                MainPageView.newTab(getUsersListContent(getUsersListItems(postItemController.getPost().getLikes())), "Likes");

            }
        });

    }

    @FXML
    void OnCommentButtonAction(ActionEvent event) {

        Dialogs.newPostDialog(MainPageView.universalCenterStackPane, postItemController, new Runnable() {
            @Override
            public
            void run() {
                refresh();
            }
        });


    }

    @FXML
    void OnLikeButtonAction(ActionEvent event) {

        LikePostEvent likePostEvent;
        likePostEvent = new LikePostEvent(LoggedInUser.getUser().getId(), postItemController.getPost().getId());
        if (postItemController.getPost().notOriginalPost()) {
            likePostEvent = new LikePostEvent(LoggedInUser.getUser().getId(), postItemController.getPost().getOriginalPost().getId());

        }
        postItemController.like(likePostEvent, new Runnable() {
            @Override
            public
            void run() {
                refresh();
            }
        });


    }

    @FXML
    void OnReportButtonAction(ActionEvent event) {
        Dialogs.confirmDialog(MainPageView.universalCenterStackPane, "Report", "Are you sure to report this post?", new EventHandler() {
            @Override
            public
            void handle(Event event) {
                postItemController.report();
            }
        });


    }

    @FXML
    void OnRepostButtonAction(ActionEvent event) {
        Post post;
        String ownerName;
        if (postItemController.getPost().notOriginalPost()) {
            post = postItemController.getPost().getOriginalPost();

        } else {
            post = postItemController.getPost();

        }
        ownerName = post.getOwner().getFullName();
        Dialogs.confirmDialog(MainPageView.universalCenterStackPane,
                "Repost from" + ownerName,
                "Are you sure to repost this post?",
                new EventHandler() {
                    @Override
                    public
                    void handle(Event event) {
                        NewPostEvent newPostEvent = new NewPostEvent(post);
                        postItemController.NewPost(newPostEvent, null);

                    }
                });
    }

    @FXML
    void OnShareButtonAction(ActionEvent event) {

        JFXDialog dialog = Dialogs.SimpleDialog(MainPageView.universalCenterStackPane, "Share", getSimpleMessagingEnvironmentsListContent(postItemController.getMessagingEnvironments()));
        dialog.setOnDialogOpened(new EventHandler <JFXDialogEvent>() {
            @Override
            public
            void handle(JFXDialogEvent event) {
                postItemController.getForwardingList().clear();
            }
        });
        dialog.setOnDialogClosed(new EventHandler <JFXDialogEvent>() {
            @Override
            public
            void handle(JFXDialogEvent event) {
                for (MessagingEnvironment messagingEnvironment : postItemController.getForwardingList()) {
                    NewMessageEvent newMessageEvent;

                    newMessageEvent = new NewMessageEvent(LoggedInUser.getUser().getId(), messagingEnvironment.getId(), "", null, null, postItemController.getPost().getId());

                    postItemController.newMessage(newMessageEvent);

                }
                refresh();

            }
        });
    }

    @FXML
    void OnUserInfoBarClicked(MouseEvent event) {
if(!LoggedInUser.getOffline()) {
    FXMLLoader userProfileLoader = new FXMLLoader(getClass().getResource("/fxmls/user/UserProfile.fxml"));
    try {
        ScrollPane userProfile = userProfileLoader.load();
        UserProfileView userProfileView = userProfileLoader.getController();
        userProfileView.initializeValues(postItemController.getPost().getOwner());
        MainPageView.newTab(userProfile, postItemController.getPost().getOwner().getFullName());

    } catch (IOException e) {
        e.printStackTrace();
    }

}
    }

    @FXML
    void OnEditButtonAction(ActionEvent event) {

    }


    @FXML
    void OnRepostedButtonAction(ActionEvent event) {
        FXMLLoader userProfileLoader = new FXMLLoader(getClass().getResource("/fxmls/user/UserProfile.fxml"));
        try {
            ScrollPane userProfile = userProfileLoader.load();
            UserProfileView userProfileView = userProfileLoader.getController();
            userProfileView.initializeValues(postItemController.getPost().getOriginalPost().getOwner());
            MainPageView.newTab(userProfile, postItemController.getPost().getOriginalPost().getOwner().getFullName());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public
    void initialize(URL location, ResourceBundle resources) {


    }
    public  void updateOfflineMode(){
        if(LoggedInUser.getOffline()){
like_button.setDisable(true);
comment_button.setDisable(true);
share_button.setDisable(true);
report_button.setDisable(true);
all_comment_button.setDisable(true);
all_likes_button.setDisable(true);
report_button.setDisable(true);
reposted_button.setDisable(true);




        }else{
            like_button.setDisable(false);
            comment_button.setDisable(false);
            share_button.setDisable(false);
            report_button.setDisable(false);
            all_comment_button.setDisable(false);
            all_likes_button.setDisable(false);
            report_button.setDisable(false);
            reposted_button.setDisable(false);


        }
    }
    public
    void initializeValues(Post post) {

        postItemController = new PostItemController(post);
        user_real_name_label.setText(postItemController.getPost().getOwner().getFullName());
        username_label.setText(postItemController.getPost().getOwner().getUserName());
        post_text_label.setText(postItemController.getPost().getText());
        TimeAgo time = new TimeAgo();

        ZonedDateTime zonedDateTime=ZonedDateTime.of(postItemController.getPost().getCreation_date(), ZoneId.systemDefault());

        long messagemilles=zonedDateTime.toInstant().toEpochMilli();
        post_creation_date_label.setText(time.timeAgo(messagemilles));
        if (postItemController.getPost().getImage() != null) {
            post_image.setImage(ImageController.getImage(postItemController.getPost().getImage()));
        } else {
            if (postItemController.getPost().notOriginalPost()) {
                if (postItemController.getPost().getOriginalPost().getImage() != null)
                    post_image.setImage(ImageController.getImage(postItemController.getPost().getOriginalPost().getImage()));
                else {
                    post_image_anchor.getChildren().remove(post_image);
                }
            } else {
                post_image_anchor.getChildren().remove(post_image);
            }

        }
        if (postItemController.getPost().getOwner().getImage() != null) {
            avatar_circle.setFill(new ImagePattern(ImageController.getImage(postItemController.getPost().getOwner().getImage())));
        } else {
            avatar_circle.setFill(new ImagePattern(new Image(getClass().getResource("/user_avatar.jpg").toExternalForm())));

        }
        if (postItemController.getPost().notOriginalPost()) {
            post_text_label.setText(postItemController.getPost().getOriginalPost().getText());

            reposted_button.setText("Reposted from " + postItemController.getPost().getOriginalPost().getOwner().getFullName());
        } else {
            user_detail_vbox.getChildren().remove(reposted_button);

        }
        if (LoggedInUser.getUser().equals(postItemController.getPost().getOwner()) && !postItemController.getPost().notOriginalPost()) {
            edit_button.setVisible(true);
        }
        refresh();
updateOfflineMode();

    }

    public
    void updateLikedState() {

        if (postItemController.getPost().isLikedByMe()) {
            like_button_icon.setFill(Paint.valueOf("black"));
        } else {
            like_button_icon.setFill(Paint.valueOf("transparent"));
        }
        all_likes_button.setText(postItemController.getPost().getNumberOfLikes() + " Likes");

    }

    public
    void updateCommentsState() {

        int commentsSize = postItemController.getPost().getNumberOfComments();
        all_comment_button.setText(commentsSize + " comments");

    }

    public
    void refresh() {


        updateCommentsState();
        updateLikedState();
    }

    public
    Node getCommentsListContent() {

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmls/post/PostsList.fxml"));
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PostsListView postsListView = loader.getController();
        postsListView.initializeValues(PostListMode.COMMENTS, postItemController.getPost().getId());
        MainPageView.universalViews.add(postsListView);
        return node;
    }

    public
    Node getUsersListContent(LinkedList <Node> list) {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmls/user/UsersList.fxml"));
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UsersListView usersListView = loader.getController();
        usersListView.initializeValues(list);
        return node;
    }


    LinkedList <Node> getUsersListItems(List <User> list) {


        LinkedList <Node> UsersItems = new LinkedList <>();
        for (User user : list) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/user/UserItem.fxml"));
                Node node = fxmlLoader.load();
                UserItemView userItemView = fxmlLoader.getController();
                userItemView.initializeValues(user);
                UsersItems.add(node);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return UsersItems;
    }


    Node getSimpleMessagingEnvironmentsListContent(List <MessagingEnvironment> messages) {

        LinkedList <SimpleMessagingEnvironmentItemView> messageItemViews = new LinkedList <>();
        LinkedList <Node> messageItems = new LinkedList <>();
        for (MessagingEnvironment message : messages) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/messaging/SimpleMessagingEnvironmentItem.fxml"));
                Node node = fxmlLoader.load();

                SimpleMessagingEnvironmentItemView simpleMessagingEnvironmentItemView = fxmlLoader.getController();
                simpleMessagingEnvironmentItemView.initializeValues(message);
                simpleMessagingEnvironmentItemView.getToggle_button().setOnAction(new EventHandler <ActionEvent>() {
                    @Override
                    public
                    void handle(ActionEvent event) {
                        if (!simpleMessagingEnvironmentItemView.getToggle_button().isSelected()) {
                            postItemController.getForwardingList().remove(message);
                        } else {
                            postItemController.getForwardingList().add(message);
                        }
                    }
                });
                messageItems.add(node);
                messageItemViews.add(simpleMessagingEnvironmentItemView);
                MainPageView.universalViews.add(simpleMessagingEnvironmentItemView);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmls/user/UsersList.fxml"));
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UsersListView usersListView = loader.getController();
        usersListView.initializeValues(messageItems);
        MainPageView.universalViews.add(usersListView);
        return node;
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

}
