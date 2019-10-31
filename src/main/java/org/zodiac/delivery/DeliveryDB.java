package org.zodiac.delivery;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.zodiac.delivery.model.Delivery;
import org.zodiac.delivery.model.User;

public class DeliveryDB {
    private static final String USER_ID = "zodiac";
    private static final String USER_PW = "_Zodiac_Ray_25";
    private static final String DATABASE_NAME = "Delivery";
    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/" + DATABASE_NAME;

    private static final String USER_SELECT = "SELECT no, id, pw, name, phone, email, address FROM user ";
    private static final String DELIVERY_SELECT = "SELECT delivery.no, S.name, delivery.name, delivery.phone, delivery.email, delivery.address, delivery.status LEFT JOIN user S ON delivery.sender = S.no ";

    private static final String USER_INSERT = "INSERT INTO user VALUES (NULL, '%s', '%s', '%s', '%s', '%s', '%s')";
    
    private Statement statement;

    public DeliveryDB() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection connection = DriverManager.getConnection(SERVER_URL, USER_ID, USER_PW);
        statement = connection.createStatement();
    }

    public ArrayList<User> getUsers() throws SQLException {
        ResultSet resultSet = statement.executeQuery(USER_SELECT);
        ArrayList<User> users = new ArrayList<>();
        while (resultSet.next()) {
            User user = generateUser(resultSet);
            users.add(user);
        }

        return users;
    }

    public User findUserByNo(int no) throws SQLException {
        ResultSet resultSet = statement.executeQuery(USER_SELECT + String.format("WHERE no = %d", no));
        if (!resultSet.first())
            return null;

        return generateUser(resultSet);
    }

    public User findUserById(String id) throws SQLException {
        ResultSet resultSet = statement.executeQuery(USER_SELECT + String.format("WHERE id = '%s'", id));
        if (!resultSet.first())
            return null;

        return generateUser(resultSet);
    }

    private User generateUser(ResultSet resultSet) throws SQLException {
        int no = resultSet.getInt(1);
        String id = resultSet.getString(2);
        String pw = resultSet.getString(3);
        String name = resultSet.getString(4);
        String phone = resultSet.getString(5);
        String email = resultSet.getString(6);
        String address = resultSet.getString(7);

        return new User(no, id, pw, name, phone, email, address);
    }

    public Delivery findDeliveryByNo(int no) throws SQLException {
        ResultSet resultSet = statement.executeQuery(DELIVERY_SELECT + String.format("WHERE no = %d", no));
        if (!resultSet.first())
            return null;

        return generateDelivery(resultSet);
    }

    private Delivery generateDelivery(ResultSet resultSet) throws SQLException {
        int no = resultSet.getInt(1);
        String senderName = resultSet.getString(2);
        String senderPhone = resultSet.getString(3);
        String name = resultSet.getString(4);
        String phone = resultSet.getString(5);
        String email = resultSet.getString(6);
        String address = resultSet.getString(7);
        int status = resultSet.getInt(8);

        return new Delivery(no, senderName, senderPhone, name, phone, email, address, status);
    }

    public boolean addUser(String id, String pw, String name, String phone, String email, String address) {
        try {
            int status = statement.executeUpdate(String.format(USER_INSERT, id, pw, name, phone, email, address));
            
            return (status == 1) ? true : false;
        } catch (SQLException e) {
            System.out.println("An exception occured while adding user");
            return false;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        statement.close();
    }
}