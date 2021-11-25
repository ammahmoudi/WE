package db;

import model.User;

public
class Context {
    public UserDB Users=new UserDB();
    public PostDB Posts=new PostDB();
    public NotificationDB Notifications=new NotificationDB();
    public MessageDB Messages=new MessageDB();
    public MessagingEnvironmentDB MessagingEnvironmentDB=new MessagingEnvironmentDB();
    public CategoryDB Categories=new CategoryDB();
}
