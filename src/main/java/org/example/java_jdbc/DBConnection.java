package org.example.java_jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public Connection getDBConnection() throws SQLException {
       // String url = System.getenv("JDBC_URL");
        //String user = System.getenv("USERNAME");
        //String password = System.getenv("PASSWORD");

        String url="jdbc:postgresql://localhost:5432/mini_football_db" ;
       String user ="mini_football_db_manager ";
        String password ="fitia";

        return DriverManager.getConnection(url, user, password);
    }
}

