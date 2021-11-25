package controller.server;

import JSon.JSonController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import request.Request;
import response.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public
class Transmitter {

    private static final Logger LOGGER = LogManager.getLogger(Transmitter.class);

    public static synchronized
    void sendResponse(PrintWriter printWriter, Response response) {
        String data;
        data = JSonController.objectToStringMapper(response);
        printWriter.println(data);

    }

    public static synchronized
    Request getRequest(BufferedReader bufferedReader) {
        byte[] jsonBytes;
        String requestJson = null;
        try {
            requestJson = bufferedReader.readLine();
        } catch (IOException e) {

        }
        Request request = null;
        if (requestJson != null)
            request = JSonController.stringToObjectMapper(requestJson, Request.class);
        return request;
    }


}
