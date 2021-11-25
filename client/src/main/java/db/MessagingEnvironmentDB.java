package db;

import model.Message;
import model.MessagingEnvironment;
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
class MessagingEnvironmentDB implements DBSet<MessagingEnvironment>{

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
        MessagingEnvironment result = (MessagingEnvironment) query.getSingleResult();
        session.getTransaction().commit();
        session.close();

        if (result!=null) {
            result.getMessages().sort(Comparator.comparing(Message::getCreation_date));
            return result;
        }
        return null;
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
        session.saveOrUpdate(messagingEnvironment);
        session.getTransaction().commit();
        session.close();

    }

}
