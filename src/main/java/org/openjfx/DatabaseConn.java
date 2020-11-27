package org.openjfx;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConn {
    public Connection connection;
    public Connection getConnection(){
        String dbName = "shopcs";
        String userName = "testownik43";
        String password = "Jakizal1";
        try{
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://hackheroes.cba.pl:3306/"+dbName+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",userName,password);
            System.out.println ("Database connection established");
        }catch (Exception e){
            System.err.println ("Cannot connect to database server");
            e.printStackTrace();
        }
        return connection;
    }
}
