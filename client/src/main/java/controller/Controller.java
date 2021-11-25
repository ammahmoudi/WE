package controller;

import db.Context;
import view.generalPages.WEConfig;

public
class Controller {
    protected Context context;
protected
    WEConfig weConfig;
    public Controller(){
        context = new Context()
        ;
        weConfig=new WEConfig();
    }
}
