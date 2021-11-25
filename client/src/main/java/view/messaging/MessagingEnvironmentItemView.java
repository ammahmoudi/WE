package view.messaging;

import com.github.kevinsawicki.timeago.TimeAgo;
import com.jfoenix.controls.JFXBadge;
import com.jfoenix.controls.JFXButton;
import controller.ImageController;
import controller.messaging.MessagingEnvironmentItemController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import model.LoggedInUser;
import model.MessagingEnvironment;
import org.w3c.dom.events.MouseEvent;
import view.View;
import view.generalPages.MainPageView;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public
class MessagingEnvironmentItemView extends View {

    MessagingEnvironmentItemController messagingEnvironmentItemController;
    @FXML
    private JFXButton Message_button;
    @FXML
    private FontAwesomeIcon type_icon;
    @FXML
    private HBox hbox;
    @FXML
    private Circle avatar_circle;
    @FXML
    private Label name_label;
    @FXML
    private Label message_label;
    @FXML
    private Label unseen_label;
    @FXML
    private Label message_time;
    @FXML
    private FontAwesomeIcon seen_icon;
    @FXML
    private JFXBadge unseen_badge;

    @FXML
    void OnAvatarCircleClicked(MouseEvent event) {

    }

    @FXML
    void OnUserClicked(MouseEvent event) {

    }

    @FXML
    void OnUserInfoBarClicked(MouseEvent event) {

    }

    @FXML
    void onMessageButtonAction(ActionEvent event) {
   messagingEnvironmentItemController.getFullMessagingEnvironment(new Runnable() {
       @Override
       public
       void run() {
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/messaging/MessagingPage.fxml"));
           try {
               Node privateChat = loader.load();
               MessagingPageView messagingPageView = loader.getController();
               if (messagingEnvironmentItemController.getMessagingEnvironment().isGroup()) {
                   MainPageView.newTab(privateChat, messagingEnvironmentItemController.getMessagingEnvironment().getName() + " chat");
               } else {
                   MainPageView.newTab(privateChat, messagingEnvironmentItemController.getTakerUser().getFullName() + " chat");
               }

               messagingPageView.initializeValues(messagingEnvironmentItemController.getMessagingEnvironment());


           } catch (IOException e) {
               e.printStackTrace();
           }
       }
   }).start();
    }

    public
    void initializeValues(MessagingEnvironment messagingEnvironment) {


        message_time.setGraphic(null);
        messagingEnvironmentItemController = new MessagingEnvironmentItemController(messagingEnvironment);
        if (messagingEnvironmentItemController.getMessagingEnvironment().isGroup()) {
           // System.out.println(messagingEnvironmentItemController.getMessagingEnvironment().getOwner());
            name_label.setText(messagingEnvironmentItemController.getMessagingEnvironment().getName());
            if (messagingEnvironmentItemController.getMessagingEnvironment().getImage() != null) {
                avatar_circle.setFill(new ImagePattern(ImageController.getImage(messagingEnvironmentItemController.getMessagingEnvironment().getImage())));
            } else {
               avatar_circle.setFill(new ImagePattern(new Image(getClass().getResource("/group_avatar.jpg").toExternalForm())));
            }

        } else {
            if(LoggedInUser.getOffline()){
                messagingEnvironmentItemController.getMessagingEnvironment().applyTaker(LoggedInUser.getUser());
                messagingEnvironmentItemController.getMessagingEnvironment().applySavedMessage();
                messagingEnvironmentItemController.getMessagingEnvironment().applyLastMessage();
                messagingEnvironmentItemController.getMessagingEnvironment().setUnseenCounter(messagingEnvironmentItemController.getMessagingEnvironment().getUnseenMessagesNumberBy(LoggedInUser.getUser()));
            }

            if (messagingEnvironmentItemController.getMessagingEnvironment().getSavedMessage()==1) {
               avatar_circle.setFill(new ImagePattern(new Image(getClass().getResource("/saved_message.jpg").toExternalForm())));
                name_label.setText("Saved Messages");
            } else {
                name_label.setText(messagingEnvironmentItemController.getMessagingEnvironment().getTaker().getFullName());
                if (messagingEnvironmentItemController.getMessagingEnvironment().getTaker().getImage() != null) {
                    avatar_circle.setFill(new ImagePattern(ImageController.getImage(messagingEnvironmentItemController.getMessagingEnvironment().getTaker().getImage())));
                } else {
                    avatar_circle.setFill(new ImagePattern(new Image(getClass().getResource("/user_avatar.jpg").toExternalForm())));
                }
            }


            name_label.setGraphic(null);
        }
        if (messagingEnvironmentItemController.getMessagingEnvironment().getLastMessage() != null) {
           // System.out.println("last message is taken");
            message_label.setText(messagingEnvironmentItemController.getMessagingEnvironment().getLastMessage().getText());
            if (messagingEnvironmentItemController.getMessagingEnvironment().getLastMessage().getOwner().equals(LoggedInUser.getUser())) {
                if (messagingEnvironmentItemController.getMessagingEnvironment().isLastMessageSeen()) {
                    seen_icon.setIconName("EYE");
                    seen_icon.setFill(Paint.valueOf("#0EA973"));
                    message_time.setGraphic(seen_icon);
                } else if (messagingEnvironmentItemController.getMessagingEnvironment().getLastMessage().getServerSeen() == null) {
                    seen_icon.setIconName("CLOCK_ALT");
                    seen_icon.setFill(Paint.valueOf("#757373"));
                    message_time.setGraphic(seen_icon);


                } else if (messagingEnvironmentItemController.getMessagingEnvironment().getLastMessage().getServerSeen()) {
                    seen_icon.setIconName("EYE_SLASH");
                    seen_icon.setFill(Paint.valueOf("#0EA973"));
                    message_time.setGraphic(seen_icon);
                } else if (!messagingEnvironmentItemController.getMessagingEnvironment().getLastMessage().getServerSeen()) {
                    seen_icon.setIconName("EYE_SLASH");
                    seen_icon.setFill(Paint.valueOf("#757373"));
                    message_time.setGraphic(seen_icon);

                } else {

                    message_time.setGraphic(null);
                }

            }
        }
        TimeAgo time = new TimeAgo();

        ZonedDateTime zonedDateTime=ZonedDateTime.of(messagingEnvironmentItemController.getMessagingEnvironment().getLastMessageCreatedDate(), ZoneId.systemDefault());

        long messagemilles=zonedDateTime.toInstant().toEpochMilli();
        message_time.setText(time.timeAgo(messagemilles));
        unseen_label.setText(String.valueOf(messagingEnvironmentItemController.getMessagingEnvironment().getUnseenCounter()));
        if (messagingEnvironmentItemController.getMessagingEnvironment().getUnseenCounter() == 0) {
            unseen_badge.setVisible(false);

        } else {
            unseen_badge.setVisible(true);
        }


    }

}
