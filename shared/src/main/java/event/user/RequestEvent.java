package event.user;

public class RequestEvent  {
    Integer from;
    Integer to;
    boolean accept;

    public
    RequestEvent(Integer from, Integer to, boolean accept) {

       this.from=from;
       this.to=to;
       this.accept=accept;

    }
    public RequestEvent(){

    }

    public
    Integer getFrom() {
        return from;
    }

    public
    RequestEvent setFrom(Integer from) {
        this.from = from;
        return this;
    }

    public
    Integer getTo() {
        return to;
    }

    public
    RequestEvent setTo(Integer to) {
        this.to = to;
        return this;
    }

    public
    boolean isAccept() {
        return accept;
    }

    public
    RequestEvent setAccept(boolean accept) {
        this.accept = accept;
        return this;
    }
}
