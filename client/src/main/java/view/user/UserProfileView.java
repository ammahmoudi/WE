/**
 * Sample Skeleton for 'UserProfile.fxml' Controller Class
 */

package view.user;

import com.github.kevinsawicki.timeago.TimeAgo;
import com.jfoenix.controls.JFXBadge;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.events.JFXDialogEvent;
import controller.ImageController;
import controller.post.PostListMode;
import controller.user.UserProfileController;
import event.user.RelationshipEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import model.LoggedInUser;
import model.RelationshipType;
import model.User;
import view.View;
import view.dialog.Dialogs;
import view.generalPages.MainPageView;
import view.messaging.MessagingPageView;
import view.post.PostsListView;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public
class UserProfileView extends View implements Initializable {

    UserProfileController userProfileController;
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;
    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    @FXML // fx:id="tab_pane"
    private JFXTabPane tab_pane; // Value injected by FXMLLoader
    @FXML
    private JFXBadge silented_badge;
    @FXML // fx:id="posts_tab"
    private Tab posts_tab; // Value injected by FXMLLoader
    @FXML // fx:id="followings_tab"
    private Tab followings_tab; // Value injected by FXMLLoader
    @FXML // fx:id="followers_tab"
    private Tab followers_tab; // Value injected by FXMLLoader
    @FXML // fx:id="information_tab"
    private Tab information_tab; // Value injected by FXMLLoader
    @FXML // fx:id="avatar_circle"
    private Circle avatar_circle; // Value injected by FXMLLoader
    @FXML // fx:id="user_real_name_label"
    private Label user_real_name_label; // Value injected by FXMLLoader
    @FXML // fx:id="more_options_button"
    private JFXButton more_options_button; // Value injected by FXMLLoader
    @FXML // fx:id="username_label"
    private Label username_label; // Value injected by FXMLLoader
    @FXML // fx:id="last_seen_label"
    private Label last_seen_label; // Value injected by FXMLLoader
    @FXML // fx:id="bio_label"
    private Label bio_label; // Value injected by FXMLLoader
    @FXML // fx:id="follow_button"
    private JFXButton follow_button; // Value injected by FXMLLoader
    @FXML // fx:id="message_Button"
    private JFXButton message_Button; // Value injected by FXMLLoader
    @FXML
    private VBox buttons_vbox;

    @FXML
    void OnAvatarCircleClicked(MouseEvent event) {

    }

    @FXML
    void OnFollowButtonAction(ActionEvent event) {
        RelationshipEvent relationshipEvent = new RelationshipEvent(LoggedInUser.getUser().getId(), userProfileController.getUser().getId());
        userProfileController.applyRelationship(relationshipEvent, new Runnable() {
            @Override
            public
            void run() {
                updateUser();
                updateButtons();
            }
        });

    }

    @FXML
    void OnMessageButtonAction(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/messaging/MessagingPage.fxml"));
        try {
            Node privateChat = loader.load();
            MessagingPageView messagingPageView = loader.getController();


            userProfileController.getPrivateChat(new Runnable() {
                @Override
                public
                void run() {

                    MainPageView.newTab(privateChat, userProfileController.getUser().getFullName() + " chat");
                    messagingPageView.initializeValues(userProfileController.getMessagingEnvironment());
                }
            });



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnMoreOptionsButtonAction(ActionEvent event) {

        Dialogs.userProfileOptions(MainPageView.universalCenterStackPane, userProfileController).setOnDialogClosed(new EventHandler <JFXDialogEvent>() {
            @Override
            public
            void handle(JFXDialogEvent event) {
                userProfileController.updateUser(new Runnable() {
                    @Override
                    public
                    void run() {
                        updateUser();
                        userProfileController.updateRelationship(new Runnable() {
                            @Override
                            public
                            void run() {
                                updateButtons();
                            }
                        });
                    }
                });
            }
        });


    }

    @Override
    public
    void initialize(URL location, ResourceBundle resources) {

    }

    public
    void initializeValues(User user) {


        userProfileController = new UserProfileController(user);
        userProfileController.updateUser(new Runnable() {
            @Override
            public
            void run() {

                userProfileController.updateRelationship(new Runnable() {
                    @Override
                    public
                    void run() {
                        updateUser();
                     //   System.out.println("!");
                        updateButtons();
                        updatePosts();

                        // System.out.println("!!");
                        userProfileController.updateFollowers(new Runnable() {
                            @Override
                            public
                            void run() {
                              //  System.out.println("xx");
                                updateFollowers();
                                userProfileController.updateFollowings(new Runnable() {
                                    @Override
                                    public
                                    void run() {
                                        updateFollowings();
                                    }
                                });
                            }
                        });
                    }
                });

            }
        });




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

    LinkedList <Node> getUsersListItems(String mode) {

        List <User> list;
        if (mode.equals("followers")) {
            list = userProfileController.getUser().getFollowers();

        } else {
            list = userProfileController.getUser().getFollowings();
        }
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

    public
    Node getUserPostListContent() {

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmls/post/PostsList.fxml"));
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PostsListView postsListView = loader.getController();
        postsListView.initializeValues(PostListMode.USER_POST, userProfileController.getUser().getId());
        MainPageView.universalViews.add(postsListView);
        return node;
    }

    public
    void updateButtons() {
        if (userProfileController.getUser().equals(LoggedInUser.getUser())) {
            buttons_vbox.getChildren().remove(follow_button);
            user_real_name_label.setGraphic(null);
            silented_badge.setVisible(false);
        } else {

            if (userProfileController.getLoggedInUserRelationship() == RelationshipType.FOLLOWING || userProfileController.getLoggedInUserRelationship() == RelationshipType.FOLLOWING_MUTED) {
                follow_button.setText("Following");
                follow_button.getStyleClass().removeAll("no_border","border","green","dark","no_color","default","default_border","black_border");
             //   follow_button.getStyleClass().clear();
                follow_button.getStyleClass().add("rounded_button");
                follow_button.getStyleClass().add("border");
                follow_button.getStyleClass().add("no_color");


//                follow_button.setTextFill(Paint.valueOf("#000000"));
//                follow_button.setStyle("-fx-background-color: white;-fx-border-color: black");


            } else if (userProfileController.getLoggedInUserRelationship() == RelationshipType.REQUESTED) {
                follow_button.setText("Requested");
         //       follow_button.getStyleClass().clear();
                follow_button.getStyleClass().removeAll("no_border","border","green","dark","no_color","default","default_border","black_border");
                follow_button.getStyleClass().addAll("rounded_button","default_border","no_color");
             //   follow_button.getStyleClass().add("default_border");
             //   follow_button.getStyleClass().add("no_color");
//                follow_button.setTextFill(Paint.valueOf("#c17f00"));
//                follow_button.setStyle("-fx-background-color: #ffffff;-fx-border-color: #c17f00");


            } else if (userProfileController.getLoggedInUserRelationship() == RelationshipType.BLOCKED) {
                follow_button.setText("UnBlock");
                follow_button.getStyleClass().removeAll("no_border","border","green","dark","no_color","default","default_border","black_border");
              //  follow_button.getStyleClass().clear();
                follow_button.getStyleClass().add("rounded_button");
                follow_button.getStyleClass().add("black_border");
                follow_button.getStyleClass().add("no_color");
//                follow_button.setTextFill(Paint.valueOf("#a20000"));
//                follow_button.setStyle("-fx-background-color: #ffffff;-fx-border-color: #a20000");


            } else if (userProfileController.getUserRelationship() == RelationshipType.BLOCKED) {
                follow_button.setText("You Are Blocked");
             //   follow_button.getStyleClass().clear();
                follow_button.getStyleClass().removeAll("no_border","border","green","dark","no_color","default","default_border","black_border");
                follow_button.getStyleClass().add("rounded_button");
                follow_button.getStyleClass().add("no_border");
                follow_button.getStyleClass().add("dark");
             //  follow_button.setTextFill(Paint.valueOf("#ffffff"));
               // follow_button.setStyle("-fx-background-color: #262424;-fx-border-color: #262424");

                follow_button.setDisable(true);


            } else if (userProfileController.getUserRelationship() == RelationshipType.FOLLOWING) {
                follow_button.setText("Follow Back");
              //  follow_button.getStyleClass().clear();
                follow_button.getStyleClass().removeAll("no_border","border","green","dark","no_color","default","default_border","black_border");
                follow_button.getStyleClass().add("rounded_button");
                follow_button.getStyleClass().add("no_border");
                follow_button.getStyleClass().add("green");

                //follow_button.setTextFill(Paint.valueOf("#ffffff"));
               // follow_button.setStyle("-fx-background-color: #009d3f;-fx-border-color: #009d3f");

            } else {

                follow_button.setText("Follow");
              //  follow_button.getStyleClass().clear();
                follow_button.getStyleClass().removeAll("no_border","border","green","dark","no_color","default","default_border","black_border");
                follow_button.getStyleClass().add("rounded_button");
                follow_button.getStyleClass().add("no_border");
                follow_button.getStyleClass().add("default");
               // follow_button.setTextFill(Paint.valueOf("#ffffff"));
             //   follow_button.setStyle("-fx-background-color: #dd5202;-fx-border-color: #dd5202");


            }
            if (userProfileController.getLoggedInUserRelationship() == RelationshipType.MUTED || userProfileController.getLoggedInUserRelationship() == RelationshipType.FOLLOWING_MUTED)
                silented_badge.setVisible(true);
            else {
                silented_badge.setVisible(false);
            }
        }
    }

    public
    void updateFollowings() {
        if (!userProfileController.isPrivatePage()) {
            followings_tab.setContent(getUsersListContent(getUsersListItems("followings")));
            followings_tab.setText(userProfileController.getUser().getFollowings().size() + " Following");
        }
    }

    public
    void updateFollowers() {
        if (!userProfileController.isPrivatePage()) {
            followers_tab.setText(userProfileController.getUser().getFollowers().size() + " Followers");

            followers_tab.setContent(getUsersListContent(getUsersListItems("followers")));
        }
    }

    public
    void updatePosts() {
        // posts_tab.setText(userProfileController.getUser().getPosts().size() + " Posts");
        if (!userProfileController.isPrivatePage()||(userProfileController.isPrivatePage()&&(userProfileController.getLoggedInUserRelationship()==RelationshipType.FOLLOWING||userProfileController.getLoggedInUserRelationship()==RelationshipType.FOLLOWING_MUTED)))
            posts_tab.setContent(getUserPostListContent());

    }

    public
    void updateUser() {
        username_label.setText(userProfileController.getUser().getUserName());
        user_real_name_label.setText(userProfileController.getUser().getFullName());
        TimeAgo time = new TimeAgo();
        String s;
        if(userProfileController.getUser().getLastSeen()!=null){
            ZonedDateTime zonedDateTime=ZonedDateTime.of(userProfileController.getUser().getLastSeen(), ZoneId.systemDefault());

            long messagemilles=zonedDateTime.toInstant().toEpochMilli();
            if(System.currentTimeMillis()-messagemilles<3000){
                s="Online";
            }else{
                s=time.timeAgo(messagemilles);

            }}else{
            s="Last seen recently";
        }
        last_seen_label.setText(s);
        bio_label.setText(userProfileController.getUser().getBio());

        if (userProfileController.getUserRelationship() == RelationshipType.BLOCKED) {
            tab_pane.getTabs().clear();
            Tab nothing = new Tab();
            nothing.setContent(Dialogs.nothingPage("This account has Blocked you :)", "BAN"));
            nothing.setText("Blocked");
            tab_pane.getTabs().add(nothing);
        } else if (userProfileController.isPrivatePage()&& userProfileController.getLoggedInUserRelationship()!=RelationshipType.FOLLOWING_MUTED&&userProfileController.getLoggedInUserRelationship()!=RelationshipType.FOLLOWING) {
            tab_pane.getTabs().clear();
            Tab nothing = new Tab();
            nothing.setContent(Dialogs.nothingPage("This account is private.Follow for more Access", "LOCK"));
            nothing.setText("Private Page");
            tab_pane.getTabs().add(nothing);
        } else {
            tab_pane.getTabs().clear();
            tab_pane.getTabs().add(posts_tab);
            tab_pane.getTabs().add(followers_tab);
            tab_pane.getTabs().add(followings_tab);

        }

        if (userProfileController.getUser().getImage() != null) {
            avatar_circle.setFill(new ImagePattern(ImageController.getImage(userProfileController.getUser().getImage())));
        } else {
            avatar_circle.setFill(new ImagePattern(new Image(getClass().getResource("/user_avatar.jpg").toExternalForm())));

        }


    }
}
