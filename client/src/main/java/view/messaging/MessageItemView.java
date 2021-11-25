package view.messaging;

import com.github.kevinsawicki.timeago.TimeAgo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import controller.ImageController;
import controller.messaging.MessageItemController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
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
import model.Message;
import model.MessagingEnvironment;
import model.Post;
import view.View;
import view.generalPages.MainPageView;
import view.post.PostItemView;
import view.post.PostsListView;
import view.user.UserProfileView;
import view.user.UsersListView;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public
class MessageItemView extends View {
    JFXListView <Label> list = new JFXListView <>();


    JFXPopup popup = new JFXPopup();
    Label menu_forward = new Label();
    Label menu_edit = new Label();
    Label menu_delete = new Label();
    MessageItemController messageItemController;
    @FXML
    private StackPane message_stackpane;
    @FXML
    private HBox message_hbox;
    @FXML
    private Circle avatar_circle;
    @FXML
    private Label name_button;
    @FXML
    private JFXButton forward_button;
    @FXML
    private Label forwarded_button;
    @FXML
    private Label message_text;
    @FXML
    private Label message_time;
    @FXML
    private FontAwesomeIcon seen_icon;
    @FXML
    private VBox message_vbox;
    @FXML
    private AnchorPane image_anchor;
    @FXML
    private ImageView message_image;

    @FXML
    void OnAvatarCircleClicked(MouseEvent event) {

    }

    @FXML
    void OnForwardButtonAction(ActionEvent event) {


    }

    @FXML
    void OnForwardedClick(MouseEvent event) {
        if (!LoggedInUser.getOffline()) {
            FXMLLoader userProfileLoader = new FXMLLoader(getClass().getResource("/fxmls/user/UserProfile.fxml"));
            try {
                ScrollPane userProfile = userProfileLoader.load();
                UserProfileView userProfileView = userProfileLoader.getController();
                userProfileView.initializeValues(messageItemController.getMessage().getOriginalMessage().getOwner());
                MainPageView.newTab(userProfile, messageItemController.getMessage().getOriginalMessage().getOwner().getFullName());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void OnNameClicked(MouseEvent event) {
        if (!LoggedInUser.getOffline()) {
            FXMLLoader userProfileLoader = new FXMLLoader(getClass().getResource("/fxmls/user/UserProfile.fxml"));
            try {
                ScrollPane userProfile = userProfileLoader.load();
                UserProfileView userProfileView = userProfileLoader.getController();
                userProfileView.initializeValues(messageItemController.getMessage().getOwner());
                MainPageView.newTab(userProfile, messageItemController.getMessage().getOwner().getFullName());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void OnUserInfoBarClicked(MouseEvent event) {

    }

    @FXML
    void OnMessageHBoxMouseEntered(MouseEvent event) {
        forward_button.setVisible(true);
    }

    @FXML
    void OnMessageHBoxMouseExited(MouseEvent event) {
        forward_button.setVisible(false);
    }


    public
    void initializeValues(Message message) {

        messageItemController = new MessageItemController(message);
        if (messageItemController.getMessage().getOwner().equals(LoggedInUser.getUser())) {

            menu_delete.setText("Delete");
            menu_edit.setText("Edit");
            menu_forward.setText("Forward");
            menu_edit.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            menu_delete.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            menu_edit.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            list.setPrefWidth(100);
            list.setStyle("-fx-background-radius: 10px;-fx-background-color: white;");

            list.setPadding(new Insets(4, 4, 4, 4));
            list.getItems().add(menu_forward);
            if (!messageItemController.getMessage().isForwarded()) {
                list.getItems().add(menu_edit);
            }
            list.getItems().add(menu_delete);
            menu_forward.setOnMouseClicked(event -> {
                forward_button.fire();
                popup.hide();
            });
            popup.autoFixProperty().setValue(true);
            popup.autoHideProperty().setValue(true);
            popup.setPopupContent(list);
            popup.getPopupContent().setStyle("-fx-background-color: transparent;");

            for (Node node : list.getItems()) {
                node.setStyle("-fx-background-radius:10px");
            }
            if (!LoggedInUser.getOffline())
                message_stackpane.setOnContextMenuRequested(event -> popup.show(message_stackpane, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, event.getX(), event.getY()));
        }

        name_button.setText(messageItemController.getMessage().getOwner().getFullName());
        if (messageItemController.getMessage().isForwarded()) {
            forwarded_button.setText("Forwarded From " + messageItemController.getMessage().getOriginalMessage().getOwner().getFullName());
            message_text.setText(messageItemController.getMessage().getOriginalMessage().getText());
            if (messageItemController.getMessage().getOriginalMessage().getImage() != null) {
                message_image.setImage(ImageController.getImage(messageItemController.getMessage().getOriginalMessage().getImage()));
            } else {
                image_anchor.getChildren().remove(message_image);
            }

            if (messageItemController.getMessage().getOriginalMessage().checkPost()) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/post/postItem.fxml"));
                Node node = null;
                try {
                    node = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                PostItemView postItemView = fxmlLoader.getController();
                postItemView.initializeValues(messageItemController.getMessage().getPost());
                node.setEffect(null);
                node.setStyle("-fx-border-color: black;-fx-border-width: 1;-fx-background-color: white;");

                image_anchor.getChildren().clear();
                image_anchor.getChildren().add(node);


            }

        } else {
            if (messageItemController.getMessage().getImage() != null) {
                message_image.setImage(ImageController.getImage(messageItemController.getMessage().getImage()));
            } else {
                image_anchor.getChildren().remove(message_image);
            }
            message_vbox.getChildren().remove(forwarded_button);
            message_text.setText(messageItemController.getMessage().getText());
        }
        if (messageItemController.getMessage().getOwner().getImage() != null) {
            avatar_circle.setFill(new ImagePattern(ImageController.getImage(messageItemController.getMessage().getOwner().getImage())));
        } else {
            avatar_circle.setFill(new ImagePattern(new Image(getClass().getResource("/user_avatar.jpg").toExternalForm())));

        }
        TimeAgo time = new TimeAgo();

        ZonedDateTime zonedDateTime = ZonedDateTime.of(messageItemController.getMessage().getCreation_date(), ZoneId.systemDefault());

        long messagemilles = zonedDateTime.toInstant().toEpochMilli();
        String s;

        if (System.currentTimeMillis() - messagemilles < (1000 * 60 * 60 * 24)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm a");
            s = messageItemController.getMessage().getCreation_date().format(formatter);

        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");

            s = messageItemController.getMessage().getCreation_date().format(formatter);

        }
        message_time.setText(s);

        if (message.getOwner().equals(LoggedInUser.getUser())) {
            if (messageItemController.isSeen()) {
                seen_icon.setIconName("EYE");
                seen_icon.setFill(Paint.valueOf("#0EA973"));
                message_time.setGraphic(seen_icon);
            } else if (messageItemController.getMessage().getServerSeen() == null) {
                seen_icon.setIconName("CLOCK_ALT");
                seen_icon.setFill(Paint.valueOf("#757373"));
                message_time.setGraphic(seen_icon);


            } else if (messageItemController.getMessage().getServerSeen()) {
                seen_icon.setIconName("EYE_SLASH");
                seen_icon.setFill(Paint.valueOf("#0EA973"));
                message_time.setGraphic(seen_icon);
            } else if (!messageItemController.getMessage().getServerSeen()) {
                seen_icon.setIconName("EYE_SLASH");
                seen_icon.setFill(Paint.valueOf("#757373"));
                message_time.setGraphic(seen_icon);

            } else {

                message_time.setGraphic(null);
            }
            message_stackpane.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);

            message_vbox.setStyle("-fx-background-color: #fadede;-fx-background-radius:15px;");
        } else {
            messageItemController.Seen();
            message_time.setGraphic(null);

        }
        if (message.checkPost()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/post/postItem.fxml"));
            Node node = null;
            try {
                node = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            PostItemView postItemView = fxmlLoader.getController();
            postItemView.initializeValues(messageItemController.getMessage().getPost());
            node.setEffect(null);
            node.setStyle("-fx-border-color: black;-fx-border-width: 1;-fx-background-color: white;");

            image_anchor.getChildren().clear();
            image_anchor.getChildren().add(node);


        }
        updateOfflineMode();

    }

    public
    void updateOfflineMode() {
        if (!LoggedInUser.getOffline()) {
            forward_button.setDisable(false);


        } else {
            forward_button.setDisable(true);
        }
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
    StackPane getMessage_stackpane() {
        return message_stackpane;
    }

    public
    HBox getMessage_hbox() {
        return message_hbox;
    }

    public
    Circle getAvatar_circle() {
        return avatar_circle;
    }

    public
    Label getName_button() {
        return name_button;
    }

    public
    JFXButton getForward_button() {
        return forward_button;
    }

    public
    Label getForwarded_button() {
        return forwarded_button;
    }

    public
    Label getMessage_text() {
        return message_text;
    }

    public
    Label getMessage_time() {
        return message_time;
    }

    public
    FontAwesomeIcon getSeen_icon() {
        return seen_icon;
    }

    public
    VBox getMessage_vbox() {
        return message_vbox;
    }

    public
    JFXPopup getPopup() {
        return popup;
    }

    public
    Label getMenu_forward() {
        return menu_forward;
    }

    public
    Label getMenu_edit() {
        return menu_edit;
    }

    public
    Label getMenu_delete() {
        return menu_delete;
    }

    public
    Node getPostListContent(LinkedList <Post> feedPosts) {
        LinkedList <PostItemView> postItemsViews = new LinkedList <>();
        LinkedList <Node> postItems = new LinkedList <>();
        for (Post post : feedPosts) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/post/postItem.fxml"));
                Node node = fxmlLoader.load();
                PostItemView postItemView = fxmlLoader.getController();
                postItemView.initializeValues(post);

                postItems.add(node);
                postItemsViews.add(postItemView);
                MainPageView.universalViews.add(postItemView);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmls/post/PostsList.fxml"));
        ScrollPane scrollPane = null;
        try {
            scrollPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PostsListView postsListView = loader.getController();
        //   postsListView.initializeValues(postItems, postItemsViews);
        MainPageView.universalViews.add(postsListView);
        return scrollPane;
    }
}



