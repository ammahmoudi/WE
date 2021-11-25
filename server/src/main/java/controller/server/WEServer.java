package controller.server;

import JSon.JSonController;
import controller.AuthController;
import db.Context;
import event.messaging.NewScheduledMessageEvent;
import event.user.SignInFormEvent;
import model.Message;
import model.MessagingEnvironment;
import model.Post;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public
class WEServer implements Runnable {
    public static int SERVER_PORT=8083;
    static Logger LOGGER = LogManager.getLogger(WEServer.class.getName());
    private ServerSocket serverSocket;
    private
    LinkedList<ClientHandler> clientHandlers=new LinkedList <>();
   public static LinkedList<NewScheduledMessageEvent> scheduledMessageEvents=new LinkedList <>();
    public static void main(String[] args) {
        WEServer weServer=new WEServer();
        new Thread(weServer).start();
    }



    @Override
    public void run() {
        try {
            System.out.println("WE Server");

            serverSocket = new ServerSocket(SERVER_PORT);
            Context context=new Context();
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
           scheduledMessageEvents=context.ScheduledMessages.all();
            Runnable runnable=new Runnable() {
                @Override
                public
                void run() {
                    try{

                    if(scheduledMessageEvents.size()!=0)
                        System.out.println("sendind scheduled messags:"+scheduledMessageEvents.size());
                    for(NewScheduledMessageEvent newScheduledMessageEvent:scheduledMessageEvents){
                        if(LocalDateTime.now().isAfter(newScheduledMessageEvent.getSendingDate())){
                            User sender=context.Users.get(newScheduledMessageEvent.getSender());
                            MessagingEnvironment taker=context.MessagingEnvironmentDB.get(newScheduledMessageEvent.getTaker());
                            List <Message> messageList = context.MessagingEnvironmentDB.getList(newScheduledMessageEvent.getTaker(), Message.class, null);
                          taker.setMessages(messageList);
                            Message originalMessage = null;
                            Post post=null;
                            if(newScheduledMessageEvent.getOriginalMessage()!=null)

                                originalMessage = context.Messages.get(newScheduledMessageEvent.getOriginalMessage());
                            if(newScheduledMessageEvent.getPost()!=null)
                          post = context.Posts.get(newScheduledMessageEvent.getPost());

                            Message message=new Message(sender,taker,newScheduledMessageEvent.getText(),originalMessage, newScheduledMessageEvent.getImage(), post);
                            message.setCreation_date(newScheduledMessageEvent.getSendingDate());
                            context.Messages.add(message);

                            context.ScheduledMessages.remove(newScheduledMessageEvent);
                        }
                    }
                }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            };

            executor.scheduleAtFixedRate(runnable,2000,120000, TimeUnit.MILLISECONDS);

           // SERVER_PORT = serverSocket.getLocalPort();

            System.out.println("Server running on: " + serverSocket.getLocalSocketAddress());
            System.out.println("Port: " + SERVER_PORT);
//            AuthController authController=new AuthController(null);
//            authController.signIn(new SignInFormEvent("mahsa","mahsa"));
//            Context context=new Context();
//            MessagingEnvironment messagingEnvironment=context.MessagingEnvironmentDB.get(2);
//            List <Message> messageList=context.MessagingEnvironmentDB.getList(2,Message.class);
//            List<User> userList=context.MessagingEnvironmentDB.getList(2,User.class);
//          //  messagingEnvironment.setUsers(userList);
//            messagingEnvironment.setMessages(messageList);
//            //   List <Message> messageList = context.MessagingEnvironmentDB.getList(, Message.class);
//
//            String data;
//
//            data = JSonController.objectToStringMapper(messagingEnvironment);

       //     System.out.println(data);
            while (!serverSocket.isClosed()) {

                Socket socket = serverSocket.accept();


                //create a new connection
               ClientHandler clientHandler= new ClientHandler(this, socket);
               clientHandler.start();
                System.out.println("client connected as socket " +  socket+" in ClientHandler "+ clientHandler.getName());
                clientHandlers.add(clientHandler);
            }

        } catch (Exception e) {
            LOGGER.error("Something went wrong while creating the serversocket");
            System.out.println("Something went wrong while creating the serversocket");

        } finally {
            try {
                if (serverSocket != null & !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {

                System.out.println("Something went wrong while closing the serversocket");
                LOGGER.error("Something went wrong while creating the serversocket");

            }
        }

    }
}
