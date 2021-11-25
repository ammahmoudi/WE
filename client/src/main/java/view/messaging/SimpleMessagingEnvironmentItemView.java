package view.messaging;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleNode;
import controller.ImageController;
import controller.messaging.MessagingEnvironmentItemController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import model.MessagingEnvironment;
import org.w3c.dom.events.MouseEvent;
import view.View;

public class SimpleMessagingEnvironmentItemView extends View {

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
    private JFXToggleNode toggle_button;


    @FXML
    void OnAvatarCircleClicked(MouseEvent event) {

    }


    @FXML
    void onMessageButtonAction(ActionEvent event) {
    }


    MessagingEnvironmentItemController messagingEnvironmentItemController;


    public void initializeValues(MessagingEnvironment messagingEnvironment){
     messagingEnvironmentItemController=new MessagingEnvironmentItemController(messagingEnvironment);
     if(messagingEnvironmentItemController.getMessagingEnvironment().isGroup()){
         name_label.setText(messagingEnvironmentItemController.getMessagingEnvironment().getName());
         if(messagingEnvironmentItemController.getMessagingEnvironment().getImage()!=null){
             avatar_circle.setFill(new ImagePattern(ImageController.getImage(messagingEnvironmentItemController.getMessagingEnvironment().getImage())));
         }else{
             avatar_circle.setFill(new ImagePattern(new Image(getClass().getResource("/group_avatar.jpg").toExternalForm())));

         }


     }else{
         if (messagingEnvironmentItemController.getTakerUser().getImage() != null) {
             avatar_circle.setFill(new ImagePattern(ImageController.getImage(messagingEnvironmentItemController.getTakerUser().getImage())));
         }else{
             avatar_circle.setFill(new ImagePattern(new Image(getClass().getResource("/user_avatar.jpg").toExternalForm())));

         }
         name_label.setText(messagingEnvironmentItemController.getTakerUser().getFullName());
         name_label.setGraphic(null);
     }






    }

    public
    JFXButton getMessage_button() {
        return Message_button;
    }

    public
    HBox getHbox() {
        return hbox;
    }

    public
    Circle getAvatar_circle() {
        return avatar_circle;
    }

    public
    Label getName_label() {
        return name_label;
    }

    public
    JFXToggleNode getToggle_button() {
        return toggle_button;
    }
}
