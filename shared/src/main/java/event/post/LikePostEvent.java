package event.post;

public
class LikePostEvent {
    Integer liker;
    Integer post;

    public
    LikePostEvent(Integer liker, Integer post) {

        this.liker = liker;
        this.post = post;

    }

    public
    LikePostEvent() {


    }

    public
    Integer getLiker() {
        return liker;
    }

    public
    LikePostEvent setLiker(Integer liker) {
        this.liker = liker;
        return this;
    }

    public
    Integer getPost() {
        return post;
    }

    public
    LikePostEvent setPost(Integer post) {
        this.post = post;
        return this;
    }
}
