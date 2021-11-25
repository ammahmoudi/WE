package db;


import model.*;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public
class UserDB implements DBSet <User> {

    @Override
    public
    User get(int id) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root <User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), id));
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();
        session.getTransaction().commit();
        session.close();

        if (!results.isEmpty()) {
            return (User) results.get(0);
        }
        return null;
    }

    public
    void serverSeen (int id) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root <User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), id));
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();

User user=null;
        if (!results.isEmpty()) {
            user=(User) results.get(0);
        }
        for(MessagingEnvironment messagingEnvironment:user.getMessagingEnvironments()){
            for(Message message:messagingEnvironment.getMessages()){
                if(message.getOwner().equals(user))continue;
                if(message.getServerSeen())continue;
                message.setServerSeen(true);
            }
            session.update(messagingEnvironment);
        }

        session.getTransaction().commit();
        session.close();
    }
    public
    RelationshipType
    getRelationship(int fromId, int toId) {
        User from = null;
        User to = null;
        RelationshipType relationshipType;
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root <User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), fromId));
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();

        if (!results.isEmpty()) {
            from = (User) results.get(0);
        }
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), toId));
        Query query2 = session.createQuery(criteriaQuery);
        List results2 = query2.getResultList();

        if (!results2.isEmpty()) {
            to = (User) results2.get(0);
        }
        relationshipType = from.getRelationshipTo(to);
        session.getTransaction().commit();
        session.close();
        return relationshipType;
    }

    public
    RelationshipType
    follow(int fromId, int toId) {
        User from = null;
        User to = null;
        RelationshipType relationshipType;
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root <User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), fromId));
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();

        if (!results.isEmpty()) {
            from = (User) results.get(0);
        }
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), toId));
        Query query2 = session.createQuery(criteriaQuery);
        List results2 = query2.getResultList();

        if (!results2.isEmpty()) {
            to = (User) results2.get(0);
        }
        from.follow(to);
        session.update(from);
        relationshipType = from.getRelationshipTo(to);
        session.getTransaction().commit();
        session.close();
        return relationshipType;
    }

    public
    RelationshipType
    applyRequest(int fromId, int toId, boolean accept) {
        User from = null;
        User to = null;
        RelationshipType relationshipType;
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        from = session.get(User.class, fromId);
        to = session.get(User.class, toId);
        if (accept) from.accept(to);
        else from.deny(to);
        session.update(from);
        relationshipType = to.getRelationshipTo(from);
        session.getTransaction().commit();
        session.close();
        return relationshipType;
    }


    public
    RelationshipType
    block(int fromId, int toId) {
        User from = null;
        User to = null;
        RelationshipType relationshipType;
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root <User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), fromId));
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();

        if (!results.isEmpty()) {
            from = (User) results.get(0);
        }
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), toId));
        Query query2 = session.createQuery(criteriaQuery);
        List results2 = query2.getResultList();

        if (!results2.isEmpty()) {
            to = (User) results2.get(0);
        }
        from.block(to);
        session.update(from);
        relationshipType = from.getRelationshipTo(to);
        session.getTransaction().commit();
        session.close();
        return relationshipType;
    }

    public
    RelationshipType
    mute(int fromId, int toId) {
        User from = null;
        User to = null;
        RelationshipType relationshipType;
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root <User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), fromId));
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();

        if (!results.isEmpty()) {
            from = (User) results.get(0);
        }
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), toId));
        Query query2 = session.createQuery(criteriaQuery);
        List results2 = query2.getResultList();

        if (!results2.isEmpty()) {
            to = (User) results2.get(0);
        }
        from.silent(to);
        session.update(from);
        relationshipType = from.getRelationshipTo(to);
      //  System.out.println(relationshipType);
        session.getTransaction().commit();
        session.close();
        return relationshipType;
    }

    public
    User getByuserName(String userName) {

        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root <User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("userName"), userName));
        Query query = session.createQuery(criteriaQuery);
        List results;

        results = query.getResultList();
        User user = null;
        if (!results.isEmpty()) {
            user=(User) results.get(0);
           // System.out.println(user.getFollowers().size());
        }
        session.getTransaction().commit();
        session.close();

      return  user;

    }

    public
    <T> List <T> getList(String loggedInUserToken, Class <T> type, String mode, Integer userId) {

        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root <User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("token"), loggedInUserToken));
        Query query = session.createQuery(criteriaQuery);
        List results;

        results = query.getResultList();
        User user = null;
        User loggedInUser = null;
        user = session.get(User.class, userId);
        if (!results.isEmpty()) {

            loggedInUser = (User) results.get(0);
        }
        List <T> list = new LinkedList <>();
        if (MessagingEnvironment.class.equals(type)) {
            List <MessagingEnvironment> messagingEnvironmentList = null;
            switch (mode) {
                case "MessagingEnvironments":
                    messagingEnvironmentList = user.getMessagingEnvironments();

                    for (MessagingEnvironment messagingEnvironment : messagingEnvironmentList) {
                        messagingEnvironment.setLastMessage(messagingEnvironment.applyLastMessage());
                        messagingEnvironment.applySavedMessage();
                        messagingEnvironment.applyTaker(loggedInUser);
                        messagingEnvironment.setUnseenCounter(messagingEnvironment.getUnseenMessagesNumberBy(loggedInUser));
                        messagingEnvironment.setLastMessageCreationDateString(messagingEnvironment.getLastMessageCreatedDateString());
                        if(messagingEnvironment.getLastMessage()!=null){
                        messagingEnvironment.setLastMessageSeen(!messagingEnvironment.getLastMessage().getSeenList().isEmpty());}else{
                            messagingEnvironment.setLastMessageSeen(false);
                        }if(messagingEnvironment.isGroup()) {
                            User owner = messagingEnvironment.getOwner();
                            switch (owner.getLastSeenPrivacy()) {
                                case EVERYONE:
                                    break;
                                case NOBODY:
                                    owner.setLastSeen(null);
                                    break;
                                case ONLY_FOLLOWINGS:
                                    RelationshipType relationshipType = owner.getRelationshipTo(loggedInUser);
                                    if (relationshipType != RelationshipType.FOLLOWING && relationshipType != RelationshipType.FOLLOWING_MUTED) {
                                        owner.setLastSeen(null);
                                    }
                                    break;

                            }

                        }
                    }
                    messagingEnvironmentList.sort(Comparator.comparing(MessagingEnvironment::getLastMessageCreatedDate).reversed());

                    break;
            }
            list = (List <T>) messagingEnvironmentList;
        }
        if (User.class.equals(type)) {
            List <User> userList = null;
            switch (mode) {
                case "Followings":

                    userList = user.getFollowings();
                    break;
                case "Followers":
                    userList = user.getFollowers();
                    break;
                case "Requests":
                    userList = user.getRequests();
                    break;
                case "Requesteds":
                    userList = user.getRequesteds();
                    break;
                case "Blockers":
                    userList = user.getBlockers();
                    break;
                case "Blockeds":
                    userList = user.getBlockeds();
                    break;
                case "Silenteds":
                    userList = user.getSilenteds();
            }
            for(User x:userList){
                switch (x.getLastSeenPrivacy()){
                    case EVERYONE:break;
                    case NOBODY:x.setLastSeen(null);break;
                    case ONLY_FOLLOWINGS:
                        RelationshipType relationshipType=x.getRelationshipTo(loggedInUser);
                        if(relationshipType!=RelationshipType.FOLLOWING&&relationshipType!=RelationshipType.FOLLOWING_MUTED){
                            x.setLastSeen(null);
                        }
                        break;

                }
            }
            list = (List <T>) userList;
        }
        if (Category.class.equals(type)) {
            List <Category> categoryList = user.getCategories();
            for (Category category : categoryList) {
                category.setNumberOfUsers(category.getUserList().size());
                for (User x : category.getUserList()) {
                    switch (x.getLastSeenPrivacy()) {
                        case EVERYONE:
                            break;
                        case NOBODY:
                            x.setLastSeen(null);
                            break;
                        case ONLY_FOLLOWINGS:
                            RelationshipType relationshipType = x.getRelationshipTo(loggedInUser);
                            if (relationshipType != RelationshipType.FOLLOWING && relationshipType != RelationshipType.FOLLOWING_MUTED) {
                                x.setLastSeen(null);
                            }
                            break;

                    }
                }
            }
            list = (List <T>) categoryList;
        }
        if (Notification.class.equals(type)) {
            List <Notification> notifications = user.getNotifications();
            for (Notification notification : notifications) {
                notification.setSenderUser(session.get(User.class, notification.getSenderId()));
                User x=notification.getSenderUser();
                switch (x.getLastSeenPrivacy()) {
                    case EVERYONE:
                        break;
                    case NOBODY:
                        x.setLastSeen(null);
                        break;
                    case ONLY_FOLLOWINGS:
                        RelationshipType relationshipType = x.getRelationshipTo(loggedInUser);
                        if (relationshipType != RelationshipType.FOLLOWING && relationshipType != RelationshipType.FOLLOWING_MUTED) {
                            x.setLastSeen(null);
                        }
                        break;

                }
            }
            list = (List <T>) notifications;
        }
        list.size();
        session.getTransaction().commit();
        session.close();
if(list==null)list=new LinkedList <>();
        return list;
    }

    public
    User getByToken(String token) {

        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root <User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("token"), token));
        Query query = session.createQuery(criteriaQuery);
        List results;
        results = query.getResultList();
        session.getTransaction().commit();
        session.close();

        if (!results.isEmpty()) {
            return (User) results.get(0);
        }
        return null;
    }

    public
    LinkedList <User> search(String text) {

        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root <User> root = criteriaQuery.from(User.class);
        Predicate inFirstName = criteriaBuilder.like(root.get("firstName"), "%" + text + "%");
        Predicate inLastName = criteriaBuilder.like(root.get("lastName"), "%" + text + "%");
        Predicate inUserName = criteriaBuilder.like(root.get("userName"), "%" + text + "%");
        criteriaQuery.select(root).where(criteriaBuilder.or(inFirstName, inLastName, inUserName));
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();
        session.getTransaction().commit();
        session.close();
        LinkedList <User> users = new LinkedList <User>();
        users.addAll(results);
        return users;
    }

    public
    User getByEmail(String email) {

        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root <User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("email"), email));
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();
        session.getTransaction().commit();
        session.close();

        if (!results.isEmpty()) {
            return (User) results.get(0);
        }
        return null;
    }

    @Override
    public
    LinkedList <User> all() {
        return null;
    }

    @Override
    public
    void add(User user) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public
    void remove(User user) {

    }

    @Override
    public
    void update(User user) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        //  user=session.get(User,user.getId());
        session.update(user);
        session.getTransaction().commit();
        session.close();

    }
}
