package command;

import util.Configuration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String page = Configuration.getInstance().getConfig(Configuration.LOGIN);
        HttpSession session = request.getSession(false);
        if (session.getAttribute("user") != null) {
            session.setAttribute("user", null);
        }
        return page;
    }
}
