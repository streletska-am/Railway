package dao.mysql;

import dao.RouteDAO;
import dao.mysql.util.LogMessageDAOUtil;
import dao.mysql.util.QueryDAOUtil;
import model.entity.Route;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class MySQLRouteDAO implements RouteDAO {
    private static final Logger LOG = Logger.getLogger(MySQLRouteDAO.class.getName());
    private static final MySQLRouteDAO INSTANCE = new MySQLRouteDAO();

    private static final String TABLE_NAME = "route";

    private static final String LABEL_ID = "id";
    private static final String LABEL_PRICE_ID = "price_id";

    private static final String LABEL_FROM_ID = "from_id";
    private static final String LABEL_TO_ID = "to_id";

    private static final String LABEL_FROM_TIME = "from_time";
    private static final String LABEL_TO_TIME = "to_time";

    private static final String LABEL_DISTANCE = "distance";

    private MySQLRouteDAO() {
    }

    static MySQLRouteDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Route> findAll() {
        List<Route> result = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;

        try {
            String findAllQuery = QueryDAOUtil.createFindAllQuery(TABLE_NAME);

            connection = MySQLConnectionPool.getInstance().getConnection();
            statement = connection.createStatement();

            ResultSet set = statement.executeQuery(findAllQuery);

            while (set.next()) {
                result.add(getRoute(set));
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
    public Route findById(String id) {
        List<Route> result = findByParameter(LABEL_ID, id);
        if (result.size() != 1)
            return null;

        return result.get(0);
    }

    @Override
    public List<Route> findByFromId(String id) {
        return findByParameter(LABEL_FROM_ID, id);
    }

    @Override
    public Route create(Route route) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            String createQuery = QueryDAOUtil.createInsertQuery(
                    TABLE_NAME,
                    LABEL_ID,
                    LABEL_PRICE_ID,
                    LABEL_FROM_ID,
                    LABEL_TO_ID,
                    LABEL_FROM_TIME,
                    LABEL_TO_TIME,
                    LABEL_DISTANCE
            );

            connection = MySQLConnectionPool.getInstance().getConnection();

            statement = connection.prepareStatement(createQuery);

            statement.setString(1, route.getId());
            statement.setString(2, route.getPriceId());
            statement.setString(3, route.getFromId());
            statement.setString(4, route.getToId());
            statement.setString(5, route.getFromTime());
            statement.setString(6, route.getToTime());
            statement.setDouble(7, route.getDistance());

            statement.executeUpdate();

            LOG.info(LogMessageDAOUtil.createInfoCreate(TABLE_NAME, route.getId()));
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createErrorCreate(TABLE_NAME));
        } finally {
            close(connection, statement);
        }

        return route;
    }

    @Override
    public Route update(Route route) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            String createQuery = QueryDAOUtil.createUpdateQuery(TABLE_NAME, LABEL_ID,
                    LABEL_PRICE_ID,
                    LABEL_FROM_ID,
                    LABEL_TO_ID,
                    LABEL_FROM_TIME,
                    LABEL_TO_TIME,
                    LABEL_DISTANCE
            );

            connection = MySQLConnectionPool.getInstance().getConnection();

            statement = connection.prepareStatement(createQuery);

            statement.setString(1, route.getPriceId());
            statement.setString(2, route.getFromId());
            statement.setString(3, route.getToId());
            statement.setString(4, route.getFromTime());
            statement.setString(5, route.getToTime());
            statement.setDouble(6, route.getDistance());

            statement.setString(7, route.getId());

            statement.executeUpdate();

            LOG.info(LogMessageDAOUtil.createInfoUpdate(TABLE_NAME, route.getId()));
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createInfoUpdate(TABLE_NAME, route.getId()));
        } finally {
            close(connection, statement);
        }

        return route;
    }

    @Override
    public void delete(Route route) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            String createQuery = QueryDAOUtil.createDeleteQuery(TABLE_NAME, LABEL_ID);

            connection = MySQLConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(createQuery);

            statement.setString(1, route.getId());
            statement.executeUpdate();

            LOG.info(LogMessageDAOUtil.createInfoDelete(TABLE_NAME, route.getId()));
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createErrorCreate(TABLE_NAME));
        } finally {
            close(connection, statement);
        }
    }


    private Route getRoute(ResultSet set) throws SQLException {
        Route result = new Route();
        result.setId(set.getString(LABEL_ID));

        result.setPriceId(set.getString(LABEL_PRICE_ID));

        result.setFromId(set.getString(LABEL_FROM_ID));
        result.setToId(set.getString(LABEL_TO_ID));

        result.setFromTime(set.getString(LABEL_FROM_TIME));
        result.setToTime(set.getString(LABEL_TO_TIME));

        result.setDistance(set.getDouble(LABEL_DISTANCE));

        return result;
    }

    private List<Route> findByParameter(String label, String parameter) {
        List<Route> result = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            String findByIdQuery = QueryDAOUtil.createFindByParameterQuery(TABLE_NAME, label);

            connection = MySQLConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(findByIdQuery);
            statement.setString(1, parameter);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                result.add(getRoute(set));
            }

            LOG.info(LogMessageDAOUtil.createInfoFindByParameter(TABLE_NAME, label, parameter));
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createErrorFindByParameter(TABLE_NAME, label, parameter));
            result = null;
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

}
