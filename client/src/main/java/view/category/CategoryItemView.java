package view.category;

import com.jfoenix.controls.JFXButton;
import controller.category.CategoryItemController;
import controller.ImageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import model.Category;
import model.MessagingEnvironment;
import org.w3c.dom.events.MouseEvent;
import view.*;
import view.dialog.Dialogs;
import view.generalPages.MainPageView;
import view.messaging.MessagingEnvironmentItemView;
import view.messaging.MessagingEnvironmentsListView;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CategoryItemView extends View {

    @FXML
    private HBox user_info_bar;

    @FXML
    private Circle avatar_circle;

    @FXML
    private VBox user_detail_vbox;

    @FXML
    private Label category_name_label;

    @FXML
    private Label users_number_label;

    @FXML
    private VBox buttons_vbox;

    @FXML
    private JFXButton edit_button;



    @FXML
    private JFXButton send_button;

    @FXML
    private AnchorPane post_image_anchor;

    @FXML
    void OnAvatarCircleClicked(MouseEvent event) {

    }

    @FXML
    void OnEditButtonAction(ActionEvent event) {


    }

    @FXML
    void OnSendButtonAction(ActionEvent event) {
Dialogs.newMessageDialog(MainPageView.universalCenterStackPane,categoryItemController.getCategory());
        MainPageView.setLeftStackPaneContent(getMessagingEnvironmentsListContent(categoryItemController.getMessagingEnvironments()));


    }
    @FXML
    void OnUserInfoBarClicked(MouseEvent event) {

    }
    CategoryItemController categoryItemController;
    public
    void initializeValues(Category category) {
        categoryItemController=new CategoryItemController(category);
      category_name_label.setText(categoryItemController.getCategory().getName());
      users_number_label.setText(categoryItemController.getCategory().getUserList().size()+" Users");
      if (categoryItemController.getCategory().getImage()!=null){
          avatar_circle.setFill(new ImagePattern(ImageController.getImage(categoryItemController.getCategory().getImage())));
      }else{
          avatar_circle.setFill(new ImagePattern(new Image(getClass().getResource("/category_avatar.jpg").toExternalForm())));
      }

    }

    public
    JFXButton getEdit_button() {
        return edit_button;
    }

    public
    CategoryItemController getCategoryItemController() {
        return categoryItemController;
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
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmls/messaging/MessagingEnvironmentsList.fxml"));
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MessagingEnvironmentsListView messagesListView = loader.getController();
        messagesListView.initializeValues(messageItems, messageItemViews);
        MainPageView.universalViews.add(messagesListView);
        return node;
    }
}
