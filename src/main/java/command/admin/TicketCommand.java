package command.admin;

import command.Command;
import model.entity.User;
import service.RequestService;
import util.Configuration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static command.admin.CommandAdminUtil.TICKETS_ATTRIBUTE;
import static command.admin.CommandAdminUtil.USERNAME_ATTRIBUTE;
import static command.admin.CommandAdminUtil.USER_ATTRIBUTE;

public class TicketCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User userNow = (User) request.getSession(false).getAttribute(USER_ATTRIBUTE);
        if (userNow == null || !userNow.isAdmin())
            return Configuration.getInstance().getConfig(Configuration.LOGIN);

        request.setAttribute(USERNAME_ATTRIBUTE, userNow.getName());
        request.setAttribute(TICKETS_ATTRIBUTE, RequestService.getInstance().findAllTickets());
        return Configuration.getInstance().getConfig(Configuration.TICKETS_ADMIN);
    }
}
