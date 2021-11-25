package db;

import event.messaging.NewScheduledMessageEvent;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

public
class ScheduledMessageDB implements DBSet <NewScheduledMessageEvent> {

    @Override
    public
    NewScheduledMessageEvent get(int id) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();

        NewScheduledMessageEvent newScheduledMessageEvent = session.get(NewScheduledMessageEvent.class, id);
        session.getTransaction().commit();
        session.close();

        return newScheduledMessageEvent;
    }

    @Override
    public
    LinkedList <NewScheduledMessageEvent> all() {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <NewScheduledMessageEvent> criteriaQuery = criteriaBuilder.createQuery(NewScheduledMessageEvent.class);
        Root <NewScheduledMessageEvent> root = criteriaQuery.from(NewScheduledMessageEvent.class);
        criteriaQuery.select(root);
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();
        System.out.println(results.size());
        return new LinkedList <>(results);
    }


    @Override
    public
    void add(NewScheduledMessageEvent newScheduledMessageEvent) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(newScheduledMessageEvent);
        session.getTransaction().commit();
        session.close();


    }

    @Override
    public
    void remove(NewScheduledMessageEvent newScheduledMessageEvent) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        if (newScheduledMessageEvent != null) {
            newScheduledMessageEvent = session.get(NewScheduledMessageEvent.class, newScheduledMessageEvent.getId());

            session.remove(newScheduledMessageEvent);
        }
        session.getTransaction().commit();
        session.close();

    }

    @Override
    public
    void update(NewScheduledMessageEvent newScheduledMessageEvent) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        session.saveOrUpdate(newScheduledMessageEvent);
        session.getTransaction().commit();
        session.close();

    }


}
