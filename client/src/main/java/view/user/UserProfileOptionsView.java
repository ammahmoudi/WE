package view.user;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import controller.user.UserProfileController;
import controller.user.UserProfileOptionsController;
import event.user.BlockEvent;
import event.user.MuteEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import model.LoggedInUser;
import model.User;
import view.View;

public class UserProfileOptionsView extends View {
    boolean isBlocked;
    boolean isMuted;
    UserProfileOptionsController userProfileOptionsController;

    @FXML
    private JFXButton close_button;

    @FXML
    private JFXToggleButton block_toggle;

    @FXML
    private JFXToggleButton mute_toggle;

    @FXML
    private JFXButton apply_button;

    @FXML
    void OnApplyButtonAction(ActionEvent event) {


        if(isBlocked^ getBlock_toggle().isSelected()){

            BlockEvent blockEvent =new BlockEvent(LoggedInUser.getUser().getId(),userProfileOptionsController.getUserProfileController().getUser().getId());
            userProfileOptionsController.applyBlock(blockEvent, new Runnable() {
                @Override
                public
                void run() {
                    close_button.fire();
                }
            });
        }
        if(isMuted^ getMute_toggle().isSelected()){
            MuteEvent muteEvent =new MuteEvent(LoggedInUser.getUser().getId(),userProfileOptionsController.getUserProfileController().getUser().getId());
            userProfileOptionsController.applyMute(muteEvent, new Runnable() {
                @Override
                public
                void run() {
                    close_button.fire();
                }
            });
        }

    }




    @FXML
    void OnCloseButtonAction(ActionEvent event) {

    }
public void initializeValues(UserProfileController userProfileController){
       userProfileOptionsController=new UserProfileOptionsController(userProfileController);
this.isBlocked=userProfileOptionsController.isBLocked();
this.isMuted=userProfileOptionsController.isMuted();
        block_toggle.setSelected(isBlocked);
        mute_toggle.setSelected(isMuted);
        if(isBlocked)mute_toggle.setDisable(true);
block_toggle.selectedProperty().addListener(new ChangeListener <Boolean>() {
    @Override
    public
    void changed(ObservableValue <? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if(block_toggle.isSelected())mute_toggle.setDisable(true);
        else mute_toggle.setDisable(false);
    }
});
}


    public
    JFXButton getClose_button() {
        return close_button;
    }

    public
    JFXToggleButton getBlock_toggle() {
        return block_toggle;
    }

    public
    JFXToggleButton getMute_toggle() {
        return mute_toggle;
    }
}
