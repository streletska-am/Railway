package service;

import dao.AbstractDAOFactory;
import dao.DAOFactory;
import dao.DataBase;
import model.entity.User;
import org.apache.commons.codec.digest.DigestUtils;
import service.util.LogMessageServiceUtil;
import util.Configuration;

import java.util.UUID;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;
import static util.Configuration.PROFILE_DATABASE;

public class LoginService {
    private static final Logger LOG = Logger.getLogger(LoginService.class.getName());
    private static LoginService INSTANCE;

    private static final String USER_DAO = "UserDAO";

    private static final String ADD_USER = "addUser()";
    private static final String IS_PRESENT_LOGIN = "isPresentUser()";

    private DAOFactory factory;

    private LoginService() {
        DataBase dataBase = DataBase.fromValue(Configuration.getInstance().getConfig(PROFILE_DATABASE));
        factory = AbstractDAOFactory.createDAOFactory(dataBase);
    }

    public static LoginService getInstance() {
        if (INSTANCE == null) {
            synchronized (LoginService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LoginService();
                }
            }
        }

        return INSTANCE;
    }

    public User isPresentLogin(String login) {
        User user = factory.createUserDAO().findByEmail(login);
        LOG.info(LogMessageServiceUtil.createMethodInfo(USER_DAO, IS_PRESENT_LOGIN));
        return user;
    }

    public User addUser(User user) {
        user = securePassword(user);
        user = generateId(user);
        User createdUser = factory.createUserDAO().create(user);
        if (createdUser == null) {
            LOG.severe(LogMessageServiceUtil.createMethodError(USER_DAO, ADD_USER));
        }

        LOG.info(LogMessageServiceUtil.createMethodInfo(USER_DAO, ADD_USER));
        return createdUser;
    }

    public boolean checkPassword(User user, String password) {
        String securePassword = DigestUtils.md5Hex(password);
        return securePassword.equals(user.getPassword());
    }

    User securePassword(final User user) {
        System.out.println(user.getPassword());
        String securePassword = DigestUtils.md5Hex(user.getPassword());
        System.out.println(securePassword);
        user.setPassword(securePassword);
        return user;
    }

    User generateId(final User user) {
        String id = UUID.nameUUIDFromBytes(user.getEmail().getBytes(UTF_8)).toString();
        user.setId(id);
        return user;
    }

    public void setFactory(DAOFactory factory) {
        this.factory = factory;
    }
}
