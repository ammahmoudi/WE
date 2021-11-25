package view.messaging;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import view.dialog.Dialogs;
import view.View;

import java.util.LinkedList;

public class MessagesListView extends View {
LinkedList<MessageItemView> messageItemViews;
    @FXML
    private ScrollPane scrollpane;

    @FXML
    private VBox messages_VBox;
    public void initializeValues(LinkedList <Node> list, LinkedList<MessageItemView> messageItemViews){
        if (list.isEmpty()){
            scrollpane.setContent(Dialogs.nothingPage("No Message!","EXCLAMATION"));
        }
        messages_VBox.getChildren().addAll(list);
        this.messageItemViews=messageItemViews;
    }

}
