package controller;

import JSon.JSonController;
import controller.messaging.AddMemberController;
import controller.user.AuthController;
import db.Context;
import event.user.SignInFormEvent;
import javafx.application.Platform;
import model.LoggedInUser;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import request.Request;
import request.RequestType;
import response.Response;
import response.ResponseType;
import view.generalPages.MainPageView;
import view.generalPages.WEConfig;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public
class ServerHandler {
    public static int SERVER_PORT=8083;
    static Logger LOGGER = LogManager.getLogger(ServerHandler.class.getName());
    static DataOutputStream dataOut;
    private static Socket socket;
    private static DataInputStream dataIn;
    private static PrintWriter printWriter;
    private static
    BufferedReader bufferedReader;
private static
    Context context=new Context();

private static
    WEConfig weConfig=new WEConfig();
    public static
    DataOutputStream getDataOut() {
        return dataOut;
    }

    public static
    void setDataOut(DataOutputStream dataOut) {
        ServerHandler.dataOut = dataOut;
    }

    public static
    Socket getSocket() {
        return socket;
    }

    public static
    void setSocket(Socket socket1) {
        socket = socket1;

        try {
            if (socket1 != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                printWriter = new PrintWriter(socket.getOutputStream(), true);
                //    dataIn = new DataInputStream(socket.getInputStream());
                //   dataOut = new DataOutputStream(socket.getOutputStream());
            } else {
                bufferedReader = null;
                printWriter = null;
                //  dataIn = null;
                //  dataOut = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static
    DataInputStream getDataIn() {
        return dataIn;
    }

    public static
    void setDataIn(DataInputStream dataIn) {
        ServerHandler.dataIn = dataIn;

    }

    private static Lock lock =new ReentrantLock();
    public static
    Response transmitter(Request request) {
        lock.lock();
        try {
            if (printWriter == null) {
                if (MainPageView.universalServerButtonIcon != null)
                    MainPageView.universalServerButtonIcon.setIconName("REPEAT");
                return new Response(ResponseType.ERROR_NO_SERVER, "No Server");
            }
            String data;
            data = JSonController.objectToStringMapper(request);
            printWriter.println(data);
            if (bufferedReader == null) {
                if (MainPageView.universalServerButtonIcon != null)
                    MainPageView.universalServerButtonIcon.setIconName("REPEAT");

                return new Response(ResponseType.ERROR_NO_SERVER, "No Server");
            }


            String responseJson = null;

            try {

                responseJson = bufferedReader.readLine();
                if (responseJson.equals("") || responseJson == null) {
                    disconnectFromServer();
                    MainPageView.universalServerButtonIcon.setIconName("REPEAT");
                    return new Response(ResponseType.ERROR_NO_SERVER, "Error");
                }
            } catch (IOException e) {
                LOGGER.error("Couldn't get response from server");
                System.out.println("Couldn't get response from server");
                disconnectFromServer();

                MainPageView.universalServerButtonIcon.setIconName("REPEAT");
            }


            return JSonController.stringToObjectMapper(responseJson, Response.class);
        }finally {
            lock.unlock();
        }

    }

    public static
    Response getResponse2() {
        if (dataIn == null) {
            if (MainPageView.universalServerButtonIcon != null)
                MainPageView.universalServerButtonIcon.setIconName("REPEAT");
            return new Response(ResponseType.ERROR_NO_SERVER, "No Server");
        }

        byte[] jsonBytes;
        String responseJson;
        try {
            sleep(300);
        } catch (InterruptedException interruptedException) {
            LOGGER.error("sleep thread interrupted");
        }
        try {
            int length = dataIn.readInt();
            jsonBytes = new byte[length];
            dataIn.readFully(jsonBytes, 0, length);
            responseJson = new String(jsonBytes, "UTF-8");

            Response response = JSonController.stringToObjectMapper(responseJson, Response.class);
            //  System.out.println(response.getData());
            return response;

        } catch (IOException ioException) {
            LOGGER.error("Couldn't get response from server");

            disconnectFromServer();
            MainPageView.universalServerButtonIcon.setIconName("REPEAT");
        }
        return null;
    }

    public static
    void disconnectFromServer() {
        try {
            if (socket != null && !socket.isClosed()) {
                LoggedInUser.setOffline(true);
                LoggedInUser.setUser(context.Users.get(Integer.parseInt(weConfig.loadProperties().getProperty("LoggedInUser"))));
              Platform.runLater(Actions::cancelUpdaters);
           Platform.runLater(new Runnable() {
               @Override
               public
               void run() {
                   if(MainPageView.universalTabPane!=null)MainPageView.universalTabPane.getTabs().clear();
                   MainPageView.mainPageView.updateOfflineMode();
               }
           });
                socket.close();
                setSocket(null);

                LOGGER.info("Disconnected from server");
                System.out.println("Disconnected from server.");
                if (MainPageView.universalServerButtonIcon != null)
                    MainPageView.universalServerButtonIcon.setIconName("REPEAT");
            }
        } catch (IOException e) {
            LOGGER.error("error in disconnecting  from server");

            e.printStackTrace();
        }
    }

    public static
    String reconnectToServer() {
        AuthController authController=new AuthController();
        if (socket == null) {
            System.out.println("reconnecting to server");
            connectToServer();
            Response response =authController.reSignIn();
            if(response!=null && response.getType()==ResponseType.ACCEPT_SIGN_IN){
                Actions.restartUpdaters();
                if(MainPageView.universalTabPane!=null)MainPageView.universalTabPane.getTabs().clear();
                return null;
            }
        if(response!=null)return response.getData();

        }
        return "Already Connected";
    }

    public static
    void connectToServer() {
        try {
            System.out.println("Connecting to server");
            setSocket(new Socket(InetAddress.getLocalHost(), SERVER_PORT));
            System.out.println("Connected to Server.");
            LOGGER.info("Connected to server");
        } catch (IOException e) {
            System.out.println("Cannot connect to server");
            LOGGER.error("Error in connecting to server.");

        }
    }

    public static
    PrintWriter getPrintWriter() {
        return printWriter;
    }

    public static
    void setPrintWriter(PrintWriter printWriter) {
        ServerHandler.printWriter = printWriter;
    }

    public static
    BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public static
    void setBufferedReader(BufferedReader bufferedReader) {
        ServerHandler.bufferedReader = bufferedReader;
    }

    public static
    Boolean isConnectionAlive() {
        Response response= ServerHandler.transmitter(new Request(RequestType.ALIVE_CONNECTION, "am I Alive?"));

        if (response.getType() == ResponseType.ALIVE_CONNECTION) return true;
        if (response.getType() == ResponseType.ERROR_NO_SERVER) return null;
        return false;

    }


//    public static synchronized
//    Response getResponse() {
//        if (bufferedReader == null) {
//            if (MainPageView.universalServerButtonIcon != null)
//                MainPageView.universalServerButtonIcon.setIconName("REPEAT");
//            Actions.pauseTimeLines();
//            return new Response(ResponseType.ERROR_NO_SERVER, "No Server");
//        }
//
//
//        String responseJson = null;
////        try {
////            sleep(100);
////        } catch (InterruptedException e) {
////            System.out.println("sleep error");
////        }
//
//        try {
//
//            responseJson = bufferedReader.readLine();
//        } catch (IOException e) {
//            LOGGER.error("Couldn't get response from server");
//            System.out.println("Couldn't get response from server");
//
//            disconnectFromServer();
//            Actions.pauseTimeLines();
//            MainPageView.universalServerButtonIcon.setIconName("REPEAT");
//        }
//        if (responseJson.equals("") || responseJson == null) {
//
//            disconnectFromServer();
//            Actions.pauseTimeLines();
//            MainPageView.universalServerButtonIcon.setIconName("REPEAT");
//            return null;
//        }
//        Response response = JSonController.stringToObjectMapper(responseJson, Response.class);
//
//        //  System.out.println(response.getData());
//        return response;
//
//
//    }

//    public synchronized static
//    void sendRequest(Request request) {
//        if (printWriter == null) {
//            if (MainPageView.universalServerButtonIcon != null)
//                MainPageView.universalServerButtonIcon.setIconName("REPEAT");
//            return;
//        }
//        String data;
//        data = JSonController.objectToStringMapper(request);
//        printWriter.println(data);
//
//    }

    public synchronized static
    void sendRequest2(Request request) {
        if (dataIn == null) {
            if (MainPageView.universalServerButtonIcon != null)
                MainPageView.universalServerButtonIcon.setIconName("REPEAT");
            return;
        }
        String data;
        data = JSonController.objectToStringMapper(request);
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        try {
            dataOut.writeInt(dataBytes.length);
            dataOut.write(dataBytes, 0, dataBytes.length);
            dataOut.flush();
        } catch (IOException ioException) {
            LOGGER.error("Couldn't send Request to server");
            disconnectFromServer();
            MainPageView.universalServerButtonIcon.setIconName("REPEAT");
        }
    }
}


