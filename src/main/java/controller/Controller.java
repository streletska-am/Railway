package controller;

import command.Command;
import util.Configuration;
import util.Message;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(Controller.class.getName());

    private static final String CHARACTER_ENCODING = "UTF-8";
    private static final String CONTENT_TYPE = "text/html;charset=utf-8";
    private static final String MESSAGE_ERROR_ATTRIBUTE = "messageError";
    private static final String PAGE_IS_NULL = "Page is NULL";

    private ControllerHelper controllerHelper = ControllerHelper.getInstance();

    @Override
    public void init() throws ServletException {
        super.init();
        Locale.setDefault(Locale.US);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String page;
        try {
            HttpSession session = request.getSession(false);
            if (session == null) {
                request.getSession(true);
            }

            Command command = controllerHelper.getCommand(request);
            page = command.execute(request, response);
        } catch (ServletException e) {
            //LOG.severe(e.getMessage());
            LOG.log(Level.SEVERE, e.getMessage(), e);
            request.setAttribute(MESSAGE_ERROR_ATTRIBUTE, Message.getInstance().getMessage(Message.SERVLET_EXCEPTION));
            page = Configuration.getInstance().getConfig(Configuration.ERROR);

        } catch (IOException e) {
            //LOG.severe(e.getMessage());
            LOG.log(Level.SEVERE, e.getMessage(), e);
            request.setAttribute(MESSAGE_ERROR_ATTRIBUTE, Message.getInstance().getMessage(Message.IO_EXCEPTION));
            page = Configuration.getInstance().getConfig(Configuration.ERROR);

        } catch (Exception e) {
            //LOG.severe(e.getMessage());
            LOG.log(Level.SEVERE, e.getMessage(), e);
            request.setAttribute(MESSAGE_ERROR_ATTRIBUTE, Message.getInstance().getMessage(Message.EXCEPTION));
            page = Configuration.getInstance().getConfig(Configuration.ERROR);

        }

        if (page == null) {
            LOG.severe(PAGE_IS_NULL);
            request.setAttribute(MESSAGE_ERROR_ATTRIBUTE, Message.getInstance().getMessage(Message.PAGE_IS_NULL));
            page = Configuration.getInstance().getConfig(Configuration.ERROR);
        }

        response.setCharacterEncoding(CHARACTER_ENCODING);
        response.setContentType(CONTENT_TYPE);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
        dispatcher.forward(request, response);
    }
}
