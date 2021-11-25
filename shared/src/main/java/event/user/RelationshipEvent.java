package event.user;

public class RelationshipEvent  {
    Integer from;
    Integer to;

    public
    RelationshipEvent(Integer from, Integer to) {

       this.from=from;
       this.to=to;

    }
    public
    RelationshipEvent() {



    }

    public
    Integer getFrom() {
        return from;
    }

    public
    RelationshipEvent setFrom(Integer from) {
        this.from = from;
        return this;
    }

    public
    Integer getTo() {
        return to;
    }

    public
    RelationshipEvent setTo(Integer to) {
        this.to = to;
        return this;
    }
}
