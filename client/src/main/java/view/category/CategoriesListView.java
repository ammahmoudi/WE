package view.category;

import com.jfoenix.controls.JFXMasonryPane;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import view.dialog.Dialogs;
import view.View;

import java.util.LinkedList;

public class CategoriesListView extends View {
        LinkedList<CategoryItemView> categoryItemViews;
    @FXML
    private
    JFXMasonryPane categories_masonery;
    @FXML
    private StackPane stackpane;
    @FXML
    private ScrollPane scrollpane;
    public void initializeValues(LinkedList <Node> list,   LinkedList<CategoryItemView> categoryItemViews){
        if (list.isEmpty()){
            stackpane.getChildren().clear();
            stackpane.getChildren().add(Dialogs.nothingPage("No Categories!","BOOK"));
        }
        categories_masonery.getChildren().addAll(list);
    this.categoryItemViews=categoryItemViews;
    }
    public  void refresh(){
        for(CategoryItemView categoryItemView :categoryItemViews){
            categoryItemView.initializeValues(categoryItemView.categoryItemController.getCategory());

        }

    }
}
