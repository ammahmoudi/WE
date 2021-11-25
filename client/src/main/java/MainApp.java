import controller.ServerHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import view.View;
import view.generalPages.SignInView;
import view.generalPages.WEConfig;

import java.io.IOException;

public
class MainApp extends Application {

    public static
    void main(String[] args) {
        launch(args);

    }

    @Override
    public
    void start(Stage primaryStage)  {

        WEConfig weConfig=new WEConfig();
        if(weConfig.loadProperties().containsKey("ServerPort")){
            ServerHandler.SERVER_PORT= Integer.parseInt(weConfig.loadProperties().getProperty("ServerPort"));
        }else{
            weConfig.appProps.setProperty("ServerPort",String.valueOf(ServerHandler.SERVER_PORT));
            weConfig.saveProperties();
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxmls/generalPages/SignIn.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);

        SignInView signInView = loader.getController();


        primaryStage.setScene(scene);
primaryStage.getIcons().add(new Image(getClass().getResource("icon.png").toExternalForm()));
        View.setMainStage(primaryStage);
        View.setScene(scene);
        primaryStage.show();
        signInView.initialize_values();


    }
    @Override
    public void stop() {
        System.exit(0);
    }

}