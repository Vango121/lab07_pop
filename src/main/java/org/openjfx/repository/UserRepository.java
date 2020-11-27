package org.openjfx.repository;

import org.openjfx.AccountManager;
import org.openjfx.DatabaseConn;
import org.openjfx.models.Offer;
import org.openjfx.models.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private DatabaseConn databaseConn;
    private Connection connection;
    public List<Offer> getOffers() throws SQLException {
        List<Offer> list = new ArrayList<>();
        databaseConn = new DatabaseConn();
        connection = databaseConn.getConnection();
        String query = "SELECT * FROM `offer`";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            System.out.println(resultSet.getString("description"));
            Offer offer = new Offer(resultSet.getInt("id"),
                    resultSet.getString("description"),
                    resultSet.getString("unit"),
                    resultSet.getFloat("price"));
            list.add(offer);
        }
        return list;
    }
    public void saveOrder(Order order) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "INSERT INTO `orders`(`id`, `offerId`, `quantity`, `userId`, `status`) VALUES (NULL,"+order.getOffer().getId()+","+order.getQuantity()+","+ AccountManager.user.getId()+",0)";
        statement.executeUpdate(query);

    }
}
