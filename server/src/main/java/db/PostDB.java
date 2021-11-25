package db;

import model.Post;
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
class PostDB implements DBSet <Post> {

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


    public
    Post get(int id, User user) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        Post post = null;
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <Post> criteriaQuery = criteriaBuilder.createQuery(Post.class);
        Root <Post> root = criteriaQuery.from(Post.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), id));
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();


        if (!results.isEmpty()) {
            post = (Post) results.get(0);
        }
        if (user != null) {
            if (post.notOriginalPost()) {
                post.setNumberOfComments(post.getOriginalPost().getComments().size());
                post.setNumberOfLikes(post.getOriginalPost().getLikes().size());
            } else {
                post.setNumberOfComments(post.getComments().size());
                post.setNumberOfLikes(post.getLikes().size());
            }
            post.setLikedByMe(post.likedChecker(user));
        }
        session.getTransaction().commit();
        session.close();
        return post;
    }

    public
    Post like(int id, User user) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <Post> criteriaQuery = criteriaBuilder.createQuery(Post.class);
        Root <Post> root = criteriaQuery.from(Post.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), id));
        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();
        Post post = null;

        if (!results.isEmpty()) {
            post = (Post) results.get(0);
        }
        post.like(user);

        session.update(post);
        if (post.notOriginalPost()) {
            post.setNumberOfComments(post.getOriginalPost().getComments().size());
            post.setNumberOfLikes(post.getOriginalPost().getLikes().size());
        } else {
            post.setNumberOfComments(post.getComments().size());
            post.setNumberOfLikes(post.getLikes().size());
        }
        post.setLikedByMe(post.likedChecker(user));
        session.getTransaction().commit();
        session.close();
        return post;
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
        LinkedList <Post> posts = new LinkedList <>();
        posts.addAll(results);
        if (!results.isEmpty()) {
            results.sort(Comparator.comparing(Post::getCreation_date).reversed());
            return posts;
        }
        return new LinkedList <Post>();
    }

    public
    LinkedList <Post> postsList(User user, User loggedInUser) {

        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <Post> criteriaQuery = criteriaBuilder.createQuery(Post.class);
        Root <Post> root = criteriaQuery.from(Post.class);
        criteriaQuery.select(root).where(criteriaBuilder.and(criteriaBuilder.isNull(root.get("parrent"))), criteriaBuilder.equal(root.get("owner"), user));


        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();

        LinkedList <Post> posts = new LinkedList <>();
        posts.addAll(results);
        if (!results.isEmpty()) {
            for (Post userPost : posts) {
                if (userPost.notOriginalPost()) {
                    User owner = userPost.getOwner();

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
                    switch (userPost.getOriginalPost().getOwner().getLastSeenPrivacy()) {
                        case EVERYONE:
                            break;
                        case NOBODY:
                            userPost.getOriginalPost().getOwner().setLastSeen(null);
                            break;
                        case ONLY_FOLLOWINGS:
                            RelationshipType relationshipType = userPost.getOriginalPost().getOwner().getRelationshipTo(loggedInUser);
                            if (relationshipType != RelationshipType.FOLLOWING && relationshipType != RelationshipType.FOLLOWING_MUTED) {
                                userPost.getOriginalPost().getOwner().setLastSeen(null);
                            }
                            break;

                    }
                    userPost.setNumberOfComments(userPost.getOriginalPost().getComments().size());
                    userPost.setNumberOfLikes(userPost.getOriginalPost().getLikes().size());
                } else {
                    User owner = userPost.getOwner();

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
                    userPost.setNumberOfComments(userPost.getComments().size());
                    userPost.setNumberOfLikes(userPost.getLikes().size());
                }
                userPost.setLikedByMe(userPost.likedChecker(loggedInUser));


            }
            posts.sort(Comparator.comparing(Post::getCreation_date).reversed());
            session.getTransaction().commit();
            session.close();
            return posts;
        }
        return new LinkedList <Post>();
    }


    public
    LinkedList <Post> commentList(int postId, User loggedInUser) {

        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery <Post> criteriaQuery = criteriaBuilder.createQuery(Post.class);
        Root <Post> root = criteriaQuery.from(Post.class);
        criteriaQuery.select(root).where(criteriaBuilder.and(criteriaBuilder.isNotNull(root.get("parrent"))), criteriaBuilder.equal(root.get("parrent"), postId));

        Query query = session.createQuery(criteriaQuery);
        List results = query.getResultList();

        LinkedList <Post> posts = new LinkedList <>();
        posts.addAll(results);
        if (!results.isEmpty()) {
            for (Post userPost : posts) {
                if (userPost.notOriginalPost()) {
                    User owner = userPost.getOwner();

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
                    switch (userPost.getOriginalPost().getOwner().getLastSeenPrivacy()) {
                        case EVERYONE:
                            break;
                        case NOBODY:
                            userPost.getOriginalPost().getOwner().setLastSeen(null);
                            break;
                        case ONLY_FOLLOWINGS:
                            RelationshipType relationshipType = userPost.getOriginalPost().getOwner().getRelationshipTo(loggedInUser);
                            if (relationshipType != RelationshipType.FOLLOWING && relationshipType != RelationshipType.FOLLOWING_MUTED) {
                                userPost.getOriginalPost().getOwner().setLastSeen(null);
                            }
                            break;

                    }
                    userPost.setNumberOfComments(userPost.getOriginalPost().getComments().size());
                    userPost.setNumberOfLikes(userPost.getOriginalPost().getLikes().size());
                } else {
                    User owner = userPost.getOwner();

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
                    userPost.setNumberOfComments(userPost.getComments().size());
                    userPost.setNumberOfLikes(userPost.getLikes().size());
                }
                userPost.setLikedByMe(userPost.likedChecker(loggedInUser));


            }
            posts.sort(Comparator.comparing(Post::getCreation_date).reversed());
            session.getTransaction().commit();
            session.close();
            return posts;
        }
        return new LinkedList <Post>();
    }


    public
    LinkedList <User> likesList(int postId, User loggedInUser) {

        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        Post post = session.get(Post.class, postId);
        LinkedList <User> likers = new LinkedList <>(post.getLikes());
        for (User user : likers) {

            switch (user.getLastSeenPrivacy()) {
                case EVERYONE:
                    break;
                case NOBODY:
                    user.setLastSeen(null);
                    break;
                case ONLY_FOLLOWINGS:
                    RelationshipType relationshipType = user.getRelationshipTo(loggedInUser);
                    if (relationshipType != RelationshipType.FOLLOWING && relationshipType != RelationshipType.FOLLOWING_MUTED) {
                        user.setLastSeen(null);
                    }
                    break;
            }

        }
        session.getTransaction().commit();
        session.close();
        return likers;
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
        session.update(post);
        session.getTransaction().commit();
        session.close();

    }

    public
    LinkedList <Post> popularPosts(User loggedInUser) {
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
        results.removeIf(post -> post.notPost());
        for (Post userPost : results) {

            if (userPost.notOriginalPost()) {
                User owner = userPost.getOwner();
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
                switch (userPost.getOriginalPost().getOwner().getLastSeenPrivacy()) {
                    case EVERYONE:
                        break;
                    case NOBODY:
                        userPost.getOriginalPost().getOwner().setLastSeen(null);
                        break;
                    case ONLY_FOLLOWINGS:
                        RelationshipType relationshipType = userPost.getOriginalPost().getOwner().getRelationshipTo(loggedInUser);
                        if (relationshipType != RelationshipType.FOLLOWING && relationshipType != RelationshipType.FOLLOWING_MUTED) {
                            userPost.getOriginalPost().getOwner().setLastSeen(null);
                        }
                        break;
                }
                userPost.setNumberOfComments(userPost.getOriginalPost().getComments().size());
                userPost.setNumberOfLikes(userPost.getOriginalPost().getLikes().size());


            } else {
                User owner = userPost.getOwner();
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
                userPost.setNumberOfComments(userPost.getComments().size());
                userPost.setNumberOfLikes(userPost.getLikes().size());
            }
            userPost.setLikedByMe(userPost.likedChecker(loggedInUser));

        }
        results.removeIf(post -> {
            return post.getOwner().isBlockedBy(loggedInUser) || post.getOwner().isSilentedBy(loggedInUser);
        });


        session.getTransaction().commit();
        session.close();
        results.removeIf(Post::fullyReported);

        return (LinkedList <Post>) results;


    }

    public
    LinkedList <Post> feedPosts(User me) {
        Session session = DBTools.getSessionFactory().openSession();
        session.beginTransaction();
        me = session.get(User.class, me.getId());


        List <Post> results = new LinkedList <>();

        for (User user : me.getFollowings()) {

            if (me.getSilenteds().contains(user)) continue;

            List <Post> postList = new LinkedList <>(user.getPosts());
            postList.removeIf(post -> post.notPost());

            for (Post userPost : postList) {

                User owner = userPost.getOwner();
                switch (owner.getLastSeenPrivacy()) {
                    case EVERYONE:
                        break;
                    case NOBODY:
                        owner.setLastSeen(null);
                        break;
                    case ONLY_FOLLOWINGS:
                        RelationshipType relationshipType = owner.getRelationshipTo(me);
                        if (relationshipType != RelationshipType.FOLLOWING && relationshipType != RelationshipType.FOLLOWING_MUTED) {
                            owner.setLastSeen(null);
                        }
                        break;

                }
                if (userPost.notOriginalPost()) {
                    switch (userPost.getOriginalPost().getOwner().getLastSeenPrivacy()) {
                        case EVERYONE:
                            break;
                        case NOBODY:
                            userPost.getOriginalPost().getOwner().setLastSeen(null);
                            break;
                        case ONLY_FOLLOWINGS:
                            RelationshipType relationshipType = userPost.getOriginalPost().getOwner().getRelationshipTo(me);
                            if (relationshipType != RelationshipType.FOLLOWING && relationshipType != RelationshipType.FOLLOWING_MUTED) {
                                userPost.getOriginalPost().getOwner().setLastSeen(null);
                            }
                            break;
                    }
                    userPost.setNumberOfComments(userPost.getOriginalPost().getComments().size());
                    userPost.setNumberOfLikes(userPost.getOriginalPost().getLikes().size());


                } else {
                    userPost.setNumberOfComments(userPost.getComments().size());
                    userPost.setNumberOfLikes(userPost.getLikes().size());
                }
                userPost.setLikedByMe(userPost.likedChecker(me));

                results.add(userPost);
            }

        }
        results.sort(Comparator.comparing(Post::getCreation_date).reversed());
        results.removeIf(Post::fullyReported);

        session.getTransaction().commit();
        session.close();
        return (LinkedList <Post>) results;


    }


}
