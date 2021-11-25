package model;

import JSon.JSonController;
import com.fasterxml.jackson.core.type.TypeReference;
import controller.Actions;
import controller.ServerHandler;
import controller.messaging.MessagingPageController;
import db.Context;
import event.messaging.NewMessageEvent;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import request.Request;
import request.RequestType;
import response.Response;
import response.ResponseType;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public
class LoggedInUser {
    static User user;
    private static boolean changedFollowings = true;
    private static boolean changedFollowers = true;
    private static boolean changedMessagingEnvironments = true;
    private static ScheduledService followingsUpdater;
    private static ScheduledService followersUpdater;
    private static ScheduledService messagingEnvironmentsUpdater;
    private static ScheduledService notificationsUpdater;
    private static ScheduledService onlineUpdater;
    private static Context context=new Context();
    private static Boolean offline=false;


    public static
    User getUser() {
        return user;
    }

    public static
    void setUser(User user) {
        if (user != null) {
            if (user.getFirstTimeToken()!=null&&user.getToken()==null)
            user.setToken(user.getFirstTimeToken());

            if(user.getMyOwnLastSeenPrivacy()==null)user.setMyOwnLastSeenPrivacy(user.getLastSeenPrivacy());
            if(user.getLastSeenPrivacy()==null)user.setLastSeenPrivacy(user.getLastSeenPrivacy());
       //     user.setFirstTimeToken(null);
        }
        LoggedInUser.user = user;
      //  System.out.println(user.getToken());
    }

    public static
    ScheduledService scheduledUpdateMessagingEnvironments(Runnable runnable) {
        final List <MessagingEnvironment>[] messagingEnvironmentList = new List[]{new LinkedList <>()};
        Runnable task = new Runnable() {
            @Override
            public
            void run() {
                Response response = ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.UPDATE_MESSAGING_ENVIRONMENTS, null, LoggedInUser.getUser().getToken()));
//                 System.out.println("updated left list :"+response.getType());

                if (response!=null&&response.getType() == ResponseType.UPDATED_MESSAGING_ENVIRONMENTS) {
                    messagingEnvironmentList[0] = JSonController.stringToObjectMapper(response.getData(), new TypeReference <List <MessagingEnvironment>>() {
                    });
                    if (LoggedInUser.getUser().getMessagingEnvironments() == null) {
                        LoggedInUser.getUser().setMessagingEnvironments(new LinkedList <>());
                        LoggedInUser.getUser().getMessagingEnvironments().addAll(messagingEnvironmentList[0]);
                        //      LoggedInUser.getUser().setMessagingEnvironments(messagingEnvironmentList[0]);
                    }
                    //    System.out.println(messagingEnvironmentList[0].size());
                    if (messagingEnvironmentsChangeFinder(LoggedInUser.getUser().getMessagingEnvironments(), messagingEnvironmentList[0])) {
                         System.out.println("change detected");
                        //   System.out.println("new" + messagingEnvironmentList[0].size());
                        //  System.out.println("old" + LoggedInUser.getUser().getMessagingEnvironments().size());
                        //   System.out.println(messagingEnvironmentsChangeFinder(LoggedInUser.getUser().getMessagingEnvironments(),messagingEnvironmentList[0]));
                        setChangedMessagingEnvironments(true);

                        //  LoggedInUser.getUser().setMessagingEnvironments(messagingEnvironmentList[0]);
                        LoggedInUser.getUser().getMessagingEnvironments().clear();
                        LoggedInUser.getUser().getMessagingEnvironments().addAll(messagingEnvironmentList[0]);
//                        for (MessagingEnvironment messagingEnvironment:LoggedInUser.getUser().getMessagingEnvironments()){
//                            context.MessagingEnvironmentDB.update(messagingEnvironment);
//                        }
                    }
                }

                //LoggedInUser.getUser().setMessagingEnvironments(new LinkedList <>());

                //   LoggedInUser.getUser().getMessagingEnvironments().addAll(messagingEnvironmentList[0]);

            }

        };
        messagingEnvironmentsUpdater = Actions.actionLopper3(500, task, runnable);
        return messagingEnvironmentsUpdater;

    }

    public static
    ScheduledService scheduledUpdateFollowings(Runnable runnable) {
        final List <User>[] updatedUsers = new List[]{new LinkedList <>()};

        Runnable task = new Runnable() {
            @Override
            public
            void run() {


                Response response = ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.UPDATE_FOLLOWINGS, LoggedInUser.getUser().getToken(), LoggedInUser.getUser().getToken()));


                if (response.getType() == ResponseType.UPDATED_FOLLOWINGS) {

                    updatedUsers[0] = JSonController.stringToObjectMapper(response.getData(), new TypeReference <List <User>>() {
                    });

                    if (LoggedInUser.getUser().getFollowings() == null)
                        LoggedInUser.getUser().setFollowings(updatedUsers[0]);


                }
                if (updatedUsers[0].size() != LoggedInUser.getUser().getFollowings().size()) {
                    setChangedFollowings(true);
                }
                LoggedInUser.getUser().setFollowings(updatedUsers[0]);


            }
        };
        followingsUpdater = Actions.actionLopper3(5000, task, runnable);
        return followingsUpdater;
    }

    public static
    ScheduledService scheduledUpdateFollowers(Runnable runnable) {
        final List <User>[] updatedUsers = new List[]{new LinkedList <>()};

        Runnable task = new Runnable() {
            @Override
            public
            void run() {


                Response response = ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.UPDATE_FOLLOWERS, LoggedInUser.getUser().getToken(), LoggedInUser.getUser().getToken()));


                if (response.getType() == ResponseType.UPDATED_FOLLOWERS) {

                    updatedUsers[0] = JSonController.stringToObjectMapper(response.getData(), new TypeReference <List <User>>() {
                    });
                    //   System.out.println(updatedUsers[0].size());
                    if (LoggedInUser.getUser().getFollowers() == null)
                        LoggedInUser.getUser().setFollowers(updatedUsers[0]);


                }
                if (updatedUsers[0].size() != LoggedInUser.getUser().getFollowings().size()) {
                    setChangedFollowers(true);
                }
                LoggedInUser.getUser().setFollowers(updatedUsers[0]);


            }
        };
        followersUpdater = Actions.actionLopper3(7000, task, runnable);
        return followersUpdater;
    }

    public static
    Service updateMessagingEnvironments(Runnable runnable) {
        final List <MessagingEnvironment>[] messagingEnvironmentList = new List[]{new LinkedList <>()};
        Runnable task = new Runnable() {
            @Override
            public
            void run() {

                Response response = ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.UPDATE_MESSAGING_ENVIRONMENTS, null, LoggedInUser.getUser().getToken()));


                if (response.getType() == ResponseType.UPDATED_MESSAGING_ENVIRONMENTS) {

                    messagingEnvironmentList[0] = JSonController.stringToObjectMapper(response.getData(), new TypeReference <List <MessagingEnvironment>>() {
                    });

                }
                LoggedInUser.getUser().setMessagingEnvironments(new LinkedList <>());
                LoggedInUser.getUser().getMessagingEnvironments().addAll(messagingEnvironmentList[0]);

            }
        };
        Service service = Actions.executeOnce(task, runnable);
        service.start();
        return service;

    }

    public static
    Service updateFollowings(Runnable runnable) {
        final List <User>[] updatedUsers = new List[]{new LinkedList <>()};

        Runnable task = new Runnable() {
            @Override
            public
            void run() {


                Response response = ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.UPDATE_FOLLOWINGS, LoggedInUser.getUser().getToken(), LoggedInUser.getUser().getToken()));


                if (response.getType() == ResponseType.UPDATED_FOLLOWINGS) {

                    updatedUsers[0] = JSonController.stringToObjectMapper(response.getData(), new TypeReference <List <User>>() {
                    });

                    if (LoggedInUser.getUser().getFollowings() == null)
                        LoggedInUser.getUser().setFollowings(updatedUsers[0]);


                }
                if (updatedUsers[0].size() != LoggedInUser.getUser().getFollowings().size()) {
                    setChangedFollowings(true);
                }
                LoggedInUser.getUser().setFollowings(updatedUsers[0]);


            }
        };
        Service service = Actions.executeOnce(task, runnable);
        service.start();
        return service;
    }

    public static
    Service updateFollowers(Runnable runnable) {
        final List <User>[] updatedUsers = new List[]{new LinkedList <>()};

        Runnable task = new Runnable() {
            @Override
            public
            void run() {


                Response response = ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.UPDATE_FOLLOWERS, LoggedInUser.getUser().getToken(), LoggedInUser.getUser().getToken()));


                if (response.getType() == ResponseType.UPDATED_FOLLOWERS) {

                    updatedUsers[0] = JSonController.stringToObjectMapper(response.getData(), new TypeReference <List <User>>() {
                    });
                    //   System.out.println(updatedUsers[0].size());
                    if (LoggedInUser.getUser().getFollowers() == null)
                        LoggedInUser.getUser().setFollowers(updatedUsers[0]);


                }
                if (updatedUsers[0].size() != LoggedInUser.getUser().getFollowings().size()) {
                    setChangedFollowers(true);
                }
                LoggedInUser.getUser().setFollowers(updatedUsers[0]);


            }
        };
        Service service = Actions.executeOnce(task, runnable);
        service.start();
        return service;
    }

    public static
    Service updateCategories(Runnable runnable) {
        final List <Category>[] updatedCategories = new List[]{new LinkedList <>()};

        Runnable task = new Runnable() {
            @Override
            public
            void run() {


                Response response = ServerHandler.transmitter(new Request(getUser().getId(), RequestType.CATEGORIES, getUser().getId().toString(), getUser().getToken()));

                if (response.getType() == ResponseType.CATEGORIES) {
                    updatedCategories[0] = JSonController.stringToObjectMapper(response.getData(), new TypeReference <List <Category>>() {
                    });
                }
                getUser().setCategories(updatedCategories[0]);


            }
        };
        Service service = Actions.executeOnce(task, runnable);
        service.start();
        return service;
    }

    public static
    Service updateNotifications(Runnable runnable) {
        final List <Notification>[] updatedNotifications = new List[]{new LinkedList <>()};

        Runnable task = new Runnable() {
            @Override
            public
            void run() {


                Response response = ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.UPDATE_NOTIFICATIONS, LoggedInUser.getUser().getId().toString(), LoggedInUser.getUser().getToken()));


                if (response.getType() == ResponseType.UPDATED_NOTIFICATIONS) {

                    updatedNotifications[0] = JSonController.stringToObjectMapper(response.getData(), new TypeReference <List <Notification>>() {
                    });
                    LoggedInUser.getUser().setNotifications(updatedNotifications[0]);

                }
            }

            ;
        };
        Service service = Actions.executeOnce(task, runnable);
        service.start();
        return service;
    }

    public static
    Service updateRequests(Runnable runnable) {
        final List <User>[] updatedUsers = new List[]{new LinkedList <>()};

        Runnable task = new Runnable() {
            @Override
            public
            void run() {


                Response response = ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.UPDATE_REQUESTS, LoggedInUser.getUser().getId().toString(), LoggedInUser.getUser().getToken()));


                if (response.getType() == ResponseType.UPDATED_REQUESTS) {

                    updatedUsers[0] = JSonController.stringToObjectMapper(response.getData(), new TypeReference <List <User>>() {
                    });
                    LoggedInUser.getUser().setRequests(updatedUsers[0]);

                }
            }

            ;
        };
        Service service = Actions.executeOnce(task, runnable);
        service.start();
        return service;
    }

    public static
    Service updateRequesteds(Runnable runnable) {
        final List <User>[] updatedUsers = new List[]{new LinkedList <>()};

        Runnable task = new Runnable() {
            @Override
            public
            void run() {


                Response response = ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.UPDATE_REQUESTEDS, LoggedInUser.getUser().getId().toString(), LoggedInUser.getUser().getToken()));


                if (response.getType() == ResponseType.UPDATED_REQUESTEDS) {
                    System.out.println("updated Requesteds");
                    updatedUsers[0] = JSonController.stringToObjectMapper(response.getData(), new TypeReference <List <User>>() {
                    });
                    LoggedInUser.getUser().setRequesteds(updatedUsers[0]);

                }
            }

            ;
        };
        Service service = Actions.executeOnce(task, runnable);
        service.start();
        return service;
    }

    public static
    ScheduledService onlineRequest(Runnable runnable) {


        Runnable task = new Runnable() {
            @Override
            public
            void run() {


                Response response = ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.ONLINE, LoggedInUser.getUser().getId().toString(), LoggedInUser.getUser().getToken()));


                if (response!=null&&response.getType() == ResponseType.ONLINE) {

                    LoggedInUser.getUser().setLastSeen(JSonController.stringToObjectMapper(response.getData(), LocalDateTime.class));
                    //    System.out.println("onlone");

                }
            }

            ;
        };
        onlineUpdater = Actions.actionLopper3(3000, task, runnable);

        return onlineUpdater;
    }

    public static
    boolean isChangedFollowings() {
        return changedFollowings;
    }

    public static
    void setChangedFollowings(boolean changedFollowings) {
        LoggedInUser.changedFollowings = changedFollowings;
    }

    public static
    boolean isChangedFollowers() {
        return changedFollowers;
    }

    public static
    void setChangedFollowers(boolean changedFollowers) {
        LoggedInUser.changedFollowers = changedFollowers;
    }

    public static
    boolean isChangedMessagingEnvironments() {
        return changedMessagingEnvironments;
    }

    public static
    void setChangedMessagingEnvironments(boolean changedMessagingEnvironments) {
        LoggedInUser.changedMessagingEnvironments = changedMessagingEnvironments;
    }

    public static
    ScheduledService getFollowingsUpdater() {
        return followingsUpdater;
    }

    public static
    ScheduledService getFollowersUpdater() {
        return followersUpdater;
    }

    public static
    ScheduledService getMessagingEnvironmentsUpdater() {
        return messagingEnvironmentsUpdater;
    }

    public static
    ScheduledService getNotificationsUpdater() {
        return notificationsUpdater;
    }

    public static
    ScheduledService getOnlineUpdater() {
        return onlineUpdater;
    }

    private static
    boolean messagingEnvironmentsChangeFinder(List <MessagingEnvironment> olds, List <MessagingEnvironment> news) {
//        System.out.println(olds.size());
//        System.out.println(news.size());
        if (news.size() != olds.size()) return true;

        for (int i = 0; i < news.size(); i++) {
            //System.out.println(i);
            if (!news.get(i).getUnseenCounter().equals(olds.get(i).getUnseenCounter())) return true;
            //System.out.println(news.get(i).getId()+"..."+olds.get(i).getId());
            if (!news.get(i).getId().equals(olds.get(i).getId())) return true;
            if (news.get(i).isGroup() && olds.get(i).isGroup()) {
                if (!news.get(i).getName().equals(olds.get(i).getName())) return true;
                if (!Arrays.equals(news.get(i).getImage(), olds.get(i).getImage())) return true;
            } else {
               // System.out.println(news.get(i).getOwner());
            if(news.get(i).getTaker()!=null &&olds.get(i).getTaker()!=null){
                if (!news.get(i).getTaker().getFullName().equals(olds.get(i).getTaker().getFullName())) return true;
                if (!Arrays.equals(news.get(i).getTaker().getImage(), olds.get(i).getTaker().getImage())) return true;}
            }


            if (news.get(i).getLastMessage() != null && olds.get(i) != null) {
                //  System.out.println(news.get(i).isLastMessageSeen()+"..."+olds.get(i).isLastMessageSeen());
                if (news.get(i).isLastMessageSeen() != olds.get(i).isLastMessageSeen()) return true;
                //System.out.println(news.get(i).getLastMessage().getId() + "..." + olds.get(i).getLastMessage().getId());
                if (!news.get(i).getLastMessage().getId().equals(olds.get(i).getLastMessage().getId())) return true;
            } else {
            }
        }
        return false;
    }

    public
    Service updateBlockers(Runnable runnable) {
        final List <User>[] updatedUsers = new List[]{new LinkedList <>()};

        Runnable task = new Runnable() {
            @Override
            public
            void run() {


                Response response = ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.UPDATE_BLOCKERS, LoggedInUser.getUser().getId().toString(), LoggedInUser.getUser().getToken()));


                if (response.getType() == ResponseType.UPDATED_BLOCKERS) {

                    updatedUsers[0] = JSonController.stringToObjectMapper(response.getData(), new TypeReference <List <User>>() {
                    });
                    LoggedInUser.getUser().setBlockers(updatedUsers[0]);

                }
            }

            ;
        };
        Service service = Actions.executeOnce(task, runnable);
        service.start();
        return service;
    }

    public
    Service updateBlockeds(Runnable runnable) {
        final List <User>[] updatedUsers = new List[]{new LinkedList <>()};

        Runnable task = new Runnable() {
            @Override
            public
            void run() {


                Response response = ServerHandler.transmitter(new Request(LoggedInUser.getUser().getId(), RequestType.UPDATE_BLOCKEDS, LoggedInUser.getUser().getId().toString(), LoggedInUser.getUser().getToken()));


                if (response.getType() == ResponseType.UPDATED_BLOCKEDS) {

                    updatedUsers[0] = JSonController.stringToObjectMapper(response.getData(), new TypeReference <List <User>>() {
                    });
                    LoggedInUser.getUser().setBlockeds(updatedUsers[0]);

                }
            }

            ;
        };
        Service service = Actions.executeOnce(task, runnable);
        service.start();
        return service;
    }


    public static
    Boolean getOffline() {
        return offline;
    }

    public static
    void setOffline(Boolean offline) {
        LoggedInUser.offline = offline;
    }

    public static
    void setFollowingsUpdater(ScheduledService followingsUpdater) {
        LoggedInUser.followingsUpdater = followingsUpdater;
    }

    public static
    void setFollowersUpdater(ScheduledService followersUpdater) {
        LoggedInUser.followersUpdater = followersUpdater;
    }

    public static
    void setMessagingEnvironmentsUpdater(ScheduledService messagingEnvironmentsUpdater) {
        LoggedInUser.messagingEnvironmentsUpdater = messagingEnvironmentsUpdater;
    }

    public static
    void setNotificationsUpdater(ScheduledService notificationsUpdater) {
        LoggedInUser.notificationsUpdater = notificationsUpdater;
    }

    public static
    void setOnlineUpdater(ScheduledService onlineUpdater) {
        LoggedInUser.onlineUpdater = onlineUpdater;
    }
    public static void sendOfflineMessages(){
        try {
            System.out.println("start sending offline messages:"+context.Messages.getOfflineMessages().size());
            MessagingPageController messagingPageController=new MessagingPageController(null);
            List<Message> offlineMessages=context.Messages.getOfflineMessages();
            ListIterator<Message> listIterator= offlineMessages.listIterator();
            while (listIterator.hasNext()){
                Message message=listIterator.next();
                Integer postId=null;
                Integer originalMessageId=null;
                if(message.getPost()!=null)postId=message.getPost().getId();
                if(message.getOriginalMessage()!=null)originalMessageId=message.getOriginalMessage().getId();
                NewMessageEvent newMessageEvent=new NewMessageEvent(message.getOwner().getId(),message.getMessagingEnvironment().getId(),message.getText(),originalMessageId,message.getImage(),postId);
                messagingPageController.newMessage(newMessageEvent,null);
                listIterator.remove();

            }
            messagingPageController=null;
            System.out.println("finished sending offline messages");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
