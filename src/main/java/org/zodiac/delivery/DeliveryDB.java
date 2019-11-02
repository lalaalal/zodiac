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
    private static final String DELIVERY_SELECT = "SELECT delivery.no, delivery.describe, S.name, S.phone, delivery.name, delivery.phone, delivery.email, delivery.address, delivery.status, delivery.method FROM delivery LEFT JOIN user S ON delivery.sender = S.no ";

    private static final String USER_INSERT = "INSERT INTO user VALUES (NULL, '%s', '%s', '%s', '%s', '%s', '%s')";
    private static final String DELIVERY_INSERT = "INSERT INTO delivery VALUES (NULL, '%s', %s, NULL, '%s', '%s', '%s', '%s', 0, %d)";

    private static final String DELIVERY_STATUS_UPDATE = "UPDATE delivery SET status = %d";
    private static final String DELIVERY_ADDRESS_UPDATE = "UPDATE delivery SET address = '%s'";
    
    Connection connection;
    private Statement statement;

    public DeliveryDB() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");

        connection = DriverManager.getConnection(SERVER_URL, USER_ID, USER_PW);
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

    public ArrayList<Delivery> getDeliveries() throws SQLException {
        ResultSet resultSet = statement.executeQuery(DELIVERY_SELECT);
        ArrayList<Delivery> deliveries = new ArrayList<>();
        while (resultSet.next()) {
            Delivery delivery = generateDelivery(resultSet);
            deliveries.add(delivery);
        }

        return deliveries;
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

    public User findUserByIdPw(String id, String pw) throws SQLException {
        ResultSet resultSet = statement.executeQuery(USER_SELECT + String.format("WHERE id = '%s' AND pw='%s'", id, pw));
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
        ResultSet resultSet = statement.executeQuery(DELIVERY_SELECT + String.format("WHERE delivery.no = %d", no));
        if (!resultSet.first())
            return null;

        return generateDelivery(resultSet);
    }

    private Delivery generateDelivery(ResultSet resultSet) throws SQLException {
        int no = resultSet.getInt(1);
        String describe = resultSet.getString(2);
        String senderName = resultSet.getString(3);
        String senderPhone = resultSet.getString(4);
        String name = resultSet.getString(5);
        String phone = resultSet.getString(6);
        String email = resultSet.getString(7);
        String address = resultSet.getString(8);
        int status = resultSet.getInt(9);
        int method = resultSet.getInt(10);

        return new Delivery(no, describe, senderName, senderPhone, name, phone, email, address, status, method);
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

    public int addDelivery(String describe, User sender, String name, String phone, String email, String address, int method) {
        try {
            statement.executeUpdate(String.format(DELIVERY_INSERT, describe, sender.toString(), name, phone, email, address, method), Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            int id = 0;
            if (resultSet.first())
                id = resultSet.getInt(1);
            System.out.println(id);
            return id;
        } catch (SQLException e) {
            System.out.println("An exception occured while adding delivery");
            return -1;
        }
    }

    public boolean updateStatus(int status) {
        try {
            int stat = statement.executeUpdate(String.format(DELIVERY_STATUS_UPDATE, status));

            return (stat == 1) ? true : false;
        } catch(Exception e) {
            return false;
        }
    }

    public boolean updateAddress(String address) {
        try {
            System.out.println(String.format(DELIVERY_ADDRESS_UPDATE, address));
            int stat = statement.executeUpdate(String.format(DELIVERY_ADDRESS_UPDATE, address));

            return (stat == 1) ? true : false;
        } catch(Exception e) {
            return false;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        statement.close();
    }
}