package command;

import model.entity.User;
import service.AdminService;
import service.LoginService;
import service.RouteService;
import util.Configuration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static command.CommandUtil.CITIES_FROM_ATTRIBUTE;
import static command.CommandUtil.CITIES_TO_ATTRIBUTE;
import static command.CommandUtil.DATE_FORMAT;
import static command.CommandUtil.DATE_NOW_ATTRIBUTE;
import static command.CommandUtil.ERROR_MESSAGE;
import static command.CommandUtil.TRAINS_ATTRIBUTE;
import static command.CommandUtil.USERNAME_ATTRIBUTE;
import static command.CommandUtil.USERS_ATTRIBUTE;
import static command.CommandUtil.USER_ATTRIBUTE;

public class LoginCommand implements Command {
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String page;
        String email = request.getParameter(EMAIL).trim();
        String password = request.getParameter(PASSWORD).trim();

        User user = LoginService.getInstance().isPresentLogin(email);

        if (user == null) {
            page = redirectToErrorPage(request);
        } else {
            page = checkIfCorrectPassword(user, request, password);
        }
        return page;
    }

    private String checkIfCorrectPassword(User user, HttpServletRequest request, String inputPassword) {
        String page;
        if (LoginService.getInstance().checkPassword(user, inputPassword)) {
            page = checkIfAdmin(user, request);
        } else {
            page = redirectToErrorPage(request);
        }

        return page;
    }

    private String checkIfAdmin(User user, HttpServletRequest request) {
        String page;
        if (user.isAdmin()) {
            page = redirectToAdminPage(request, user);
        } else {
            page = redirectToUserPage(request, user);
        }
        return page;
    }

    private String redirectToAdminPage(HttpServletRequest request, User user) {
        HttpSession session = request.getSession(false);
        session.setAttribute(USER_ATTRIBUTE, user);

        request.setAttribute(USERS_ATTRIBUTE, AdminService.getInstance().getAllUsers());
        request.setAttribute(USERNAME_ATTRIBUTE, user.getName());
        return Configuration.getInstance().getConfig(Configuration.ADMIN);
    }

    private String redirectToUserPage(HttpServletRequest request, User user) {
        HttpSession session = request.getSession(false);
        session.setAttribute(USER_ATTRIBUTE, user);

        request.setAttribute(CITIES_FROM_ATTRIBUTE, RouteService.getInstance().findAvailableFromStations());
        request.setAttribute(CITIES_TO_ATTRIBUTE, RouteService.getInstance().findAvailableToStations());
        request.setAttribute(TRAINS_ATTRIBUTE, null);

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        request.setAttribute(DATE_NOW_ATTRIBUTE, format.format(new Date()));

        request.setAttribute(USERNAME_ATTRIBUTE, user.getName());
        return Configuration.getInstance().getConfig(Configuration.DATE);
    }

    private String redirectToErrorPage(HttpServletRequest request) {
        request.setAttribute(ERROR_MESSAGE, true);
        return Configuration.getInstance().getConfig(Configuration.LOGIN);
    }
}
