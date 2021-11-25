package view.generalPages;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class NothingPageView {

    @FXML
    private Label text;

    @FXML
    private FontAwesomeIcon icon;
    public void initializeValues(String text,String iconName){
        this.text.setText(text);
        this.icon.setIconName(iconName);

    }

}
