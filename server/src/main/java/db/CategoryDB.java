package db;

import model.Category;
import model.RelationshipType;
import model.User;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

public
class CategoryDB implements DBSet<Category>{

    @Override
    public
    Category get(int id) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <Category> criteriaQuery = criteriaBuilder.createQuery(Category.class);
        Root <Category> root = criteriaQuery.from(Category.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), id));
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();
        Category category=(Category) results.get(0);
        category.setNumberOfUsers(category.getUserList().size());
        for(User user :category.getUserList()){
            switch (user.getLastSeenPrivacy()){
                case EVERYONE:break;
                case NOBODY:user.setLastSeen(null);break;
                case ONLY_FOLLOWINGS:

                    user.setLastSeen(null);break;
            }
        }
        session.getTransaction().commit();
        session.close();

        if (!results.isEmpty()) {
            return category;
        }
        return null;
    }



    @Override
    public
    LinkedList <Category> all() {
        return null;
    }

    @Override
    public
    void add(Category category) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(category);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public
    void remove(Category category) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        session.remove(category);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public
    void update(Category category) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
      //  user=session.get(User,user.getId());
        session.update(category);
        session.getTransaction().commit();
        session.close();

    }
}
