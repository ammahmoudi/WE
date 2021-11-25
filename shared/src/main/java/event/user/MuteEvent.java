package event.user;

public class MuteEvent  {
    Integer from;
    Integer to;

    public
    MuteEvent(Integer from, Integer to) {

       this.from=from;
       this.to=to;

    }
public MuteEvent(){

}
    public
    Integer getFrom() {
        return from;
    }

    public
    MuteEvent setFrom(Integer from) {
        this.from = from;
        return this;
    }

    public
    Integer getTo() {
        return to;
    }

    public
    MuteEvent setTo(Integer to) {
        this.to = to;
        return this;
    }
}
