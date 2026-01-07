package org.example.java_jdbc;


public class JavaJdbcApplication {

    public static void main(String[] args) throws Exception {

        DBConnection dbConnection = new DBConnection();
        DataRetriever dataRetriever = new DataRetriever(dbConnection);

            dbConnection.getDBConnection();
            System.out.println("Connexion PostgreSQL OK ✅");

        System.out.println(
                dataRetriever.findTeamById(1).getName()
        );

    }
}


