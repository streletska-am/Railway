package command.user;

import command.Command;
import dto.TrainRoute;
import model.entity.User;
import service.RouteService;
import service.TrainService;
import util.Configuration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static command.user.CommandUserUtil.CITIES_FROM_ATTRIBUTE;
import static command.user.CommandUserUtil.CITIES_TO_ATTRIBUTE;
import static command.user.CommandUserUtil.DATE_FORMAT;
import static command.user.CommandUserUtil.DATE_NOW_ATTRIBUTE;
import static command.user.CommandUserUtil.DATE_PARAMETER;
import static command.user.CommandUserUtil.FROM_PARAMETER;
import static command.user.CommandUserUtil.NO_TRAINS_ATTRIBUTE;
import static command.user.CommandUserUtil.TIME_PARAMETER;
import static command.user.CommandUserUtil.TO_PARAMETER;
import static command.user.CommandUserUtil.TRAINS_ATTRIBUTE;
import static command.user.CommandUserUtil.USERNAME_ATTRIBUTE;
import static command.user.CommandUserUtil.USER_ATTRIBUTE;

public class SelectCityDateTimeCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User userNow = (User) request.getSession(false).getAttribute(USER_ATTRIBUTE);
        if (userNow == null)
            return Configuration.getInstance().getConfig(Configuration.LOGIN);

        String page = Configuration.getInstance().getConfig(Configuration.DATE);
        Long from_id = Long.parseLong(request.getParameter(FROM_PARAMETER));
        Long to_id = Long.parseLong(request.getParameter(TO_PARAMETER));
        Integer time = Integer.parseInt(request.getParameter(TIME_PARAMETER));
        String dateString = request.getParameter(DATE_PARAMETER);
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);

        Date date = null;
        try {
            date = format.parse(dateString);
            date.setHours(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<TrainRoute> trains = TrainService.getInstance().findTrainsAndRoutes(from_id, to_id, date);

        request.setAttribute(CITIES_FROM_ATTRIBUTE, RouteService.getInstance().findAvailableFromStations());
        request.setAttribute(CITIES_TO_ATTRIBUTE, RouteService.getInstance().findAvailableToStations());

        request.setAttribute(FROM_PARAMETER, from_id);
        request.setAttribute(TO_PARAMETER, to_id);
        request.setAttribute(TRAINS_ATTRIBUTE, trains);
        if (trains.isEmpty()) {
            request.setAttribute(NO_TRAINS_ATTRIBUTE, true);
        }
        request.setAttribute(DATE_NOW_ATTRIBUTE, format.format(date));
        request.setAttribute(TIME_PARAMETER, time);

        request.setAttribute(USERNAME_ATTRIBUTE, userNow.getName());
        return page;
    }
}
