package view.messaging;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import controller.messaging.GroupProfileController;
import controller.ImageController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import event.messaging.ModifyGroupInfoEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import model.LoggedInUser;
import model.MessagingEnvironment;
import model.User;
import org.w3c.dom.events.MouseEvent;
import view.dialog.Dialogs;
import view.View;
import view.generalPages.MainPageView;
import view.user.UserItemView;
import view.user.UserProfileView;
import view.user.UsersListView;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class GroupProfileView extends View {
    @FXML
    private JFXButton close_button;
    @FXML
    private JFXButton owner_button;

    @FXML
    private BorderPane members_borderpane;
    @FXML
    private VBox details_vbox;

    @FXML
    private JFXTextField group_name_field;

    @FXML
    private Circle avatar_circle;

    @FXML
    private Label group_name_label;

    @FXML
    private Label member_number_label;

    @FXML
    private JFXButton leave_button;


    @FXML
    private VBox avatar_vbox;
    @FXML
    private JFXButton apply_button;
    @FXML
    private JFXButton add_member_Button;


    @FXML
    private JFXButton add_photo_button;

    @FXML
    private FontAwesomeIcon add_photo_icon;

    @FXML
    private VBox buttons_vbox;
    private
    byte[] byteArrayedImage;

    @FXML
    void OnAddPhotoButton(ActionEvent event) {
        if (byteArrayedImage != null) {
            byteArrayedImage = null;
            add_photo_button.setText("Add Photo");
            add_photo_button.setEffect(null);
            add_photo_button.setStyle(" -fx-background-color: #fc9c0c");
            add_photo_icon.setVisible(true);
        } else {

            byteArrayedImage = ImageController.pickImage();
            if (byteArrayedImage != null) {


                add_photo_button.setText("Delete");
                avatar_circle.setFill(new ImagePattern(ImageController.getImage(byteArrayedImage)));

            }

        }

    }
    GroupProfileController groupProfileController;

    @FXML
    void OnApplyButtonAction(ActionEvent event) {
        ModifyGroupInfoEvent modifyGroupInfoEvent=new ModifyGroupInfoEvent(groupProfileController.getMessagingEnvironment().getId(),group_name_field.getText(),byteArrayedImage);
        groupProfileController.applyChanges(modifyGroupInfoEvent);
        initializeValues(groupProfileController.getMessagingEnvironment());

    }
    @FXML
    void OnAddMemberButtonAction(ActionEvent event) {
        Dialogs.AddMemberDialog(MainPageView.universalCenterStackPane,groupProfileController.getMessagingEnvironment()).setOnDialogClosed(new EventHandler <JFXDialogEvent>() {
            @Override
            public
            void handle(JFXDialogEvent event) {

                initializeValues(groupProfileController.getMessagingEnvironment());
            }
        });

    }

    @FXML
    void OnAvatarCircleClicked(MouseEvent event) {

    }

    @FXML
    void OnLeaveButtonAction(ActionEvent event) {
groupProfileController.leave(new Runnable() {
    @Override
    public
    void run() {
    Tab tab=    MainPageView.universalTabPane.getSelectionModel().getSelectedItem();
        MainPageView.universalTabPane.getTabs().remove(tab);

    }
});
        close_button.fire();
    }

    @FXML
    void OnOwnerButtonAction(ActionEvent event) {
        FXMLLoader userProfileLoader = new FXMLLoader(getClass().getResource("/fxmls/user/UserProfile.fxml"));
        try {
            Node userProfile = userProfileLoader.load();
            UserProfileView userProfileView = userProfileLoader.getController();
            userProfileView.initializeValues(groupProfileController.getMessagingEnvironment().getOwner());
            MainPageView.newTab(userProfile, groupProfileController.getMessagingEnvironment().getOwner().getFullName());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initializeValues(MessagingEnvironment messagingEnvironment){
        groupProfileController=new GroupProfileController(messagingEnvironment);


        member_number_label.setText(groupProfileController.getMessagingEnvironment().getUsers().size()+" members");
        owner_button.setText("Owned by "+groupProfileController.getMessagingEnvironment().getOwner().getFullName());


        members_borderpane.setCenter(getUsersListContent(getUsersListItems(groupProfileController.getMessagingEnvironment().getUsers())));
      if(groupProfileController.getMessagingEnvironment().getOwner().equals(LoggedInUser.getUser())){
          group_name_field.setText(groupProfileController.getMessagingEnvironment().getName());

          details_vbox.getChildren().remove(group_name_label);

      }else{
          group_name_label.setText(groupProfileController.getMessagingEnvironment().getName());
          details_vbox.getChildren().remove(group_name_field);
          avatar_vbox.getChildren().remove(add_photo_button);
          buttons_vbox.getChildren().remove(apply_button);

      }

        if(groupProfileController.getMessagingEnvironment().getImage()!=null){
            avatar_circle.setFill(new ImagePattern(ImageController.getImage(groupProfileController.getMessagingEnvironment().getImage())));
            byteArrayedImage=groupProfileController.getMessagingEnvironment().getImage();
            add_photo_button.setText("Delete");

        }else{
            avatar_circle.setFill(new ImagePattern(new Image(getClass().getResource("/group_avatar.jpg").toExternalForm())));
            byteArrayedImage=null;
            add_photo_button.setText("Add Photo");
        }


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


    LinkedList <Node> getUsersListItems(List<User> list) {


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
    JFXButton getClose_button() {
        return close_button;
    }
}
