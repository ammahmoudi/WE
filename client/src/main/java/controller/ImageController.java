package controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public
class ImageController {


    public static
    byte[] pickImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.gif", "*.bmp"));
        File selectedFile=fileChooser
                .showOpenDialog(new Stage());
        byte[] byteArrayedImage=null;
        if(selectedFile!=null){
            try {
                byteArrayedImage= Files.readAllBytes(selectedFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
return byteArrayedImage;

    }

public static
    javafx.scene.image.Image getImage(byte[] byteArrayedImage){
        if (byteArrayedImage!=null){
            return new javafx.scene.image.Image(new ByteArrayInputStream(byteArrayedImage));
        }else return null;
}
 public static   byte[] getByteArrayedImage(javafx.scene.image.Image image){
        if (image!=null){
            BufferedImage bImage = SwingFXUtils.fromFXImage(image,null);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                ImageIO.write(bImage, "jpg", bos );
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte [] data = bos.toByteArray();
            return data;
        }else return null;
    }


}
