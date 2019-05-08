package command.admin;

import command.Command;
import model.entity.User;
import service.AdminService;
import util.Configuration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static command.admin.CommandAdminUtil.USERNAME_ATTRIBUTE;
import static command.admin.CommandAdminUtil.USERS_ATTRIBUTE;
import static command.admin.CommandAdminUtil.USER_ATTRIBUTE;

public class UsersCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User userNow = (User) request.getSession(false).getAttribute(USER_ATTRIBUTE);
        if (userNow == null || !userNow.isAdmin())
            return Configuration.getInstance().getConfig(Configuration.LOGIN);

        request.setAttribute(USERNAME_ATTRIBUTE, userNow.getName());
        request.setAttribute(USERS_ATTRIBUTE, AdminService.getInstance().getAllUsers());
        return Configuration.getInstance().getConfig(Configuration.ADMIN);
    }
}
