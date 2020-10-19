package com.omniscient.FarmCraft.Utils;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.omniscient.FarmCraft.FarmCraft;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    public static Connection connection;
    public static String host = "localhost";
    public static String database = "farmcraft";
    public static String username = "root";
    public static String password = "";
    public static int port = 3306;

    public static boolean isConnected = false;

    public static void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setServerName(host);
            dataSource.setDatabaseName(database);
            dataSource.setUser(username);
            dataSource.setPassword(password);
            dataSource.setPort(port);
            connection = dataSource.getConnection();
            Methods.consoleLog("&aConexão com a base de dados bem sucedida!");
            isConnected = true;
        } catch (SQLException | ClassNotFoundException e) {
            Methods.consoleLog("&cA conexão com a base de dados não foi bem sucedida.");
            isConnected = false;
        }
        int i = Bukkit.getScheduler().scheduleSyncRepeatingTask(FarmCraft.instance, new Runnable() {
            @Override
            public void run() {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    MysqlDataSource dataSource = new MysqlDataSource();
                    dataSource.setServerName(host);
                    dataSource.setDatabaseName(database);
                    dataSource.setUser(username);
                    dataSource.setPassword(password);
                    dataSource.setPort(port);
                    connection = dataSource.getConnection();
                    Methods.consoleLog("&aConexão com a base de dados bem sucedida!");
                    isConnected = true;
                } catch (SQLException | ClassNotFoundException e) {
                    Methods.consoleLog("&cA conexão com a base de dados não foi bem sucedida.");
                    isConnected = false;
                }
            }
        }, 600 * 20, 600 * 20);
    }

    public static void set(String statement) throws SQLException {
        if (isConnected) {
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.execute();
            return;
        }
        Methods.consoleLog("&cA base de dados não respondeu ao comando \"&7" + statement + "&c\".");
    }

    public static ResultSet get(String statement) throws SQLException {
        if (isConnected) {
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            return preparedStatement.executeQuery();
        }
        Methods.consoleLog("&cA base de dados não respondeu ao comando \"&7" + statement + "&c\".");
        return null;
    }
}
