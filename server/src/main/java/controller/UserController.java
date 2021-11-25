package controller;

import JSon.JSonController;
import controller.server.ClientHandler;
import controller.server.Transmitter;
import event.user.*;
import model.*;
import response.Response;
import response.ResponseType;

import java.time.LocalDateTime;
import java.util.List;

public
class UserController extends Controller {


    public
    UserController(ClientHandler clientHandler) {
        super(clientHandler);
    }

    public
    void sendMessagingEnvironments(String token) {

        List <MessagingEnvironment> messagingEnvironmentList = context.Users.getList(token, MessagingEnvironment.class, "MessagingEnvironments",clientHandler.getLoggedInUser().getId());
        String data;
        data = JSonController.objectToStringMapper(messagingEnvironmentList);
   //     System.out.println(messagingEnvironmentList.size());
        Response response = new Response(ResponseType.UPDATED_MESSAGING_ENVIRONMENTS, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(),response);


    }
    public
    void sendCategories(String token) {

        List <Category> categoryList = context.Users.getList(token, Category.class, "Categories",clientHandler.getLoggedInUser().getId());
        String data;
        data = JSonController.objectToStringMapper(categoryList);

        Response response = new Response(ResponseType.CATEGORIES, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);


    }

    public
    void sendLoggedInUserRelationship(int id) {
        RelationshipType relationshipType = context.Users.getRelationship(clientHandler.getLoggedInUser().getId(), id);
        String data;
        data = JSonController.objectToStringMapper(relationshipType);
        Response response = new Response(ResponseType.LOGGED_IN_USER_RELATIONSHIP, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }

    public
    void applyRelationship(RelationshipEvent relationshipEvent) {
        RelationshipType relationshipType = context.Users.follow(relationshipEvent.getFrom(), relationshipEvent.getTo());
        String data;
        data = JSonController.objectToStringMapper(relationshipType);
        Response response = new Response(ResponseType.APPLY_RELATIONSHIP, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }
    public
    void applyRequest(RequestEvent requestEvent) {
        RelationshipType relationshipType = context.Users.applyRequest(requestEvent.getFrom(), requestEvent.getTo(), requestEvent.isAccept());
        String data;
        data = JSonController.objectToStringMapper(relationshipType);
        Response response = new Response(ResponseType.APPLY_REQUEST, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }
    public
    void blockUser(BlockEvent blockEvent) {
        RelationshipType relationshipType = context.Users.block(blockEvent.getFrom(), blockEvent.getTo());
        String data;
        data = JSonController.objectToStringMapper(relationshipType);
        Response response = new Response(ResponseType.BLOCK_USER, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(),
                response);

    }

    public
    void muteUser(MuteEvent muteEvent) {
        RelationshipType relationshipType = context.Users.mute(muteEvent.getFrom(), muteEvent.getTo());
        String data;
        data = JSonController.objectToStringMapper(relationshipType);
        Response response = new Response(ResponseType.MUTE_USER, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(),
                response);

    }

    public
    void sendUserRelationship(int id) {
        RelationshipType relationshipType = context.Users.getRelationship(id, clientHandler.getLoggedInUser().getId());
        String data;
        data = JSonController.objectToStringMapper(relationshipType);
        Response response = new Response(ResponseType.USER_RELATIONSHIP, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }

    public
    void sendUpdatedFollowings() {
        List <User> userList = context.Users.getList(clientHandler.getLoggedInUser().getToken(), User.class, "Followings",clientHandler.getLoggedInUser().getId());
        String users;
        users = JSonController.objectToStringMapper(userList);
        Response response = new Response(ResponseType.UPDATED_FOLLOWINGS, users);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }
    public
    void sendUpdatedRequests() {
        List <User> userList = context.Users.getList(clientHandler.getLoggedInUser().getToken(), User.class, "Requests",clientHandler.getLoggedInUser().getId());
        String users;
        users = JSonController.objectToStringMapper(userList);
        Response response = new Response(ResponseType.UPDATED_REQUESTS, users);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }
    public
    void sendUpdatedRequesteds() {
        List <User> userList = context.Users.getList(clientHandler.getLoggedInUser().getToken(), User.class, "Requesteds",clientHandler.getLoggedInUser().getId());
        String users;
        users = JSonController.objectToStringMapper(userList);
        Response response = new Response(ResponseType.UPDATED_REQUESTEDS, users);

        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);


    }
    public
    void sendUpdatedBlockers() {
        List <User> userList = context.Users.getList(clientHandler.getLoggedInUser().getToken(), User.class, "Blockers",clientHandler.getLoggedInUser().getId());
        String users;
        users = JSonController.objectToStringMapper(userList);
        Response response = new Response(ResponseType.UPDATED_BLOCKERS, users);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }
    public
    void sendUpdatedBlockeds() {
        List <User> userList = context.Users.getList(clientHandler.getLoggedInUser().getToken(), User.class, "Blockeds",clientHandler.getLoggedInUser().getId());
        String users;
        users = JSonController.objectToStringMapper(userList);
        Response response = new Response(ResponseType.UPDATED_BLOCKEDS, users);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }
    public
    void sendUpdatedSilenteds() {
        List <User> userList = context.Users.getList(clientHandler.getLoggedInUser().getToken(), User.class, "Silenteds",clientHandler.getLoggedInUser().getId());
        String users;
        users = JSonController.objectToStringMapper(userList);
        Response response = new Response(ResponseType.UPDATED_SILENTEDS, users);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }
    public
    void sendUserFollowings(int id) {
        User user=context.Users.get(id);

        List <User> userList;
        RelationshipType relationshipType=context.Users.getRelationship(clientHandler.getLoggedInUser().getId(),user.getId());
        if((user.isPrivated()&&(relationshipType== RelationshipType.FOLLOWING||relationshipType==RelationshipType.FOLLOWING_MUTED))||!user.isPrivated()||user.equals(clientHandler.getLoggedInUser())){

            userList = context.Users.getList(clientHandler.getLoggedInUser().getToken(), User.class, "Followings",id);
        }else userList=null;
        String users;
        users = JSonController.objectToStringMapper(userList);
        Response response = new Response(ResponseType.USER_FOLLOWINGS, users);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }
    public
    void sendUser(int id) {
        User user=context.Users.get(id);
        String data;
        data = JSonController.objectToStringMapper(user);
        Response response = new Response(ResponseType.UPDATE_USER, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }


    public
    void sendUserFollowers(int id) {
        User user=context.Users.get(id);

        List <User> userList;
        RelationshipType relationshipType=context.Users.getRelationship(clientHandler.getLoggedInUser().getId(),user.getId());
        if((user.isPrivated()&&(relationshipType== RelationshipType.FOLLOWING||relationshipType==RelationshipType.FOLLOWING_MUTED))||!user.isPrivated()||user.equals(clientHandler.getLoggedInUser())){
            userList = context.Users.getList(clientHandler.getLoggedInUser().getToken(), User.class, "Followers",id);
        }else userList=null;
        String users;
        users = JSonController.objectToStringMapper(userList);

        Response response = new Response(ResponseType.USER_FOLLOWERS, users);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }
    public
    void sendUpdatedFollowers() {
        List <User> userList = context.Users.getList(clientHandler.getLoggedInUser().getToken(), User.class, "Followers",clientHandler.getLoggedInUser().getId());
        String users;

        users = JSonController.objectToStringMapper(userList);

        Response response = new Response(ResponseType.UPDATED_FOLLOWERS, users);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }

    public
    void isUsedUsername(String text) {
        Boolean result;
        User user = context.Users.getByuserName(text);
        if (user == null) {
            result = false;
        } else {
            result = !user.equals(clientHandler.getLoggedInUser());
        }
        Response response = new Response(ResponseType.IS_USED_USERNAME, JSonController.objectToStringMapper(result));
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);
    }

    public
    void isUsedEmail(String text) {
        Boolean result;
        User user = context.Users.getByuserName(text);
        if (user == null) {
            result = false;
        } else {
            result = !user.equals(clientHandler.getLoggedInUser());
        }
        Response response = new Response(ResponseType.IS_USED_EMAIL, JSonController.objectToStringMapper(result));
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);
    }

    public
    void isCurrentPassword(String text) {

        Boolean result = clientHandler.getLoggedInUser().getPassword().equals(text);
        Response response = new Response(ResponseType.IS_CURRENT_PASSWORD, JSonController.objectToStringMapper(result));
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }
    public void changeUserSettings(ChangeUserInfoEvent changeUserInfoEvent){
        clientHandler.setLoggedInUser(context.Users.get(clientHandler.getLoggedInUser().getId()));
        if(clientHandler.getLoggedInUser().getPassword().equals(changeUserInfoEvent.getOldPassword())) {

            clientHandler.getLoggedInUser().changeInfo(changeUserInfoEvent.getFirstName(), changeUserInfoEvent.getLastName(), changeUserInfoEvent.getNewPassword(), changeUserInfoEvent.getUsername(), changeUserInfoEvent.getPhoneNumber(), changeUserInfoEvent.getEmail(), changeUserInfoEvent.getBio(), changeUserInfoEvent.getBirthDay(), changeUserInfoEvent.getLastSeenPrivacy(), changeUserInfoEvent.isPrivateAccount(),changeUserInfoEvent.getImage());
        }else{
            clientHandler.getLoggedInUser().changeInfo(changeUserInfoEvent.getFirstName(), changeUserInfoEvent.getLastName(), clientHandler.getLoggedInUser().getPassword(), changeUserInfoEvent.getUsername(), changeUserInfoEvent.getPhoneNumber(), changeUserInfoEvent.getEmail(), changeUserInfoEvent.getBio(), changeUserInfoEvent.getBirthDay(), changeUserInfoEvent.getLastSeenPrivacy(), changeUserInfoEvent.isPrivateAccount(),changeUserInfoEvent.getImage());

        }
        context.Users.update(clientHandler.getLoggedInUser());
        clientHandler.getLoggedInUser().setMyOwnLastSeenPrivacy(clientHandler.getLoggedInUser().getLastSeenPrivacy());
        Response response = new Response(ResponseType.CHANGE_USER_SETTING, JSonController.objectToStringMapper(clientHandler.getLoggedInUser()));
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }
    public void deactivateAccount(){
        clientHandler.getLoggedInUser().setActivate(false);
        context.Users.update(clientHandler.getLoggedInUser());
        Response response = new Response(ResponseType.DEACTIVATE_ACCOUNT, "n");
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }
    public void deleteAccount(){
        clientHandler.getLoggedInUser().setActivate(false);
        clientHandler.getLoggedInUser().setDeleted(true);
        clientHandler.getLoggedInUser().setUserName(null);
        context.Users.update(clientHandler.getLoggedInUser());
        Response response = new Response(ResponseType.DELETE_ACCOUNT, "n");
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }
    public void sendSearchedUsers(String text){
    String data;
    List<User> users=context.Users.search(text);
    data=JSonController.objectToStringMapper(users);
        Response response = new Response(ResponseType.SEARCH_USER, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);

    }
    public void applyOnlineRequest(){

        clientHandler.setLoggedInUser(context.Users.get(clientHandler.getLoggedInUser().getId()));
        clientHandler.getLoggedInUser().setLastSeen(LocalDateTime.now());
        context.Users.update(clientHandler.getLoggedInUser());
        String data;
        data=JSonController.objectToStringMapper(clientHandler.getLoggedInUser().getLastSeen());
        Response response = new Response(ResponseType.ONLINE, data);
        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);
        context.Users.serverSeen(clientHandler.getLoggedInUser().getId());
    }
}
