package view.generalPages;

import com.jfoenix.controls.*;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import controller.ServerHandler;
import controller.user.AuthController;
import event.user.SignUpFormEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.StackPane;
import view.dialog.Dialogs;
import view.View;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public
class SignUpView extends View implements Initializable {

    AuthController authController;
    @FXML
    private JFXTextField first_name_field;
    @FXML
    private JFXButton back_to_sign_in_page_button;
    @FXML
    private JFXTextField last_name_field;
    @FXML
    private JFXTextField username_field;
    @FXML
    private JFXTextField email_field;
    @FXML
    private JFXPasswordField password_field;
    @FXML
    private JFXPasswordField confirm_password_field;
    @FXML
    private JFXDatePicker birthday_field;
    @FXML
    private JFXTextField phone_number_field;
    @FXML
    private JFXTextArea bio_field;
    @FXML
    private JFXButton yes_button;
    @FXML
    private
    StackPane general_stackpane;

    @FXML
    void OnYesButtonAction(ActionEvent event) {
        SignUpFormEvent signUpFormEvent;
        if (first_name_field.validate() && last_name_field.validate() && username_field.validate() && email_field.validate() && phone_number_field.validate() && password_field.validate() && confirm_password_field.validate()) {
            signUpFormEvent = new SignUpFormEvent( first_name_field.getText(), last_name_field.getText(), password_field.getText(), username_field.getText(), phone_number_field.getText(), email_field.getText(), bio_field.getText(), birthday_field.getValue());
            String result = authController.signUp(signUpFormEvent);
            if (result == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/generalPages/SignIn.fxml"));
                try {
                    Parent root = loader.load();
                    View.getScene().setRoot(root);
                    ServerHandler.disconnectFromServer();


                } catch (IOException e) {
                    e.printStackTrace();

                }

            } else {
                Label label = new Label(result);
                label.setStyle("-fx-font-size: 16");
                Dialogs.SimpleDialog(general_stackpane, "Error!", label);
            }

        }
    }

    @FXML
    void OnBackToSignInButtonAction(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/generalPages/SignIn.fxml"));
        try {
            Parent root = loader.load();
            scene = username_field.getScene();
            scene.setRoot(root);
            ServerHandler.disconnectFromServer();


        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @Override
    public
    void initialize(URL location, ResourceBundle resources) {
        authController = new AuthController();
        ValidatorBase checkingConfrimPassword = new ValidatorBase() {
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
                hasErrors.set(!password_field.getText().equals(textField.getText()));
            }
        };
        checkingConfrimPassword.setMessage("Passwords do not match!");
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
                hasErrors.set(authController.isUsedUsername(textField.getText()));
            }
        };
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
                hasErrors.set(authController.isUsedEmail(textField.getText()));
            }
        };
        checkingEmail.setMessage("Email is Already taken!");
        RequiredFieldValidator firstNameRequiredFieldValidator = new RequiredFieldValidator();
        firstNameRequiredFieldValidator.setMessage("First name is Required!");
        RequiredFieldValidator lastNameRequiredFieldValidator = new RequiredFieldValidator();
        lastNameRequiredFieldValidator.setMessage("Last name is Required!");
        RequiredFieldValidator userNameRequiredFieldValidator = new RequiredFieldValidator();
        userNameRequiredFieldValidator.setMessage("Username is Required!");
        RequiredFieldValidator passwordRequiredFieldValidator = new RequiredFieldValidator();
        passwordRequiredFieldValidator.setMessage("Password is Required!");
        RequiredFieldValidator emailRequiredFieldValidator = new RequiredFieldValidator();
        emailRequiredFieldValidator.setMessage("Email is Required!");
        RequiredFieldValidator confirmPasswordRequiredFieldValidator = new RequiredFieldValidator();
        lastNameRequiredFieldValidator.setMessage("Confirm is Required!");
        NumberValidator numberValidator = new NumberValidator();
        numberValidator.setMessage("Enter Numbers!");
        RegexValidator emailValidator = new RegexValidator();
        emailValidator.setRegexPattern("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        emailValidator.setMessage("Email is not Valid");
        first_name_field.setValidators(firstNameRequiredFieldValidator);
        last_name_field.setValidators(lastNameRequiredFieldValidator);
        username_field.setValidators(userNameRequiredFieldValidator, checkingUsername);
        password_field.setValidators(passwordRequiredFieldValidator);
        confirm_password_field.setValidators(confirmPasswordRequiredFieldValidator,checkingConfrimPassword);
        email_field.setValidators(emailRequiredFieldValidator, emailValidator, checkingEmail);
        phone_number_field.setValidators(numberValidator);

        first_name_field.textProperty().addListener((observable, oldValue, newValue) -> {
            first_name_field.validate();

        });
        last_name_field.textProperty().addListener((observable, oldValue, newValue) -> {
            last_name_field.validate();
        });
        username_field.textProperty().addListener((observable, oldValue, newValue) -> {
            username_field.validate();
        });
        password_field.textProperty().addListener((observable, oldValue, newValue) -> {
            password_field.validate();
        });
        confirm_password_field.textProperty().addListener((observable, oldValue, newValue) -> {
            confirm_password_field.validate();
        });
        email_field.textProperty().addListener((observable, oldValue, newValue) -> {
            email_field.validate();
        });
        phone_number_field.textProperty().addListener((observable, oldValue, newValue) -> {
            phone_number_field.validate();
        });
    }

}
