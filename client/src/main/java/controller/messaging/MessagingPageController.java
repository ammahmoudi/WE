package controller.messaging;

import JSon.JSonController;
import com.fasterxml.jackson.core.type.TypeReference;
import controller.Actions;
import controller.Controller;
import controller.ServerHandler;
import event.messaging.NewMessageEvent;
import event.messaging.NewScheduledMessageEvent;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import model.LoggedInUser;
import model.Message;
import model.MessagingEnvironment;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import request.Request;
import request.RequestType;
import response.Response;
import response.ResponseType;

import java.util.LinkedList;
import java.util.List;

public
class MessagingPageController extends Controller {
    static Logger LOGGER = LogManager.getLogger(MessagingPageController.class.getName());
    MessagingEnvironment messagingEnvironment;
    boolean messagingEnvironmentChanged = true;
    boolean messagesChanged = true;
    boolean usersChanged = true;
    ScheduledService messagesUpdater = null;
    ScheduledService usersUpdater = null;
    ScheduledService messagingEnvironmentUpdater = null;
    LinkedList <MessagingEnvironment> forwardingList = new LinkedList <>();

    public
    MessagingPageController(MessagingEnvironment messagingEnvironment) {
        this.messagingEnvironment = messagingEnvironment;
    }

    public synchronized
    MessagingEnvironment getMessagingEnvironment() {
        if (LoggedInUser.getOffline()) {
            return context.MessagingEnvironmentDB.get(messagingEnvironment.getId());
        }
        //  if(messagingEnvironment.getUsers()==null) System.out.println("null");
        if (messagingEnvironment.getUsers().contains(LoggedInUser.getUser())) {
            // System.out.println("fonded");
            return messagingEnvironment;
        }
        return null;
    }

    public synchronized
    MessagingPageController setMessagingEnvironment(MessagingEnvironment messagingEnvironment) {
        this.messagingEnvironment = messagingEnvironment;
        return this;
    }

    public
    User getTakerUser() {
        if (!messagingEnvironment.isGroup()) {
            for (User user : messagingEnvironment.getUsers()) {
                if (!user.equals(LoggedInUser.getUser())) return user;
            }
            return LoggedInUser.getUser();

        }
        return null;
    }

    public
    void newMessage(NewMessageEvent newMessageEvent, Runnable runnable) {

        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.NEW_MESSAGE, JSonController.objectToStringMapper(newMessageEvent), LoggedInUser.getUser().getToken());
        Service service = Actions.executeOnce(new Runnable() {
            @Override
            public
            void run() {
                if (LoggedInUser.getOffline()) {
                    Message message = new Message(context.Users.get(newMessageEvent.getSender()), context.MessagingEnvironmentDB.get(newMessageEvent.getTaker()), newMessageEvent.getText(), null, newMessageEvent.getImage(), null);
                    message.setServerSeen(null);
                    message.setId(context.Messages.getLastId() + 1);
                    context.Messages.add(message);
                    System.out.println("new offline Messages sent");
                    LOGGER.info(LoggedInUser.getUser().getId()+" sent a new offline message");

                    LoggedInUser.setUser(context.Users.get(LoggedInUser.getUser().getId()));
                } else {
                    Response response = ServerHandler.transmitter(request);

                    if (response.getType() == ResponseType.ACCEPT_NEW_MESSAGE) {
                        System.out.println("new Messages sent");
                        LOGGER.info(LoggedInUser.getUser().getId()+" sent a new message");

                    }
                }
            }
        }, runnable);
        service.start();

    }
    public
    void newScheduledMessage(NewScheduledMessageEvent newScheduledMessageEvent, Runnable runnable) {

        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.NEW_SCHEDULED_MESSAGE, JSonController.objectToStringMapper(newScheduledMessageEvent), LoggedInUser.getUser().getToken());
        Service service = Actions.executeOnce(new Runnable() {
            @Override
            public
            void run() {
                if (LoggedInUser.getOffline()) {

                } else {
                    Response response = ServerHandler.transmitter(request);

                    if (response.getType() == ResponseType.NEW_SCHEDULED_MESSAGE) {
                        System.out.println("new Scheduled Messages sent");
                        LOGGER.info(LoggedInUser.getUser().getId()+" sent a new scheduled message");
                    }
                }
            }
        }, runnable);
        service.start();

    }

    public
    void editMessage(Message message, String text) {
        message.setText(text);
        Request request = new Request(LoggedInUser.getUser().getId(), RequestType.EDIT_MESSAGE, JSonController.objectToStringMapper(message), LoggedInUser.getUser().getToken());
        Service service = Actions.executeOnce(new Runnable() {
            @Override
            public
            void run() {
                Response response = ServerHandler.transmitter(request);

                if (response.getType() == ResponseType.ACCEPT_EDIT_MESSAGE) {
                    System.out.println("message eddited");
                    setMessagesChanged(true);

                }
            }
        }, null);
        service.start();
        LOGGER.info("User " + message.getOwner().getId() + "edited message " + message.getId() + "in messaging Environment " + message.getMessagingEnvironment().getId());

    }

    public
    List <MessagingEnvironment> getMessagingEnvironments() {
        return LoggedInUser.getUser().getMessagingEnvironments();
    }

    public
    LinkedList <MessagingEnvironment> getForwardingList() {
        return forwardingList;
    }


    public
    ScheduledService updateMessagingEnvironment(Runnable runnable) {
        final MessagingEnvironment[] updatedMessagingEnvironment = new MessagingEnvironment[1];
        Runnable task = new Runnable() {
            @Override
            public
            void run() {

                Response response = ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.UPDATE_MESSAGING_ENVIRONMENT, messagingEnvironment.getId().toString(), LoggedInUser.getUser().getToken()));

                if (response.getType() == ResponseType.UPDATED_MESSAGING_ENVIRONMENT) {
                    updatedMessagingEnvironment[0] = JSonController.stringToObjectMapper(response.getData(), MessagingEnvironment.class);

                }


                if (updatedMessagingEnvironment[0] != null && !updatedMessagingEnvironment[0].getName().equals(getMessagingEnvironment().getName())) {
                    setMessagingEnvironmentChanged(true);
                    getMessagingEnvironment().setName(updatedMessagingEnvironment[0].getName());

                }
            }
        };

        messagingEnvironmentUpdater = Actions.actionLopper3(5000, task, runnable);
        return messagingEnvironmentUpdater;
    }

    public
    ScheduledService updateMessages(Runnable runnable) {
        final List <Message>[] updatedMessages = new List[]{new LinkedList <>()};

        Runnable task = new Runnable() {
            @Override
            public
            void run() {
                //  System.out.println("updating messages");

                Response response = ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.UPDATE_MESSAGES, messagingEnvironment.getId().toString(), LoggedInUser.getUser().getToken()));
                //   System.out.println("updated meesages :"+response.getType());
                if (response!=null&&response.getType() == ResponseType.UPDATED_MESSAGES) {
                    updatedMessages[0] = JSonController.stringToObjectMapper(response.getData(), new TypeReference <List <Message>>() {
                    });
                    if (messagingEnvironment.getMessages() == null) {
                        messagingEnvironment.setMessages(updatedMessages[0]);
                    }
                    if (messagesChangeFinder(messagingEnvironment.getMessages(), updatedMessages[0])) {
                  //      System.out.println("find");
                        setMessagesChanged(true);
                        getMessagingEnvironment().setMessages(updatedMessages[0]);


                }
                }


            }
        };
        messagesUpdater = Actions.actionLopper3(1000, task, runnable);
        return messagesUpdater;
    }

    public
    ScheduledService updateUsers(Runnable runnable) {
        final List <User>[] updatedUsers = new List[]{new LinkedList <>()};

        Runnable task = new Runnable() {
            @Override
            public
            void run() {
                //  System.out.println("updating users");


                Response response = ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.UPDATE_USERS, messagingEnvironment.getId().toString(), LoggedInUser.getUser().getToken()));


                if (response!=null&&response.getType() == ResponseType.UPDATED_USERS) {

                    updatedUsers[0] = JSonController.stringToObjectMapper(response.getData(), new TypeReference <List <User>>() {
                    });
                    //   System.out.println(updatedUsers[0].size());
                    if (messagingEnvironment.getUsers() == null)
                        messagingEnvironment.setUsers(updatedUsers[0]);
                    messagingEnvironment.applyTaker(LoggedInUser.getUser());

                }
                if (updatedUsers[0].size() != getMessagingEnvironment().getUsers().size()) {
                    setUsersChanged(true);
                }
                if (!updatedUsers[0].isEmpty())
                    getMessagingEnvironment().setUsers(updatedUsers[0]);
                //    System.out.println(getTakerUser().getFirstName());
                //   System.out.println(getMessagingEnvironment().getUsers().size());
                //   System.out.println(getMessagingEnvironment().getUsers().get(0).getFirstName());
                // System.out.println(getMessagingEnvironment().getUsers().get(1).getFirstName());
                // System.out.println("taker :"+getTakerUser().getFirstName());


            }
        };
        usersUpdater = Actions.actionLopper3(3000, task, runnable);
        return usersUpdater;
    }

public void saveOffline(){
            if (getMessagingEnvironment().isGroup()) {
                if (!getMessagingEnvironment().getOwner().equals(LoggedInUser.getUser())){

                    context.Users.update(getMessagingEnvironment().getOwner());
                }
            }
            for (User user : getMessagingEnvironment().getUsers()) {
                if (!user.equals(LoggedInUser.getUser())) context.Users.update(user);
            }
            for (Message message : getMessagingEnvironment().getMessages()) {
                if (!message.getOwner().equals(LoggedInUser.getUser()))
                    context.Users.update(message.getOwner());
                if (message.isForwarded()) {
                    if (!message.getOriginalMessage().getOwner().equals(LoggedInUser.getUser()))
                        context.Users.update(message.getOriginalMessage().getOwner());
                    if (message.getOriginalMessage().getMessagingEnvironment().isGroup()) {
                        if (!message.getOriginalMessage().getMessagingEnvironment().getOwner().equals(LoggedInUser.getUser()))
                            context.Users.update(message.getOriginalMessage().getMessagingEnvironment().getOwner());

                    }
                    context.MessagingEnvironmentDB.update(message.getOriginalMessage().getMessagingEnvironment());
                    context.Messages.update(message.getOriginalMessage());
                }
                if (message.getPost() != null) {
                    if(message.getPost().notOriginalPost()){
                        if(!message.getPost().getOriginalPost().getOwner().equals(LoggedInUser.getUser()))context.Users.update(message.getPost().getOriginalPost().getOwner());
                        context.Posts.update(message.getPost().getOriginalPost());
                    }
                    context.Posts.update(message.getPost());
                }
            }
            context.MessagingEnvironmentDB.update(getMessagingEnvironment());
            System.out.println("offlinilezed message");

        }

    public
    boolean changeFinder(MessagingEnvironment oldMessagingEnvironment, MessagingEnvironment newMessagingEnvironment) {
        if (oldMessagingEnvironment == null || newMessagingEnvironment == null) {
            // System.out.println("1");
            return true;
        }
        if (oldMessagingEnvironment.getMessages() == null || newMessagingEnvironment.getMessages() == null) {
            // System.out.println("2");
            return true;
        }
        if (oldMessagingEnvironment.getUsers() == null || newMessagingEnvironment.getUsers() == null) {
            //   System.out.println("3");

            return true;
        }
        if (oldMessagingEnvironment.getMessages().size() != newMessagingEnvironment.getMessages().size()) return true;
        if (oldMessagingEnvironment.getUsers().size() != newMessagingEnvironment.getUsers().size()) return true;
        for (int i = 0; i < oldMessagingEnvironment.getMessages().size(); i++) {
            if (!newMessagingEnvironment.getMessages().get(i).getText().equals(oldMessagingEnvironment.getMessages().get(i).getText())) {
                return true;
            }
        }

        return false;
    }


    public
    boolean messagesChangeFinder(List <Message> oldMessages, List <Message> newMessages) {

        if (oldMessages == null || newMessages == null) {
            return true;
        }
        if (oldMessages.size() != newMessages.size()) return true;

        for (int i = newMessages.size() - 1; i >= 0; i--) {
            if (!newMessages.get(i).getId().equals(oldMessages.get(i).getId())) return true;
            if (!newMessages.get(i).getText().equals(oldMessages.get(i).getText())) return true;
            if (newMessages.get(i).isFinalSeen() != (oldMessages.get(i).isFinalSeen())) return true;
            if (newMessages.get(i).getServerSeen() != (oldMessages.get(i).getServerSeen())) return true;

        }

        return false;
    }


    public
    boolean isMessagingEnvironmentChanged() {
        return messagingEnvironmentChanged;
    }

    public
    MessagingPageController setMessagingEnvironmentChanged(boolean messagingEnvironmentChanged) {
        this.messagingEnvironmentChanged = messagingEnvironmentChanged;
        return this;
    }

    public synchronized
    boolean isMessagesChanged() {
        return messagesChanged;
    }

    public synchronized
    MessagingPageController setMessagesChanged(boolean messagesChanged) {
        this.messagesChanged = messagesChanged;
        return this;
    }

    public
    boolean isUsersChanged() {
        return usersChanged;
    }

    public
    MessagingPageController setUsersChanged(boolean usersChanged) {
        this.usersChanged = usersChanged;
        return this;
    }

    public
    ScheduledService getMessagesUpdater() {
        return messagesUpdater;
    }

    public
    MessagingPageController setMessagesUpdater(ScheduledService messagesUpdater) {
        this.messagesUpdater = messagesUpdater;
        return this;
    }

    public
    ScheduledService getUsersUpdater() {
        return usersUpdater;
    }

    public
    MessagingPageController setUsersUpdater(ScheduledService usersUpdater) {
        this.usersUpdater = usersUpdater;
        return this;
    }

    public
    ScheduledService getMessagingEnvironmentUpdater() {
        return messagingEnvironmentUpdater;
    }

    public
    MessagingPageController setMessagingEnvironmentUpdater(ScheduledService messagingEnvironmentUpdater) {
        this.messagingEnvironmentUpdater = messagingEnvironmentUpdater;
        return this;
    }
}


//    public
//    void updateMessagingEnvironment() {
//        final MessagingEnvironment[] updatedMessagingEnvironment = new MessagingEnvironment[1];
//        final List <Message>[] updatedMessages = new List[]{new LinkedList <>()};
//        final List <User>[] updatedUsers = new List[]{new LinkedList <>()};
//        TimerTask task = new TimerTask() {
//            @Override
//            public
//            void run() {
//                System.out.println("updating");
//
//                Response response2;
//                Response response3;
//                 Response response= ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.UPDATE_MESSAGING_ENVIRONMENT, messagingEnvironment.getId().toString(), LoggedInUser.getUser().getToken()));
//
//
//
//
//
//                if (response.getType() == ResponseType.UPDATED_MESSAGING_ENVIRONMENT) {
//
//                    //        System.out.println(response.getData());
//                    updatedMessagingEnvironment[0] = JSonController.stringToObjectMapper(response.getData(), MessagingEnvironment.class);
//                    response2 = ServerHandler.getResponse();
//                    if (response2.getType() == ResponseType.UPDATED_MESSAGES) {
//
//                        updatedMessages[0] = JSonController.stringToObjectMapper(response2.getData(), new TypeReference <List <Message>>() {
//                        });
//                        updatedMessagingEnvironment[0].setMessages(updatedMessages[0]);
//                        response3 = ServerHandler.getResponse();
//                        if (response3.getType() == ResponseType.UPDATED_USERS) {
//                            updatedUsers[0] = JSonController.stringToObjectMapper(response3.getData(), new TypeReference <List <User>>() {
//                            });
//                            updatedMessagingEnvironment[0].setUsers(updatedUsers[0]);
//
//                        }
//                    }
//                }
//                // System.out.println(getMessagingEnvironment().getLastMessage().getId());
//                //  System.out.println(updatedMessagingEnvironment[0].getLastMessage().getId());
//                if (changeFinder(getMessagingEnvironment(), updatedMessagingEnvironment[0])) {
//                    setChanged(true);
//                    System.out.println("changed==true");
//                    setMessagingEnvironment(updatedMessagingEnvironment[0]);
////                    System.out.println(messagingEnvironment.getMessages().size());
//                }
//
//            }
//        };
//        Actions.nonUIActionLopper(2, task);
//    }
//
//    public
//    Loop updateMessagingEnvironmentLoop() {
//        final MessagingEnvironment[] updatedMessagingEnvironment = new MessagingEnvironment[1];
//        final List <Message>[] updatedMessages = new List[]{new LinkedList <>()};
//        final List <User>[] updatedUsers = new List[]{new LinkedList <>()};
//        Runnable runnable = new Runnable() {
//            @Override
//            public
//            void run() {
//                System.out.println("updating");
//
//                Response response2;
//                Response response3;
//                 Response response= ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.UPDATE_MESSAGING_ENVIRONMENT, messagingEnvironment.getId().toString(), LoggedInUser.getUser().getToken()));
//
//
//
//
//
//                if (response.getType() == ResponseType.UPDATED_MESSAGING_ENVIRONMENT) {
//
//                    //        System.out.println(response.getData());
//                    updatedMessagingEnvironment[0] = JSonController.stringToObjectMapper(response.getData(), MessagingEnvironment.class);
//                    response2 = ServerHandler.getResponse();
//                    if (response2.getType() == ResponseType.UPDATED_MESSAGES) {
//
//                        updatedMessages[0] = JSonController.stringToObjectMapper(response2.getData(), new TypeReference <List <Message>>() {
//                        });
//                        updatedMessagingEnvironment[0].setMessages(updatedMessages[0]);
//                        response3 = ServerHandler.getResponse();
//                        if (response3.getType() == ResponseType.UPDATED_USERS) {
//                            updatedUsers[0] = JSonController.stringToObjectMapper(response3.getData(), new TypeReference <List <User>>() {
//                            });
//                            updatedMessagingEnvironment[0].setUsers(updatedUsers[0]);
//
//                        }
//                    }
//                }
//                // System.out.println(getMessagingEnvironment().getLastMessage().getId());
//                //  System.out.println(updatedMessagingEnvironment[0].getLastMessage().getId());
//                if (changeFinder(getMessagingEnvironment(), updatedMessagingEnvironment[0])) {
//                    setChanged(true);
//                    System.out.println("changed==true");
//                    setMessagingEnvironment(updatedMessagingEnvironment[0]);
////                    System.out.println(messagingEnvironment.getMessages().size());
//                }
//            }
//        };
//        Loop loop = new Loop(1, runnable);
//        loop.start();
//        return loop;
//    }
