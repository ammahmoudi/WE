package view;




import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.GenericTypeInterface;

import view.generalPages.WEConfig;

import java.util.Properties;

public
class View {
    public static Scene scene;
    public static Stage mainStage;

    public
    WEConfig weConfig=new WEConfig();


    public void refresh(){
    }
    public static
    Scene getScene() {
        return scene;
    }

    public static
    void setScene(Scene scene1) {
        scene = scene1;
      //  Font.loadFont(this.getClass().getResource("/fa-solid-900.woff")toExternalForm(), 12);
    }

    public static
    Stage getMainStage() {
        return mainStage;
    }

    public static
    void setMainStage(Stage mainStage) {
        mainStage = mainStage;
    }
}
