package dao.mysql.util;

public class LogMessageDAOUtil {
    private static final String CREATE = "CREATE";
    private static final String UPDATE = "UPDATE";
    private static final String DELETE = "DELETE";

    private static final String ID = "ID";
    private static final String WITH = "with";
    private static final String CANNOT = "Cannot";
    private static final String CANNOT_GET = "Cannot get";
    private static final String CLOSE = "close()";
    private static final String LIST = "LIST";
    private static final String OF = "of";

    private static final String SUCCESSFUL = "Successful";
    private static final String FIND_ALL = "Find All";
    private static final String FIND_BY_PARAMETER = "Find by parameter";

    private static final Character SPACE = ' ';
    private static final Character EQUALLY = '=';

    /**
     * @param tableName
     * @return Successful Find All {tableName}
     */
    public static String createInfoFindAll(String tableName) {
        StringBuilder builder = new StringBuilder();
        builder.append(SUCCESSFUL).append(SPACE);
        builder.append(FIND_ALL).append(SPACE);
        builder.append(tableName.toUpperCase());
        return builder.toString();
    }


    public static String createInfoFindByParameter(String tableName, String parameterLabel, Object parameter) {
        StringBuilder builder = new StringBuilder();
        builder.append(SUCCESSFUL).append(SPACE);
        builder.append(FIND_BY_PARAMETER).append(SPACE);
        builder.append(parameterLabel.toUpperCase()).append(SPACE);
        builder.append(EQUALLY).append(SPACE);
        builder.append(parameter);

        return builder.toString();
    }

    /**
     * @param tableName
     * @param id
     * @return CREATE {tableName} with ID = {id}
     */
    public static String createInfoCreate(String tableName, String id) {
        return createInfo(CREATE, tableName, ID, id);
    }

    /**
     * @param tableName
     * @param id
     * @return UPDATE {tableName} with ID = {id}
     */
    public static String createInfoUpdate(String tableName, String id) {
        return createInfo(UPDATE, tableName, ID, id);
    }

    /**
     * @param tableName
     * @param id
     * @return DELETE {tableName} with ID = {id}
     */
    public static String createInfoDelete(String tableName, String id) {
        return createInfo(DELETE, tableName, ID, id);
    }


    /**
     * @param tableName
     * @return Cannot get LIST of {tableName}
     */
    public static String createErrorFindAll(String tableName) {
        StringBuilder builder = new StringBuilder();
        builder.append(CANNOT_GET).append(SPACE);
        builder.append(LIST).append(SPACE);
        builder.append(OF).append(SPACE);
        builder.append(tableName.toUpperCase());
        return builder.toString();
    }

    /**
     * @param tableName
     * @param parameterLabel
     * @param parameter
     * @return Cannot get {tableName} with {parameterLabel} = {id}
     */
    public static String createErrorFindByParameter(String tableName, String parameterLabel, Object parameter) {
        return createInfo(CANNOT_GET, tableName, parameterLabel, parameter);
    }

    /**
     * @param tableName
     * @return Cannot CREATE {tableName}
     */
    public static String createErrorCreate(String tableName) {
        StringBuilder builder = new StringBuilder();
        builder.append(CANNOT).append(SPACE);
        builder.append(CREATE).append(SPACE);
        builder.append(tableName.toUpperCase());
        return builder.toString();
    }

    /**
     * @param tableName
     * @param id
     * @return Cannot UPDATE {tableName} with ID = {id}
     */
    public static String createErrorUpdate(String tableName, String id) {
        return createInfo(CANNOT + SPACE + UPDATE, tableName, ID, id);
    }

    /**
     * @param tableName
     * @param id
     * @return Cannot DELETE {tableName} with ID = {id}
     */
    public static String createErrorDelete(String tableName, String id) {
        return createInfo(CANNOT + SPACE + DELETE, tableName, ID, id);
    }

    /**
     * @return Cannot close()
     */
    public static String createErrorClose() {
        return new StringBuilder()
                .append(CANNOT)
                .append(SPACE)
                .append(CLOSE)
                .toString();
    }

    /**
     * PRIVATE util method.
     *
     * @param operation
     * @param tableName
     * @param parameterLabel
     * @param parameter
     * @return
     */
    private static String createInfo(String operation, String tableName, String parameterLabel, Object parameter) {
        StringBuilder builder = new StringBuilder();
        builder.append(operation).append(SPACE);
        builder.append(tableName.toUpperCase()).append(SPACE);
        builder.append(WITH).append(SPACE);
        builder.append(parameterLabel.toUpperCase()).append(SPACE);
        builder.append(EQUALLY).append(SPACE);
        builder.append(parameter);

        return builder.toString();
    }
}
