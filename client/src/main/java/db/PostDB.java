package db;

import model.Post;
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
class PostDB implements DBSet<Post>{

    @Override
    public
    Post get(int id) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <Post> criteriaQuery = criteriaBuilder.createQuery(Post.class);
        Root <Post> root = criteriaQuery.from(Post.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), id));
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();
        session.getTransaction().commit();
        session.close();

        if (!results.isEmpty()) {
            return (Post) results.get(0);
        }
        return null;
    }



  @Override
  public
  LinkedList <Post> all() {
      Session session = DBTools.getSessionFactory().openSession();
      session.beginTransaction();
      CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
      CriteriaQuery <Post> criteriaQuery = criteriaBuilder.createQuery(Post.class);
      Root <Post> root = criteriaQuery.from(Post.class);
      criteriaQuery.select(root).where(criteriaBuilder.isNull(root.get("parrent")));


      Query query = session.createQuery(criteriaQuery);
      List results = query.getResultList();
      session.getTransaction().commit();
      session.close();
      LinkedList<Post> posts=new LinkedList <>();
      posts.addAll(results);
      if (!results.isEmpty()) {
          results.sort(Comparator.comparing(Post::getCreation_date).reversed());
          return posts;
      }
      return new LinkedList <Post>();
  }
    public
    LinkedList <Post> postsList(User user) {

      Session session = DBTools.getSessionFactory().openSession();
      session.beginTransaction();
      CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
      CriteriaQuery <Post> criteriaQuery = criteriaBuilder.createQuery(Post.class);
      Root <Post> root = criteriaQuery.from(Post.class);
      criteriaQuery.select(root).where(criteriaBuilder.and(criteriaBuilder.isNull(root.get("parrent"))), criteriaBuilder.equal(root.get("owner"), user));


      Query query = session.createQuery(criteriaQuery);
      List results = query.getResultList();
      session.getTransaction().commit();
      session.close();
      LinkedList<Post> posts=new LinkedList <>();
      posts.addAll(results);
      if (!results.isEmpty()) {
          results.sort(Comparator.comparing(Post::getCreation_date).reversed());
          return posts;
      }
      return new LinkedList <Post>();
    }

    @Override
    public
    void add(Post post) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(post);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public
    void remove(Post post) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(post);
        session.getTransaction().commit();
        session.close();

    }

    @Override
    public
    void update(Post post) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
      //  user=session.get(User,user.getId());
        session.saveOrUpdate(post);
        session.getTransaction().commit();
        session.close();

    }
  public   LinkedList <Post> popularPosts() {
        List <Post> results = new LinkedList <>();
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

        CriteriaQuery <User> criteriaQueryUsers = criteriaBuilder.createQuery(User.class);
        Root <User> rootUsers = criteriaQueryUsers.from(User.class);
        criteriaQueryUsers.select(rootUsers).where(
                criteriaBuilder.and(criteriaBuilder.equal(rootUsers.get("activate"), true)), criteriaBuilder.equal(rootUsers.get("privated"), false));

        Query queryUsers = session.createQuery(criteriaQueryUsers);
        queryUsers.setMaxResults(10);
        List <User> users = (List <User>) queryUsers.getResultList();
        if (!users.isEmpty()) {
            for (User user : users) {
                CriteriaQuery <Post> criteriaQuery = criteriaBuilder.createQuery(Post.class);
                Root <Post> root = criteriaQuery.from(Post.class);
                criteriaQuery.select(root).where(criteriaBuilder.and(criteriaBuilder.equal(root.get("owner"), user)), criteriaBuilder.isNull(root.get("parrent")));

                Query query = session.createQuery(criteriaQuery);
                query.setMaxResults(5);
                List <Post> posts = (List <Post>) query.getResultList();
                results.addAll(posts);

            }
        }


        session.getTransaction().commit();
        session.close();
results.removeIf(Post::fullyReported);

            return (LinkedList <Post>) results;


    }

  public   LinkedList <Post> feedPosts(User me) {
        List <Post> results = new LinkedList <>();

        for (User user : me.getFollowings()) {
            if (me.getSilenteds().contains(user)) {
                continue;
            }
            List <Post> postList = new LinkedList <>(user.getPosts());
            postList.removeIf(post -> post.notPost());
            for (Post userPost : postList) {
                results.add(userPost);
            }

        }
        results.sort(Comparator.comparing(Post::getCreation_date).reversed());
        results.removeIf(Post::fullyReported);


            return (LinkedList <Post>) results;


    }


}
