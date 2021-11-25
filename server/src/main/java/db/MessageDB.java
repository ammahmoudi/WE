package db;

import model.Message;
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
class MessageDB implements DBSet<Message>{

    @Override
    public
    Message get(int id) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <Message> criteriaQuery = criteriaBuilder.createQuery(Message.class);
        Root <Message> root = criteriaQuery.from(Message.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), id));
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();
        Message message = null;
        if (!results.isEmpty()) {

            message = (Message) results.get(0);
            message.getSeenList();
            message.getSeenList().size();
        }
        session.getTransaction().commit();
        session.close();

        if (!results.isEmpty()) {
            return message;
        }
        return null;
    }

    public
     void forward(int originalMessageId,int childMessageId) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <Message> criteriaQuery = criteriaBuilder.createQuery(Message.class);
        Root <Message> root = criteriaQuery.from(Message.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), originalMessageId));
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();
        Message originalMessage = null;
        if (!results.isEmpty()) {
            originalMessage = (Message) results.get(0);

        }


        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), childMessageId));
        Query query2 = session.createQuery(criteriaQuery);
        List results2 = query2.getResultList();
        Message childMessage = null;
        if (!results2.isEmpty()) {
            childMessage = (Message) results2.get(0);

        }
        originalMessage.forward(childMessage);
        session.update(childMessage);
        session.getTransaction().commit();
        session.close();


    }



public void seenBy(int messageId,int userId){
    Session session = DBTools.getSessionFactory().openSession();
    session.beginTransaction();
    CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
    CriteriaQuery <Message> criteriaQuery = criteriaBuilder.createQuery(Message.class);
    Root <Message> root = criteriaQuery.from(Message.class);
    criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), messageId));
    Query query = session.createQuery(criteriaQuery);
    List results = query.getResultList();
    Message message = null;
    User user = null;
    if (!results.isEmpty()) {

        message = (Message) results.get(0);
        message.getSeenList();
        CriteriaBuilder criteriaBuilder2 = session.getCriteriaBuilder();
        CriteriaQuery <User> criteriaQuery2 = criteriaBuilder2.createQuery(User.class);
        Root <User> root2 = criteriaQuery2.from(User.class);
        criteriaQuery2.select(root2).where(criteriaBuilder2.equal(root.get("id"), userId));
        Query query2 = session.createQuery(criteriaQuery2);
        List results2 = query2.getResultList();

        if (!results2.isEmpty()) {

            user = (User) results2.get(0);
            message.seenBy(user);
            session.update(message);
        }
    }



    session.getTransaction().commit();
    session.close();

}




  @Override
  public
  LinkedList <Message> all() {
        return null;
  }

    @Override
    public
    void add(Message message) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(message);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public
    void remove(Message message) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(message);
        session.getTransaction().commit();
        session.close();

    }

    @Override
    public
    void update(Message message) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
      //  user=session.get(User,user.getId());
        session.update(message);
        session.getTransaction().commit();
        session.close();

    }
    public
    List <Message> messages(User from, User to) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <Message> criteriaQuery = criteriaBuilder.createQuery(Message.class);
        Root <Message> root = criteriaQuery.from(Message.class);
        criteriaQuery.select(root).where(criteriaBuilder.and(criteriaBuilder.equal(root.get("taker"), to), criteriaBuilder.equal(root.get("owner"), from)));
        Query query = session.createQuery(criteriaQuery);
        List <Message> results = (List <Message>) query.getResultList();
        session.getTransaction().commit();
        session.close();
        results.sort(Comparator.comparing(Message::getCreation_date).reversed());
        return results;
    }


}
