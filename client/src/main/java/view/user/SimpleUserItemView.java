package view.user;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleNode;
import controller.ImageController;
import controller.user.UserItemController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import model.User;
import org.w3c.dom.events.MouseEvent;
import view.View;

public class SimpleUserItemView extends View {

    @FXML
    private JFXButton Message_button;

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


   UserItemController userItemController;


    public void initializeValues(User user){
        userItemController = new UserItemController(user);
        name_label.setText(userItemController.getUser().getFullName());
        if (userItemController.getUser().getImage()!=null){
            avatar_circle.setFill(new ImagePattern(ImageController.getImage(userItemController.getUser().getImage())));
        }else{
            avatar_circle.setFill(new ImagePattern(new Image(getClass().getResource("/user_avatar.jpg").toExternalForm())));

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
