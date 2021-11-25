package view.generalPages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controller.ServerHandler;
import controller.user.AuthController;
import event.user.SignInFormEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import view.View;
import view.dialog.Dialogs;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public
class SignInView extends View{

    AuthController authController;
    @FXML
    private JFXTextField username_field;
    @FXML
    private JFXPasswordField password_field;
    @FXML
    private JFXButton signIn_button;
    @FXML
    private
    StackPane stackpane;
    @FXML
    private JFXButton sign_up_page_button;

    @FXML
    void onTextKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            signIn_button.fire();
        }
    }

    @FXML
    public
    void initialize() {
     //   System.out.println("x");
        authController = new AuthController();

    }

    public
    void initialize_values() {
        authController.checkForLoggedInUser();
    //    System.out.println(authController.isLoggedIn());
        if(authController.isLoggedIn()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/generalPages/MainPage.fxml"));
            Parent root = null;
            try {
                root = loader.load();
                View.getScene().setRoot(root);
            } catch (IOException e) {

                e.printStackTrace();
            }

        }

    }

    @FXML
    void onSignInButtonAction(ActionEvent event) throws IOException {
        SignInFormEvent signInFormEvent = new SignInFormEvent(getUsernameField(), getPasswordField());
        String result = authController.signIn(signInFormEvent);
        if (result == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/generalPages/MainPage.fxml"));
            Parent root = loader.load();
            View.getScene().setRoot(root);

        } else {
            Label label = new Label(result);
            label.setStyle("-fx-font-size: 16");
            Dialogs.SimpleDialog(stackpane, "Error!", label);
        }
    }

    public
    String getUsernameField() {
        return username_field.getText();
    }

    public
    String getPasswordField() {
        return password_field.getText();
    }

    @FXML
    void onSignUpPageAction(ActionEvent event) {
        ServerHandler.connectToServer();
        Boolean status=ServerHandler.isConnectionAlive();
        if(status==null){
            Label label = new Label("Please check your connection.");
            label.setStyle("-fx-font-size: 16");
            Dialogs.SimpleDialog(stackpane, "No server!", label);
        }else if (status){


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/generalPages/SignUp.fxml"));
        try {
            Parent root = loader.load();
            View.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();

        }

    }
    }
}
