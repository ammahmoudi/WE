package view.user;

import com.jfoenix.controls.JFXButton;
import controller.ImageController;
import controller.user.UserItemController;
import event.user.RelationshipEvent;
import event.user.RequestEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import model.LoggedInUser;
import model.RelationshipType;
import model.User;
import view.View;
import view.generalPages.MainPageView;

import java.io.IOException;

public
class UserItemView extends View {

    @FXML
    private Circle avatar_circle;

    @FXML
    private
    HBox hbox;

    @FXML
    private Label user_real_name_label;

    @FXML
    private Label username_label;

    @FXML
    private JFXButton accept_button;

    @FXML
    private JFXButton deny_button;

    @FXML
    private JFXButton follow_button;

    private UserItemController userItemController;

    @FXML
    void OnAcceptButtonAction(ActionEvent event) {
        RequestEvent requestEvent = new RequestEvent( LoggedInUser.getUser().getId(), userItemController.getUser().getId(), true);
        userItemController.applyRequest(requestEvent, new Runnable() {
            @Override
            public
            void run() {
                updateButtons();
            }
        });
    }

    @FXML
    void OnAvatarCircleClicked(MouseEvent event) {

    }

    @FXML
    void OnDenyButtonAction(ActionEvent event) {
        RequestEvent requestEvent = new RequestEvent( LoggedInUser.getUser().getId(), userItemController.getUser().getId(), false);
        userItemController.applyRequest(requestEvent, new Runnable() {
            @Override
            public
            void run() {
                updateButtons();
            }
        });
    }


    @FXML
    void OnFollowButtonAction(ActionEvent event) {
        RelationshipEvent relationshipEvent = new RelationshipEvent( LoggedInUser.getUser().getId(), userItemController.getUser().getId());
        userItemController.applyRelationship(relationshipEvent, new Runnable() {
            @Override
            public
            void run() {
                updateButtons();
            }
        });

    }

    @FXML
    void OnUserClicked(MouseEvent event) {
        FXMLLoader userProfileLoader = new FXMLLoader(getClass().getResource("/fxmls/user/UserProfile.fxml"));
        try {
            Node userProfile = userProfileLoader.load();
            UserProfileView userProfileView = userProfileLoader.getController();
            userProfileView.initializeValues(userItemController.getUser());
            MainPageView.newTab(userProfile, userItemController.getUser().getFullName());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public
    void initializeValues(User user) {
        userItemController = new UserItemController(user);
        username_label.setText(user.getUserName());
        user_real_name_label.setText(user.getFullName());
       userItemController.updateRelationship(new Runnable() {
           @Override
           public
           void run() {
               updateButtons();
           }
       });

        if (user.getImage() != null) {
            avatar_circle.setFill(new ImagePattern(ImageController.getImage(user.getImage())));
        } else {
            avatar_circle.setFill(new ImagePattern(new Image(getClass().getResource("/user_avatar.jpg").toExternalForm())));

        }

    }

    public
    void updateButtons() {
        if (userItemController.getUser().equals(LoggedInUser.getUser())) {
            hbox.getChildren().clear();
        } else {
            if (userItemController.getUserRelationship() == RelationshipType.REQUESTED) {
                hbox.getChildren().clear();
                hbox.getChildren().add(accept_button);
                hbox.getChildren().add(deny_button);
            } else {
                hbox.getChildren().clear();
                hbox.getChildren().add(follow_button);
            }
            if (userItemController.getLoggedInUserRelationship() == RelationshipType.FOLLOWING || userItemController.getLoggedInUserRelationship() == RelationshipType.FOLLOWING_MUTED) {
                follow_button.setText("Following");
                follow_button.setTextFill(Paint.valueOf("#000000"));
                follow_button.setStyle("-fx-background-color: white;-fx-border-color: black");


            } else if (userItemController.getLoggedInUserRelationship() == RelationshipType.REQUESTED) {
                follow_button.setText("Requested");
                follow_button.setTextFill(Paint.valueOf("#c17f00"));
                follow_button.setStyle("-fx-background-color: #ffffff;-fx-border-color: #c17f00");


            } else if (userItemController.getLoggedInUserRelationship() == RelationshipType.BLOCKED) {
                follow_button.setText("UnBlock");
                follow_button.setTextFill(Paint.valueOf("#a20000"));
                follow_button.setStyle("-fx-background-color: #ffffff;-fx-border-color: #a20000");


            } else if (userItemController.getUserRelationship() == RelationshipType.BLOCKED) {
                follow_button.setText("You Are Blocked");
                follow_button.setTextFill(Paint.valueOf("#ffffff"));
                follow_button.setStyle("-fx-background-color: #262424;-fx-border-color: #262424");

                follow_button.setDisable(true);


            } else if (userItemController.getUserRelationship() == RelationshipType.FOLLOWING) {
                follow_button.setText("Follow Back");
                follow_button.setTextFill(Paint.valueOf("#ffffff"));
                follow_button.setStyle("-fx-background-color: #009d3f;-fx-border-color: #009d3f");

            } else {

                follow_button.setText("Follow");
                follow_button.setTextFill(Paint.valueOf("#ffffff"));
                follow_button.setStyle("-fx-background-color: #dd5202;-fx-border-color: #dd5202");


            }
        }
    }

}
