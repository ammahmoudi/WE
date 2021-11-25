package controller.server;

import JSon.JSonController;
import controller.*;
import event.category.ModifyCategoryUsersEvent;
import event.category.NewCategoryEvent;
import event.messaging.*;
import event.post.LikePostEvent;
import event.post.NewPostEvent;
import event.user.*;
import model.Category;
import model.Message;
import request.Request;
import response.Response;
import response.ResponseType;

public
class RequestHandler {
    AuthController authController;
    UserController userController;
    MessagingEnvironmentController messagingEnvironmentController;
    PostController postController;
    CategoryController categoryController;
    NotificationController notificationController;
    ClientHandler clientHandler;

    public
    RequestHandler(ClientHandler clientHandler) {

        this.clientHandler = clientHandler;
        authController = new AuthController(clientHandler);
        userController = new UserController(clientHandler);
        messagingEnvironmentController = new MessagingEnvironmentController(clientHandler);
        postController = new PostController(clientHandler);
        categoryController = new CategoryController(clientHandler);
        notificationController = new NotificationController(clientHandler);

    }

    public
    void executeRequest(Request request) {
        switch (request.getType()) {
            case SIGN_IN:
                if (request.getToken() != null) {
                    authController.signIn(request.getUserId(), request.getToken());
                } else {

                    SignInFormEvent signInFormEvent = JSonController.stringToObjectMapper(request.getData(), SignInFormEvent.class);
                    authController.signIn(signInFormEvent);
                }
                break;
            case SIGN_UP:

                SignUpFormEvent signUpFormEvent = JSonController.stringToObjectMapper(request.getData(), SignUpFormEvent.class);
                authController.signUp(signUpFormEvent);
                break;
            case UPDATE_MESSAGING_ENVIRONMENTS:
                userController.sendMessagingEnvironments(request.getToken());
                break;
            case UPDATE_MESSAGING_ENVIRONMENT:

                if (authController.validateToken(request.getToken())) {
                    messagingEnvironmentController.sendMessagingEnvironment(Integer.parseInt(request.getData()));
                }
                break;
            case UPDATE_MESSAGES:

                if (authController.validateToken(request.getToken())) {
                    messagingEnvironmentController.sendUpdatedMessages(Integer.parseInt(request.getData()));
                }
                break;
            case UPDATE_USERS:

                if (authController.validateToken(request.getToken())) {
                    messagingEnvironmentController.sendUpdatedUsers(Integer.parseInt(request.getData()));
                }
                break;
            case NEW_MESSAGE:

                if (authController.validateToken(request.getToken())) {
                    NewMessageEvent newMessageEvent = JSonController.stringToObjectMapper(request.getData(), NewMessageEvent.class);
                    messagingEnvironmentController.newMessage(newMessageEvent);

                }
                break;
            case EDIT_MESSAGE:
                if (authController.validateToken(request.getToken())) {
                    Message message = JSonController.stringToObjectMapper(request.getData(), Message.class);
                    messagingEnvironmentController.editMessage(message);

                }
                break;
            case DELETE_MESSAGE:
                if (authController.validateToken(request.getToken())) {

                    messagingEnvironmentController.deleteMessage(Integer.parseInt(request.getData()));

                }
                break;
            case SEEN:
                if (authController.validateToken(request.getToken())) {

                    messagingEnvironmentController.seenMessage(Integer.parseInt(request.getData()));

                }
                break;
            case LOGGED_IN_USER_RELATIONSHIP:
                if (authController.validateToken(request.getToken())) {

                    userController.sendLoggedInUserRelationship(Integer.parseInt(request.getData()));

                }
                break;
            case USER_RELATIONSHIP:
                if (authController.validateToken(request.getToken())) {

                    userController.sendUserRelationship(Integer.parseInt(request.getData()));

                }
                break;
            case APPLY_RELATIONSHIP:
                if (authController.validateToken(request.getToken())) {
                    RelationshipEvent relationshipEvent = JSonController.stringToObjectMapper(request.getData(), RelationshipEvent.class);
                    userController.applyRelationship(relationshipEvent);

                }
                break;
            case UPDATE_FOLLOWINGS:

                if (authController.validateToken(request.getToken())) {
                    userController.sendUpdatedFollowings();
                }
                break;
            case UPDATE_FOLLOWERS:

                if (authController.validateToken(request.getToken())) {
                    userController.sendUpdatedFollowers();
                }
                break;
            case UPDATE_REQUESTEDS:

                if (authController.validateToken(request.getToken())) {
                    userController.sendUpdatedRequesteds();
                }
                break;
            case UPDATE_REQUESTS:

                if (authController.validateToken(request.getToken())) {
                    userController.sendUpdatedRequests();
                }
                break;
            case UPDATE_BLOCKEDS:

                if (authController.validateToken(request.getToken())) {
                    userController.sendUpdatedBlockeds();
                }
                break;
            case UPDATE_BLOCKERS:

                if (authController.validateToken(request.getToken())) {
                    userController.sendUpdatedBlockers();
                }
                break;
            case UPDATE_SILENTEDS:

                if (authController.validateToken(request.getToken())) {
                    userController.sendUpdatedSilenteds();
                }
                break;
            case ADD_MEMBER:

                if (authController.validateToken(request.getToken())) {
                    AddMemberEvent addMemberEvent = JSonController.stringToObjectMapper(request.getData(), AddMemberEvent.class);
                    messagingEnvironmentController.addMember(addMemberEvent);
                }
                break;
            case MODIFY_GROUP_INFO:

                if (authController.validateToken(request.getToken())) {
                    ModifyGroupInfoEvent modifyGroupInfoEvent = JSonController.stringToObjectMapper(request.getData(), ModifyGroupInfoEvent.class);
                    messagingEnvironmentController.modifyGroupInfo(modifyGroupInfoEvent);
                }
                break;

            case LEAVE_GROUP:

                if (authController.validateToken(request.getToken())) {

                    messagingEnvironmentController.leaveGroup(Integer.parseInt(request.getData()));
                }
                break;

            case NEW_GROUP:

                if (authController.validateToken(request.getToken())) {
                    NewGroupEvent newGroupEvent = JSonController.stringToObjectMapper(request.getData(), NewGroupEvent.class);
                    messagingEnvironmentController.newGroup(newGroupEvent);
                }
                break;
            case IS_USED_EMAIL:


                userController.isUsedEmail(request.getData());

                break;
            case IS_USED_USERNAME:


                userController.isUsedUsername(request.getData());

                break;
            case IS_CURRENT_PASSWORD:

                if (authController.validateToken(request.getToken())) {

                    userController.isCurrentPassword(request.getData());
                }
                break;
            case CHANGE_USER_SETTING:

                if (authController.validateToken(request.getToken())) {
                    ChangeUserInfoEvent changeUserInfoEvent = JSonController.stringToObjectMapper(request.getData(), ChangeUserInfoEvent.class);
                    userController.changeUserSettings(changeUserInfoEvent);
                }

                break;
            case DEACTIVATE_ACCOUNT:

                if (authController.validateToken(request.getToken())) {

                    userController.deactivateAccount();
                }
                break;
            case DELETE_ACCOUNT:

                if (authController.validateToken(request.getToken())) {

                    userController.deleteAccount();
                }
                break;
            case NEW_POST:

                if (authController.validateToken(request.getToken())) {
                    NewPostEvent newPostEvent = JSonController.stringToObjectMapper(request.getData(), NewPostEvent.class);
                    postController.newPost(newPostEvent);
                }
                break;
            case UPDATE_FEED:

                if (authController.validateToken(request.getToken())) {
                    postController.sendFeedPosts();
                }
                break;
            case UPDATE_EXPLORER:

                if (authController.validateToken(request.getToken())) {
                    postController.sendExplorerPosts();
                }
                break;
            case LIKE_POST:

                if (authController.validateToken(request.getToken())) {
                    LikePostEvent likePostEvent = JSonController.stringToObjectMapper(request.getData(), LikePostEvent.class);

                    postController.like(likePostEvent);
                }
                break;
            case USER_FOLLOWINGS:

                if (authController.validateToken(request.getToken())) {
                    userController.sendUserFollowings(Integer.parseInt(request.getData()));
                }
                break;
            case USER_FOLLOWERS:

                if (authController.validateToken(request.getToken())) {
                    userController.sendUserFollowers(Integer.parseInt(request.getData()));
                }
                break;
            case UPDATE_USER:

                if (authController.validateToken(request.getToken())) {
                    userController.sendUser(Integer.parseInt(request.getData()));
                }
                break;
            case UPDATE_USER_POSTS:

                if (authController.validateToken(request.getToken())) {
                    postController.sendUserPosts(Integer.parseInt(request.getData()));
                }
                break;
            case BLOCK_USER:

                if (authController.validateToken(request.getToken())) {
                    BlockEvent blockEvent = JSonController.stringToObjectMapper(request.getData(), BlockEvent.class);
                    userController.blockUser(blockEvent);
                }
                break;
            case MUTE_USER:

                if (authController.validateToken(request.getToken())) {
                    MuteEvent muteEvent = JSonController.stringToObjectMapper(request.getData(), MuteEvent.class);
                    userController.muteUser(muteEvent);
                }
                break;
            case PRIVATE_CHAT:

                if (authController.validateToken(request.getToken())) {

                    messagingEnvironmentController.sendPrivateChat(Integer.parseInt(request.getData()));
                }
                break;
            case SEARCH_USER:

                if (authController.validateToken(request.getToken())) {

                    userController.sendSearchedUsers(request.getData());
                }
                break;
            case CATEGORIES:

                if (authController.validateToken(request.getToken())) {

                    userController.sendCategories(request.getData());
                }
                break;
            case NEW_CATEGORY:

                if (authController.validateToken(request.getToken())) {
                    NewCategoryEvent newCategoryEvent = JSonController.stringToObjectMapper(request.getData(), NewCategoryEvent.class);
                    categoryController.newCategory(newCategoryEvent);
                }

                break;
            case DELETE_CATEGORY:

                if (authController.validateToken(request.getToken())) {

                    categoryController.deleteCategory(Integer.parseInt(request.getData()));
                }
                break;
            case CATEGORY:

                if (authController.validateToken(request.getToken())) {

                    categoryController.sendCategory(Integer.parseInt(request.getData()));
                }
                break;
            case MODIFY_CATEGORY_INFO:

                if (authController.validateToken(request.getToken())) {
                    Category category = JSonController.stringToObjectMapper(request.getData(), Category.class);
                    categoryController.modifyCategoryInfo(category);
                }
                break;
            case MODIFY_CATEGORY_USERS:

                if (authController.validateToken(request.getToken())) {
                    ModifyCategoryUsersEvent modifyCategoryUsersEvent = JSonController.stringToObjectMapper(request.getData(), ModifyCategoryUsersEvent.class);
                    categoryController.modifyCategoryUsers(modifyCategoryUsersEvent);
                }
                break;
            case UPDATE_NOTIFICATIONS:

                if (authController.validateToken(request.getToken())) {
                    notificationController.sendNotifications();
                }
                break;
            case SEEN_NOTIFICATION:

                if (authController.validateToken(request.getToken())) {
                    notificationController.seenNotification(Integer.parseInt(request.getData()));
                }
                break;
            case APPLY_REQUEST:
                if (authController.validateToken(request.getToken())) {
                    RequestEvent requestEvent = JSonController.stringToObjectMapper(request.getData(), RequestEvent.class);
                    userController.applyRequest(requestEvent);
                }
                break;
            case ONLINE:
                if (authController.validateToken(request.getToken())) {
                    userController.applyOnlineRequest();
                }
                break;
            case UPDATE_COMMENTS:
                if (authController.validateToken(request.getToken())) {
                    postController.sendComments(Integer.parseInt(request.getData()));
                }
                break;
            case UPDATE_POST_LIKERS:
                if (authController.validateToken(request.getToken())) {
                    postController.sendLikes(Integer.parseInt(request.getData()));
                }
                break;
            case NEW_SCHEDULED_MESSAGE:
                if (authController.validateToken(request.getToken())) {
                    NewScheduledMessageEvent newScheduledMessageEvent=JSonController.stringToObjectMapper(request.getData(),NewScheduledMessageEvent.class);
                    messagingEnvironmentController.newScheduledMessage(newScheduledMessageEvent);
                }
                break;
            case ALIVE_CONNECTION:
                Transmitter.sendResponse(clientHandler.getPrintWriter(), new Response(ResponseType.ALIVE_CONNECTION, "yes you Are alive :)"));
        }

    }
}
