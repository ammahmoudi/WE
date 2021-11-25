package view.post;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXMasonryPane;
import controller.post.PostListController;
import controller.post.PostListMode;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import model.Post;
import view.View;
import view.dialog.Dialogs;
import view.generalPages.MainPageView;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public
class PostsListView extends View {

    PostListController postListController;
    @FXML
    private JFXButton reload_button;
    @FXML
    private FontAwesomeIcon reload_button_icon;
    @FXML
    private
    JFXMasonryPane posts_masonery;
    @FXML
    private StackPane stackpane;
    @FXML
    private ScrollPane scrollpane;

    @FXML
    void OnReloadButtonAction(ActionEvent event) {
        refresh();
    }

    public
    void initializeValues(PostListMode mode, Integer id) {
        postListController = new PostListController(mode, id);
        refresh();


    }

    public
    List <Node> getPostItems(List <Post> posts) {
        LinkedList <PostItemView> postItemsViews = new LinkedList <>();
        LinkedList <Node> postItems = new LinkedList <>();
        for (Post post : posts) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/post/postItem.fxml"));
                Node node = fxmlLoader.load();
                PostItemView postItemView = fxmlLoader.getController();
                postItemView.initializeValues(post);
                postItems.add(node);
                postItemsViews.add(postItemView);
                MainPageView.universalViews.add(postItemView);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return postItems;
    }

    public
    void refresh() {
        postListController.updateList(new Runnable() {
            @Override
            public
            void run() {
                List <Node> postItems = getPostItems(postListController.getPosts());
               posts_masonery.getChildren().clear();
                if (postItems.isEmpty()) {
                    stackpane.getChildren().clear();
                    stackpane.getChildren().add(Dialogs.nothingPage("No Posts!", "BOOK"));
                }

                posts_masonery.getChildren().addAll(postItems);

            }
        });

    }
}
