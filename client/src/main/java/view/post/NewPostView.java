/**
 * Sample Skeleton for 'NewPost.fxml' Controller Class
 */

package view.post;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import controller.ImageController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.effect.MotionBlur;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ResourceBundle;

public
class NewPostView {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML
    private FontAwesomeIcon add_photo_icon;
    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="post_text_input"
    private JFXTextArea post_text_input; // Value injected by FXMLLoader

    @FXML // fx:id="post_button"
    private JFXButton post_button; // Value injected by FXMLLoader

    @FXML // fx:id="add_photo_button"
    private JFXButton add_photo_button; // Value injected by FXMLLoader

    @FXML // fx:id="close_button"
    private JFXButton close_button; // Value injected by FXMLLoader
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
                //add_photo_icon.setFill(Paint.valueOf("#000000"));


            }


            //  Image te;
            //   te=ImageController.getImage(byteArrayedImage);
            //  byteArrayedImage=ImageController.getByteArrayedImage(te);
            //  System.out.println(byteArrayedImage.length);
//            if (byteArrayedImage != null) {
//                ImageCropperView imageCropperView = new ImageCropperView(1.59);
//
//                JFXDialog dialog = Dialogs.imageCropper(MainPageView.universalCenterStackPane, ImageController.getImage(byteArrayedImage), imageCropperView);
//                dialog.setOnDialogClosed(new EventHandler <JFXDialogEvent>() {
//                    @Override
//                    public
//                    void handle(JFXDialogEvent event) {
//                        if (imageCropperView.getCroppedImage() != null) {
//
//
//                            add_photo_button.setStyle(null);
//                            byteArrayedImage = imageCropperView.getCroppedArrayedImage();
//
//
//                            Background bg = new Background(new
//                                    BackgroundImage(imageCropperView.getCroppedImage(),
//                                    BackgroundRepeat.NO_REPEAT,
//                                    BackgroundRepeat.NO_REPEAT,
//                                    BackgroundPosition.CENTER,
//                                    new BackgroundSize(100,
//                                            100,
//                                            true,
//                                            true,
//                                            true,
//                                            false)));
//                            add_photo_button.setBackground(bg);
//                            add_photo_button.setEffect(new MotionBlur());
//                            add_photo_icon.setVisible(false);
//                            add_photo_button.setText("");
//                            //add_photo_icon.setFill(Paint.valueOf("#000000"));
//
//                        }
//                    }
//                });
//
//            }
        }
    }

    @FXML
    void OnCloseButtonAction(ActionEvent event) {

    }

    @FXML
    void OnPostButtonAction(ActionEvent event) {

        close_button.fire();
    }

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {


    }

    public
    JFXButton getClose_button() {
        return close_button;
    }

    public
    JFXTextArea getPost_text_input() {
        return post_text_input;
    }

    public
    JFXButton getPost_button() {
        return post_button;
    }

    public
    JFXButton getAdd_photo_button() {
        return add_photo_button;
    }

    public
    byte[] getByteArrayedImage() {
        return byteArrayedImage;
    }

    public
    NewPostView setByteArrayedImage(byte[] byteArrayedImage) {
        this.byteArrayedImage = byteArrayedImage;
        return this;
    }
}

