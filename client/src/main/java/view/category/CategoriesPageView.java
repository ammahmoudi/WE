package view.category;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.events.JFXDialogEvent;
import controller.category.CategoriesPageController;
import controller.messaging.MessagingPageController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import model.Category;
import model.LoggedInUser;
import model.MessagingEnvironment;
import view.*;
import view.dialog.Dialogs;
import view.generalPages.MainPageView;
import view.messaging.MessagingEnvironmentItemView;
import view.messaging.MessagingEnvironmentsListView;
import view.messaging.SimpleMessagingEnvironmentItemView;
import view.user.UsersListView;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public
class CategoriesPageView extends View implements Initializable {

    MessagingPageController messagingPageController;

    @FXML
    private ScrollPane scrollpane;
    @FXML
    private BorderPane border_pane;
    @FXML
    private JFXButton create_button;
    @FXML
    void OnCreateButtonAction(ActionEvent event) {
  LoggedInUser.updateFollowings(new Runnable() {
      @Override
      public
      void run() {
          Dialogs.NewCategoryDialog(MainPageView.universalCenterStackPane,LoggedInUser.getUser().getFollowings()).setOnDialogClosed(new EventHandler <JFXDialogEvent>() {
              @Override
              public
              void handle(JFXDialogEvent event) {
                  LoggedInUser.updateCategories(new Runnable() {
                      @Override
                      public
                      void run() {
                          border_pane.setCenter(getCategoriesListContent(categoriesPageController.getCategories()));

                      }
                  });
              }
          });
      }
  });

    }


    CategoriesPageController categoriesPageController;

    @FXML
    void OnUserInfoBarClicked(MouseEvent event) {

    }

    public
    void initializeValues() {
        categoriesPageController=new CategoriesPageController(LoggedInUser.getUser());

    if(!MainPageView.universalTabPane.getTabs().isEmpty()){
        Tab thisTab = MainPageView.universalTabPane.getSelectionModel().getSelectedItem();

        thisTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                refresh();
            }

        });
    }
            LoggedInUser.updateCategories(new Runnable() {
                @Override
                public
                void run() {
                    border_pane.setCenter(getCategoriesListContent(categoriesPageController.getCategories()));
                }
            });


        }


    public
    void refresh() {
       // initializeValues(messagingPageController.getMessagingEnvironment());
    }


    Node getCategoriesListContent(List <Category> categories) {

        LinkedList <CategoryItemView> categoryItemViews = new LinkedList <>();
        LinkedList <Node> categoryItems = new LinkedList <>();
        for (Category category:categories) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/category/CategoryItem.fxml"));
                Node node = fxmlLoader.load();

             CategoryItemView categoryItemView=fxmlLoader.getController();
             categoryItemView.initializeValues(category);
             categoryItemView.getEdit_button().setOnAction(new EventHandler <ActionEvent>() {
                 @Override
                 public
                 void handle(ActionEvent event) {
                     Dialogs.CategoryProfileDialog(MainPageView.universalCenterStackPane,categoryItemView.categoryItemController.getCategory()).setOnDialogClosed(new EventHandler <JFXDialogEvent>() {
                         @Override
                         public
                         void handle(JFXDialogEvent event) {
                             LoggedInUser.updateCategories(new Runnable() {
                                 @Override
                                 public
                                 void run() {
                                     border_pane.setCenter(getCategoriesListContent(categoriesPageController.getCategories()));
                                 }
                             });
                         }
                     });

                 }
             });
             categoryItems.add(node);
              categoryItemViews.add(categoryItemView);
                MainPageView.universalViews.add(categoryItemView);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmls/category/CategoriesList.fxml"));
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
      CategoriesListView categoriesListView=loader.getController();
        categoriesListView.initializeValues(categoryItems,categoryItemViews);
        MainPageView.universalViews.add(categoriesListView);
        return node;
    }

    Node getSimpleMessagingEnvironmentsListContent(List <MessagingEnvironment> messages) {

        LinkedList <SimpleMessagingEnvironmentItemView> messageItemViews = new LinkedList <>();
        LinkedList <Node> messageItems = new LinkedList <>();
        for (MessagingEnvironment message : messages) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/messaging/SimpleMessagingEnvironmentItem.fxml"));
                Node node = fxmlLoader.load();

                SimpleMessagingEnvironmentItemView simpleMessagingEnvironmentItemView = fxmlLoader.getController();
                simpleMessagingEnvironmentItemView.initializeValues(message);
                simpleMessagingEnvironmentItemView.getToggle_button().setOnAction(new EventHandler <ActionEvent>() {
                    @Override
                    public
                    void handle(ActionEvent event) {
                        if (!simpleMessagingEnvironmentItemView.getToggle_button().isSelected()) {
                            messagingPageController.getForwardingList().remove(message);
                        } else {
                            messagingPageController.getForwardingList().add(message);
                        }
                    }
                });
                messageItems.add(node);
                messageItemViews.add(simpleMessagingEnvironmentItemView);
                MainPageView.universalViews.add(simpleMessagingEnvironmentItemView);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmls/user/UsersList.fxml"));
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UsersListView usersListView = loader.getController();
        usersListView.initializeValues(messageItems);
        MainPageView.universalViews.add(usersListView);
        return node;
    }

    @Override
    public
    void initialize(URL location, ResourceBundle resources) {


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
}

