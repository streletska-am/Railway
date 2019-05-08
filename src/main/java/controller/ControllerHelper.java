package controller;

import command.Command;
import command.LoginCommand;
import command.LogoutCommand;
import command.MissingCommand;
import command.RegisterCommand;
import command.admin.CancelAllTicketsCommand;
import command.admin.CancelTicketsCommand;
import command.admin.ChangeUsersCommand;
import command.admin.TicketCommand;
import command.admin.UsersCommand;
import command.localization.SetENCommand;
import command.localization.SetUKRCommand;
import command.user.BookTicketsCommand;
import command.user.MainPageCommand;
import command.user.MakeTicketsCommand;
import command.user.SelectCityDateTimeCommand;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ControllerHelper {
    private Map<String, Command> commands = new HashMap<>();
    private static final String BUNDLE_NAME = "command";
    private static final String PARAMETER = "command";
    private static ControllerHelper INSTANCE;

    private ResourceBundle bundle;

    private ControllerHelper() {
        bundle = ResourceBundle.getBundle(BUNDLE_NAME);
        commands.put(bundle.getString("command.login"), new LoginCommand());
        commands.put(bundle.getString("command.register"), new RegisterCommand());
        commands.put(bundle.getString("command.logout"), new LogoutCommand());

        commands.put(bundle.getString("command.user.main"), new MainPageCommand());
        commands.put(bundle.getString("command.user.selectDateTime"), new SelectCityDateTimeCommand());
        commands.put(bundle.getString("command.user.make"), new MakeTicketsCommand());
        commands.put(bundle.getString("command.user.book"), new BookTicketsCommand());

        commands.put(bundle.getString("command.admin.adminUser"), new ChangeUsersCommand());
        commands.put(bundle.getString("command.admin.tickets"), new TicketCommand());
        commands.put(bundle.getString("command.admin.users"), new UsersCommand());
        commands.put(bundle.getString("command.admin.cancel"), new CancelTicketsCommand());
        commands.put(bundle.getString("command.admin.cancelAll"), new CancelAllTicketsCommand());

        commands.put(bundle.getString("command.en"), new SetENCommand());
        commands.put(bundle.getString("command.ukr"), new SetUKRCommand());
    }

    public static ControllerHelper getInstance() {
        if (INSTANCE == null) {
            synchronized (ControllerHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ControllerHelper();
                }
            }
        }
        return INSTANCE;
    }

    public Command getCommand(HttpServletRequest request) {
        String commandString = request.getParameter(PARAMETER);
        Command command = commands.get(commandString);
        if (command == null) {
            command = new MissingCommand();
        }
        return command;
    }

}
