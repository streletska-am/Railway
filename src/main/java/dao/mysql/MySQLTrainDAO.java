package dao.mysql;

import dao.TrainDAO;
import dao.mysql.util.LogMessageDAOUtil;
import dao.mysql.util.QueryDAOUtil;
import model.entity.Train;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class MySQLTrainDAO implements TrainDAO {
    private static final Logger LOG = Logger.getLogger(MySQLTrainDAO.class.getName());
    private static final MySQLTrainDAO INSTANCE = new MySQLTrainDAO();

    private static final String TABLE_NAME = "train";

    private static final String LABEL_ID = "id";
    private static final String LABEL_ROUTE_ID = "route_id";
    private static final String LABEL_COMPARTMENT_FREE = "compartment_free";
    private static final String LABEL_BERTH_FREE = "berth_free";
    private static final String LABEL_DELUXE_FREE = "deluxe_free";

    private MySQLTrainDAO() {
    }

    static MySQLTrainDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Train> findAll() {
        List<Train> result = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;

        try {
            String findAllQuery = QueryDAOUtil.createFindAllQuery(TABLE_NAME);

            connection = MySQLConnectionPool.getInstance().getConnection();
            statement = connection.createStatement();

            ResultSet set = statement.executeQuery(findAllQuery);

            while (set.next()) {
                result.add(getTrain(set));
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
    public List<Train> findByRoute(Long route_id) {
        return findByParameter(LABEL_ROUTE_ID, route_id);
    }

    @Override
    public Train findById(Long id) {
        List<Train> result = findByParameter(LABEL_ID, id);
        if (result.size() != 1)
            return null;

        return result.get(0);
    }

    @Override
    public Train create(Train train) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            String createQuery = QueryDAOUtil.createInsertQuery(
                    TABLE_NAME,
                    LABEL_ID,
                    LABEL_ROUTE_ID,
                    LABEL_BERTH_FREE,
                    LABEL_COMPARTMENT_FREE,
                    LABEL_DELUXE_FREE
            );

            connection = MySQLConnectionPool.getInstance().getConnection();

            statement = connection.prepareStatement(createQuery);
            statement.setLong(1, train.getId());
            statement.setLong(2, train.getRouteId());
            statement.setLong(3, train.getBerthFree());
            statement.setLong(4, train.getCompartmentFree());
            statement.setLong(5, train.getDeluxeFree());

            statement.executeUpdate();

            LOG.info(LogMessageDAOUtil.createInfoCreate(TABLE_NAME, train.getId()));
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createErrorCreate(TABLE_NAME));
        } finally {
            close(connection, statement);
        }

        return train;
    }

    @Override
    public Train update(Train train) {
        Connection connection = null;
        PreparedStatement statement = null;


        try {
            String createQuery = QueryDAOUtil.createUpdateQuery(
                    TABLE_NAME,
                    LABEL_ID,
                    LABEL_ROUTE_ID,
                    LABEL_BERTH_FREE,
                    LABEL_COMPARTMENT_FREE,
                    LABEL_DELUXE_FREE
            );

            connection = MySQLConnectionPool.getInstance().getConnection();

            statement = connection.prepareStatement(createQuery);
            statement.setLong(1, train.getRouteId());
            statement.setLong(2, train.getBerthFree());
            statement.setLong(3, train.getCompartmentFree());
            statement.setLong(4, train.getDeluxeFree());

            statement.setLong(5, train.getId());

            statement.executeUpdate();

            LOG.info(LogMessageDAOUtil.createInfoUpdate(TABLE_NAME, train.getId()));
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createErrorUpdate(TABLE_NAME, train.getId()));
        } finally {
            close(connection, statement);
        }

        return train;
    }

    @Override
    public void delete(Train train) {

    }

    private List<Train> findByParameter(String label, Long parameter) {
        List<Train> result = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            String findByIdQuery = QueryDAOUtil.createFindByParameterQuery(TABLE_NAME, label);

            connection = MySQLConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(findByIdQuery);
            statement.setLong(1, parameter);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                result.add(getTrain(set));
            }

            LOG.info(LogMessageDAOUtil.createInfoFindByParameter(TABLE_NAME, label, parameter));
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createErrorFindByParameter(TABLE_NAME, label, parameter));
        } finally {
            close(connection, statement);
        }

        return result;
    }

    private Train getTrain(ResultSet set) throws SQLException {
        Train result = new Train();

        result.setId(set.getLong(LABEL_ID));
        result.setRouteId(set.getLong(LABEL_ROUTE_ID));
        result.setBerthFree(set.getInt(LABEL_BERTH_FREE));
        result.setCompartmentFree(set.getInt(LABEL_COMPARTMENT_FREE));
        result.setDeluxeFree(set.getInt(LABEL_DELUXE_FREE));

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
