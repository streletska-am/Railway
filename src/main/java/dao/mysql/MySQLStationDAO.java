package dao.mysql;

import dao.StationDAO;
import dao.mysql.util.LogMessageDAOUtil;
import dao.mysql.util.QueryDAOUtil;
import model.entity.Station;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class MySQLStationDAO implements StationDAO {
    private static final Logger LOG = Logger.getLogger(MySQLStationDAO.class.getName());
    private static final MySQLStationDAO INSTANCE = new MySQLStationDAO();

    private static final String TABLE_NAME = "station";

    private static final String LABEL_ID = "id";
    private static final String LABEL_NAME = "name";

    private MySQLStationDAO() {
    }

    static MySQLStationDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Station> findAll() {
        List<Station> result = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;

        try {
            String findAllQuery = QueryDAOUtil.createFindAllQuery(TABLE_NAME);

            connection = MySQLConnectionPool.getInstance().getConnection();
            statement = connection.createStatement();

            ResultSet set = statement.executeQuery(findAllQuery);

            while (set.next()) {
                result.add(getStation(set));
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
    public Station findById(String id) {
        Station result = null;
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            String findByIdQuery = QueryDAOUtil.createFindByParameterQuery(TABLE_NAME, LABEL_ID);

            connection = MySQLConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(findByIdQuery);
            statement.setString(1, id);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                result = getStation(set);
            }

            LOG.info(LogMessageDAOUtil.createInfoFindByParameter(TABLE_NAME, LABEL_ID, id));
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createErrorFindByParameter(TABLE_NAME, LABEL_ID, id));
        } finally {
            close(connection, statement);
        }

        return result;
    }

    @Override
    public Station create(Station station) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            String createQuery = QueryDAOUtil.createInsertQuery(
                    TABLE_NAME,
                    LABEL_ID,
                    LABEL_NAME
            );

            connection = MySQLConnectionPool.getInstance().getConnection();

            statement = connection.prepareStatement(createQuery);
            statement.setString(1, station.getId());
            statement.setString(2, station.getName());

            statement.executeUpdate();

            LOG.info(LogMessageDAOUtil.createInfoCreate(TABLE_NAME, station.getId()));
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createErrorCreate(TABLE_NAME));
        } finally {
            close(connection, statement);
        }

        return station;
    }

    @Override
    public Station update(Station station) {
        Connection connection = null;
        PreparedStatement statement = null;


        try {
            String createQuery = QueryDAOUtil.createUpdateQuery(
                    TABLE_NAME,
                    LABEL_ID,
                    LABEL_NAME
            );

            connection = MySQLConnectionPool.getInstance().getConnection();

            statement = connection.prepareStatement(createQuery);
            statement.setString(1, station.getName());
            statement.setString(2, station.getId());

            statement.executeUpdate();

            LOG.info(LogMessageDAOUtil.createInfoUpdate(TABLE_NAME, station.getId()));
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createErrorUpdate(TABLE_NAME, station.getId()));
        } finally {
            close(connection, statement);
        }

        return station;
    }

    @Override
    public void delete(Station station) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            String createQuery = QueryDAOUtil.createDeleteQuery(TABLE_NAME, LABEL_ID);

            connection = MySQLConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(createQuery);

            statement.setString(1, station.getId());
            statement.executeUpdate();

            LOG.info(LogMessageDAOUtil.createInfoDelete(TABLE_NAME, station.getId()));
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createErrorDelete(TABLE_NAME, station.getId()));
        } finally {
            close(connection, statement);
        }
    }

    private Station getStation(ResultSet set) throws SQLException {
        Station result = new Station();

        result.setId(set.getString(LABEL_ID));
        result.setName(set.getString(LABEL_NAME));

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
