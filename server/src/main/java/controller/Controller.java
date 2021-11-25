package controller;

import controller.server.ClientHandler;
import db.Context;

import java.io.DataOutputStream;
import java.io.PrintWriter;


public
class Controller {
    protected   Context context=new Context();
    ClientHandler clientHandler;

    public
    Controller(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public
    ClientHandler getClientHandler() {
        return clientHandler;
    }

    public
    Controller setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        return this;
    }
}


