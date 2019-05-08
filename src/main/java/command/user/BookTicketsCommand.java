package command.user;

import command.Command;
import dto.Ticket;
import exception.InvalidDataBaseOperation;
import model.entity.User;
import service.RequestService;
import service.RouteService;
import util.Configuration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static command.user.CommandUserUtil.CITIES_FROM_ATTRIBUTE;
import static command.user.CommandUserUtil.CITIES_TO_ATTRIBUTE;
import static command.user.CommandUserUtil.DATE_FORMAT;
import static command.user.CommandUserUtil.DATE_NOW_ATTRIBUTE;
import static command.user.CommandUserUtil.MESSAGE_ERROR_ATTRIBUTE;
import static command.user.CommandUserUtil.TICKETS_ATTRIBUTE;
import static command.user.CommandUserUtil.TRAINS_ATTRIBUTE;
import static command.user.CommandUserUtil.USERNAME_ATTRIBUTE;
import static command.user.CommandUserUtil.USER_ATTRIBUTE;

public class BookTicketsCommand implements Command {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User userNow = (User) request.getSession(false).getAttribute(USER_ATTRIBUTE);
        if (userNow == null)
            return Configuration.getInstance().getConfig(Configuration.LOGIN);

        String page = Configuration.getInstance().getConfig(Configuration.TICKET);

        List<Ticket> tickets = (List<Ticket>) request.getSession(false).getAttribute(TICKETS_ATTRIBUTE);
        if (tickets == null) {
            request.setAttribute(CITIES_FROM_ATTRIBUTE, RouteService.getInstance().findAvailableFromStations());
            request.setAttribute(CITIES_TO_ATTRIBUTE, RouteService.getInstance().findAvailableToStations());
            request.setAttribute(TRAINS_ATTRIBUTE, null);

            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            request.setAttribute(DATE_NOW_ATTRIBUTE, format.format(new Date()));
            page = Configuration.getInstance().getConfig(Configuration.DATE);
        } else {
            List<Ticket> resultTickets = new ArrayList<>();

            for (Ticket ticket : tickets) {
                Integer count = Integer.parseInt(request.getParameter(ticket.getTrainId()));
                resultTickets.addAll(RequestService.getInstance().addTickets(ticket, count));
            }

            try {
                RequestService.getInstance().reserveTickets(resultTickets);
                request.setAttribute(TICKETS_ATTRIBUTE, resultTickets);
            } catch (InvalidDataBaseOperation e) {
                request.setAttribute(MESSAGE_ERROR_ATTRIBUTE, e.getMessage());
                page = Configuration.getInstance().getConfig(Configuration.ERROR);
            }

        }
        request.setAttribute(USERNAME_ATTRIBUTE, userNow.getName());
        return page;
    }
}
