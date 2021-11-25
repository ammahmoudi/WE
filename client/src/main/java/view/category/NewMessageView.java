package view.category;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import controller.ImageController;
import controller.category.NewMessageController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import event.messaging.NewMessageEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Category;
import model.LoggedInUser;
import model.User;

public class NewMessageView {

    @FXML
    private JFXButton send_button;

    @FXML
    private FontAwesomeIcon send_icon;

    @FXML
    private JFXButton close_button;

    @FXML
    private JFXTextArea send_message_text;

    @FXML
    private JFXButton image_button;

    @FXML
    private FontAwesomeIcon image_icon;
    byte[] byteArrayedImage=null;

    @FXML
    void OnCloseButtonAction(ActionEvent event) {

    }

    @FXML
    void OnImageButtonAction(ActionEvent event) {

            if (byteArrayedImage != null) {
                byteArrayedImage = null;

                image_button.setStyle(" -fx-background-color: #fc9c0c");
                image_icon.setIconName("IMAGE");
                image_button.setText("Add photo");
            } else {

                byteArrayedImage = ImageController.pickImage();
                if (byteArrayedImage != null) {

                    image_icon.setIconName("CLOSE");
                    image_button.setText("Delete photo");


                }
            }

    }


    @FXML
    void OnSendButtonAction(ActionEvent event) {
        if (!send_message_text.getText().isBlank()) {

            for(User user:newMessageController.getCategory().getUserList()){
            NewMessageEvent newMessageEvent = new NewMessageEvent(LoggedInUser.getUser().getId(), newMessageController.getPrivateChat(user.getId()).getId(), send_message_text.getText(), null, byteArrayedImage,null);
            newMessageController.newMessage(newMessageEvent);
            }


        }
close_button.fire();

    }


    @FXML
    void onMessageTextKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            send_button.fire();
        }

    }


    NewMessageController newMessageController;
    public void  initializeValues(Category category){
        newMessageController=new NewMessageController(category);


    }

    public
    JFXButton getClose_button() {
        return close_button;
    }
}
