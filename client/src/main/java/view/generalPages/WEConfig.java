package view.generalPages;

import javafx.application.Application;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public
class WEConfig {


    String name = "";
    Boolean go= false;
    String rootPath;
    String appConfigPath = "app.properties";
    String catalogConfigPath = rootPath + "catalog";

    public  Properties appProps = new Properties();
    public
    WEConfig(){
          }
public Properties loadProperties(){
    try {
        appProps.load(new FileInputStream(appConfigPath));
    } catch (IOException e) {
        e.printStackTrace();
    }
    return appProps;
}
    public void saveProperties(){
        String newAppConfigPropertiesFile = "app.properties";
        try {
            appProps.store(new FileWriter(newAppConfigPropertiesFile), "store to properties file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}