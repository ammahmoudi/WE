package view.dialog;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class SimpleDialogView {

    @FXML
    private BorderPane borderpane;

    @FXML
    private Label title_label;

    @FXML
    private JFXButton close_button;
    Node content;

    @FXML
    void OnCloseButtonAction(ActionEvent event) {

    }

    public  void  initializeValues(String title, Node content){
        title_label.setText(title);
        this.content=content;
        borderpane.setCenter(content);
    }

    public
    Label getTitle_label() {
        return title_label;
    }

    public
    SimpleDialogView setTitle_label(Label title_label) {
        this.title_label = title_label;
        return this;
    }

    public
    JFXButton getClose_button() {
        return close_button;
    }

    public
    SimpleDialogView setClose_button(JFXButton close_button) {
        this.close_button = close_button;
        return this;
    }
}
