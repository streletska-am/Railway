package dao.mysql;

import dao.RequestDAO;
import dao.mysql.util.LogMessageDAOUtil;
import dao.mysql.util.QueryDAOUtil;
import model.entity.Request;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class MySQLRequestDAO implements RequestDAO {
    private static final Logger LOG = Logger.getLogger(MySQLRequestDAO.class.getName());
    private static final MySQLRequestDAO INSTANCE = new MySQLRequestDAO();

    private static final String TABLE_NAME = "request";

    private static final String LABEL_ID = "id";
    private static final String LABEL_USER_ID = "user_id";
    private static final String LABEL_TRAIN_ID = "train_id";
    private static final String LABEL_TYPE = "type";
    private static final String LABEL_PRICE = "price";

    private MySQLRequestDAO() {
    }

    static MySQLRequestDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Request> findAll() {
        List<Request> result = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;

        try {
            String findAllQuery = QueryDAOUtil.createFindAllQuery(TABLE_NAME);

            connection = MySQLConnectionPool.getInstance().getConnection();
            statement = connection.createStatement();
            ResultSet set = statement.executeQuery(findAllQuery);

            while (set.next()) {
                result.add(getRequest(set));
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
    public Request findById(String id) {
        List<Request> result = findByParameter(id, LABEL_ID);
        if (result.size() != 1)
            return null;

        return result.get(0);
    }

    @Override
    public Request create(Request request) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            String createQuery = QueryDAOUtil.createInsertQuery(
                    TABLE_NAME,
                    LABEL_ID,
                    LABEL_USER_ID,
                    LABEL_TRAIN_ID,
                    LABEL_PRICE,
                    LABEL_TYPE);

            connection = MySQLConnectionPool.getInstance().getConnection();

            statement = connection.prepareStatement(createQuery);
            statement.setString(1, request.getId());
            statement.setString(2, request.getUserId());
            statement.setString(3, request.getTrainId());
            statement.setDouble(4, request.getPrice());
            statement.setString(5, request.getType().toString());

            statement.executeUpdate();

            LOG.info(LogMessageDAOUtil.createInfoCreate(TABLE_NAME, request.getId()));
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createErrorCreate(TABLE_NAME));
        } finally {
            close(connection, statement);
        }

        return request;
    }

    @Override
    public Request update(Request request) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            String createQuery = QueryDAOUtil.createUpdateQuery(TABLE_NAME, LABEL_ID,
                    LABEL_USER_ID,
                    LABEL_TRAIN_ID,
                    LABEL_PRICE,
                    LABEL_TYPE);

            connection = MySQLConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(createQuery);

            statement.setString(1, request.getUserId());
            statement.setString(2, request.getTrainId());
            statement.setDouble(3, request.getPrice());
            statement.setString(4, request.getType().toString());

            statement.setString(5, request.getId());

            statement.executeUpdate();

            LOG.info(LogMessageDAOUtil.createInfoUpdate(TABLE_NAME, request.getId()));
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createErrorUpdate(TABLE_NAME, request.getId()));
        } finally {
            close(connection, statement);
        }

        return request;
    }

    @Override
    public void delete(Request request) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            String createQuery = QueryDAOUtil.createDeleteQuery(TABLE_NAME, LABEL_ID);

            connection = MySQLConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(createQuery);

            statement.setString(1, request.getId());
            statement.executeUpdate();

            LOG.info(LogMessageDAOUtil.createInfoDelete(TABLE_NAME, request.getId()));
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createErrorDelete(TABLE_NAME, request.getId()));
        } finally {
            close(connection, statement);
        }

    }

    private List<Request> findByParameter(String id, String parameterLabel) {
        List<Request> result = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            String findByIdQuery = QueryDAOUtil.createFindByParameterQuery(TABLE_NAME, parameterLabel);

            connection = MySQLConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(findByIdQuery);
            statement.setString(1, id);
            ResultSet set = statement.executeQuery();

            while (set.next()) {
                result.add(getRequest(set));
            }
            LOG.info(LogMessageDAOUtil.createInfoFindByParameter(TABLE_NAME, parameterLabel, id));
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createErrorFindByParameter(TABLE_NAME, parameterLabel, id));
        } finally {
            close(connection, statement);
        }

        return result;
    }

    private Request getRequest(ResultSet set) throws SQLException {
        Request request = new Request();
        request.setId(set.getString(LABEL_ID));
        request.setTrainId(set.getString(LABEL_TRAIN_ID));
        request.setUserId(set.getString(LABEL_USER_ID));
        request.setPrice(set.getDouble(LABEL_PRICE));
        request.setType(TypePlace.valueOf(set.getString(LABEL_TYPE)));
        return request;
    }

    private void close(Connection connection, Statement statement) {
        try {
            if (connection != null) connection.close();
            if (statement != null) statement.close();
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createErrorClose());
        }
    }
}
