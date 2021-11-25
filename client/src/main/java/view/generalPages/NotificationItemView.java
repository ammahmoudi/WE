package view.generalPages;

import com.github.kevinsawicki.timeago.TimeAgo;
import com.jfoenix.controls.JFXButton;
import controller.ImageController;
import controller.notification.NotificationItemController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import model.Notification;
import view.user.UserProfileView;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class NotificationItemView {

    @FXML
    private HBox hbox;

    @FXML
    private Circle avatar_circle;

    @FXML
    private Label text_label;

    @FXML
    private Label creation_date_label;

    @FXML
    private JFXButton close_button;
    @FXML
    private JFXButton notification_button;

        NotificationItemController notificationItemController;
    @FXML
    void OnAvatarCircleClicked(MouseEvent event) {

    }

    @FXML
    void OnCloseButtonAction(ActionEvent event) {

    }

    @FXML
    void OnUserClicked(MouseEvent event) {

    }
    @FXML
    void OnNotificationButtonAction(ActionEvent event) {
        FXMLLoader userProfileLoader = new FXMLLoader(getClass().getResource("/fxmls/user/UserProfile.fxml"));
        try {
            ScrollPane userProfile = userProfileLoader.load();
            UserProfileView userProfileView = userProfileLoader.getController();
            userProfileView.initializeValues(notificationItemController.getSender());
            MainPageView.newTab(userProfile, notificationItemController.getSender().getFullName());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void initializeValues(Notification notification){
        notificationItemController=new NotificationItemController(notification);
        if(notificationItemController.getSender().getImage()==null){
            avatar_circle.setFill(new ImagePattern(new Image(getClass().getResource("/user_avatar.jpg").toExternalForm())));

        }else{
            avatar_circle.setFill(new ImagePattern(ImageController.getImage(notificationItemController.getSender().getImage())));

        }

        text_label.setText(notificationItemController.getNotification().getText());
        TimeAgo time = new TimeAgo();

        ZonedDateTime zonedDateTime=ZonedDateTime.of(notificationItemController.getNotification().getCreation_date(), ZoneId.systemDefault());

        long messagemilles=zonedDateTime.toInstant().toEpochMilli();
        creation_date_label.setText(time.timeAgo(messagemilles));
        if(notificationItemController.getNotification().isSeen()){
            hbox.setEffect(null);
        }
        notificationItemController.seen();
    }

}
