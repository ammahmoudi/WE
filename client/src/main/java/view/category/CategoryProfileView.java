package view.category;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import controller.category.CategoryProfileController;
import controller.ImageController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import model.Category;
import model.User;
import org.w3c.dom.events.MouseEvent;
import view.*;
import view.dialog.Dialogs;
import view.generalPages.MainPageView;
import view.user.UserItemView;
import view.user.UsersListView;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public
class CategoryProfileView extends View {
    @FXML
    private JFXButton close_button;


    @FXML
    private BorderPane members_borderpane;
    @FXML
    private VBox details_vbox;

    @FXML
    private JFXTextField category_name_field;

    @FXML
    private Circle avatar_circle;


    @FXML
    private Label member_number_label;

    @FXML
    private JFXButton delete_button;


    @FXML
    private VBox avatar_vbox;
    @FXML
    private JFXButton apply_button;
    @FXML
    private JFXButton modify_users_button;


    @FXML
    private JFXButton add_photo_button;

    @FXML
    private FontAwesomeIcon add_photo_icon;

    @FXML
    private VBox buttons_vbox;
    private
    byte[] byteArrayedImage;
    private
    CategoryProfileController categoryProfileController;

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

    @FXML
    void OnApplyButtonAction(ActionEvent event) {
        categoryProfileController.applyChanges(category_name_field.getText(), byteArrayedImage, new Runnable() {
            @Override
            public
            void run() {
              categoryProfileController.updateCategory(new Runnable() {
                  @Override
                  public
                  void run() {
                      initializeValues(categoryProfileController.getCategory());
                  }
              });
            }
        });


    }

    @FXML
    void OnModifyUsersButtonAction(ActionEvent event) {
        Dialogs.CategoryMemeberChooserDialog(MainPageView.universalCenterStackPane, categoryProfileController.getCategory()).setOnDialogClosed(new EventHandler <JFXDialogEvent>() {
            @Override
            public
            void handle(JFXDialogEvent event) {
               categoryProfileController.updateCategory(new Runnable() {
                   @Override
                   public
                   void run() {
                       initializeValues(categoryProfileController.getCategory());
                   }
               });
            }
        });

    }

    @FXML
    void OnAvatarCircleClicked(MouseEvent event) {

    }

    @FXML
    void OnDeleteButtonAction(ActionEvent event) {
        Dialogs.confirmDialog(MainPageView.universalCenterStackPane, "Delete category", "Are you sure to delete this category?", new EventHandler() {
            @Override
            public
            void handle(Event event) {
                categoryProfileController.delete(new Runnable() {
                    @Override
                    public
                    void run() {
                        close_button.fire();
                    }
                });

            }
        });

    }


    public
    void initializeValues(Category category) {
        categoryProfileController = new CategoryProfileController(category);
        category_name_field.setText(categoryProfileController.getCategory().getName());
        member_number_label.setText(categoryProfileController.getCategory().getUserList().size() + " users");

        if (categoryProfileController.getCategory().getImage() != null) {
            avatar_circle.setFill(new ImagePattern(ImageController.getImage(categoryProfileController.getCategory().getImage())));
            byteArrayedImage = categoryProfileController.getCategory().getImage();
            add_photo_button.setText("Delete");

        }
            else{
                avatar_circle.setFill(new ImagePattern(new Image(getClass().getResource("/category_avatar.jpg").toExternalForm())));
            byteArrayedImage = null;
            add_photo_button.setText("Add Photo");
        }

        members_borderpane.setCenter(getUsersListContent(getUsersListItems(categoryProfileController.getCategory().getUserList())));

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


    LinkedList <Node> getUsersListItems(List <User> list) {


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
