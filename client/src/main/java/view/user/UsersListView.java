package view.user;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.dialog.Dialogs;
import view.View;

import java.util.LinkedList;

public class UsersListView extends View {

    @FXML
    private VBox users_VBox;
    @FXML
    private ScrollPane scrollpane;
    @FXML
    private
    StackPane stackpane;


   public void initializeValues(LinkedList <Node>list){
       if (list.isEmpty()){
           stackpane.getChildren().clear();
           stackpane.getChildren().add(Dialogs.nothingPage("No User!","USER"));
       }
       users_VBox.getChildren().addAll(list);

   }
}
