package command;

import model.entity.User;
import service.LoginService;
import util.Configuration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static util.Configuration.LOGIN;
import static util.Configuration.REGISTER;

public class RegisterCommand implements Command {
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String NAME = "name";
    private static final String SURNAME = "surname";
    private static final String PHONE = "phone";


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String page;
        String email = request.getParameter(EMAIL).trim();
        String password = request.getParameter(PASSWORD).trim();
        String name = request.getParameter(NAME).trim();
        String surname = request.getParameter(SURNAME).trim();
        String phone = request.getParameter(PHONE).trim();


        if (LoginService.getInstance().isPresentLogin(email) == null) {
            User user = new User.Builder()
                    .setEmail(email)
                    .setPassword(password)
                    .setName(name)
                    .setPhone(phone)
                    .setSurname(surname)
                    .isAdmin(false)
                    .build();

            LoginService.getInstance().addUser(user);
            page = Configuration.getInstance().getConfig(LOGIN);
        } else {
            request.setAttribute("errorMessage", true);
            page = Configuration.getInstance().getConfig(REGISTER);
        }

        return page;
    }
}
