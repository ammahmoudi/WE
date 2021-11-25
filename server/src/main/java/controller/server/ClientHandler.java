package controller.server;

import controller.Loop;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import request.Request;

import java.io.*;
import java.net.Socket;

public
class ClientHandler extends Thread {

    static Logger LOGGER = LogManager.getLogger(ClientHandler.class.getName());

    private Socket socket;
    private WEServer weServer;
    // the game this connection is associated with
    private
    InputStream inputStream;
    private
    OutputStream outputStream;
    private
    DataOutputStream dataOutputStream;
    private
    DataInputStream dataInputStream;
    private User loggedInUser = null;
    private RequestHandler requestHandler;
    private
    BufferedReader bufferedReader;
    private  PrintWriter printWriter;
    private Loop serverSeenLoop;


    ClientHandler(WEServer weServer, Socket socket) {
        this.weServer = weServer;
        this.socket = socket;


    }

    @Override
    public
    void run() {
        try {

            inputStream = socket.getInputStream();
        } catch (IOException ioException) {
            LOGGER.error("Couldn't get Socket " + socket + " inputStream");
        }
        try {
            outputStream = socket.getOutputStream();
        } catch (IOException ioException) {
            LOGGER.error("Couldn't get Socket " + socket + " outputStream");
        }
        bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
        printWriter=new PrintWriter(outputStream,true);
      //  dataInputStream = new DataInputStream(inputStream);
      //  dataOutputStream = new DataOutputStream(outputStream);
        requestHandler = new RequestHandler(this);

        while (true) {
            Request request = Transmitter.getRequest(bufferedReader);
            if (request == null){
                LOGGER.info("Socket " + socket + " closed.");
                System.out.println(this.getName()+" disconnected");
             //   serverSeenLoop.stop();

                return;

            }
            requestHandler.executeRequest(request);

        }
    }

    public
    User getLoggedInUser() {
        return loggedInUser;
    }

    public
    ClientHandler setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        return this;
    }

    public
    BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public
    ClientHandler setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
        return this;
    }

    public
    PrintWriter getPrintWriter() {
        return printWriter;
    }

    public
    ClientHandler setPrintWriter(PrintWriter printWriter) {
        this.printWriter = printWriter;
        return this;
    }

    public
    Loop getServerSeenLoop() {
        return serverSeenLoop;
    }

    public
    ClientHandler setServerSeenLoop(Loop serverSeenLoop) {
        this.serverSeenLoop = serverSeenLoop;
        return this;
    }
}

