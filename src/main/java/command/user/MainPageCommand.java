package command.user;

import command.Command;
import model.entity.User;
import service.RouteService;
import util.Configuration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static command.user.CommandUserUtil.CITIES_FROM_ATTRIBUTE;
import static command.user.CommandUserUtil.CITIES_TO_ATTRIBUTE;
import static command.user.CommandUserUtil.DATE_FORMAT;
import static command.user.CommandUserUtil.DATE_NOW_ATTRIBUTE;
import static command.user.CommandUserUtil.TRAINS_ATTRIBUTE;
import static command.user.CommandUserUtil.USERNAME_ATTRIBUTE;
import static command.user.CommandUserUtil.USER_ATTRIBUTE;

public class MainPageCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User userNow = (User) request.getSession(false).getAttribute(USER_ATTRIBUTE);
        if (userNow == null)
            return Configuration.getInstance().getConfig(Configuration.LOGIN);

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);

        request.setAttribute(CITIES_FROM_ATTRIBUTE, RouteService.getInstance().findAvailableFromStations());
        request.setAttribute(CITIES_TO_ATTRIBUTE, RouteService.getInstance().findAvailableToStations());
        request.setAttribute(TRAINS_ATTRIBUTE, null);
        request.setAttribute(USERNAME_ATTRIBUTE, userNow.getName());
        request.setAttribute(DATE_NOW_ATTRIBUTE, format.format(new Date()));
        return Configuration.getInstance().getConfig(Configuration.DATE);
    }
}
