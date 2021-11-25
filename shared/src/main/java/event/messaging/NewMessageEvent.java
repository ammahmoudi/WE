package event.messaging;

public
class NewMessageEvent {
    Integer Sender;
    Integer taker;
    String Text;
    Integer originalMessage;
    byte[] image;
    Integer post;

    public
    NewMessageEvent(Integer sender, Integer taker, String text, Integer originalMessage, byte[] image, Integer post) {

        Sender = sender;
        this.taker = taker;
        Text = text;
        this.originalMessage = originalMessage;
        this.image=image;
        this.post=post;
    }
    public
    NewMessageEvent( ) {

    }
    public
    int getSender() {
        return Sender;
    }

    public
    NewMessageEvent setSender(int sender) {
        Sender = sender;
        return this;
    }

    public
    Integer getTaker() {
        return taker;
    }

    public
    NewMessageEvent setTaker(Integer taker) {
        this.taker = taker;
        return this;
    }

    public
    String getText() {
        return Text;
    }

    public
    NewMessageEvent setText(String text) {
        Text = text;
        return this;
    }

    public
    Integer getOriginalMessage() {
        return originalMessage;
    }

    public
    NewMessageEvent setOriginalMessage(Integer originalMessage) {
        this.originalMessage = originalMessage;
        return this;
    }

    public
    byte[] getImage() {
        return image;
    }

    public
    NewMessageEvent setImage(byte[] image) {
        this.image = image;
        return this;
    }

    public
    Integer getPost() {
        return post;
    }

    public
    NewMessageEvent setPost(Integer post) {
        this.post = post;
        return this;
    }
}
