package hexatorn.util.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


class DataBase_CreateConnection {

    private final static String DRIVER = "org.sqlite.JDBC";
    private static Connection connection;

    static Connection connect(){


        String DB_URL = "jdbc:sqlite:baza.db";


        try {
            Class.forName(DataBase_CreateConnection.DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Brak sterownika JDBC");
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(DB_URL);
            //query = connection.createStatement();
        } catch (SQLException e){
            System.err.println("Problem z otwarciem polaczenia");
            e.printStackTrace();
        }

        return connection;
    }

    static Statement getQueryStatment(){

        Statement query = null;

        try {
            query = connection.createStatement();
        } catch (SQLException e) {
            System.err.println("Problem połączenia z bazą danych");
            e.printStackTrace();
        }
        return query;
    }


}
