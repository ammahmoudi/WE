package view.messaging;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.dialog.Dialogs;
import view.View;

import java.util.LinkedList;

public class MessagingEnvironmentsListView extends View {
LinkedList<MessagingEnvironmentItemView> messagingEnvironmentItemViews;


    @FXML
    private StackPane stackpane;

    @FXML
    private ScrollPane scrollpane;

    @FXML
    private VBox buttons_VBox;
    public void initializeValues(LinkedList <Node> list, LinkedList<MessagingEnvironmentItemView> messagingEnvironmentsListViews){
        if (list.isEmpty()){
            stackpane.getChildren().clear();
           stackpane.getChildren().add((Dialogs.nothingPage("No User","USER")));
        }
        buttons_VBox.getChildren().addAll(list);
     this.messagingEnvironmentItemViews=messagingEnvironmentsListViews;
    }

}
