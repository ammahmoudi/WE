package view.generalPages;

import com.jfoenix.controls.*;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import controller.ImageController;
import controller.generalPages.SettingsController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import event.user.ChangeUserInfoEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import model.LastSeenPrivacy;
import model.LoggedInUser;
import view.dialog.Dialogs;
import view.View;

import java.io.IOException;

import static model.LastSeenPrivacy.NOBODY;

public
class SettingsView extends View {
    SettingsController settingsController;
    @FXML
    private FontAwesomeIcon add_photo_icon;
    @FXML
    private BorderPane borderpane;
    @FXML
    private Label title_label;
    @FXML
    private JFXButton close_button;
    @FXML
    private BorderPane members_borderpane;
    @FXML
    private JFXTextField first_name_field;
    @FXML
    private JFXTextField last_name_field;
    @FXML
    private JFXTextField username_field;
    @FXML
    private JFXTextField email_field;
    @FXML
    private JFXButton add_photo_button;
    @FXML
    private JFXDatePicker birthday_field;
    @FXML
    private JFXTextField phone_number_field;
    @FXML
    private JFXTextArea bio_field;
    @FXML
    private JFXRadioButton everyone_radio_button;
    @FXML
    private JFXRadioButton noone_radio_button;
    @FXML
    private JFXRadioButton only_followings_radio_button;
    @FXML
    private JFXToggleButton account_type_toggle_button;
    @FXML
    private ToggleGroup lastseen;
    @FXML
    private JFXPasswordField current_password_field;
    @FXML
    private JFXPasswordField new_password_field;
    @FXML
    private JFXButton deactive_button;
    @FXML
    private JFXButton delete_account_button;
    @FXML
    private Circle avatar_circle;
    @FXML
    private Label user_real_name_label;
    @FXML
    private Label username_label;
    @FXML
    private Label bio_label;
    @FXML
    private JFXButton apply_button;
    private
    byte[] byteArrayedImage;

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


                add_photo_button.setStyle(null);


                Background bg = new Background(new
                        BackgroundImage(ImageController.getImage(byteArrayedImage),
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(100,
                                100,
                                true,
                                true,
                                true,
                                false)));
                add_photo_button.setBackground(bg);
                add_photo_button.setEffect(new MotionBlur());
                add_photo_icon.setVisible(false);
                add_photo_button.setText("");



            }
        }
    }

    @FXML
    void OnApplyButtonAction(ActionEvent event) {

        if (current_password_field.validate() && new_password_field.validate() && first_name_field.validate() && last_name_field.validate() && username_field.validate() && email_field.validate() && phone_number_field.validate()) {

            ChangeUserInfoEvent changeUserInfoEvent = new ChangeUserInfoEvent( first_name_field.getText(), last_name_field.getText(), current_password_field.getText(), new_password_field.getText(), username_field.getText(), phone_number_field.getText(), email_field.getText(), bio_field.getText(), birthday_field.getValue(), (LastSeenPrivacy) lastseen.getSelectedToggle().getUserData(), account_type_toggle_button.isSelected(),byteArrayedImage);
            settingsController.applyChanges(changeUserInfoEvent, new Runnable() {
                @Override
                public
                void run() {

                    initialize();}
            });

        }
    }

    @FXML
    void OnDeactiveButtonAction(ActionEvent event) {
Dialogs.confirmDialog(MainPageView.universalCenterStackPane, "Deactive your Account", "Are you sure to deactive your account? It will be acitvated again after your next sign in.", new EventHandler() {
    @Override
    public
    void handle(Event event) {
        settingsController.deActive();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/generalPages/SignIn.fxml"));
        try {
            Parent root = loader.load();
            View.setScene(add_photo_button.getScene());
            View.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
});
    }

    @FXML
    void OnDeleteAccountButtonAction(ActionEvent event) {
        Dialogs.confirmDialog(MainPageView.universalCenterStackPane, "Delete your Account", "Are you sure to delete your account?  All your data will be deleted.", new EventHandler() {
            @Override
            public
            void handle(Event event) {
                settingsController.deleteAccount();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/generalPages/SignIn.fxml"));
                try {
                    Parent root = loader.load();
                    View.setScene(add_photo_button.getScene());
                    View.getScene().setRoot(root);

                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        });
    }

    @FXML
    public
    void initialize() {

        settingsController = new SettingsController();
        if(LoggedInUser.getUser().getImage()!=null){
            avatar_circle.setFill(new ImagePattern(ImageController.getImage(LoggedInUser.getUser().getImage())));
            byteArrayedImage=LoggedInUser.getUser().getImage();
            add_photo_button.setText("Delete Photo");

        }else{
            avatar_circle.setFill(new ImagePattern(new Image(getClass().getResource("/user_avatar.jpg").toExternalForm())));

            byteArrayedImage=null;
            add_photo_button.setText("Add Photo");
        }
        RequiredFieldValidator newPasswordRequiredFieldValidator = new RequiredFieldValidator();
        newPasswordRequiredFieldValidator.setMessage("New password is empty!");
        ValidatorBase checkingUsername = new ValidatorBase() {
            @Override
            protected
            void eval() {
                if (srcControl.get() instanceof TextInputControl) {
                    evalTextInputField();
                }

            }

            private
            void evalTextInputField() {
                TextInputControl textField = (TextInputControl) srcControl.get();
                hasErrors.set(settingsController.isUsedUsername(textField.getText()));
            }
        };
        ValidatorBase checkingCurrent = new ValidatorBase() {
            @Override
            protected
            void eval() {
                if (srcControl.get() instanceof TextInputControl) {
                    evalTextInputField();
                }

            }

            private
            void evalTextInputField() {
                TextInputControl textField = (TextInputControl) srcControl.get();

                if (!settingsController.isCurrentPassword(textField.getText())) {

                    new_password_field.setDisable(true);
                    new_password_field.getValidators().clear();
                    hasErrors.set(true);
                } else {

                    if (textField.getText().isBlank()) {
                    }
                    new_password_field.setDisable(false);
                    new_password_field.setValidators(newPasswordRequiredFieldValidator);
                    hasErrors.set(false);
                }
                if (textField.getText().isBlank()) {
                    new_password_field.setDisable(true);
                    new_password_field.getValidators().clear();
                    hasErrors.set(false);
                }


            }
        };

        checkingCurrent.setMessage("Current message is wrong!");
        checkingUsername.setMessage("User name is Already taken!");
        ValidatorBase checkingEmail = new ValidatorBase() {
            @Override
            protected
            void eval() {
                if (srcControl.get() instanceof TextInputControl) {
                    evalTextInputField();
                }

            }

            private
            void evalTextInputField() {
                TextInputControl textField = (TextInputControl) srcControl.get();
                hasErrors.set(settingsController.isUsedEmail(textField.getText()));
            }
        };
        checkingEmail.setMessage("Email is Already taken!");
        RequiredFieldValidator firstNameRequiredFieldValidator = new RequiredFieldValidator();
        firstNameRequiredFieldValidator.setMessage("First name is Required!");
        RequiredFieldValidator lastNameRequiredFieldValidator = new RequiredFieldValidator();
        lastNameRequiredFieldValidator.setMessage("Last name is Required!");
        RequiredFieldValidator userNameRequiredFieldValidator = new RequiredFieldValidator();

        RequiredFieldValidator emailRequiredFieldValidator = new RequiredFieldValidator();
        emailRequiredFieldValidator.setMessage("Email is Required!");


        NumberValidator numberValidator = new NumberValidator();
        numberValidator.setMessage("Enter Numbers!");
        RegexValidator emailValidator = new RegexValidator();
        emailValidator.setRegexPattern("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        emailValidator.setMessage("Email is not Valid");
        current_password_field.setValidators(checkingCurrent);
        current_password_field.textProperty().addListener((observable, oldValue, newValue) -> {
          //  System.out.println(settingsController.isCurrentPassword(current_password_field.getText()));
            current_password_field.validate();
        });
        first_name_field.setValidators(firstNameRequiredFieldValidator);
        last_name_field.setValidators(lastNameRequiredFieldValidator);
        username_field.setValidators(userNameRequiredFieldValidator, checkingUsername);

        email_field.setValidators(emailRequiredFieldValidator, emailValidator, checkingEmail);
        phone_number_field.setValidators(numberValidator);
        new_password_field.textProperty().addListener((observable, oldValue, newValue) -> {
            new_password_field.validate();
        });
        first_name_field.textProperty().addListener((observable, oldValue, newValue) -> {
            first_name_field.validate();

        });
        last_name_field.textProperty().addListener((observable, oldValue, newValue) -> {
            last_name_field.validate();
        });
        username_field.textProperty().addListener((observable, oldValue, newValue) -> {
            username_field.validate();
        });

        email_field.textProperty().addListener((observable, oldValue, newValue) -> {
            email_field.validate();
        });
        phone_number_field.textProperty().addListener((observable, oldValue, newValue) -> {
            phone_number_field.validate();
        });
        everyone_radio_button.setUserData(LastSeenPrivacy.EVERYONE);
        noone_radio_button.setUserData(NOBODY);
        only_followings_radio_button.setUserData(LastSeenPrivacy.ONLY_FOLLOWINGS);
        first_name_field.setText(LoggedInUser.getUser().getFirstName());
        last_name_field.setText(LoggedInUser.getUser().getLastName());
        username_field.setText(LoggedInUser.getUser().getUserName());
        email_field.setText(LoggedInUser.getUser().getEmail());
        account_type_toggle_button.setSelected(LoggedInUser.getUser().isPrivated());
        switch (LoggedInUser.getUser().getMyOwnLastSeenPrivacy()) {
            case NOBODY:
                lastseen.selectToggle(noone_radio_button);
                break;
            case EVERYONE:
                lastseen.selectToggle(everyone_radio_button);
                break;
            case ONLY_FOLLOWINGS:
                lastseen.selectToggle(only_followings_radio_button);
                break;
        }
        bio_field.setText(LoggedInUser.getUser().getBio());
        phone_number_field.setText(LoggedInUser.getUser().getPhoneNumber());
        birthday_field.setValue(LoggedInUser.getUser().getBirthDay());
        user_real_name_label.setText(LoggedInUser.getUser().getFullName());
        username_label.setText(LoggedInUser.getUser().getUserName());
        bio_label.setText(LoggedInUser.getUser().getBio());

updateOfflineMode();


    }
    public  void updateOfflineMode(){
        if(LoggedInUser.getOffline()){

           new_password_field.setDisable(true);
           current_password_field.setDisable(true);
           deactive_button.setDisable(true);
           delete_account_button.setDisable(true);
           username_field.setDisable(true);
           email_field.setDisable(true);



        }else{
            new_password_field.setDisable(false);
            current_password_field.setDisable(true);
            deactive_button.setDisable(false);
            delete_account_button.setDisable(false);
            username_field.setDisable(false);
            email_field.setDisable(false);

        }
    }

    public
    JFXButton getClose_button() {
        return close_button;
    }
}
