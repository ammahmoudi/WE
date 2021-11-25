package db;

import model.LoggedInUser;
import model.MessagingEnvironment;
import model.User;
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
class UserDB implements DBSet<User>{

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
        User user = null;
        if (!results.isEmpty()) {
            user= (User) results.get(0);
            user.getMessagingEnvironments();

        //    System.out.println(user.getMessagingEnvironments().size());
            for(MessagingEnvironment messagingEnvironment:user.getMessagingEnvironments()){
              messagingEnvironment.applyTaker(LoggedInUser.getUser());
                messagingEnvironment.applySavedMessage();
                messagingEnvironment.applyLastMessage();
                messagingEnvironment.setUnseenCounter( messagingEnvironment.getMessages().size());

              messagingEnvironment.setMessages(  messagingEnvironment.getMessages());
               messagingEnvironment.setUsers( messagingEnvironment.getUsers());
            }
            user.getMessagingEnvironments().sort(Comparator.comparing(MessagingEnvironment::getLastMessageCreatedDate).reversed());
        }

        session.getTransaction().commit();
        session.close();


        return user;
    }
public void clearDB(){
    Session session = DBTools.getSessionFactory().openSession();
    session.beginTransaction();

    String stringQuery = "delete Message";
    Query query = session.createQuery(stringQuery);
    query.executeUpdate();
    stringQuery="delete Post ";
    query=session.createQuery(stringQuery);
    query.executeUpdate();
stringQuery="delete MessagingEnvironment";
query=session.createQuery(stringQuery);
query.executeUpdate();
    stringQuery="delete User";
    query=session.createQuery(stringQuery);
    query.executeUpdate();
    session.getTransaction().commit();
    session.close();
    }
  public
  User getByuserName(String userName){

        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <User> criteriaQuery= criteriaBuilder.createQuery(User.class);
        Root <User> root =criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("userName"),userName));
        Query query = session.createQuery(criteriaQuery);
        List results;
      results = query.getResultList();
      session.getTransaction().commit();
        session.close();

        if (!results.isEmpty()){
            return (User) results.get(0);
        }
        return null;
    }
    public   LinkedList<User> search(String text){

        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <User> criteriaQuery= criteriaBuilder.createQuery(User.class);
        Root <User> root =criteriaQuery.from(User.class);
        Predicate inFirstName = criteriaBuilder.like(root.get("firstName"), "%"+text+"%");
        Predicate inLastName = criteriaBuilder.like(root.get("lastName"), "%"+text+"%");
        Predicate inUserName = criteriaBuilder.like(root.get("userName"), "%"+text+"%");
        criteriaQuery.select(root).where(criteriaBuilder.or(inFirstName,inLastName,inUserName));
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();
        session.getTransaction().commit();
        session.close();
        LinkedList<User> users=new LinkedList <User>();
       users.addAll(results) ;
       return users;
    }
    public
    User getByEmail(String email){

        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <User> criteriaQuery= criteriaBuilder.createQuery(User.class);
        Root <User> root =criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("email"),email));
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();
        session.getTransaction().commit();
        session.close();

        if (!results.isEmpty()){
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

        session.saveOrUpdate(user);
        session.getTransaction().commit();
        session.close();

    }
}
