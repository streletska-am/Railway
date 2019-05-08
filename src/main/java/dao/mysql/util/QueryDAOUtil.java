package dao.mysql.util;

public class QueryDAOUtil {
    private static final String SELECT = "SELECT";
    private static final String UPDATE = "UPDATE";
    private static final String DELETE = "DELETE";
    private static final String INSERT = "INSERT";

    private static final String INTO = "INTO";
    private static final String SET = "SET";
    private static final String AND = "AND";
    private static final String FROM = "FROM";
    private static final String WHERE = "WHERE";
    private static final String VALUES = "VALUES";

    private static final Character SPACE = ' ';
    private static final Character EQUALLY = '=';
    private static final Character QUESTION = '?';
    private static final Character COMMA = ',';
    private static final Character STAR = '*';

    private static final Character LEFT_BRACKET = '(';
    private static final Character RIGHT_BRACKET = ')';

    /**
     * @param tableName
     * @return SELECT * FROM {tableName}
     */
    public static String createFindAllQuery(String tableName) {
        StringBuilder builder = new StringBuilder();
        builder.append(SELECT).append(SPACE);
        builder.append(STAR).append(SPACE);
        builder.append(FROM).append(SPACE);
        builder.append(tableName);
        return builder.toString();
    }

    /**
     * @param tableName
     * @param labelParameters
     * @return SELECT * FROM {tableName} WHERE {labelParameters}=? AND {labelParameters}=? ...
     */
    public static String createFindByParameterQuery(String tableName, String... labelParameters) {
        StringBuilder builder = new StringBuilder();
        builder.append(SELECT).append(SPACE);
        builder.append(STAR).append(SPACE);
        builder.append(FROM).append(SPACE);
        builder.append(tableName.toUpperCase()).append(SPACE);
        builder.append(WHERE).append(SPACE);
        builder.append(labelParameters[0]).append(EQUALLY).append(QUESTION).append(SPACE);

        for (int i = 1; i < labelParameters.length; i++) {
            builder.append(AND).append(SPACE);
            builder.append(labelParameters[i]).append(EQUALLY).append(QUESTION).append(SPACE);
        }

        return builder.toString();
    }

    /**
     * @param tableName
     * @param parameters
     * @return INSERT INTO {tableName(parameters with comma)} VALUES (?,?, ...)
     */
    public static String createInsertQuery(String tableName, String... parameters) {
        StringBuilder builder = new StringBuilder();
        builder.append(INSERT).append(SPACE);
        builder.append(INTO).append(SPACE);
        builder.append(tableName.toUpperCase()).append(LEFT_BRACKET);

        for (int i = 0; i < parameters.length - 1; i++) {
            builder.append(parameters[i]).append(COMMA);
        }
        builder.append(parameters[parameters.length - 1])
                .append(RIGHT_BRACKET)
                .append(SPACE);

        builder.append(VALUES).append(LEFT_BRACKET);

        for (int i = 0; i < parameters.length - 1; i++) {
            builder.append(QUESTION).append(COMMA);
        }
        builder.append(QUESTION)
                .append(RIGHT_BRACKET)
                .append(SPACE);

        return builder.toString();
    }

    /**
     * @param tableName
     * @param parameterToSearch
     * @param parametersToUpdate
     * @return UPDATE {tableName} SET {parametersToUpdate with commas} WHERE {parameterToSearch}=?
     */
    public static String createUpdateQuery(String tableName, String parameterToSearch, String... parametersToUpdate) {
        StringBuilder builder = new StringBuilder();
        builder.append(UPDATE).append(SPACE);
        builder.append(tableName.toUpperCase()).append(SPACE);
        builder.append(SET).append(SPACE);

        for (int i = 0; i < parametersToUpdate.length - 1; i++) {
            builder.append(parametersToUpdate[i])
                    .append(EQUALLY)
                    .append(QUESTION)
                    .append(COMMA);
        }
        builder.append(parametersToUpdate[parametersToUpdate.length - 1])
                .append(EQUALLY)
                .append(QUESTION)
                .append(SPACE);
        builder.append(WHERE).append(SPACE);
        builder.append(parameterToSearch).append(EQUALLY).append(QUESTION);

        return builder.toString();

    }

    /**
     * @param tableName
     * @param parameter
     * @return DELETE {tableName} WHERE {parameter}=?
     */
    public static String createDeleteQuery(String tableName, String parameter) {
        StringBuilder builder = new StringBuilder();
        builder.append(DELETE).append(SPACE);
        builder.append(FROM).append(SPACE);
        builder.append(tableName.toUpperCase()).append(SPACE);
        builder.append(WHERE).append(SPACE);
        builder.append(parameter).append(EQUALLY).append(QUESTION);

        return builder.toString();
    }

}
