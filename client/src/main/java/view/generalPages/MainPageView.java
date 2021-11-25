package view.generalPages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.events.JFXDrawerEvent;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import controller.Actions;
import controller.ServerHandler;
import controller.generalPages.MainPageController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.LoggedInUser;
import model.MessagingEnvironment;
import model.Post;
import model.User;
import view.View;
import view.dialog.Dialogs;
import view.messaging.MessagingEnvironmentItemView;
import view.messaging.MessagingEnvironmentsListView;
import view.post.PostItemView;
import view.post.PostsListView;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public
class MainPageView extends View implements Initializable {


    public static LinkedList <View> universalViews;
    public static StackPane universalCenterStackPane;
    public static TabPane universalTabPane;
    public static JFXDrawer universalSideDrawer;
    public static StackPane getUniversalLeftStackPane;
    public static FontAwesomeIcon universalServerButtonIcon;

public  static MainPageView mainPageView;
    MainPageController mainPageController;
    @FXML
    private AnchorPane center_side;
    @FXML
    private JFXTabPane mainpage_tabpane;
    @FXML
    private AnchorPane left_Side;
    @FXML
    private StackPane left_stackpane;
    @FXML
    private StackPane center_stackpane;
    @FXML
    private JFXDrawer side_drawer;
    @FXML
    private JFXHamburger menu_hamburger;
    @FXML
    private BorderPane general_borderpane;
    @FXML
    private FontAwesomeIcon server_button_icon;
    @FXML
    private JFXButton server_button;

    public static
    void newTab(Node node, String title) {

        Tab tab = new Tab();
        tab.setText(title);
        node.setCache(true);

        tab.setContent(node);
        universalTabPane.getTabs().add(tab);
        SelectionModel <Tab> x = universalTabPane.getSelectionModel();
        x.select(tab);

//        tab.setOnSelectionChanged(new EventHandler <Event>() {
//            @Override
//            public
//            void handle(Event event) {
//                for (View view : universalViews) {
//
//                    //   view.refresh();
//                }
//
//            }
//        });

    }

    public static
    void setLeftStackPaneContent(Node node) {
        getUniversalLeftStackPane.getChildren().clear();
        getUniversalLeftStackPane.getChildren().add(node);
    }

    @FXML
    void OnHumbergerMouseClicked(MouseEvent event) {
        if (side_drawer.isOpened()) {
            side_drawer.close();
        }
        if (side_drawer.isClosed()) {
            side_drawer.open();
        }
    }


    @FXML
    void OnServerButtonAction(ActionEvent event) {
        String result = ServerHandler.reconnectToServer();
        if (result == null) {
            universalServerButtonIcon.setIconName("CHECK_CIRCLE");
            updateOfflineMode();

        } else if (result.equals("Already Connected")) {
            universalServerButtonIcon.setIconName("CHECK_CIRCLE");
            updateOfflineMode();
        } else {
            Label label = new Label(result);
            label.setStyle("-fx-font-size: 16");
            Dialogs.SimpleDialog(center_stackpane, "Error!", label);
            universalServerButtonIcon.setIconName("REPEAT");
            updateOfflineMode();

        }
    }


    @Override
    public
    void initialize(URL location, ResourceBundle resources) {
        System.out.println("main page");
//        System.out.println("at first main"+LoggedInUser.getUser().getMessagingEnvironments().size());
        getUniversalLeftStackPane = left_stackpane;
        universalCenterStackPane = center_stackpane;
        universalServerButtonIcon = server_button_icon;
        universalTabPane = mainpage_tabpane;
        universalViews = new LinkedList <>();
        universalSideDrawer = side_drawer;
        mainPageController = new MainPageController(LoggedInUser.getUser());
        mainPageView=this;

//        newTab(getPostListContent(mainPageController.getAllPosts()), "ALlPosts");
        HamburgerSlideCloseTransition burgerTask = new HamburgerSlideCloseTransition(menu_hamburger);
        burgerTask.setRate(-1);
        FXMLLoader sideMenuLoader = new FXMLLoader(getClass().getResource("/fxmls/generalPages/side_menu.fxml"));
        ScrollPane sideMenu = null;
        SideMenuView sideMenuView = null;
        try {
            sideMenu = sideMenuLoader.load();

            sideMenuView = sideMenuLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        side_drawer.setOnDrawerClosing(new EventHandler <JFXDrawerEvent>() {
            @Override
            public
            void handle(JFXDrawerEvent event) {
                burgerTask.setRate(burgerTask.getRate() * -1);
                burgerTask.play();
            }
        });
        SideMenuView finalSideMenuView = sideMenuView;
        side_drawer.setOnDrawerOpening(new EventHandler <JFXDrawerEvent>() {
            @Override
            public
            void handle(JFXDrawerEvent event) {
                finalSideMenuView.initialize();
                        burgerTask.setRate(burgerTask.getRate() * -1);
                burgerTask.play();
            }
        });
        side_drawer.setSidePane(sideMenu);
        side_drawer.setContent(general_borderpane);
        LoggedInUser.scheduledUpdateMessagingEnvironments(new Runnable() {
            @Override
            public
            void run() {
                updateMessagingEnvironments();
            }
        });
        LoggedInUser.onlineRequest(null);



//LoggedInUser.scheduledUpdateFollowings(null).start();
//LoggedInUser.scheduledUpdateFollowers(null).start();
    //    System.out.println("in main"+LoggedInUser.getUser().getMessagingEnvironments().size());

      updateOfflineMode();
    }

public void updateOfflineMode(){
    System.out.println(LoggedInUser.getOffline());
        if(LoggedInUser.getOffline()){
            Platform.runLater(new Runnable() {
                @Override
                public
                void run() {

                    updateMessagingEnvironments();
                    universalServerButtonIcon.setIconName("REPEAT");
                }
            });

        }
        if (!LoggedInUser.getOffline()){
            Platform.runLater(new Runnable() {
                @Override
                public
                void run() {
                    System.out.println("start online updates");
                    LoggedInUser.setChangedMessagingEnvironments(true);
                    LoggedInUser.getMessagingEnvironmentsUpdater().restart();
                    LoggedInUser.getOnlineUpdater().restart();
                }
            });

            universalServerButtonIcon.setIconName("CHECK_CIRCLE");
        }
}
    public
    void initialize_values(User user, Stage stage) {


    }

    public
    Node getPostListContent(LinkedList <Post> feedPosts) {
        LinkedList <PostItemView> postItemsViews = new LinkedList <>();
        LinkedList <Node> postItems = new LinkedList <>();
        for (Post post : feedPosts) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/post/postItem.fxml"));
                Node node = fxmlLoader.load();
                PostItemView postItemView = fxmlLoader.getController();
                postItemView.initializeValues(post);
                postItems.add(node);
                postItemsViews.add(postItemView);
                universalViews.add(postItemView);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmls/post/PostsList.fxml"));
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PostsListView postsListView = loader.getController();
        //  postsListView.initializeValues(postItems, postItemsViews);
        universalViews.add(postsListView);
        return node;
    }

    public
    Node getMessagingEnvironmentsListContent(List <MessagingEnvironment> messages) {

        LinkedList <MessagingEnvironmentItemView> messageItemViews = new LinkedList <>();
        LinkedList <Node> messageItems = new LinkedList <>();
        for (MessagingEnvironment message : messages) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/messaging/MessagingEnvironmentItem.fxml"));
                Node node = fxmlLoader.load();

                MessagingEnvironmentItemView messagingEnvironmentItemView = fxmlLoader.getController();
                messagingEnvironmentItemView.initializeValues(message);
                messageItems.add(node);
                messageItemViews.add(messagingEnvironmentItemView);
                MainPageView.universalViews.add(messagingEnvironmentItemView);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmls/messaging/MessagingEnvironmentsList.fxml"));
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MessagingEnvironmentsListView messagesListView = loader.getController();
        messagesListView.initializeValues(messageItems, messageItemViews);
        MainPageView.universalViews.add(messagesListView);
        return node;
    }


    public
    void updateMessagingEnvironments() {
     //   System.out.println("start update meesagings view");
        if (LoggedInUser.isChangedMessagingEnvironments()||LoggedInUser.getOffline()) {
        //    System.out.println("yes change");
         //   System.out.println("in uodate messages"+LoggedInUser.getUser().getMessagingEnvironments().size());
if(LoggedInUser.getUser()!=null)            MainPageView.setLeftStackPaneContent(getMessagingEnvironmentsListContent(LoggedInUser.getUser().getMessagingEnvironments()));
         if(!LoggedInUser.getOffline())   LoggedInUser.setChangedMessagingEnvironments(false);
            System.out.println("updated left bar");
        }
        // System.out.println("update");
    }

}
