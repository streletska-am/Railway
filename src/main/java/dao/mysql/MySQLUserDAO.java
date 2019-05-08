package dao.mysql;

import dao.UserDAO;
import dao.mysql.util.LogMessageDAOUtil;
import dao.mysql.util.QueryDAOUtil;
import model.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


class MySQLUserDAO implements UserDAO {
    private static final Logger LOG = Logger.getLogger(MySQLUserDAO.class.getName());
    private static final MySQLUserDAO INSTANCE = new MySQLUserDAO();

    private static final String TABLE_NAME = "user";

    private static final String LABEL_ID = "id";
    private static final String LABEL_EMAIL = "email";
    private static final String LABEL_PASSWORD = "password";
    private static final String LABEL_NAME = "name";
    private static final String LABEL_SURNAME = "surname";
    private static final String LABEL_PHONE = "phone";
    private static final String LABEL_ADMIN = "admin";

    private MySQLUserDAO() {
    }

    static MySQLUserDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public List<User> findAll() {
        List<User> result = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;

        try {
            String findAllQuery = QueryDAOUtil.createFindAllQuery(TABLE_NAME);

            connection = MySQLConnectionPool.getInstance().getConnection();
            statement = connection.createStatement();

            ResultSet set = statement.executeQuery(findAllQuery);

            while (set.next()) {
                result.add(getUser(set));
            }

            LOG.info(LogMessageDAOUtil.createInfoFindAll(TABLE_NAME));
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createErrorFindAll(TABLE_NAME));
        } finally {
            close(connection, statement);
        }

        return result;
    }

    @Override
    public User findById(String id) {
        List<User> result = findByParameter(LABEL_ID, id);
        if (result.size() != 1)
            return null;

        return result.get(0);
    }

    @Override
    public User findByEmail(String email) {
        List<User> result = findByParameter(LABEL_EMAIL, email);
        if (result.size() != 1)
            return null;

        return result.get(0);
    }

    @Override
    public User create(User user) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            String createQuery = QueryDAOUtil.createInsertQuery(
                    TABLE_NAME,
                    LABEL_ID,
                    LABEL_EMAIL,
                    LABEL_PASSWORD,
                    LABEL_NAME,
                    LABEL_SURNAME,
                    LABEL_PHONE,
                    LABEL_ADMIN
            );

            connection = MySQLConnectionPool.getInstance().getConnection();

            statement = connection.prepareStatement(createQuery);
            statement.setString(1, user.getId());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getName());
            statement.setString(5, user.getSurname());
            statement.setString(6, user.getPhone());
            statement.setBoolean(7, user.getAdmin());

            statement.executeUpdate();

            LOG.info(LogMessageDAOUtil.createInfoCreate(TABLE_NAME, user.getId()));
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createErrorCreate(TABLE_NAME));
        } finally {
            close(connection, statement);
        }

        return user;
    }

    @Override
    public User update(User user) {
        Connection connection = null;
        PreparedStatement statement = null;


        try {
            String createQuery = QueryDAOUtil.createUpdateQuery(
                    TABLE_NAME,
                    LABEL_ID,
                    LABEL_EMAIL,
                    LABEL_PASSWORD,
                    LABEL_NAME,
                    LABEL_SURNAME,
                    LABEL_PHONE,
                    LABEL_ADMIN
            );

            connection = MySQLConnectionPool.getInstance().getConnection();

            statement = connection.prepareStatement(createQuery);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());
            statement.setString(4, user.getSurname());
            statement.setString(5, user.getPhone());
            statement.setBoolean(6, user.getAdmin());

            statement.setString(7, user.getId());

            statement.executeUpdate();

            LOG.info(LogMessageDAOUtil.createInfoUpdate(TABLE_NAME, user.getId()));
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createInfoUpdate(TABLE_NAME, user.getId()));
        } finally {
            close(connection, statement);
        }

        return user;
    }

    @Override
    public void delete(User user) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            String createQuery = QueryDAOUtil.createDeleteQuery(TABLE_NAME, LABEL_ID);

            connection = MySQLConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(createQuery);

            statement.setString(1, user.getId());
            statement.executeUpdate();

            LOG.info(LogMessageDAOUtil.createInfoDelete(TABLE_NAME, user.getId()));
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createErrorCreate(TABLE_NAME));
        } finally {
            close(connection, statement);
        }
    }

    private List<User> findByParameter(String label, Object parameter) {
        List<User> result = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            String findByIdQuery = QueryDAOUtil.createFindByParameterQuery(TABLE_NAME, label);

            connection = MySQLConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(findByIdQuery);
            statement.setObject(1, parameter);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                result.add(getUser(set));
            }

            LOG.info(LogMessageDAOUtil.createInfoFindByParameter(TABLE_NAME, label, parameter));
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createErrorFindByParameter(TABLE_NAME, label, parameter));
        } finally {
            close(connection, statement);
        }

        return result;
    }

    private void close(Connection connection, Statement statement) {
        try {
            if (connection != null) connection.close();
            if (statement != null) statement.close();
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createErrorClose());
        }
    }

    private User getUser(ResultSet set) throws SQLException {
        User result = new User();
        result.setId(set.getString(LABEL_ID));
        result.setEmail(set.getString(LABEL_EMAIL));
        result.setPassword(set.getString(LABEL_PASSWORD));

        result.setName(set.getString(LABEL_NAME));
        result.setSurname(set.getString(LABEL_SURNAME));
        result.setPhone(set.getString(LABEL_PHONE));

        result.setAdmin(set.getBoolean(LABEL_ADMIN));
        return result;
    }
}
