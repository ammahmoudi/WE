package db;


import model.Message;
import model.MessagingEnvironment;
import model.RelationshipType;
import model.User;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public
class MessagingEnvironmentDB implements DBSet <MessagingEnvironment> {

    @Override
    public
    MessagingEnvironment get(int id) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <MessagingEnvironment> criteriaQuery = criteriaBuilder.createQuery(MessagingEnvironment.class);
        Root <MessagingEnvironment> root = criteriaQuery.from(MessagingEnvironment.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), id));
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();
        MessagingEnvironment messagingEnvironment = null;
        if (!results.isEmpty()) {

            messagingEnvironment = (MessagingEnvironment) results.get(0);
            if(messagingEnvironment.isGroup()) {
                User user = messagingEnvironment.getOwner();
                switch (user.getLastSeenPrivacy()) {
                    case EVERYONE:
                        break;
                    case NOBODY:
                        user.setLastSeen(null);
                        break;
                    case ONLY_FOLLOWINGS:

                        user.setLastSeen(null);

                        break;
                }
            }
        }

        session.getTransaction().commit();
        session.close();


        return messagingEnvironment;
    }

    public
    MessagingEnvironment getPrivateChat(int id, int id2) {
        MessagingEnvironment messagingEnvironment;
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        User user1 = session.get(User.class, id);
        User user2 = session.get(User.class, id2);
        if (user1.getPrivateChatWith(user2) != null) {
            messagingEnvironment = user1.getPrivateChatWith(user2);

        } else {
            messagingEnvironment = new MessagingEnvironment(user1.getFullName() + "," + user2.getFullName(), null);
            messagingEnvironment.addMember(user1);
            messagingEnvironment.addMember(user2);
            session.save(messagingEnvironment);

        }
        messagingEnvironment.setLastMessage(messagingEnvironment.applyLastMessage());
        messagingEnvironment.applySavedMessage();
        messagingEnvironment.applyTaker(user1);
        messagingEnvironment.setUnseenCounter(messagingEnvironment.getUnseenMessagesNumberBy(user1));
        messagingEnvironment.setLastMessageCreationDateString(messagingEnvironment.getLastMessageCreatedDateString());
        messagingEnvironment.applyLastMessageSeen();
        if(messagingEnvironment.isGroup()) {
            switch (messagingEnvironment.getOwner().getLastSeenPrivacy()) {
                case EVERYONE:
                    break;
                case NOBODY:
                    messagingEnvironment.getOwner().setLastSeen(null);
                    break;
                case ONLY_FOLLOWINGS:
                    RelationshipType relationshipType = messagingEnvironment.getOwner().getRelationshipTo(user1);
                    if (relationshipType != RelationshipType.FOLLOWING && relationshipType != RelationshipType.FOLLOWING_MUTED) {
                        messagingEnvironment.getOwner().setLastSeen(null);

                        break;

                    }
            }
        }      for(User user :messagingEnvironment.getUsers()){
            switch (user.getLastSeenPrivacy()){
                case EVERYONE:break;
                case NOBODY:user.setLastSeen(null);break;
                case ONLY_FOLLOWINGS:
                    RelationshipType relationshipType=user.getRelationshipTo(user1);
                    if(relationshipType!=RelationshipType.FOLLOWING&&relationshipType!=RelationshipType.FOLLOWING_MUTED){
                        user.setLastSeen(null);
                    }
                    break;
            }
        }
        session.getTransaction().commit();
        session.close();

        return messagingEnvironment;
    }

    public
    <T> List <T> getList(int id, Class <T> type, User loggedInUser) {

        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <MessagingEnvironment> criteriaQuery = criteriaBuilder.createQuery(MessagingEnvironment.class);
        Root <MessagingEnvironment> root = criteriaQuery.from(MessagingEnvironment.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), id));
        Query query = session.createQuery(criteriaQuery);
        List results;
        results = query.getResultList();
        MessagingEnvironment messagingEnvironment = (MessagingEnvironment) results.get(0);
        List <T> list = new LinkedList <>();
        if (Message.class.equals(type)) {
            List <Message> messageList = messagingEnvironment.getMessages();
            messageList.sort(Comparator.comparing(Message::getCreation_date));
            for (Message message : messageList) {
                message.applyFinalSeen();
                switch (message.getOwner().getLastSeenPrivacy()) {
                    case EVERYONE:
                        break;
                    case NOBODY:
                        message.getOwner().setLastSeen(null);
                        break;
                    case ONLY_FOLLOWINGS:
                        if(loggedInUser!=null){
                        RelationshipType relationshipType = message.getOwner().getRelationshipTo(loggedInUser);
                        if (relationshipType != RelationshipType.FOLLOWING && relationshipType != RelationshipType.FOLLOWING_MUTED) {
                            message.getOwner().setLastSeen(null);
                        }}
                        break;

                }
            }
            list = (List <T>) messageList;
        }
        if (User.class.equals(type)) {
            list = (List <T>) messagingEnvironment.getUsers();
        }
        list.size();
        session.getTransaction().commit();
        session.close();

        return list;
    }

    @Override
    public
    LinkedList <MessagingEnvironment> all() {
        return null;
    }

    @Override
    public
    void add(MessagingEnvironment messagingEnvironment) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(messagingEnvironment);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public
    void remove(MessagingEnvironment messagingEnvironment) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(messagingEnvironment);
        session.getTransaction().commit();
        session.close();

    }

    @Override
    public
    void update(MessagingEnvironment messagingEnvironment) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        //  user=session.get(User,user.getId());
        session.update(messagingEnvironment);
        session.getTransaction().commit();
        session.close();

    }

    public
    List <User> addMember(int id, List <Integer> userIds) {

        MessagingEnvironment messagingEnvironment = null;
        List <User> users = new LinkedList <>();
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <MessagingEnvironment> criteriaQuery = criteriaBuilder.createQuery(MessagingEnvironment.class);
        Root <MessagingEnvironment> root = criteriaQuery.from(MessagingEnvironment.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), id));
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();


        if (!results.isEmpty()) {
            messagingEnvironment = (MessagingEnvironment) results.get(0);
        }


        for (Integer userId : userIds) {
            CriteriaQuery <User> criteriaQuery2 = criteriaBuilder.createQuery(User.class);
            Root <User> root2 = criteriaQuery2.from(User.class);
            criteriaQuery2.select(root2).where(criteriaBuilder.equal(root2.get("id"), userId));
            Query query2 = session.createQuery(criteriaQuery2);
            List results2 = query2.getResultList();
            users.add((User) results2.get(0));

        }
        for (User user : users) {
            messagingEnvironment.addMember(user);
        }
        session.update(messagingEnvironment);
        session.getTransaction().commit();
        session.close();

        return messagingEnvironment.getUsers();
    }

    public
    List <User> removeMember(int id, List <Integer> userIds) {

        MessagingEnvironment messagingEnvironment = null;
        List <User> users = new LinkedList <>();
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <MessagingEnvironment> criteriaQuery = criteriaBuilder.createQuery(MessagingEnvironment.class);
        Root <MessagingEnvironment> root = criteriaQuery.from(MessagingEnvironment.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), id));
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();


        if (!results.isEmpty()) {
            messagingEnvironment = (MessagingEnvironment) results.get(0);
        }


        for (Integer userId : userIds) {
            CriteriaQuery <User> criteriaQuery2 = criteriaBuilder.createQuery(User.class);
            Root <User> root2 = criteriaQuery2.from(User.class);
            criteriaQuery2.select(root2).where(criteriaBuilder.equal(root2.get("id"), userId));
            Query query2 = session.createQuery(criteriaQuery2);
            List results2 = query2.getResultList();
            users.add((User) results2.get(0));

        }
        for (User user : users) {
            messagingEnvironment.removeMember(user);
        }
        session.update(messagingEnvironment);
        session.getTransaction().commit();
        session.close();

        return messagingEnvironment.getUsers();
    }


}
