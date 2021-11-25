package view.generalPages;

import com.jfoenix.controls.JFXBadge;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import controller.generalPages.ExplorerController;
import controller.post.PostListMode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import model.LoggedInUser;
import model.Post;
import model.User;
import view.View;
import view.post.PostItemView;
import view.post.PostsListView;
import view.user.UserItemView;
import view.user.UsersListView;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class ExplorerView extends View implements Initializable {

    @FXML
    private JFXTabPane tab_pane;

    @FXML
    private Tab posts_tab;

    @FXML
    private Tab search_tab;


    @FXML
    private BorderPane search_borderpane;

    @FXML
    private JFXTextField search_text;

//    @FXML
//    void OnSearchTextChanged(InputMethodEvent event) {
//        search_borderpane.setCenter(getUsersListContent(getUsersListItems(explorerController.search(search_text.getText()))));
//
//    }
ExplorerController explorerController;

    @Override
    public
    void initialize(URL location, ResourceBundle resources) {
        explorerController =new ExplorerController();
        posts_tab.setContent(getExplorerPostListContent());
        search_text.textProperty().addListener((observable, oldValue, newValue) -> {
            search_borderpane.setCenter(getUsersListContent(getUsersListItems(explorerController.search(newValue))));
        });

    }


    public
    Node getUsersListContent(LinkedList <Node> list) {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmls/user/UsersList.fxml"));
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UsersListView usersListView = loader.getController();
        usersListView.initializeValues(list);
        return node;
    }


    LinkedList <Node> getUsersListItems(LinkedList<User> users) {

        LinkedList <Node> UsersItems = new LinkedList <>();
        for (User user : users) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/user/UserItem.fxml"));
                Node node = fxmlLoader.load();
                UserItemView userItemView = fxmlLoader.getController();
                userItemView.initializeValues(user);
                UsersItems.add(node);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return UsersItems;
    }


    public
    Node getExplorerPostListContent() {

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmls/post/PostsList.fxml"));
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PostsListView postsListView = loader.getController();
        postsListView.initializeValues(PostListMode.EXPLORER, LoggedInUser.getUser().getId());
        MainPageView.universalViews.add(postsListView);
        return node;
    }

}
