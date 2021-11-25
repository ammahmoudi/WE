package view.dialog;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateTimePickerView {
LocalDateTime sendingDate=null;
    @FXML
    private JFXButton yes_button;

    @FXML
    private Label title_label;

    @FXML
    private JFXButton close_button;

    @FXML
    private JFXDatePicker date_picker;

    @FXML
    private JFXTimePicker time_picker;

    @FXML
    void OnCloseButtonAction(ActionEvent event) {

    }

    @FXML
    void OnYesButtonAction(ActionEvent event) {

        LocalDate date=date_picker.getValue();

        LocalTime time=time_picker.getValue();
         sendingDate=LocalDateTime.of(date,time);
         close_button.fire();



    }
    @FXML
    public void initialize(){
        date_picker.setValue(LocalDate.now());
        time_picker.setValue(LocalTime.now());
    }

    public
    JFXButton getYes_button() {
        return yes_button;
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
    JFXDatePicker getDate_picker() {
        return date_picker;
    }

    public
    JFXTimePicker getTime_picker() {
        return time_picker;
    }
}
