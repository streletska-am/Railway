package dao.mysql;

import dao.PriceDAO;
import dao.mysql.util.LogMessageDAOUtil;
import dao.mysql.util.QueryDAOUtil;
import model.entity.Price;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class MySQLPriceDAO implements PriceDAO {
    private static final Logger LOG = Logger.getLogger(MySQLPriceDAO.class.getName());
    private static final MySQLPriceDAO INSTANCE = new MySQLPriceDAO();
    private static final String TABLE_NAME = "price";

    private static final String LABEL_ID = "id";
    private static final String LABEL_BERTH_FACTOR = "berth_factor";
    private static final String LABEL_COMPARTMENT_FACTOR = "compartment_factor";
    private static final String LABEL_DELUXE_FACTOR = "deluxe_factor";

    private MySQLPriceDAO() {
    }

    static MySQLPriceDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Price> findAll() {
        List<Price> result = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;

        try {
            String findAllQuery = QueryDAOUtil.createFindAllQuery(TABLE_NAME);

            connection = MySQLConnectionPool.getInstance().getConnection();
            statement = connection.createStatement();
            ResultSet set = statement.executeQuery(findAllQuery);
            while (set.next()) {
                result.add(getPrice(set));
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
    public Price findById(String id) {
        Price result = null;
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            String findByIdQuery = QueryDAOUtil.createFindByParameterQuery(TABLE_NAME, LABEL_ID);

            connection = MySQLConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(findByIdQuery);
            statement.setString(1, id);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                result = getPrice(set);
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
    public Price create(Price price) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            String createQuery = QueryDAOUtil.createInsertQuery(
                    TABLE_NAME,
                    LABEL_ID,
                    LABEL_BERTH_FACTOR,
                    LABEL_COMPARTMENT_FACTOR,
                    LABEL_DELUXE_FACTOR);

            connection = MySQLConnectionPool.getInstance().getConnection();

            statement = connection.prepareStatement(createQuery);
            statement.setString(1, price.getId());
            statement.setDouble(2, price.getBerthFactor());
            statement.setDouble(3, price.getCompartmentFactor());
            statement.setDouble(4, price.getDeluxeFactor());

            statement.executeUpdate();

            LOG.info(LogMessageDAOUtil.createInfoCreate(TABLE_NAME, price.getId()));
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createErrorCreate(TABLE_NAME));
        } finally {
            close(connection, statement);
        }

        return price;
    }

    @Override
    public Price update(Price price) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            String updateQuery = QueryDAOUtil.createUpdateQuery(TABLE_NAME, LABEL_ID,
                    LABEL_BERTH_FACTOR,
                    LABEL_COMPARTMENT_FACTOR,
                    LABEL_DELUXE_FACTOR);

            connection = MySQLConnectionPool.getInstance().getConnection();

            statement = connection.prepareStatement(updateQuery);
            statement.setDouble(1, price.getBerthFactor());
            statement.setDouble(2, price.getCompartmentFactor());
            statement.setDouble(3, price.getDeluxeFactor());
            statement.setString(4, price.getId());

            statement.executeUpdate();

            LOG.info(LogMessageDAOUtil.createInfoUpdate(TABLE_NAME, price.getId()));
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createErrorUpdate(TABLE_NAME, price.getId()));
        } finally {
            close(connection, statement);
        }

        return price;
    }

    @Override
    public void delete(Price price) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            String deleteQuery = QueryDAOUtil.createDeleteQuery(TABLE_NAME, LABEL_ID);

            connection = MySQLConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(deleteQuery);
            statement.setString(1, price.getId());
            statement.executeUpdate();

            LOG.info(LogMessageDAOUtil.createInfoDelete(TABLE_NAME, price.getId()));
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createErrorDelete(TABLE_NAME, price.getId()));
        } finally {
            close(connection, statement);
        }

    }

    private void close(Connection connection, Statement statement) {
        try {
            if (connection != null) connection.close();
            if (statement != null) statement.close();
        } catch (SQLException e) {
            LOG.severe(LogMessageDAOUtil.createErrorClose());
        }
    }

    private Price getPrice(ResultSet set) throws SQLException {
        Price result = new Price();
        result.setId(set.getString(LABEL_ID));
        result.setBerthFactor(set.getDouble(LABEL_BERTH_FACTOR));
        result.setCompartmentFactor(set.getDouble(LABEL_BERTH_FACTOR));
        result.setDeluxeFactor(set.getDouble(LABEL_DELUXE_FACTOR));

        return result;
    }

}
