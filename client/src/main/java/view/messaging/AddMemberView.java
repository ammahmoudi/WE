package view.messaging;

import com.jfoenix.controls.JFXButton;
import controller.messaging.AddMemberController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import model.MessagingEnvironment;
import model.User;
import view.generalPages.MainPageView;
import view.user.SimpleUserItemView;
import view.View;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public
class AddMemberView extends View {

    AddMemberController addMemberController;
    @FXML
    private BorderPane borderpane;
    @FXML
    private Label title_label;
    @FXML
    private JFXButton close_button;
    @FXML
    private JFXButton add_button;
    @FXML
    private ScrollPane scrollpane;
    @FXML
    private VBox users_VBox;

    @FXML
    void OnAddButtonAction(ActionEvent event) {
                addMemberController.applyAddingMember();
                close_button.fire();
    }

    @FXML
    void OnCloseButtonAction(ActionEvent event) {

    }

    public
    void initializeValues(MessagingEnvironment messagingEnvironment) {
        addMemberController = new AddMemberController(messagingEnvironment);
        users_VBox.getChildren().addAll(getSimpleUserItems(addMemberController.getUsersList()));

    }


    LinkedList <Node> getSimpleUserItems(List <User> users) {

        LinkedList <SimpleUserItemView> userItemViews = new LinkedList <>();
        LinkedList <Node> userItems = new LinkedList <>();
        for (User user : users) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/user/SimpleUserItem.fxml"));
                Node node = fxmlLoader.load();

                SimpleUserItemView simpleUserItemView = fxmlLoader.getController();
                simpleUserItemView.initializeValues(user);

                if (addMemberController.isMember(user)) {
                    simpleUserItemView.getToggle_button().setDisable(true);
                }
                simpleUserItemView.getToggle_button().setOnAction(new EventHandler <ActionEvent>() {
                    @Override
                    public
                    void handle(ActionEvent event) {
                        if (!simpleUserItemView.getToggle_button().isSelected()) {
                            addMemberController.getSelectedUsersList().remove(user);
                        } else {
                            addMemberController.getSelectedUsersList().add(user);
                        }
                    }
                });
                userItems.add(node);
                userItemViews.add(simpleUserItemView);
                MainPageView.universalViews.add(simpleUserItemView);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return userItems;
    }

    public
    Node getMessagingPageContent(MessagingEnvironment messagingEnvironment) {
        Node chat = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/messaging/MessagingPage.fxml"));
        try {

            chat = loader.load();
            MessagingPageView messagingPageView = loader.getController();


            messagingPageView.initializeValues(messagingEnvironment);
            MainPageView.newTab(chat, messagingEnvironment.getName());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return chat;
    }

    public
    BorderPane getBorderpane() {
        return borderpane;
    }

    public
    Label getTitle_label() {
        return title_label;
    }

    public
    JFXButton getClose_button() {
        return close_button;
    }

    public
    JFXButton getAdd_button() {
        return add_button;
    }

    public
    ScrollPane getScrollpane() {
        return scrollpane;
    }

    public
    VBox getUsers_VBox() {
        return users_VBox;
    }

    public
    AddMemberController getAddMemberController() {
        return addMemberController;

    }
}
