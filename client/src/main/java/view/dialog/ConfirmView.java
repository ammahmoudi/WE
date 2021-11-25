package view.dialog;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ConfirmView {

    @FXML
    private JFXButton yes_button;

    @FXML
    private JFXButton no_button;

    @FXML
    private Label title_label;

    @FXML
    private JFXButton close_button;

    @FXML
    private Label text_label;

    @FXML
    void OnCloseButtonAction(ActionEvent event) {

    }

    @FXML
    void OnNoButtonAction(ActionEvent event) {
            close_button.fire();
    }

    @FXML
    void OnYesButtonAction(ActionEvent event) {

    }

    public
    JFXButton getYes_button() {
        return yes_button;
    }

    public
    JFXButton getNo_button() {
        return no_button;
    }

    public
    JFXButton getClose_button() {
        return close_button;
    }
    public void initializeValues(String title,String text){
        this.title_label.setText(title);
        this.text_label.setText(text);
    }
}
