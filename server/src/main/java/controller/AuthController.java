package controller;

import JSon.JSonController;
import controller.server.ClientHandler;
import controller.server.Transmitter;
import event.user.SignInFormEvent;
import event.user.SignUpFormEvent;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import response.Response;
import response.ResponseType;

public
class AuthController extends Controller {
    static Logger LOGGER = LogManager.getLogger(AuthController.class.getName());


    public
    AuthController(ClientHandler clientHandler) {
        super(clientHandler);
    }


    public
    User signIn(SignInFormEvent signInFormEvent) {
        Response response;
        User user = context.Users.getByuserName((signInFormEvent).getUsername());

        if (user != null && user.getPassword().equals(signInFormEvent.getPassword()) && !user.isDeleted()) {
            LOGGER.info("User " + user.getId() + " signed in");
            user.setToken(TokenHandler.tokenSupplier.get());
            user.setActivate(true);

            context.Users.update(user);
            user.setMyOwnLastSeenPrivacy(user.getLastSeenPrivacy());
            user.setFirstTimeToken(user.getToken());
            String data = JSonController.objectToStringMapper(user);

            response = new Response(ResponseType.ACCEPT_SIGN_IN, data);
            getClientHandler().setLoggedInUser(user);

            Transmitter.sendResponse(clientHandler.getPrintWriter(), response);


        } else {
            response = new Response(ResponseType.ERROR_SIGN_IN, "Username or password is wrong!");
            Transmitter.sendResponse(clientHandler.getPrintWriter(), response);
        }

        return user;

    }

    public
    User signIn(int userId, String token) {
        Response response;
        User user = context.Users.getByToken(token);

        if (user != null && user.getId().equals(userId) && !user.isDeleted()) {
            LOGGER.info("User " + user.getId() + " signed in");
            user.setToken(TokenHandler.tokenSupplier.get());
            user.setActivate(true);
            context.Users.update(user);
            user.setMyOwnLastSeenPrivacy(user.getLastSeenPrivacy());
            user.setFirstTimeToken(user.getToken());
            String data = JSonController.objectToStringMapper(user);
            response = new Response(ResponseType.ACCEPT_SIGN_IN, data);
            Transmitter.sendResponse(clientHandler.getPrintWriter(), response);
            getClientHandler().setLoggedInUser(user);
        } else {
            response = new Response(ResponseType.ERROR_SIGN_IN, "Username or password is wrong!");
            Transmitter.sendResponse(clientHandler.getPrintWriter(), response);
        }

        return user;

    }

    public
    boolean isUsedUsername(String text) {

        return context.Users.getByuserName(text) != null;
    }

    public
    boolean isUsedEmail(String text) {

        return context.Users.getByEmail(text) != null;
    }

    public
    void signUp(SignUpFormEvent signUpFormEvent) {
        Response response;
        String data;
        if (context.Users.getByuserName(signUpFormEvent.getUsername()) == null && context.Users.getByEmail(signUpFormEvent.getEmail()) == null & !signUpFormEvent.getPassword().equals("") && !signUpFormEvent.getFirstName().equals("") && !signUpFormEvent.getEmail().equals("") && !signUpFormEvent.getUsername().equals("")) {
            User user = new User(signUpFormEvent.getFirstName(), signUpFormEvent.getLastName(), signUpFormEvent.getPassword(), signUpFormEvent.getUsername(), signUpFormEvent.getPhoneNumber(), signUpFormEvent.getEmail(), signUpFormEvent.getBio(), signUpFormEvent.getBirthDay());
            LOGGER.info("User " + user.getId() + " signed up");
            context.Users.add(user);
            response = new Response(ResponseType.ACCEPT_SIGN_UP, "Welcome to WE");

        } else {
            if (context.Users.getByuserName(signUpFormEvent.getUsername()) != null)
                response = new Response(ResponseType.ERROR_SIGN_UP, "Username is Already taken !");
            if (context.Users.getByEmail(signUpFormEvent.getEmail()) == null)
                response = new Response(ResponseType.ERROR_SIGN_UP, "Email is Already taken !");
            if (context.Users.getByEmail(signUpFormEvent.getEmail()).equals(""))
                response = new Response(ResponseType.ERROR_SIGN_UP, "Email can not be empty !");
            if (context.Users.getByEmail(signUpFormEvent.getUsername()).equals(""))
                response = new Response(ResponseType.ERROR_SIGN_UP, "Username can not be empty !");
            if (context.Users.getByEmail(signUpFormEvent.getFirstName()).equals(""))
                response = new Response(ResponseType.ERROR_SIGN_UP, "Firstname can not be empty !");
            if (context.Users.getByEmail(signUpFormEvent.getPassword()).equals(""))
                response = new Response(ResponseType.ERROR_SIGN_UP, "Password can not be empty !");
            else

                response = new Response(ResponseType.ERROR_SIGN_UP, "Unknown Error !");


        }


        Transmitter.sendResponse(clientHandler.getPrintWriter(), response);


    }

    public
    boolean validateToken(String token) {
        if (token.equals(context.Users.getByToken(getClientHandler().getLoggedInUser().getToken()).getToken()))
            return true;
        return false;
    }

}
