package com.company.service;

import com.company.db.Database;
import com.company.model.CartProduct;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CartProductService {
    static Scanner SCANNER_NUM = new Scanner(System.in);

    public static void loadCartProductList() {
        Connection connection = Database.getConnection();
        if (connection != null) {

            try (Statement statement = connection.createStatement()) {

                Database.cartProductList.clear();

                String query = " SELECT * FROM cart_product ; ";

                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String customerId = resultSet.getString("customer_id");
                    int productId = resultSet.getInt("product");
                    int quantity = resultSet.getInt("quantity");

                    CartProduct cartProduct = new CartProduct(id, customerId, productId, quantity);

                    Database.cartProductList.add(cartProduct);
                }

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

    public static List<CartProduct> getCustomerProductList(String customerId) {
        Connection connection = Database.getConnection();
        if (connection != null) {

            try (Statement statement = connection.createStatement()) {
                String query = " SELECT * FROM cart_product where customer_id = '" + customerId +"';";

                ResultSet resultSet = statement.executeQuery(query);
                List<CartProduct> cartProductList = new ArrayList<>();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String cId = resultSet.getString("customer_id");
                    int productId = resultSet.getInt("product");
                    int quantity = resultSet.getInt("quantity");

                    CartProduct cartProduct = new CartProduct(id, cId, productId, quantity);

                    cartProductList.add(cartProduct);
                }


                return cartProductList;

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public static void addCartProduct(CartProduct cartProduct) {
        Connection connection = Database.getConnection();
        if (connection != null) {

            String query = " INSERT INTO cart_product(customer_id, product, quantity)" +
                    " VALUES(?, ?, ?); ";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, cartProduct.getCustomerId());
                preparedStatement.setInt(2, cartProduct.getProductId());
                preparedStatement.setDouble(3, cartProduct.getQuantity());


                int executeUpdate = preparedStatement.executeUpdate();
                System.out.println(executeUpdate);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        loadCartProductList();
    }





    public static void deleteProduct(Integer productId, String userId) {
        Connection connection = Database.getConnection();
        if (connection != null) {

            String query = " DELETE FROM cart_product where id = ? and customer_id = ?;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, productId);
                preparedStatement.setString(2, userId);

                int executeUpdate = preparedStatement.executeUpdate();
                System.out.println("executeUpdate = " + executeUpdate);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void deleteCartProduct(String userId) {
        Connection connection = Database.getConnection();
        if (connection != null) {
            String query = " DELETE FROM cart_product WHERE customer_id = ? ";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query);

                preparedStatement.setString(1, userId);

                int executeUpdate = preparedStatement.executeUpdate();
                System.out.println("executeUpdate = " + executeUpdate);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }


    }
}
