package event.messaging;

public
class ModifyGroupInfoEvent {
    Integer messagingEnvironmentId;
    String name;
    byte[] image;

    public
    ModifyGroupInfoEvent(Integer messagingEnvironmentId, String name, byte[] image) {
        this.messagingEnvironmentId = messagingEnvironmentId;
        this.name = name;
        this.image = image;
    }
    public
    ModifyGroupInfoEvent() {

    }

    public
    Integer getMessagingEnvironmentId() {
        return messagingEnvironmentId;
    }

    public
    ModifyGroupInfoEvent setMessagingEnvironmentId(Integer messagingEnvironmentId) {
        this.messagingEnvironmentId = messagingEnvironmentId;
        return this;
    }

    public
    String getName() {
        return name;
    }

    public
    ModifyGroupInfoEvent setName(String name) {
        this.name = name;
        return this;
    }

    public
    byte[] getImage() {
        return image;
    }

    public
    ModifyGroupInfoEvent setImage(byte[] image) {
        this.image = image;
        return this;
    }
}
