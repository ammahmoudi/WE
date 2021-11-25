package view.generalPages;

import com.jfoenix.controls.JFXBadge;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.events.JFXDialogEvent;
import controller.generalPages.ActivityController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import model.LoggedInUser;
import model.Notification;
import model.User;
import view.View;
import view.dialog.Dialogs;
import view.user.UserItemView;
import view.user.UsersListView;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public
class ActivityView extends View {
    ActivityController activityController;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXBadge sent_request_badge;

    @FXML
    private JFXBadge request_badge;
    @FXML
    private ScrollPane scrollpane;

    @FXML
    private VBox notifications_vbox;

    @FXML
    private JFXButton requests_button;

    @FXML
    private HBox hbox;

    @FXML
    private Circle requests_circle;

    @FXML
    private Label requests_number_label;

    @FXML
    private JFXButton sent_requests_button;

    @FXML
    private HBox hbox1;

    @FXML
    private Circle sent_requests_circle;

    @FXML
    private Label sent_requests_number_label;

    @FXML
    void OnAvatarCircleClicked(MouseEvent event) {

    }

    @FXML
    void OnRequestsButtonAction(ActionEvent event) {
        Dialogs.SimpleDialog(MainPageView.universalCenterStackPane, "Requests", getUsersListContent(getUsersListItems("Requests"))).setOnDialogClosed(new EventHandler <JFXDialogEvent>() {
            @Override
            public
            void handle(JFXDialogEvent event) {
                initialize();
            }
        });
    }

    @FXML
    void OnSentRequestsButtonAction(ActionEvent event) {
        Dialogs.SimpleDialog(MainPageView.universalCenterStackPane, " Sent Requests", getUsersListContent(getUsersListItems("Requesteds"))).setOnDialogClosed(new EventHandler <JFXDialogEvent>() {
            @Override
            public
            void handle(JFXDialogEvent event) {
                initialize();
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

        List <User> list = null;
        if (mode.equals("Followers")) {
            list = LoggedInUser.getUser().getFollowers();
        } else if (mode.equals("Followings")) {
            list = LoggedInUser.getUser().getFollowings();
        } else if (mode.equals("Requests")) {
            list = LoggedInUser.getUser().getRequests();
        } else if (mode.equals("Requesteds")) {
            list = LoggedInUser.getUser().getRequesteds();
        }
        LinkedList <Node> UsersItems = new LinkedList <>();
        if (list != null) {
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
        }
        return UsersItems;
    }

    @FXML
    public
    void initialize() {
        activityController = new ActivityController();
        LoggedInUser.updateNotifications(new Runnable() {
            @Override
            public
            void run() {
                notifications_vbox.getChildren().clear();
                notifications_vbox.getChildren().addAll(getNotificationsListItems());
                LoggedInUser.updateRequesteds(new Runnable() {
                    @Override
                    public
                    void run() {
                        sent_requests_number_label.setText(String.valueOf(LoggedInUser.getUser().getRequesteds().size()));
                        sent_request_badge.setVisible((LoggedInUser.getUser().getRequesteds().size()) != 0);
                        LoggedInUser.updateRequests(new Runnable() {
                            @Override
                            public
                            void run() {
                                requests_number_label.setText(String.valueOf(LoggedInUser.getUser().getRequests().size()));
                                request_badge.setVisible((LoggedInUser.getUser().getRequests().size()) != 0);
                            }
                        });

                    }
                });
            }
        });



        requests_circle.setFill(new ImagePattern(new Image(getClass().getResource("/category_avatar.jpg").toExternalForm())));
        sent_requests_circle.setFill(new ImagePattern(new Image(getClass().getResource("/group_avatar.jpg").toExternalForm())));


    }


    public
    Node getNotificationsListContent(LinkedList <Node> list) {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmls/user/UsersList.fxml"));
        ScrollPane scrollPane = null;
        try {
            scrollPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UsersListView usersListView = loader.getController();
        usersListView.initializeValues(list);
        return scrollPane;
    }

    LinkedList <Node> getNotificationsListItems() {

        List <Notification> list = LoggedInUser.getUser().getNotifications();

        LinkedList <Node> items = new LinkedList <>();
        if (list != null) {
            for (Notification notification : list) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/notification/NotificationItem.fxml"));
                    Node node = fxmlLoader.load();
                    NotificationItemView notificationItemView = fxmlLoader.getController();
                    notificationItemView.initializeValues(notification);
                    items.add(node);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return items;
    }

}
