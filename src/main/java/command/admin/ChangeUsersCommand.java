package command.admin;

import command.Command;
import model.entity.User;
import service.AdminService;
import util.Configuration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static command.admin.CommandAdminUtil.ADMIN;
import static command.admin.CommandAdminUtil.DELETE;
import static command.admin.CommandAdminUtil.USER;
import static command.admin.CommandAdminUtil.USERNAME_ATTRIBUTE;
import static command.admin.CommandAdminUtil.USERS_ATTRIBUTE;
import static command.admin.CommandAdminUtil.USER_ATTRIBUTE;


public class ChangeUsersCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User userNow = (User) request.getSession(false).getAttribute(USER_ATTRIBUTE);
        if (userNow == null || !userNow.isAdmin())
            return Configuration.getInstance().getConfig(Configuration.LOGIN);

        List<User> users = AdminService.getInstance().getAllUsers();
        for (User user : users) {
            switch (request.getParameter(user.getId().toString())) {
                case DELETE:
                    AdminService.getInstance().deleteUser(user);
                    break;
                case ADMIN:
                    user.makeAdmin();
                    AdminService.getInstance().updateUser(user);
                    break;
                case USER:
                    user.makeUser();
                    AdminService.getInstance().updateUser(user);
                    break;
                default:
                    break;

            }
        }

        request.setAttribute(USERNAME_ATTRIBUTE, userNow.getName());
        request.setAttribute(USERS_ATTRIBUTE, AdminService.getInstance().getAllUsers());
        return Configuration.getInstance().getConfig(Configuration.ADMIN);
    }
}
