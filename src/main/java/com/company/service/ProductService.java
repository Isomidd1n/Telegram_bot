package com.company.service;

import com.company.db.Database;
import com.company.model.Operations;
import com.company.model.Product;

import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

public class ProductService {
    public static Product getProductById(Integer id) {

        loadProductList();

        for (Product product : Database.productList) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }


    public static List<Product> getProductsByCategoryId(Integer categoryId) {
        return Database.productList.stream()
                .filter(product -> product.getCategoryId().equals(categoryId))
                .collect(Collectors.toList());
    }

    public static void loadProductList() {
        Connection connection = Database.getConnection();
        if (connection != null) {

            try (Statement statement = connection.createStatement()) {

                Database.productList.clear();

                String query = " SELECT * FROM product WHERE NOT deleted; ";

                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int categoryId = resultSet.getInt("category_id");
                    String name = resultSet.getString("name");
                    double price = resultSet.getDouble("price");
                    String image = resultSet.getString("image");
                    boolean deleted = resultSet.getBoolean("deleted");
                    String description = resultSet.getString("description");
                    int quantity = resultSet.getInt("quantity");


                    Product product = new Product(id, categoryId, name, price, description, quantity, image, deleted);

                    Database.productList.add(product);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    public static void loadAllProductList() {
        Connection connection = Database.getConnection();
        if (connection != null) {

            try (Statement statement = connection.createStatement()) {

                Database.productList.clear();

                String query = " SELECT * FROM product ; ";

                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int categoryId = resultSet.getInt("category_id");
                    String name = resultSet.getString("name");
                    double price = resultSet.getDouble("price");
                    String image = resultSet.getString("image");
                    boolean deleted = resultSet.getBoolean("deleted");
                    String description = resultSet.getString("description");
                    int quantity = resultSet.getInt("quantity");


                    Product product = new Product(id, categoryId, name, price, description, quantity, image, deleted);

                    Database.productList.add(product);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    public static void loadProductListUpdate() {
        Connection connection = Database.getConnection();
        if (connection != null) {

            try (Statement statement = connection.createStatement()) {

                Database.productList.clear();

                String query = " SELECT * FROM product WHERE deleted = true; ";

                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int categoryId = resultSet.getInt("category_id");
                    String name = resultSet.getString("name");
                    double price = resultSet.getDouble("price");
                    String image = resultSet.getString("image");
                    boolean deleted = resultSet.getBoolean("deleted");
                    String description = resultSet.getString("description");
                    int quantity = resultSet.getInt("quantity");


                    Product product = new Product(id, categoryId, name, price, description, quantity, image, deleted);

                    Database.productList.add(product);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }


    public static void addProduct(Product product) {
        Connection connection = Database.getConnection();
        if (connection != null) {

            String query = " INSERT INTO product(name, category_id, price,description ,image,quantity)" +
                    " VALUES(?, ?, ?, ?, ?, ?); ";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, product.getName());
                preparedStatement.setInt(2, product.getCategoryId());
                preparedStatement.setDouble(3, product.getPrice());
                preparedStatement.setString(4, product.getDescription());
                preparedStatement.setString(5, product.getImage());
                preparedStatement.setInt(6, product.getQuantity());

                int executeUpdate = preparedStatement.executeUpdate();
                System.out.println(executeUpdate);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        loadProductList();
    }


    public static void deleteProduct(Integer id) {
        Connection connection = Database.getConnection();
        if (connection != null) {

            String query = " DELETE FROM product WHERE id = ? ;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {


                preparedStatement.setInt(1, id);

                int executeUpdate = preparedStatement.executeUpdate();
                System.out.println("executeUpdate = " + executeUpdate);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }


    }

    public static void updateProductDeletedOff(Integer id) {
        Connection connection = Database.getConnection();
        if (connection != null) {

            String query = " UPDATE product SET deleted=true WHERE id = ? ;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {


                preparedStatement.setInt(1, id);

                int executeUpdate = preparedStatement.executeUpdate();
                System.out.println("executeUpdate = " + executeUpdate);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }
    }


    public static void updateProductDeletedOn(Integer id) {
        Connection connection = Database.getConnection();
        if (connection != null) {

            String query = " UPDATE product SET deleted=false WHERE id = ? ;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {


                preparedStatement.setInt(1, id);

                int executeUpdate = preparedStatement.executeUpdate();
                System.out.println("executeUpdate = " + executeUpdate);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }
    }


    public static void updateProductName(Integer id, String text) {
        Connection connection = Database.getConnection();
        if (connection != null) {


            String query = " UPDATE product SET name = ? WHERE id = ? ;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {


                preparedStatement.setString(1, text);
                preparedStatement.setInt(2, id);

                int executeUpdate = preparedStatement.executeUpdate();
                System.out.println("executeUpdate = " + executeUpdate);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }
    }


    public static void updateProductPrice(Double productPrice, Integer id) {
        Connection connection = Database.getConnection();
        if (connection != null) {

            String query = " UPDATE product SET price = ? WHERE id = ? ;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {


                preparedStatement.setDouble(1, productPrice);
                preparedStatement.setInt(2, id);

                int executeUpdate = preparedStatement.executeUpdate();
                System.out.println("executeUpdate = " + executeUpdate);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }
    }

    public static void updateProductQuantity(Double productQuantity, Integer id) {
        Connection connection = Database.getConnection();
        if (connection != null) {

            String query = " UPDATE product SET quantity = ? WHERE id = ? ;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {


                preparedStatement.setDouble(1, productQuantity);
                preparedStatement.setInt(2, id);

                int executeUpdate = preparedStatement.executeUpdate();
                System.out.println("executeUpdate = " + executeUpdate);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }
    }


    public static void updateProductDescription(Integer id, String productDescription) {
        Connection connection = Database.getConnection();
        if (connection != null) {

            String query = " UPDATE product SET description = ? WHERE id = ? ;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {


                preparedStatement.setString(1, productDescription);
                preparedStatement.setInt(2, id);

                int executeUpdate = preparedStatement.executeUpdate();
                System.out.println("executeUpdate = " + executeUpdate);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }
    }


    public static void operationsList() {
        Connection connection = Database.getConnection();
        if (connection != null) {

            try (Statement statement = connection.createStatement()) {

                Database.operationList.clear();

                String query = " SELECT * FROM operations_view;";

                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    Integer id = resultSet.getInt("id");
                    Integer category_id = resultSet.getInt("category_id");
                    Integer product_id = resultSet.getInt("product_id");
                    Timestamp updated_at = resultSet.getTimestamp("updated_at");
                    String operation_name = resultSet.getString("operation_name");

                    Operations operations = new Operations(id, category_id, product_id, updated_at, operation_name);

                    Database.operationList.add(operations);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }


    public static void deletedProductList() {
        Connection connection = Database.getConnection();
        if (connection != null) {

            try (Statement statement = connection.createStatement()) {

                Database.productList.clear();

                String query = " SELECT * FROM deleted_view;";

                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int categoryId = resultSet.getInt("category_id");
                    Integer quantity = resultSet.getInt("quantity");
                    String name = resultSet.getString("name");
                    double price = resultSet.getDouble("price");
                    String image = resultSet.getString("image");
                    boolean deleted = resultSet.getBoolean("deleted");
                    String description = resultSet.getString("description");

                    Product product = new Product(id, categoryId, name, price, description, quantity, image, deleted);

                    Database.productList.add(product);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public static void notDeletedProductList() {
        Connection connection = Database.getConnection();
        if (connection != null) {

            try (Statement statement = connection.createStatement()) {

                Database.productList.clear();

                String query = " SELECT * FROM not_deleted_view;";

                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int categoryId = resultSet.getInt("category_id");
                    String name = resultSet.getString("name");
                    double price = resultSet.getDouble("price");
                    String image = resultSet.getString("image");
                    boolean deleted = resultSet.getBoolean("deleted");
                    String description = resultSet.getString("description");
                    Integer quantity = resultSet.getInt("quantity");

                    Product product = new Product(id, categoryId, name, price, description, quantity, image, deleted);

                    Database.productList.add(product);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
