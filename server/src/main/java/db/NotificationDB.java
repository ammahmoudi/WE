package db;

import model.Notification;
import model.User;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

public
class NotificationDB implements DBSet<Notification>{

    @Override
    public
    Notification get(int id) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <Notification> criteriaQuery = criteriaBuilder.createQuery(Notification.class);
        Root <Notification> root = criteriaQuery.from(Notification.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), id));
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();
        session.getTransaction().commit();
        session.close();

        if (!results.isEmpty()) {
            return (Notification) results.get(0);
        }
        return null;
    }



  @Override
  public
  LinkedList <Notification> all() {
        return null;
  }


    @Override
    public
    void add(Notification notification) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(notification);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public
    void remove(Notification notification) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(notification);
        session.getTransaction().commit();
        session.close();

    }

    @Override
    public
    void update(Notification notification) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
      //  user=session.get(User,user.getId());
        session.update(notification);
        session.getTransaction().commit();
        session.close();

    }

}
