package view.dialog;

import com.jfoenix.controls.JFXDialog;
import controller.post.PostItemController;
import controller.user.UserProfileController;
import event.post.NewPostEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import model.Category;
import model.MessagingEnvironment;
import model.User;
import view.category.CategoryMemberChoserView;
import view.category.CategoryProfileView;
import view.category.NewCategoryView;
import view.category.NewMessageView;
import view.generalPages.NothingPageView;
import view.generalPages.SettingsView;
import view.messaging.AddMemberView;
import view.messaging.GroupProfileView;
import view.messaging.NewGroupView;
import view.post.NewPostView;
import view.user.UserProfileOptionsView;

import java.io.IOException;
import java.util.List;

public
class Dialogs {
    static public
    JFXDialog newPostDialog(StackPane parrentStackPane, PostItemController postItemController, Runnable runnable) {
        FXMLLoader fxmlLoader = new FXMLLoader(Dialogs.class.getResource("/fxmls/post/NewPost.fxml"));
        JFXDialog newCommentDialog = null;
        try {

            newCommentDialog = new JFXDialog(parrentStackPane, fxmlLoader.load(), JFXDialog.DialogTransition.CENTER, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        newCommentDialog.show();
        NewPostView newPostView = fxmlLoader.getController();
        JFXDialog finalNewCommentDialog = newCommentDialog;
        newPostView.getClose_button().setOnAction(new EventHandler <ActionEvent>() {
            @Override
            public
            void handle(ActionEvent event) {
                finalNewCommentDialog.close();
            }

        });
        newPostView.getPost_button().setOnAction(new EventHandler <ActionEvent>() {
            @Override
            public
            void handle(ActionEvent event) {
                NewPostEvent newPostEvent;
                if (postItemController.getPost() != null) {
                    if (postItemController.getPost().notOriginalPost()) {
                        newPostEvent = new NewPostEvent(newPostView.getPost_text_input().getText(), null, postItemController.getPost().getOriginalPost());
                    } else {
                        newPostEvent = new NewPostEvent(newPostView.getPost_text_input().getText(), newPostView.getByteArrayedImage(), postItemController.getPost());
                    }
                } else {

                    newPostEvent = new NewPostEvent(newPostView.getPost_text_input().getText(), newPostView.getByteArrayedImage());
                }

                postItemController.NewPost(newPostEvent, runnable);
                newPostView.getClose_button().fire();
            }
        });
        return newCommentDialog;
    }

    static public
    JFXDialog newMessageDialog(StackPane parrentStackPane, Category category) {
        FXMLLoader fxmlLoader = new FXMLLoader(Dialogs.class.getResource("/fxmls/category/NewMessage.fxml"));
        JFXDialog dialog = null;
        try {

            dialog = new JFXDialog(parrentStackPane, fxmlLoader.load(), JFXDialog.DialogTransition.CENTER, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.show();
        NewMessageView newMessageView = fxmlLoader.getController();
        newMessageView.initializeValues(category);
        JFXDialog finaldialog = dialog;
        newMessageView.getClose_button().setOnAction(new EventHandler <ActionEvent>() {
            @Override
            public
            void handle(ActionEvent event) {
                finaldialog.close();
            }

        });

        return finaldialog;
    }

    static public
    JFXDialog userProfileOptions(StackPane parrentStackPane, UserProfileController userProfileController) {
        FXMLLoader fxmlLoader = new FXMLLoader(Dialogs.class.getResource("/fxmls/user/UserProfileOptions.fxml"));
        JFXDialog dialog = null;
        try {

            dialog = new JFXDialog(parrentStackPane, fxmlLoader.load(), JFXDialog.DialogTransition.TOP, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.show();
        UserProfileOptionsView userProfileOptionsView = fxmlLoader.getController();
        JFXDialog finalDialog = dialog;
        userProfileOptionsView.getClose_button().setOnAction(new EventHandler <ActionEvent>() {
            @Override
            public
            void handle(ActionEvent event) {
                finalDialog.close();

            }

        });

        userProfileOptionsView.initializeValues(userProfileController);


        return finalDialog;
    }


    public static
    Node nothingPage(String text, String iconName) {
        Node nothingPage = null;
        FXMLLoader fxmlLoader = new FXMLLoader(Dialogs.class.getResource("/fxmls/generalPages/NothingPage.fxml"));
        try {

            nothingPage = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        NothingPageView nothingPageView = fxmlLoader.getController();
        nothingPageView.initializeValues(text, iconName);
        return nothingPage;


    }

    static public
    JFXDialog confirmDialog(StackPane parrentStackPane, String title, String text, EventHandler eventHandler) {
        FXMLLoader fxmlLoader = new FXMLLoader(Dialogs.class.getResource("/fxmls/dialog/ConfirmView.fxml"));
        JFXDialog dialog = null;
        try {

            dialog = new JFXDialog(parrentStackPane, fxmlLoader.load(), JFXDialog.DialogTransition.TOP, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.show();
        ConfirmView confirmView = fxmlLoader.getController();
        JFXDialog finalDialog = dialog;
        confirmView.getClose_button().setOnAction(new EventHandler <ActionEvent>() {
            @Override
            public
            void handle(ActionEvent event) {
                finalDialog.close();

            }

        });
        confirmView.getYes_button().setOnAction(new EventHandler <ActionEvent>() {
            @Override
            public
            void handle(ActionEvent event) {
                eventHandler.handle(event);
                finalDialog.close();
            }
        });
        confirmView.initializeValues(title, text);


        return finalDialog;
    }

    static public
    JFXDialog dateTimePickerDialog(StackPane parrentStackPane,  EventHandler eventHandler) {
        FXMLLoader fxmlLoader = new FXMLLoader(Dialogs.class.getResource("/fxmls/dialog/DateTimePickerDialog.fxml"));
        JFXDialog dialog = null;
        try {

            dialog = new JFXDialog(parrentStackPane, fxmlLoader.load(), JFXDialog.DialogTransition.TOP, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.show();
       DateTimePickerView dateTimePickerView = fxmlLoader.getController();
        JFXDialog finalDialog = dialog;
      dateTimePickerView.getClose_button().setOnAction(new EventHandler <ActionEvent>() {
            @Override
            public
            void handle(ActionEvent event) {
                finalDialog.setUserData(dateTimePickerView.sendingDate);
                finalDialog.close();

            }

        });
//        dateTimePickerView.getYes_button().setOnAction(new EventHandler <ActionEvent>() {
//            @Override
//            public
//            void handle(ActionEvent event) {
//              //  eventHandler.handle(event);
//                finalDialog.close();
//            }
//        });
        //confirmView.initializeValues(title, text);


        return finalDialog;
    }

    static public
    JFXDialog SimpleDialog(StackPane parrentStackPane, String title, Node content) {
        FXMLLoader fxmlLoader = new FXMLLoader(Dialogs.class.getResource("/fxmls/dialog/SimpleDialog.fxml"));
        JFXDialog dialog = null;
        try {

            dialog = new JFXDialog(parrentStackPane, fxmlLoader.load(), JFXDialog.DialogTransition.RIGHT, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.show();
        SimpleDialogView simpleDialogView = fxmlLoader.getController();
        JFXDialog finalDialog = dialog;
        simpleDialogView.getClose_button().setOnAction(new EventHandler <ActionEvent>() {
            @Override
            public
            void handle(ActionEvent event) {
                finalDialog.close();

            }

        });

        simpleDialogView.initializeValues(title, content);


        return finalDialog;
    }

    static public
    JFXDialog GroupProfileDialog(StackPane parrentStackPane, MessagingEnvironment messagingEnvironment) {
        FXMLLoader fxmlLoader = new FXMLLoader(Dialogs.class.getResource("/fxmls/messaging/GroupProfile.fxml"));
        JFXDialog dialog = null;
        try {
            dialog = new JFXDialog(parrentStackPane, fxmlLoader.load(), JFXDialog.DialogTransition.RIGHT, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.show();
        GroupProfileView groupProfileView = fxmlLoader.getController();
        JFXDialog finalDialog = dialog;
        groupProfileView.getClose_button().setOnAction(new EventHandler <ActionEvent>() {
            @Override
            public
            void handle(ActionEvent event) {
                finalDialog.close();

            }

        });

        groupProfileView.initializeValues(messagingEnvironment);


        return finalDialog;
    }

    static public
    JFXDialog CategoryProfileDialog(StackPane parrentStackPane, Category category) {
        FXMLLoader fxmlLoader = new FXMLLoader(Dialogs.class.getResource("/fxmls/category/CategoryProfile.fxml"));
        JFXDialog dialog = null;
        try {
            dialog = new JFXDialog(parrentStackPane, fxmlLoader.load(), JFXDialog.DialogTransition.RIGHT, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.show();
        CategoryProfileView categoryProfileView = fxmlLoader.getController();
        JFXDialog finalDialog = dialog;
        categoryProfileView.getClose_button().setOnAction(new EventHandler <ActionEvent>() {
            @Override
            public
            void handle(ActionEvent event) {
                finalDialog.close();

            }

        });

        categoryProfileView.initializeValues(category);


        return finalDialog;
    }

    static public
    JFXDialog NewGroupDialog(StackPane parrentStackPane, List <User> users) {
        FXMLLoader fxmlLoader = new FXMLLoader(Dialogs.class.getResource("/fxmls/messaging/NewGroup.fxml"));
        JFXDialog dialog = null;
        try {

            dialog = new JFXDialog(parrentStackPane, fxmlLoader.load(), JFXDialog.DialogTransition.CENTER, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.show();
        NewGroupView newGroupView = fxmlLoader.getController();
        JFXDialog finalDialog = dialog;
        newGroupView.getClose_button().setOnAction(new EventHandler <ActionEvent>() {
            @Override
            public
            void handle(ActionEvent event) {
                finalDialog.close();

            }

        });

        newGroupView.initializeValues(users);


        return finalDialog;
    }

    static public
    JFXDialog NewCategoryDialog(StackPane parrentStackPane, List <User> users) {
        FXMLLoader fxmlLoader = new FXMLLoader(Dialogs.class.getResource("/fxmls/category/NewCategory.fxml"));
        JFXDialog dialog = null;
        try {

            dialog = new JFXDialog(parrentStackPane, fxmlLoader.load(), JFXDialog.DialogTransition.CENTER, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.show();
        NewCategoryView newCategoryView = fxmlLoader.getController();
        JFXDialog finalDialog = dialog;
        newCategoryView.getClose_button().setOnAction(new EventHandler <ActionEvent>() {
            @Override
            public
            void handle(ActionEvent event) {
                finalDialog.close();

            }

        });

        newCategoryView.initializeValues(users);


        return finalDialog;
    }


    static public
    JFXDialog AddMemberDialog(StackPane parrentStackPane, MessagingEnvironment messagingEnvironment) {
        FXMLLoader fxmlLoader = new FXMLLoader(Dialogs.class.getResource("/fxmls/messaging/AddMember.fxml"));
        JFXDialog dialog = null;
        try {

            dialog = new JFXDialog(parrentStackPane, fxmlLoader.load(), JFXDialog.DialogTransition.CENTER, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.show();
        AddMemberView addMemberView = fxmlLoader.getController();
        JFXDialog finalDialog = dialog;
        addMemberView.getClose_button().setOnAction(new EventHandler <ActionEvent>() {
            @Override
            public
            void handle(ActionEvent event) {
                finalDialog.close();

            }

        });

        addMemberView.initializeValues(messagingEnvironment);


        return finalDialog;
    }


    static public
    JFXDialog CategoryMemeberChooserDialog(StackPane parrentStackPane, Category category) {
        FXMLLoader fxmlLoader = new FXMLLoader(Dialogs.class.getResource("/fxmls/category/CategoryMemberChoser.fxml"));
        JFXDialog dialog = null;
        try {

            dialog = new JFXDialog(parrentStackPane, fxmlLoader.load(), JFXDialog.DialogTransition.CENTER, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.show();
        CategoryMemberChoserView categoryMemberChoserView = fxmlLoader.getController();
        JFXDialog finalDialog = dialog;
        categoryMemberChoserView.getClose_button().setOnAction(new EventHandler <ActionEvent>() {
            @Override
            public
            void handle(ActionEvent event) {
                finalDialog.close();

            }

        });

        categoryMemberChoserView.initializeValues(category);


        return finalDialog;
    }

    static public
    JFXDialog SettingsDialog(StackPane parrentStackPane) {
        FXMLLoader fxmlLoader = new FXMLLoader(Dialogs.class.getResource("/fxmls/generalPages/Settings.fxml"));
        JFXDialog dialog = null;
        try {
            dialog = new JFXDialog(parrentStackPane, fxmlLoader.load(), JFXDialog.DialogTransition.CENTER, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.show();
        SettingsView settingsView = fxmlLoader.getController();
        JFXDialog finalDialog = dialog;
        settingsView.getClose_button().setOnAction(new EventHandler <ActionEvent>() {
            @Override
            public
            void handle(ActionEvent event) {
                finalDialog.close();

            }

        });


        return finalDialog;
    }


}
