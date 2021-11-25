package event.post;


import model.Post;
import model.PostType;

public
class NewPostEvent {
    String text;
    byte[] image = null;
    Post originalPost = null;
    Post parrent = null;
    PostType postType;


    public
    NewPostEvent(String text, byte[] image) {


        this.text = text;
        this.image = image;
        postType = PostType.POST;


    }

    public
    NewPostEvent() {


    }

    public
    NewPostEvent(String text, byte[] image, Post parrentPost) {
        this.text = text;
        this.image = image;
        this.parrent = parrentPost;
        postType = PostType.COMMENT;

    }

    public
    NewPostEvent(Post originalPost) {

        this.originalPost = originalPost;
        postType = PostType.REPOSTED_POST;

    }

    public
    String getText() {
        return text;
    }

    public
    NewPostEvent setText(String text) {
        this.text = text;
        return this;
    }

    public
    byte[] getImage() {
        return image;
    }

    public
    NewPostEvent setImage(byte[] image) {
        this.image = image;
        return this;
    }

    public
    Post getOriginalPost() {
        return originalPost;
    }

    public
    NewPostEvent setOriginalPost(Post originalPost) {
        this.originalPost = originalPost;
        return this;
    }

    public
    Post getParrent() {
        return parrent;
    }

    public
    NewPostEvent setParrent(Post parrent) {
        this.parrent = parrent;
        return this;
    }

    public
    PostType getPostType() {
        return postType;
    }

    public
    NewPostEvent setPostType(PostType postType) {
        this.postType = postType;
        return this;
    }


}

