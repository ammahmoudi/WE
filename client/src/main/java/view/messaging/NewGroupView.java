package view.messaging;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controller.messaging.NewGroupController;
import event.messaging.NewGroupEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import model.LoggedInUser;
import model.MessagingEnvironment;
import model.User;
import view.generalPages.MainPageView;
import view.user.SimpleUserItemView;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class NewGroupView {

    @FXML
    private BorderPane borderpane;

    @FXML
    private Label title_label;

    @FXML
    private JFXButton close_button;

    @FXML
    private JFXTextField group_name_field;

    @FXML
    private JFXButton create_button;

    @FXML
    private ScrollPane scrollpane;

    @FXML
    private VBox users_VBox;
    Node users;
private
    NewGroupController newGroupController;
    @FXML
    void OnCloseButtonAction(ActionEvent event) {

    }

    @FXML
    void OnCreateButtonAction(ActionEvent event) {
        LinkedList<Integer> userIds=new LinkedList <>();
        for(User x: newGroupController.getUsersList()){
            userIds.add(x.getId());
        }
        NewGroupEvent newGroupEvent=new NewGroupEvent(group_name_field.getText(), LoggedInUser.getUser().getId(),userIds);

newGroupController.newGroup(newGroupEvent);

        close_button.fire();
        newGroupController.getUsersList().clear();
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
    JFXTextField getGroup_name_field() {
        return group_name_field;
    }

    public
    JFXButton getCreate_button() {
        return create_button;
    }

    public
    ScrollPane getScrollpane() {
        return scrollpane;
    }

    public
    VBox getUsers_VBox() {
        return users_VBox;
    }
    public void initializeValues(List <User> users){
        newGroupController=new NewGroupController();
      //  this.users=users;
        users_VBox.getChildren().addAll(getSimpleUserItems(users));

    }

    LinkedList <Node> getSimpleUserItems(List<User> users) {

        LinkedList <SimpleUserItemView> userItemViews = new LinkedList <>();
        LinkedList <Node> userItems = new LinkedList <>();
        for (User user : users) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/user/SimpleUserItem.fxml"));
                Node node = fxmlLoader.load();

               SimpleUserItemView simpleUserItemView = fxmlLoader.getController();
              simpleUserItemView.initializeValues(user);
              simpleUserItemView.getToggle_button().setOnAction(new EventHandler <ActionEvent>() {
                    @Override
                    public
                    void handle(ActionEvent event) {
                        if (!simpleUserItemView.getToggle_button().isSelected()) {
                            newGroupController.getUsersList().remove(user);
                        } else {
                            newGroupController.getUsersList().add(user);
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

}
