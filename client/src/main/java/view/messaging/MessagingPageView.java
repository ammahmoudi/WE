package view.messaging;

import com.github.kevinsawicki.timeago.TimeAgo;
import com.jfoenix.controls.*;
import com.jfoenix.controls.events.JFXDialogEvent;
import controller.Actions;
import controller.ImageController;
import controller.messaging.MessagingPageController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import event.messaging.NewMessageEvent;
import event.messaging.NewScheduledMessageEvent;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.LoggedInUser;
import model.Message;
import model.MessagingEnvironment;
import view.View;
import view.dialog.Dialogs;
import view.generalPages.MainPageView;
import view.user.UserProfileView;
import view.user.UsersListView;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public
class MessagingPageView extends View implements Initializable {

    MessagingPageController messagingPageController;
    byte[] byteArrayedImage = null;
    LocalDateTime sendingDate = null;
    JFXListView <Label> list = new JFXListView <>();
    JFXPopup popup = new JFXPopup();
    Label schedule_message_label = new Label();
    Tab thisTab;
    FontAwesomeIcon schedule_menu_icon = new FontAwesomeIcon();
    @FXML
    private HBox bottom_hbox;
    @FXML
    private ScrollPane scrollpane;
    @FXML
    private BorderPane border_pane;
    @FXML
    private VBox user_detail_vbox;
    @FXML
    private Label user_real_name_label;
    @FXML
    private Label last_seen_label;
    @FXML
    private JFXButton more_options_button;
    @FXML
    private JFXButton image_button;
    @FXML
    private JFXTextArea send_message_text;
    @FXML
    private JFXButton send_button;
    private Message editingMessage;
    @FXML
    private FontAwesomeIcon image_icon;
    @FXML
    private FontAwesomeIcon send_icon;
    private
    Timeline timeline = null;

    @FXML
    void OnImageButtonAction(ActionEvent event) {
        if (editingMessage != null) {
            editingMessage = null;
            send_icon.setIconName("SEND");
            image_icon.setIconName("IMAGE");
            send_message_text.setText("");


        } else {
            if (byteArrayedImage != null) {
                byteArrayedImage = null;

                image_button.setStyle(" -fx-background-color: #fc9c0c");
                image_icon.setIconName("IMAGE");
            } else {

                byteArrayedImage = ImageController.pickImage();
                if (byteArrayedImage != null) {

                    image_icon.setIconName("CLOSE");


                }
            }
        }
    }

    @FXML
    void OnMoreOptionsButtonAction(ActionEvent event) {
        if (!LoggedInUser.getOffline()) {
            if (messagingPageController.getMessagingEnvironment().isGroup()) {
                Dialogs.GroupProfileDialog(MainPageView.universalCenterStackPane, messagingPageController.getMessagingEnvironment()).setOnDialogClosed(new EventHandler <JFXDialogEvent>() {
                    @Override
                    public
                    void handle(JFXDialogEvent event) {
                        messagingPageController.setMessagingEnvironmentChanged(true);
                        messagingPageController.setUsersChanged(true);

                    }
                });
            } else {
                FXMLLoader userProfileLoader = new FXMLLoader(getClass().getResource("/fxmls/user/UserProfile.fxml"));
                try {
                    ScrollPane userProfile = userProfileLoader.load();
                    UserProfileView userProfileView = userProfileLoader.getController();
                    userProfileView.initializeValues(messagingPageController.getTakerUser());
                    MainPageView.newTab(userProfile, messagingPageController.getTakerUser().getFullName());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @FXML
    void OnSendButtonAction(ActionEvent event) {
        if (!send_message_text.getText().isBlank()) {
            if (editingMessage != null) {
                messagingPageController.editMessage(editingMessage, send_message_text.getText());
                editingMessage = null;
                send_icon.setIconName("SEND");
                image_icon.setIconName("IMAGE");
            } else {
                if (sendingDate == null) {


                    NewMessageEvent newMessageEvent = new NewMessageEvent(LoggedInUser.getUser().getId(), messagingPageController.getMessagingEnvironment().getId(), send_message_text.getText(), null, byteArrayedImage, null);

                    if (LoggedInUser.getOffline()) {
                        messagingPageController.newMessage(newMessageEvent, new Runnable() {
                            @Override
                            public
                            void run() {
                                updateMessagesList();
                                MainPageView.setLeftStackPaneContent(getMessagingEnvironmentsListContent(LoggedInUser.getUser().getMessagingEnvironments()));
                            }
                        });
                    } else {
                        messagingPageController.newMessage(newMessageEvent, null);
                    }
                } else {
                    NewScheduledMessageEvent newScheduledMessageEvent = new NewScheduledMessageEvent(LoggedInUser.getUser().getId(), messagingPageController.getMessagingEnvironment().getId(), send_message_text.getText(), null, byteArrayedImage, null, sendingDate);
                    messagingPageController.newScheduledMessage(newScheduledMessageEvent, null);
                    sendingDate=null;

                    schedule_message_label.setText("Schedule message");
                    schedule_menu_icon.setIconName("CALENDAR_ALT");
                }

            }
            send_icon.setIconName("SEND");
            image_icon.setIconName("IMAGE");
            send_message_text.setText("");
            byteArrayedImage = null;
        }


    }

    @FXML
    void onMessageTextKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            send_button.fire();
        }

    }

    @FXML
    void OnUserInfoBarClicked(MouseEvent event) {

    }

    public
    void initializeValues(MessagingEnvironment messagingEnvironment) {

        thisTab = MainPageView.universalTabPane.getSelectionModel().getSelectedItem();
        thisTab.setOnClosed(new EventHandler <Event>() {
            @Override
            public
            void handle(Event event) {
                Actions.updaters.remove(messagingPageController.getMessagesUpdater());
                Actions.updaters.remove(messagingPageController.getUsersUpdater());
                Actions.updaters.remove(messagingPageController.getMessagingEnvironmentUpdater());
            }
        });
        if (thisTab != null) {

            thisTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    if (!LoggedInUser.getOffline()) {
                        if (messagingPageController.getMessagesUpdater() != null)
                            messagingPageController.getMessagesUpdater().restart();

                        if (messagingPageController.getUsersUpdater() != null)
                            messagingPageController.getUsersUpdater().restart();

                        if (messagingPageController.getMessagingEnvironmentUpdater() != null)
                            messagingPageController.getMessagingEnvironmentUpdater().restart();
                    }
                }
                if (oldValue) {
                    if (!LoggedInUser.getOffline()) {
                        if (messagingPageController.getMessagesUpdater() != null)
                            Platform.runLater(new Runnable() {
                                @Override
                                public
                                void run() {
                                    messagingPageController.getMessagesUpdater().cancel();
                                }
                            });

                        if (messagingPageController.getUsersUpdater() != null)
                            Platform.runLater(new Runnable() {
                                @Override
                                public
                                void run() {
                                    messagingPageController.getUsersUpdater().cancel();
                                }
                            });
                        if (messagingPageController.getMessagingEnvironmentUpdater() != null)
                            Platform.runLater(new Runnable() {
                                @Override
                                public
                                void run() {
                                    messagingPageController.getMessagingEnvironmentUpdater().cancel();
                                }
                            });
                    }
                }
            });
        }
        if (messagingEnvironment == null) {
            MainPageView.universalTabPane.getTabs().remove(thisTab);
        } else {
            schedule_message_label.setGraphic(schedule_menu_icon);

            schedule_message_label.setFont(Font.font(14));

            schedule_message_label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            list.setPrefWidth(Region.USE_COMPUTED_SIZE);
            list.setStyle("-fx-background-radius: 10px;-fx-background-color: white;");
            list.setPadding(new Insets(4, 4, 4, 4));
            list.getItems().add(schedule_message_label);
            // popup.autoFixProperty().setValue(true);
            popup.autoHideProperty().setValue(true);
            popup.setPopupContent(list);
            popup.getPopupContent().setStyle("-fx-background-color: transparent;-fx-background-radius: 10px;");
            popup.setStyle("-fx-background-radius: 10px");
            if(sendingDate==null){
                schedule_message_label.setText("Schedule message");
                schedule_menu_icon.setIconName("CALENDAR_ALT");
            }else{
                schedule_message_label.setText(" message");
                schedule_menu_icon.setIconName("SEND");
            }
            for (Node node : list.getItems()) {
                node.setStyle("-fx-background-radius:10px");
            }
            schedule_message_label.setOnMouseClicked(event -> {
                if (sendingDate == null) {
                    JFXDialog dateTimePicker = Dialogs.dateTimePickerDialog(MainPageView.universalCenterStackPane, null);
                    dateTimePicker.setOnDialogClosed(new EventHandler <JFXDialogEvent>() {
                        @Override
                        public
                        void handle(JFXDialogEvent event) {

                            sendingDate = (LocalDateTime) dateTimePicker.getUserData();
                            if (sendingDate != null) {
                                send_icon.setIconName("MOTORCYCLE");
                                schedule_message_label.setText(" message");
                                schedule_menu_icon.setIconName("SEND");

                            } else {
                                send_icon.setIconName("SEND");
                                schedule_message_label.setText("Schedule message");
                                schedule_menu_icon.setIconName("CALENDAR_ALT");
                            }

                        }
                    });

                    //     forward_button.fire();

                } else {
                    sendingDate = null;

                        send_icon.setIconName("SEND");
                        schedule_message_label.setText("Schedule message");
                        schedule_menu_icon.setIconName("CALENDAR_ALT");

                }
                popup.hide();
            });

            if (!LoggedInUser.getOffline())
                send_button.setOnContextMenuRequested(event -> popup.show(bottom_hbox, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT, 0, -bottom_hbox.getHeight()+5));


            messagingPageController = new MessagingPageController(messagingEnvironment);

            if (messagingPageController.getMessagingEnvironment().isGroup()) {
                user_real_name_label.setText(messagingEnvironment.getName());
                if (messagingPageController.getMessagingEnvironment().getUsers() != null)
                    last_seen_label.setText(messagingPageController.getMessagingEnvironment().getUsers().size() + " members");
            } else {

                user_real_name_label.setText(messagingPageController.getTakerUser().getFullName());
                last_seen_label.setText(messagingPageController.getTakerUser().getLastSeenString());
            }

            if (messagingPageController.getMessagesUpdater() == null) {
                messagingPageController.updateMessages(new Runnable() {
                    @Override
                    public
                    void run() {
                        refresh();

                    }
                });
            }
            if (messagingPageController.getUsersUpdater() == null) {
                messagingPageController.updateUsers(new Runnable() {
                    @Override
                    public
                    void run() {
                        refreshUsers();
                    }
                });
            }
            if (messagingPageController.getMessagingEnvironmentUpdater() == null) {
                messagingPageController.updateMessagingEnvironment(new Runnable() {
                    @Override
                    public
                    void run() {
                        refreshMessagingEnvironment();
                    }
                });
            }

        }

        updateOfflineMode();
    }

    public
    void updateOfflineMode() {
        if (!LoggedInUser.getOffline()) {
            if (messagingPageController.getMessagesUpdater() != null)
                messagingPageController.getMessagesUpdater().restart();
            more_options_button.setDisable(false);

            if (messagingPageController.getUsersUpdater() != null)
                messagingPageController.getUsersUpdater().restart();

            if (messagingPageController.getMessagingEnvironmentUpdater() != null)
                messagingPageController.getMessagingEnvironmentUpdater().restart();

        } else {
            updateMessagesList();
            more_options_button.setDisable(true);
        }
    }

    public
    Node getMessageListContent(List <Message> messages) {

        LinkedList <MessageItemView> messageItemViews = new LinkedList <>();
        LinkedList <Node> messageItems = new LinkedList <>();
        for (Message message : messages) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/messaging/MessageItem.fxml"));
                Node node = fxmlLoader.load();
                MessageItemView messageItemView = fxmlLoader.getController();
                if (!LoggedInUser.getOffline()) {
                    messageItemView.getMenu_delete().setOnMouseClicked(event -> {
                        //    LoggedInUser.getMessagingEnvironmentsUpdater().cancel();
                        Dialogs.confirmDialog(MainPageView.universalCenterStackPane, "Delete Message", "Are you sure to delete this message?",
                                event1 -> {
                                    messageItemView.messageItemController.remove();

                                }).setOnDialogClosed(event1 -> {
                            editingMessage = null;
                            refresh();

                        });
                        messageItemView.getPopup().hide();
                    });
                    messageItemView.getMenu_edit().setOnMouseClicked(
                            event -> {
                                send_icon.setIconName("PENCIL");
                                image_icon.setIconName("CLOSE");
                                send_message_text.setText(messageItemView.messageItemController.getMessage().getText());
                                editingMessage = messageItemView.messageItemController.getMessage();
                                messageItemView.getPopup().hide();

                            });
                    messageItemView.getForward_button().setOnAction(new EventHandler <ActionEvent>() {
                        @Override
                        public
                        void handle(ActionEvent event) {


                            JFXDialog dialog = Dialogs.SimpleDialog(MainPageView.universalCenterStackPane, "Forward", getSimpleMessagingEnvironmentsListContent(messagingPageController.getMessagingEnvironments()));
                            dialog.setOnDialogOpened(new EventHandler <JFXDialogEvent>() {
                                @Override
                                public
                                void handle(JFXDialogEvent event) {
                                    messagingPageController.getForwardingList().clear();
                                }
                            });
                            dialog.setOnDialogClosed(new EventHandler <JFXDialogEvent>() {
                                @Override
                                public
                                void handle(JFXDialogEvent event) {
                                    for (MessagingEnvironment messagingEnvironment : messagingPageController.getForwardingList()) {
                                        NewMessageEvent newMessageEvent;
                                        if (messageItemView.messageItemController.getMessage().isForwarded()) {

                                            newMessageEvent = new NewMessageEvent(LoggedInUser.getUser().getId(), messagingEnvironment.getId(), messageItemView.messageItemController.getMessage().getOriginalMessage().getText(), messageItemView.messageItemController.getMessage().getOriginalMessage().getId(), messageItemView.messageItemController.getMessage().getOriginalMessage().getImage(), messageItemView.messageItemController.getMessage().getOriginalMessage().getPost().getId());

                                        } else {
                                            if (messageItemView.messageItemController.getMessage().getPost() != null)
                                                newMessageEvent = new NewMessageEvent(LoggedInUser.getUser().getId(), messagingEnvironment.getId(), messageItemView.messageItemController.getMessage().getText(), messageItemView.messageItemController.getMessage().getId(), messageItemView.messageItemController.getMessage().getImage(), messageItemView.messageItemController.getMessage().getPost().getId());
                                            else
                                                newMessageEvent = new NewMessageEvent(LoggedInUser.getUser().getId(), messagingEnvironment.getId(), messageItemView.messageItemController.getMessage().getText(), messageItemView.messageItemController.getMessage().getId(), messageItemView.messageItemController.getMessage().getImage(), null);

                                        }
                                        messagingPageController.newMessage(newMessageEvent, null);

                                    }

                                }
                            });

                        }
                    });
                }
                messageItemView.initializeValues(message);


                messageItems.add(node);

                messageItemViews.add(messageItemView);
                MainPageView.universalViews.add(messageItemView);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmls/messaging/MessagesList.fxml"));
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MessagesListView messagesListView = loader.getController();
        messagesListView.initializeValues(messageItems, messageItemViews);
        MainPageView.universalViews.add(messagesListView);
        return node;
    }

    public synchronized
    void updateMessagesList() {
        //   List <Message> list = messagingPageController.getMessagingEnvironment().getMessages();
        border_pane.setCenter(getMessageListContent(messagingPageController.getMessagingEnvironment().getMessages()));
    }

    public
    void refresh() {
        if (messagingPageController.getMessagingEnvironment() == null) {
            if (messagingPageController.getMessagesUpdater() != null)
                messagingPageController.getMessagesUpdater().cancel();

            if (messagingPageController.getUsersUpdater() != null)
                messagingPageController.getUsersUpdater().cancel();

            if (messagingPageController.getMessagingEnvironmentUpdater() != null)
                messagingPageController.getMessagingEnvironmentUpdater().cancel();
            MainPageView.universalTabPane.getTabs().remove(thisTab);
        }
        if (messagingPageController.isMessagesChanged()) {
            messagingPageController.saveOffline();
            updateMessagesList();
            messagingPageController.setMessagesChanged(false);
            System.out.println("changed chat");
        }

    }

    public
    void refreshMessagingEnvironment() {
        if (messagingPageController.getMessagingEnvironment() == null) {
            if (messagingPageController.getMessagesUpdater() != null)
                messagingPageController.getMessagesUpdater().cancel();

            if (messagingPageController.getUsersUpdater() != null)
                messagingPageController.getUsersUpdater().cancel();

            if (messagingPageController.getMessagingEnvironmentUpdater() != null)
                messagingPageController.getMessagingEnvironmentUpdater().cancel();
            MainPageView.universalTabPane.getTabs().remove(thisTab);
        }
        if (messagingPageController.isMessagingEnvironmentChanged()) {
            if (messagingPageController.getMessagingEnvironment().isGroup()) {
                user_real_name_label.setText(messagingPageController.getMessagingEnvironment().getName());
                messagingPageController.setMessagingEnvironmentChanged(false);
                System.out.println("changed name");
            }

        }

    }

    public
    void refreshUsers() {
        if (messagingPageController.getMessagingEnvironment() == null) {
            if (messagingPageController.getMessagesUpdater() != null)
                messagingPageController.getMessagesUpdater().cancel();

            if (messagingPageController.getUsersUpdater() != null)
                messagingPageController.getUsersUpdater().cancel();

            if (messagingPageController.getMessagingEnvironmentUpdater() != null)
                messagingPageController.getMessagingEnvironmentUpdater().cancel();
            MainPageView.universalTabPane.getTabs().remove(thisTab);
        }
        if (messagingPageController.getMessagingEnvironment() != null && messagingPageController.getMessagingEnvironment().isGroup()) {
            if (messagingPageController.isUsersChanged()) {
                last_seen_label.setText(messagingPageController.getMessagingEnvironment().getUsers().size() + " members");
                messagingPageController.setUsersChanged(false);
                System.out.println("changed users");
            }
        } else {
            TimeAgo time = new TimeAgo();
            String s;
            if (messagingPageController.getTakerUser().getLastSeen() != null) {
                ZonedDateTime zonedDateTime = ZonedDateTime.of(messagingPageController.getTakerUser().getLastSeen(), ZoneId.systemDefault());

                long messagemilles = zonedDateTime.toInstant().toEpochMilli();
                if (System.currentTimeMillis() - messagemilles < 3000) {
                    s = "Online";
                } else {
                    s = time.timeAgo(messagemilles);

                }
            } else {
                s = "Last seen recently";
            }
            last_seen_label.setText(s);
            user_real_name_label.setText(messagingPageController.getTakerUser().getFullName());

            //  System.out.println("changed name");
        }

    }

    Node getGroupProfileContent() {


        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxmls/messaging/GroupProfile.fxml"));
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GroupProfileView groupProfileView = loader.getController();
        groupProfileView.initializeValues(messagingPageController.getMessagingEnvironment());

        MainPageView.universalViews.add(groupProfileView);
        return node;
    }

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
}

