package org.openjfx.repository;

import org.openjfx.DatabaseConn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginRepository {
    public ResultSet logInDB(String userName, String password) throws SQLException {
        DatabaseConn databaseConn = new DatabaseConn();
        Connection connection = databaseConn.getConnection();
        Statement statement = connection.createStatement();
        String query = "SELECT * from users WHERE username ='" + userName + "' AND password ='" + password + "'";
        ResultSet resultSet = statement.executeQuery(query);
        return resultSet;
    }
}
