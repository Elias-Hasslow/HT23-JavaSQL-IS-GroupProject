package se.lu.ics.data;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionHandler {
    private static String connectionURL;
    private static String configFilePath = "src/main/resources/config/config.properties";


    static {
        Properties connectionProperties = new Properties();
    

    try {
        FileInputStream stream = new FileInputStream(new File(configFilePath));
        connectionProperties.load(stream);
        stream.close();
    } catch (Exception e) {
        throw new RuntimeException("Could not load config file: " + configFilePath);
    }

    String databaseServerName = (String) connectionProperties.get("database.server.name");
    String databaseServerPort = (String) connectionProperties.get("database.server.port");
    String databaseName = (String) connectionProperties.get("database.name");
    String databaseUserName = (String) connectionProperties.get("database.user.name");
    String databaseUserPassword = (String) connectionProperties.get("database.user.password");

    connectionURL = "jdbc:sqlserver://"
    + databaseServerName + ":" 
    + databaseServerPort + ";"
    + "database=" + databaseName + ";"
    + "user=" + databaseUserName + ";"
    + "password=" + databaseUserPassword + ";"
    + "encrypt=true;"
    + "trustServerCertificate=true;";
    }

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(connectionURL);
    }

}
    
