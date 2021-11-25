package db;

import model.Message;
import model.User;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
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
        session.getTransaction().commit();
        session.close();

        if (!results.isEmpty()) {
            return (Message) results.get(0);
        }
        return null;
    }
    public
    int getLastId() {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <Integer> criteriaQuery = criteriaBuilder.createQuery(Integer.class);
        Root <Message> root = criteriaQuery.from(Message.class);

        criteriaQuery.select(criteriaBuilder.max(root.get("id")));
        Query query = session.createQuery(criteriaQuery);
        Integer result = (Integer) query.getSingleResult();
        session.getTransaction().commit();
        session.close();

        return result;
    }

    public
    List<Message> getOfflineMessages() {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <Message> criteriaQuery = criteriaBuilder.createQuery(Message.class);
        Root <Message> root = criteriaQuery.from(Message.class);
        criteriaQuery.select(root).where(criteriaBuilder.isNull(root.get("serverSeen")));
        Query query = session.createQuery(criteriaQuery);
        List<Message> results = query.getResultList();
        session.getTransaction().commit();
        session.close();

       return results;
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
        session.saveOrUpdate(message);
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
