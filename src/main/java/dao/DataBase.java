package dao;

public enum DataBase {

    MYSQL,
    MONGODB;

    public static DataBase fromValue(String value) {
        DataBase dataBase = null;
        for (DataBase db : values()) {
            if (db.name().equalsIgnoreCase(value)) {
                dataBase = db;
            }
        }

        return dataBase;
    }
}
