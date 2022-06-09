package com.company.service;

import com.company.db.Database;
import com.company.model.Order;

import java.sql.*;

public class OrderService {
    public static void addOrder(Order order) {
        Connection connection = Database.getConnection();
        if (connection != null) {

            //  UserId  |  FirstName  |  LastName  |  PhoneNumber  |  GeneralOrder  |  TotalPrice  \\

            String query = " INSERT INTO orders(user_id, first_name, last_name, phone_number,user_order,totalprice,date_time)" +
                    " VALUES(?, ?, ?, ?, ?, ?,?); ";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, order.getUser_id());
                preparedStatement.setString(2, order.getFirst_name());
                preparedStatement.setString(3, order.getLast_name());
                preparedStatement.setString(4, order.getPhone_number());
                preparedStatement.setString(5, order.getUser_order());
                preparedStatement.setDouble(6, order.getTotalprice());
                preparedStatement.setString(7, order.getLocalDateTime());

                int executeUpdate = preparedStatement.executeUpdate();
                System.out.println(executeUpdate);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        loadOrderList();
    }

    public static void loadOrderList() {
        Connection connection = Database.getConnection();
        if (connection != null) {

            try (Statement statement = connection.createStatement()) {

                Database.orderList.clear();

                String query = " SELECT * FROM orders order by id; ";

                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String user_id = resultSet.getString("user_id");
                    String first_name = resultSet.getString("first_name");
                    String last_name = resultSet.getString("last_name");
                    String phone_number = resultSet.getString("phone_number");
                    String user_order = resultSet.getString("user_order");
                    double totalprice = resultSet.getDouble("totalprice");
                    String localDateTime1 = resultSet.getString("date_time");

                    Order order = new Order(id, user_id, first_name, last_name, phone_number, user_order, totalprice, localDateTime1);

                    Database.orderList.add(order);
                }
                connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
