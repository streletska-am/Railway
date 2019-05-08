package command.localization;

import command.Command;
import util.Configuration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;

import static command.localization.CommandLanguageUtil.USER_ATTRIBUTE;

public class SetUKRCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession(false).setAttribute(USER_ATTRIBUTE, null);
        Config.set(request.getSession(false), Config.FMT_LOCALE, CommandLanguageUtil.UKRAINIAN);
        return Configuration.getInstance().getConfig(Configuration.LOGIN);
    }
}
