package service.util;

//TODO: JavaDoc!!
public class LogMessageServiceUtil {
    private static final String ERROR_WITH_DAO = "Error with ";
    private static final String SUCCESSFUL_OPERATION = "Successful operation with ";

    private static final String METHOD = "Method: ";
    private static final Character DOT = '.';


    public static String createMethodInfo(String dao, String method) {
        StringBuilder builder = new StringBuilder();
        builder.append(SUCCESSFUL_OPERATION).append(dao).append(DOT);
        builder.append(METHOD).append(method);
        return builder.toString();
    }

    public static String createMethodError(String dao, String method) {
        StringBuilder builder = new StringBuilder();
        builder.append(ERROR_WITH_DAO).append(dao).append(DOT);
        builder.append(METHOD).append(method);
        return builder.toString();
    }
}
