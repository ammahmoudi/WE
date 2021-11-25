package controller;

import JSon.JSonController;
import com.fasterxml.jackson.core.type.TypeReference;
import controller.server.ClientHandler;
import controller.server.Transmitter;
import controller.server.WEServer;
import event.messaging.*;
import model.*;
import response.Response;
import response.ResponseType;

import java.util.LinkedList;
import java.util.List;

public
class MessagingEnvironmentController extends Controller {

    public
    MessagingEnvironmentController(ClientHandler clientHandler) {
        super(clientHandler);
    }

    public
    void sendMessagingEnvironment(int id) {
        MessagingEnvironment messagingEnvironment = context.MessagingEnvironmentDB.get(id);
        String data;

        data = JSonController.objectToStringMapper(messagingEnvironment);
        Response response = new Response(ResponseType.UPDATED_MESSAGING_ENVIRONMENT, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);
    }
    public
    void sendPrivateChat(int id) {
        MessagingEnvironment messagingEnvironment = context.MessagingEnvironmentDB.getPrivateChat(clientHandler.getLoggedInUser().getId(),id);
        String data;
        User owner= messagingEnvironment.getOwner();


        data = JSonController.objectToStringMapper(messagingEnvironment);

        Response response = new Response(ResponseType.PRIVATE_CHAT, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);
    }


    public
    void addMember(AddMemberEvent addMemberEvent) {
        List <User> updatedUsers = context.MessagingEnvironmentDB.addMember(addMemberEvent.getMessagingEnvironmentId(), addMemberEvent.getSelectedUsersId());
        String data;
        for(User user :updatedUsers){
            switch (user.getLastSeenPrivacy()){
                case EVERYONE:break;
                case NOBODY:user.setLastSeen(null);break;
                case ONLY_FOLLOWINGS:
                    RelationshipType relationshipType=user.getRelationshipTo(clientHandler.getLoggedInUser());
                    if(relationshipType!=RelationshipType.FOLLOWING&&relationshipType!=RelationshipType.FOLLOWING_MUTED){
                        user.setLastSeen(null);
                    }
                    break;
            }
        }
        data = JSonController.objectToStringMapper(updatedUsers);
        Response response = new Response(ResponseType.ADD_MEMBER, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);
    }

    public
    void leaveGroup(int id) {
        List <Integer> ids = new LinkedList <>();
        ids.add(clientHandler.getLoggedInUser().getId());
        List <User> updatedUsers = context.MessagingEnvironmentDB.removeMember(id, ids);
        //  String data;
        // data=JSonController.objectToStringMapper(updatedUsers);
        Response response = new Response(ResponseType.LEAVE_GROUP, "1");
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);
    }


    public
    void sendUpdatedMessages(int id) {
        List <Message> messageList = context.MessagingEnvironmentDB.getList(id, Message.class,clientHandler.getLoggedInUser());
        for (Message message : messageList) {
            message = context.Messages.get(message.getId());
            message.setUserSeen(message.isSeen(clientHandler.getLoggedInUser()));



            //    message.setSeenByUser(message.seenBy(context.);)
        }


        String messages;
        messages = JSonController.objectToStringMapper(messageList);

        Response response = new Response(ResponseType.UPDATED_MESSAGES, messages);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);


    }

    public
    void sendUpdatedUsers(int id) {
        List <User> userList = context.MessagingEnvironmentDB.getList(id, User.class,clientHandler.getLoggedInUser());
        String users;
        users = JSonController.objectToStringMapper(userList);
        Response response = new Response(ResponseType.UPDATED_USERS, users);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }

    public
    void newMessage(NewMessageEvent newMessageEvent) {
        User sender = context.Users.get(newMessageEvent.getSender());
        MessagingEnvironment messagingEnvironment = context.MessagingEnvironmentDB.get(newMessageEvent.getTaker());
        List <Message> messageList = context.MessagingEnvironmentDB.getList(newMessageEvent.getTaker(), Message.class, clientHandler.getLoggedInUser());
        messagingEnvironment.setMessages(messageList);
        Message originalMessage = null;
        if (newMessageEvent.getOriginalMessage() != null)
            originalMessage = context.Messages.get(newMessageEvent.getOriginalMessage());

        Post post = null;
        if (newMessageEvent.getPost() != null)
            post = context.Posts.get(newMessageEvent.getPost());
        Message message = new Message(sender, messagingEnvironment, newMessageEvent.getText(), originalMessage, newMessageEvent.getImage(), post);


        context.Messages.add(message);
        if (message.isForwarded()) {
            context.Messages.forward(message.getOriginalMessage().getId(), message.getId());
        }
        Response response = new Response(ResponseType.ACCEPT_NEW_MESSAGE, "1");
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);
    }

    public
    void editMessage(Message message) {
        Message oldMessage = context.Messages.get(message.getId());
        oldMessage.setText(message.getText());
        context.Messages.update(oldMessage);
        Response response = new Response(ResponseType.ACCEPT_EDIT_MESSAGE, "1");
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }

    public
    void deleteMessage(int id) {
        Message message = context.Messages.get(id);
        context.Messages.remove(message);
        Response response = new Response(ResponseType.ACCEPT_DELETE_MESSAGE, "1");
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }

    public
    void seenMessage(int id) {
        context.Messages.seenBy(id, clientHandler.getLoggedInUser().getId());
        Response response = new Response(ResponseType.ACCEPT_SEEN, "1");
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }

    public
    void modifyGroupInfo(ModifyGroupInfoEvent modifyGroupInfoEvent) {
        MessagingEnvironment messagingEnvironment = context.MessagingEnvironmentDB.get(modifyGroupInfoEvent.getMessagingEnvironmentId());
        messagingEnvironment.setName(modifyGroupInfoEvent.getName());
        messagingEnvironment.setImage(modifyGroupInfoEvent.getImage());
        context.MessagingEnvironmentDB.update(messagingEnvironment);
        Response response = new Response(ResponseType.MODIFY_GROUP_INFO, "1");
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);
    }

    public
    void newGroup(NewGroupEvent newGroupEvent) {
        User owner = context.Users.get(newGroupEvent.getOwner());
        newGroupEvent.getUsersList().add(newGroupEvent.getOwner());
        MessagingEnvironment messagingEnvironment = new MessagingEnvironment(newGroupEvent.getName(), owner);
        context.MessagingEnvironmentDB.add(messagingEnvironment);
        messagingEnvironment.setUsers(context.MessagingEnvironmentDB.addMember(messagingEnvironment.getId(), newGroupEvent.getUsersList()));

        String data;
        messagingEnvironment.setUsers(null);
        messagingEnvironment.setMessages(null);
        data = JSonController.objectToStringMapper(messagingEnvironment);
        Response response = new Response(ResponseType.NEW_GROUP, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }
    public void newScheduledMessage(NewScheduledMessageEvent newScheduledMessageEvent){
        newScheduledMessageEvent.setSenderUser(context.Users.get(newScheduledMessageEvent.getSender()));
        context.ScheduledMessages.add(newScheduledMessageEvent);
        System.out.println("scheduled message saved");
        Response response = new Response(ResponseType.NEW_SCHEDULED_MESSAGE, "1");
        WEServer.scheduledMessageEvents=context.ScheduledMessages.all();
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);
    }
}
