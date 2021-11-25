package event.user;

public class BlockEvent  {
    Integer from;
    Integer to;

    public
    BlockEvent(Integer from, Integer to) {

       this.from=from;
       this.to=to;

    }
    public
    BlockEvent( ) {

    }

    public
    Integer getFrom() {
        return from;
    }

    public
    BlockEvent setFrom(Integer from) {
        this.from = from;
        return this;
    }

    public
    Integer getTo() {
        return to;
    }

    public
    BlockEvent setTo(Integer to) {
        this.to = to;
        return this;
    }
}
